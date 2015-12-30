package com.tarek.photodiscovery.view.detail;

import com.tarek.photodiscovery.models.Photo;
import java.util.ArrayList;

/**
 * Created by tarek on 12/30/15.
 */
public class DetailPresenterImpl implements DetailPresenter {

  private DetailView detailView;

  public DetailPresenterImpl(DetailView detailView) {
    this.detailView = detailView;
  }

  @Override public void setData(ArrayList<Photo> photoList, int position) {
    detailView.setList(photoList);
    detailView.setCurrentPage(position);
  }
}
