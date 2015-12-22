package com.tarek.photodiscovery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tarek.photodiscovery.R;
import com.tarek.photodiscovery.adapters.EndlessRecyclerOnScrollListener;
import com.tarek.photodiscovery.adapters.GalleryRecyclerViewAdapter;
import com.tarek.photodiscovery.adapters.RecyclerItemClickListener;
import com.tarek.photodiscovery.models.Photo;
import com.tarek.photodiscovery.utils.CheckNetworkConnection;
import java.util.ArrayList;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {

  private static final String LIST_STATE_KEY = "listState";
  private static final String PREF_KEY_KEYWORDS = "keywords";
  private static final String PHOTO_LIST = "photoList";

  private static final int PHOTO_AMOUNT = 20;

  @Bind(R.id.coordinatorLayout) CoordinatorLayout coordinatorLayout;
  @Bind(R.id.galleryRecyclerView) RecyclerView mRecyclerView;
  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.fab) FloatingActionButton fab;

  private GalleryRecyclerViewAdapter mGalleryRecyclerViewAdapter;
  private StaggeredGridLayoutManager mLayoutManager;

  private int mSpanCount;
  private int mSpanWidth;

  private ArrayList<Photo> photoList;
  private Parcelable mListState;
  private String keyWords;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    if (!CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
      showSnackBarWithStringResource(R.string.no_connection);
    }

    /** Load some photos by amount */
    if (savedInstanceState == null) {
      photoList = new ArrayList<>();
      loadPhotoByKeyWordsFromPref();
    } else {
      /** Restore photos*/
      photoList = Parcels.unwrap(savedInstanceState.getParcelable(PHOTO_LIST));

      /** Retrieve list state and list/item positions */
      mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
    }

    /**
     * Get screen dimensions
     */
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    if (displayMetrics.widthPixels < displayMetrics.heightPixels) {
      // Portrait view
      mSpanCount = 3;
    } else {
      // Landscape view
      mSpanCount = 4;
    }

    mSpanWidth = displayMetrics.widthPixels / mSpanCount;
    mLayoutManager =
        new StaggeredGridLayoutManager(mSpanCount, StaggeredGridLayoutManager.VERTICAL);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mGalleryRecyclerViewAdapter = new GalleryRecyclerViewAdapter(photoList, mSpanWidth);
    mRecyclerView.setAdapter(mGalleryRecyclerViewAdapter);

    /**
     * Listen to clicking on mRecyclerView items to open
     * new activity with the clicked photo in full screen
     */
    mRecyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
          @Override public void onItemClick(View view, int position) {
            Intent intent = DetailActivity.buildIntent(MainActivity.this, photoList, position);
            MainActivity.this.startActivity(intent);
          }
        }));

    /**
     * Listen to mRecyclerView Scrolling to load more photos
     */
    mRecyclerView.addOnScrollListener(
        new EndlessRecyclerOnScrollListener(mLayoutManager, mSpanCount) {
          @Override public void onLoadMore(int page, int totalItemsCount) {
            // If connection available load photos by amount
            if (CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
              loadPhotosByAmount(PHOTO_AMOUNT);
              mGalleryRecyclerViewAdapter.notifyItemRangeChanged(photoList.size(), PHOTO_AMOUNT);
              // Set it to true to prevent more onScroll calling till new photos is loaded.
              //this.loading = true;
            } else {
              // Set it to false to receive more onScroll calling.
              //this.loading = false;
            }
          }
        });
  }

  @OnClick(R.id.fab) void onClickFAB() {
    showInputDialog();
  }

  /**
   * Load photos by specific amount
   *
   * @param amount of photos to be loaded.
   */
  private void loadPhotosByAmount(int amount) {

    for (int i = 0; i < amount; i++) {
      photoList.add(getRandomPhoto());
    }
  }

  // Initial photo width & height
  private int width = 400;
  private int height = 550;

  // int variable is used to increment photo height by one pixel.
  private int step;

  /**
   * Returns Random Photo with every call.
   * Max unique photo url is 151
   *
   * @return Photo.
   */
  private Photo getRandomPhoto() {

    //StringBuffer randomURL = new StringBuffer("http://lorempixel.com/");
    StringBuffer randomURL = new StringBuffer("http://loremflickr.com/");

    // Set it to start
    if (step == 49) {
      step = 0;
    }

    if (height > 700) {
      step++;
      height = 600 + step;
    } else {
      height += 50;
    }

    randomURL.append(width);
    randomURL.append("/");
    randomURL.append(height);
    randomURL.append("/");
    randomURL.append(keyWords);
    randomURL.append("/all");

    return new Photo(randomURL.toString(), width, height);
  }

  @Override protected void onResume() {
    super.onResume();

    if (mListState != null) {
      mLayoutManager.onRestoreInstanceState(mListState);
    }
  }

  protected void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);

    // Save list state
    mListState = mLayoutManager.onSaveInstanceState();
    state.putParcelable(LIST_STATE_KEY, mListState);

    // Save Photo list
    state.putParcelable(PHOTO_LIST, Parcels.wrap(photoList));
  }

  /**
   * Show SnackBar with String resource id
   *
   * @param resId of String.
   */
  private void showSnackBarWithStringResource(int resId) {
    Snackbar snackbar = Snackbar.make(coordinatorLayout, resId, Snackbar.LENGTH_LONG);
    snackbar.show();
  }

  private void showInputDialog() {

    new MaterialDialog.Builder(this).title(R.string.input)
        .content(R.string.input_content)
        .inputRange(2, 32)
        .inputType(InputType.TYPE_CLASS_TEXT)
        .input(R.string.input_hint, R.string.input_pre_fill, new MaterialDialog.InputCallback() {
          @Override public void onInput(MaterialDialog dialog, CharSequence input) {
            validateKeywords(input.toString());
          }
        })
        .show();
  }

  private String getPrefKeyWords() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    return sharedPreferences.getString(PREF_KEY_KEYWORDS, "");
  }

  private void setPrefKeyWords(String keyWords) {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    sharedPreferences.edit().putString(PREF_KEY_KEYWORDS, keyWords).apply();
  }

  private void loadPhotoByKeyWordsFromPref() {
    keyWords = getPrefKeyWords();
    if (!TextUtils.isEmpty(keyWords)) {
      loadPhotosByAmount(PHOTO_AMOUNT);
    } else {
      showInputDialog();
    }
  }

  private void loadPhotoByKeyWordsFromUserInput(String keyWords) {

    if (!getPrefKeyWords().equalsIgnoreCase(keyWords)) {
      setPrefKeyWords(keyWords);
      this.keyWords = keyWords;

      if (!TextUtils.isEmpty(keyWords)) {
        photoList.clear();
        loadPhotosByAmount(PHOTO_AMOUNT);
        mGalleryRecyclerViewAdapter.notifyDataSetChanged();
        mLayoutManager.scrollToPosition(0);
      } else {
        showInputDialog();
      }
    }
  }

  private void validateKeywords(String text) {
    StringBuffer urlKeys = new StringBuffer();
    String[] keyWords = text.trim().split(" ");
    for (String keyword : keyWords) {
      urlKeys.append(keyword);
      urlKeys.append(",");
    }

    loadPhotoByKeyWordsFromUserInput(urlKeys.toString());
  }
}
