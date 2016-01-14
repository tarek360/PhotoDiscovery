package com.tarek.photodiscovery.view.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import com.tarek.photodiscovery.view.BaseActivity;
import java.util.ArrayList;
import org.parceler.Parcels;

public class MainActivity extends BaseActivity implements MainView {

  private static final String LIST_STATE_KEY = "listState";
  private static final String PHOTO_LIST = "photoList";

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

  private MainPresenter presenter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    presenter = new MainPresenterImpl(this);

    if (!CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
      presenter.showSnackBar(R.string.no_connection);
    }

    keyWords = mStorageHelper.getPrefKeyWords();

    /** Load some photos by amount */
    if (savedInstanceState == null) {
      photoList = new ArrayList<>();
      presenter.onCreate(keyWords);
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
            mNavigator.navigateToPhotoDetail(MainActivity.this, photoList, position);
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
              presenter.addRandomPhotosToPhotoList(keyWords);
            }
          }
        });
  }

  @OnClick(R.id.fab) void onClickFAB() {
    presenter.showInputDialog(keyWords);
  }

  @Override protected void onResume() {
    super.onResume();

    if (mListState != null) {
      //Restore RecyclerView state - position
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
  @Override public void showSnackBarWithStringResource(int resId) {
    Snackbar snackbar = Snackbar.make(coordinatorLayout, resId, Snackbar.LENGTH_LONG);
    snackbar.show();
  }

  @Override
  public void showInputDialog(String lastUserInput, MaterialDialog.InputCallback inputCallback) {

    new MaterialDialog.Builder(this).title(R.string.input)
        .content(R.string.input_content)
        .inputRange(2, 32)
        .inputType(InputType.TYPE_CLASS_TEXT)
        .input(getString(R.string.input_hint), lastUserInput, inputCallback)
        .show();
  }

  @Override public void loadPhotoByKeyWordsFromUserInput(String keyWords) {

    if (!mStorageHelper.getPrefKeyWords().equalsIgnoreCase(keyWords)) {

      this.keyWords = keyWords;
      mStorageHelper.setPrefKeyWords(keyWords);

      presenter.setRandomPhotosToPhotoList(keyWords);
      mLayoutManager.scrollToPosition(0);
    }
  }

  @Override public void clearPhotoList() {
    photoList.clear();
  }

  @Override public void onRecyclerViewRangeChanged(int itemCount) {
    mGalleryRecyclerViewAdapter.notifyItemRangeChanged(photoList.size(), itemCount);
  }

  @Override public void onRecyclerViewSetChanged() {
    mGalleryRecyclerViewAdapter.notifyDataSetChanged();
  }

  @Override public void addRandomPhotoToPhotoList(Photo photo) {
    photoList.add(photo);
  }
}