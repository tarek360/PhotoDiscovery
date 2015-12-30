package com.tarek.photodiscovery.view.detail;

import com.tarek.photodiscovery.models.Photo;
import java.util.ArrayList;

/**
 * Created by tarek on 12/30/15.
 */
public interface DetailPresenter {

  void setData(ArrayList<Photo> photoList, int position);

}
