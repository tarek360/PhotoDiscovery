package com.tarek.photodiscovery.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.tarek.photodiscovery.App;
import com.tarek.photodiscovery.navigation.Navigator;
import com.tarek.photodiscovery.utils.StorageHelper;
import javax.inject.Inject;

/**
 * Created by tarek on 12/23/15.
 */
public class BaseActivity extends AppCompatActivity {

  @Inject Navigator mNavigator;
  @Inject StorageHelper mStorageHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ((App) getApplication()).getStorageComponent().inject(this);
  }
}
