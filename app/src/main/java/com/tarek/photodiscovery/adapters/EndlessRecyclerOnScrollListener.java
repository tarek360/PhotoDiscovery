package com.tarek.photodiscovery.adapters;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int mSpanCount;
    private int previousTotal; // The total number of items in the dataset after the last load
    protected boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private StaggeredGridLayoutManager mLayoutManager;

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager mLayoutManager, int mSpanCount) {
        this.mLayoutManager = mLayoutManager;
        this.mSpanCount = mSpanCount;
        this.visibleThreshold = mSpanCount * 2;

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        firstVisibleItem = mLayoutManager.findFirstVisibleItemPositions(new int[mSpanCount])[0];
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();

        if (loading) {
            if (totalItemCount > previousTotal) {
                previousTotal = totalItemCount;
                loading = false;
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            onScroll();
        }
    }

    public abstract void onScroll();
}