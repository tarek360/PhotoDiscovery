package com.tarek.photodiscovery.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import com.tarek.photodiscovery.view.detail.fragments.FullScreenImageFragment;
import com.tarek.photodiscovery.models.Photo;


/**
 * Created by tarek on 11/7/15.
 */

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Photo> photoList;

    public GalleryPagerAdapter(FragmentManager fm, ArrayList<Photo> photoList) {
        super(fm);
        this.photoList = photoList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a FullScreenImageFragment.
        return FullScreenImageFragment.newInstance(photoList.get(position));
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

}