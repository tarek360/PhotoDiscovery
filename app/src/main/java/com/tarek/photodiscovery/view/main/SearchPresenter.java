package com.tarek.photodiscovery.view.main;

import com.tarek.photodiscovery.models.Photo;

/**
 * Created by tarek on 12/30/15.
 */
public interface SearchPresenter {

  String getValidateFormat(String text);

  String restoreUserInputFormat(String text);

  Photo getRandomPhoto(String keyWords);
}
