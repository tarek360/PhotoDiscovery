package com.tarek.photodiscovery.di.modules;

import com.tarek.photodiscovery.App;
import com.tarek.photodiscovery.navigation.Navigator;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by tarek on 12/22/15.
 */
@Module public class AppModule {

  private final App app;

  public AppModule(App app) {
    this.app = app;
  }

  @Singleton @Provides public App providesApp() {
    return app;
  }

  @Singleton @Provides public Navigator providesNavigator() {
    return new Navigator();
  }
}
