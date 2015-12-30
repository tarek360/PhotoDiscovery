/**
 * Copyright (C) 2015 tarek360 All rights reserved.
 *
 * @author Ahmed Tarek
 */
package com.tarek.photodiscovery.navigation;

import android.content.Context;
import android.content.Intent;
import com.tarek.photodiscovery.view.detail.DetailActivity;
import com.tarek.photodiscovery.models.Photo;
import java.util.ArrayList;

/**
 * Class used to navigate through the application.
 */

public class Navigator {

  /**
   * Goes to the user details screen.
   *
   * @param context A Context needed to open the destiny activity.
   */
  public void navigateToPhotoDetail(Context context, ArrayList<Photo> photoList, int position) {
    if (context != null) {
      Intent intentToLaunch = DetailActivity.buildIntent(context, photoList, position);
      context.startActivity(intentToLaunch);
    }
  }
}
