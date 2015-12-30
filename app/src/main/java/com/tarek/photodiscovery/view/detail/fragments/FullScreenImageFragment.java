package com.tarek.photodiscovery.view.detail.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tarek.photodiscovery.R;
import com.tarek.photodiscovery.drawables.CircularLoaderDrawable;
import com.tarek.photodiscovery.models.Photo;
import org.parceler.Parcels;

/**
 * Created by tarek on 11/7/15.
 */
public class FullScreenImageFragment extends Fragment {

  private Context context;
  private CircularLoaderDrawable circularLoaderDrawable;

  @Bind(R.id.imageView) ImageView imageView;

  /**
   * The fragment argument representing the section number for this
   * fragment.
   */
  private static final String ARG_GALLERY_PHOTO = "section_number";

  /**
   * Returns a new instance of this fragment for the given section
   * number.
   */
  public static FullScreenImageFragment newInstance(Photo photo) {
    FullScreenImageFragment fragment = new FullScreenImageFragment();
    Bundle args = new Bundle();
    args.putParcelable(ARG_GALLERY_PHOTO, Parcels.wrap(photo));
    fragment.setArguments(args);
    return fragment;
  }

  public FullScreenImageFragment() {
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.context = context;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
    ButterKnife.bind(this, rootView);

    Photo photo = Parcels.unwrap(getArguments().getParcelable(ARG_GALLERY_PHOTO));

    circularLoaderDrawable = new CircularLoaderDrawable();
    circularLoaderDrawable.animate();
    Log.d(getClass().getSimpleName(), " photo.getUrl() " + photo.getUrl());

    loadImage(imageView, photo.getUrl(), NetworkPolicy.OFFLINE);

    return rootView;
  }

  private void loadImage(final ImageView imageView, final String url,
      final NetworkPolicy networkPolicy) {

    Picasso.with(context)
        .load(url)
        .placeholder(circularLoaderDrawable)
        .networkPolicy(networkPolicy)
        .into(imageView, new com.squareup.picasso.Callback() {
          @Override public void onSuccess() {
            //
            circularLoaderDrawable.clearAnimation();
          }

          @Override public void onError() {
            if (!networkPolicy.equals(NetworkPolicy.OFFLINE)) {
              circularLoaderDrawable.setError();
            } else {
              loadImage(imageView, url, NetworkPolicy.NO_CACHE);
            }
          }
        });
  }

  @Override public void onStop() {
    super.onStop();
    circularLoaderDrawable.clearAnimation();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }
}