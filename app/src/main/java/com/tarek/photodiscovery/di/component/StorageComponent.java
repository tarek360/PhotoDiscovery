package com.tarek.photodiscovery.di.component;

import com.tarek.photodiscovery.view.activities.BaseActivity;
import com.tarek.photodiscovery.di.modules.StorageModule;
import dagger.Component;
import javax.inject.Singleton;
import com.tarek.photodiscovery.di.modules.AppModule;

/**
 * Created by tarek on 12/22/15.
 */
@Singleton @Component(modules = {AppModule.class, StorageModule.class}) public interface StorageComponent {

  void inject(BaseActivity baseActivity);

}
