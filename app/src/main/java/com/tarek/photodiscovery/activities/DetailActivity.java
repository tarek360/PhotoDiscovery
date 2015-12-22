package com.tarek.photodiscovery.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import java.util.ArrayList;

import org.parceler.Parcels;

import com.tarek.photodiscovery.R;
import com.tarek.photodiscovery.adapters.GalleryPagerAdapter;
import com.tarek.photodiscovery.models.Photo;

/**
 * Created by tarek on 11/7/15.
 */

public class DetailActivity extends AppCompatActivity {
    private ArrayList<Photo> photoList;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * the gallery fragment.
     */
    private GalleryPagerAdapter mGalleryPagerAdapter;

    /**
     * The {@link ViewPager} that will host the gallery.
     */
    private ViewPager mViewPager;

    private static final String EXTRA_GALLERY_PARCEL = "photoGallery";
    private static final String EXTRA_CURRENT_POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getWindow().setBackgroundDrawable(null);
        photoList = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_GALLERY_PARCEL));

        int position = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mGalleryPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager(), photoList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mGalleryPagerAdapter);
        mViewPager.setCurrentItem(position);
    }


    public static Intent buildIntent(Context context, ArrayList<Photo> photoList, int position) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_GALLERY_PARCEL, Parcels.wrap(photoList));
        intent.putExtra(EXTRA_CURRENT_POSITION, position);
      Log.d(DetailActivity.class.getSimpleName(),
          " photo.getUrl() " + photoList.get(position).getUrl());

        return intent;
    }


}
