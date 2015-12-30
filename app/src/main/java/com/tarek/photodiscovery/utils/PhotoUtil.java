package com.tarek.photodiscovery.utils;

import com.tarek.photodiscovery.models.Photo;

/**
 * Created by tarek on 12/23/15.
 */
public class PhotoUtil {

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
  public static Photo getRandomPhoto(String keyWords) {

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
}
