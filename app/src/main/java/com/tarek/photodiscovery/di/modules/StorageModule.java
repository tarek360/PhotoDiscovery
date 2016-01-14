package com.tarek.photodiscovery.di.modules;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.tarek.photodiscovery.App;
import com.tarek.photodiscovery.utils.StorageHelper;
import com.tarek.photodiscovery.utils.StorageHelperImpl;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by tarek on 12/22/15.
 */
@Module public class StorageModule {

  @Provides @Singleton SharedPreferences provideSharedPreferences(App app) {
    return PreferenceManager.getDefaultSharedPreferences(app);
  }

  @Provides @Singleton StorageHelper provideStorageHelper(SharedPreferences sharedPreferences) {
    return new StorageHelperImpl(sharedPreferences);
  }
}
