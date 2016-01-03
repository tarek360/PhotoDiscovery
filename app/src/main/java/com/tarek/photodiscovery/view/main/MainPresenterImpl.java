package com.tarek.photodiscovery.view.main;

import android.text.TextUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tarek.photodiscovery.models.Photo;

/**
 * Created by tarek on 12/30/15.
 */
public class MainPresenterImpl implements MainPresenter, MaterialDialog.InputCallback {

  private static final int PHOTO_AMOUNT = 20;

  private MainView mainView;

  public MainPresenterImpl(MainView mainView) {
    this.mainView = mainView;
  }

  @Override public void showInputDialog(String keyWords) {
    mainView.showInputDialog(restoreUserInputFormat(keyWords), this);
  }

  @Override public void addRandomPhotosToPhotoList(String keyWords) {
    addRandomPhotosToPhotoListByFixedAmount(keyWords);
    mainView.onRecyclerViewRangeChanged(PHOTO_AMOUNT);
  }

  @Override public void setRandomPhotosToPhotoList(String keyWords) {
    mainView.clearPhotoList();
    addRandomPhotosToPhotoListByFixedAmount(keyWords);
    mainView.onRecyclerViewSetChanged();
  }

  @Override public void showSnackBar(int noConnectionResId) {
    mainView.showSnackBarWithStringResource(noConnectionResId);
  }

  @Override public void onCreate(String keyWords) {
    if (TextUtils.isEmpty(keyWords)) {
      showInputDialog("");
    } else {
      addRandomPhotosToPhotoListByFixedAmount(keyWords);
    }
  }

  @Override public void onInput(MaterialDialog dialog, CharSequence input) {
    String keyWords = getValidateFormat(input.toString());
    mainView.loadPhotoByKeyWordsFromUserInput(keyWords);
  }

  private String getValidateFormat(String text) {
    StringBuilder urlKeys = new StringBuilder();
    String[] keyWords = text.trim().split(" ");
    for (int i = 0; i < keyWords.length; i++) {
      urlKeys.append(keyWords[i]);
      if (i != keyWords.length - 1) {
        urlKeys.append(",");
      }
    }

    return urlKeys.toString();
  }

  private String restoreUserInputFormat(String text) {
    return text.replace(',', ' ');
  }

  // Initial photo width & height
  private static int width = 400;
  private static int height = 550;

  // int variable is used to increment photo height by one pixel.
  private static int step;

  /**
   * Returns Random Photo with every call.
   * Max unique photo url is 151
   *
   * @return Photo.
   */
  private Photo getRandomPhoto(String keyWords) {

    //StringBuilder randomURL = new StringBuilder("http://lorempixel.com/");
    StringBuilder randomURL = new StringBuilder("http://loremflickr.com/");

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

  private void addRandomPhotosToPhotoListByFixedAmount(String keyWords) {
    for (int i = 0; i < PHOTO_AMOUNT; i++) {
      mainView.addRandomPhotoToPhotoList(getRandomPhoto(keyWords));
    }
  }
}
