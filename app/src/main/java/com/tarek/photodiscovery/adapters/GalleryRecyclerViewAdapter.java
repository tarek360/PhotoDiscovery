package com.tarek.photodiscovery.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.tarek.photodiscovery.drawables.CircularLoaderDrawable;
import com.tarek.photodiscovery.models.Photo;
import com.tarek.photodiscovery.utils.AnimationUtils;


/**
 * Created by tarek on 11/7/15.
 */

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Photo> photoList;
    private int mColumnWidth;

    public GalleryRecyclerViewAdapter(ArrayList<Photo> photoList, int mColumnWidth) {
        this.photoList = photoList;
        this.mColumnWidth = mColumnWidth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ImageView imageView = new ImageView(parent.getContext());

        return new ViewHolder(imageView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                mColumnWidth, (int) (photoList.get(position).getAspectRatio() * mColumnWidth));
        holder.getImageView().setLayoutParams(params);

        holder.getCircularLoaderDrawable().animate();

        loadImage(holder, position, NetworkPolicy.OFFLINE);

    }


    private void loadImage(final ViewHolder holder, final int position, final NetworkPolicy networkPolicy) {
        final WeakReference<CircularLoaderDrawable> xx = new WeakReference<>(holder.getCircularLoaderDrawable());
        Picasso.with(holder.getImageView().getContext())
                .load(photoList.get(position).getUrl())
                .resize(mColumnWidth, (int) (photoList.get(position).getAspectRatio() * mColumnWidth))
                .placeholder(xx.get())
                .noFade()
                .networkPolicy(networkPolicy)
                .into(holder.getImageView(), new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        //
                        xx.get().clearAnimation();

                        //
                        AnimationUtils.fadeIn(holder.getImageView());
                    }

                    @Override
                    public void onError() {
                        if (!networkPolicy.equals(NetworkPolicy.OFFLINE)) {
                            holder.getCircularLoaderDrawable().setError();
                        } else {
                            loadImage(holder,position, NetworkPolicy.NO_CACHE);
                        }

                    }
                });

    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CircularLoaderDrawable circularLoaderDrawable;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view;

            circularLoaderDrawable = new CircularLoaderDrawable();
        }

        public ImageView getImageView() {
            return imageView;
        }

        public CircularLoaderDrawable getCircularLoaderDrawable() {
            return circularLoaderDrawable;
        }

    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.imageView.clearAnimation();
        holder.circularLoaderDrawable.clearAnimation();
        super.onViewDetachedFromWindow(holder);

    }

}
