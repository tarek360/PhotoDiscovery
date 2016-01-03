package com.tarek.photodiscovery.view.main;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tarek.photodiscovery.models.Photo;

/**
 * Created by tarek on 12/23/15.
 */
public interface MainView {

  void showInputDialog(String lastUserInput, MaterialDialog.InputCallback inputCallback);

  void showSnackBarWithStringResource(int resId);

  void addRandomPhotoToPhotoList(Photo photo);

  void clearPhotoList();

  void loadPhotoByKeyWordsFromUserInput(String keyWords);

  void onRecyclerViewRangeChanged(int itemCount);

  void onRecyclerViewSetChanged();
}
