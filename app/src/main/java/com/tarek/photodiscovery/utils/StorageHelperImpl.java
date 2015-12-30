package com.tarek.photodiscovery.utils;

import android.content.SharedPreferences;

/**
 * Created by tarek on 12/30/15.
 */
public class StorageHelperImpl implements StorageHelper {

  private static final String PREF_KEY_KEYWORDS = "keywords";

  private final SharedPreferences mSharedPreferences;

  public StorageHelperImpl(SharedPreferences sharedPreferences) {
    mSharedPreferences = sharedPreferences;
  }

  @Override public String getPrefKeyWords() {
    return mSharedPreferences.getString(PREF_KEY_KEYWORDS, "");
  }

  @Override public void setPrefKeyWords(String keyWords) {
    mSharedPreferences.edit().putString(PREF_KEY_KEYWORDS, keyWords).apply();
  }
}
