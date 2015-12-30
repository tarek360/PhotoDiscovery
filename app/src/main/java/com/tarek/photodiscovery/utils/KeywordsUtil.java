package com.tarek.photodiscovery.utils;

/**
 * Created by tarek on 12/23/15.
 */
public class KeywordsUtil {

  public static String getValidateFormat(String text) {
    StringBuffer urlKeys = new StringBuffer();
    String[] keyWords = text.trim().split(" ");
    for (int i = 0; i < keyWords.length; i++) {
      urlKeys.append(keyWords[i]);
      if (i != keyWords.length - 1) {
        urlKeys.append(",");
      }
    }

    return urlKeys.toString();
  }

  public static String restoreUserInputFormat(String text) {
    return text.replace(',', ' ');
  }
}
