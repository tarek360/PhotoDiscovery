package com.tarek.photodiscovery.utils;

import android.content.SharedPreferences;
import javax.inject.Inject;

/**
 * Created by tarek on 12/22/15.
 */
public class StorageHelper {

  private static final String PREF_KEY_KEYWORDS = "keywords";

  private final SharedPreferences mSharedPreferences;

  public StorageHelper(SharedPreferences sharedPreferences){
    mSharedPreferences = sharedPreferences;
  }

  public String getPrefKeyWords() {
    return mSharedPreferences.getString(PREF_KEY_KEYWORDS, "");
  }

  public void setPrefKeyWords(String keyWords) {
    mSharedPreferences.edit().putString(PREF_KEY_KEYWORDS, keyWords).apply();
  }

}
