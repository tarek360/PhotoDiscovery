package com.tarek.photodiscovery.utils;

/**
 * Created by tarek on 12/23/15.
 */
public class KeywordsUtil {

  public static String getValidateKeywords(String text) {
    StringBuffer urlKeys = new StringBuffer();
    String[] keyWords = text.trim().split(" ");
    for (String keyword : keyWords) {
      urlKeys.append(keyword);
      urlKeys.append(",");
    }

    return urlKeys.toString();
  }
}
