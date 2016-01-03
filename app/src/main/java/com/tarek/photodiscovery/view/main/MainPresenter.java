package com.tarek.photodiscovery.view.main;

/**
 * Created by tarek on 12/30/15.
 */
public interface MainPresenter {

  void showInputDialog(String keyWords);

  void addRandomPhotosToPhotoList(String keyWords);

  void setRandomPhotosToPhotoList(String keyWords);

  void showSnackBar(int noConnectionResId);

  void onCreate(String keyWords);
}
