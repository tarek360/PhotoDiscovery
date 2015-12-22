package com.tarek.photodiscovery;

import android.app.Application;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.tarek.photodiscovery.di.component.DaggerStorageComponent;
import com.tarek.photodiscovery.di.component.StorageComponent;
import com.tarek.photodiscovery.di.modules.AppModule;
import com.tarek.photodiscovery.di.modules.StorageModule;
import java.io.File;
import java.io.IOException;

/**
 * Created by tarek on 12/22/15.
 */
public class App extends Application {

  StorageComponent storageComponent;

  @Override public void onCreate() {
    super.onCreate();


    storageComponent = DaggerStorageComponent.builder()
        .appModule(new AppModule(this))
        .storageModule(new StorageModule())
        .build();

    initPicasso();
  }

  private void initPicasso() {
    File cacheDirectory = new File(getCacheDir().getAbsolutePath(), "OKHttpCache");

    OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setCache(new Cache(cacheDirectory, Integer.MAX_VALUE));

    /** Dangerous interceptor that rewrites the server's cache-control header. */
    okHttpClient.networkInterceptors().add(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
            .header("Cache-Control", "public, max-age=432000")
            .header("Pragma", "")
            .build();
      }
    });

    OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);

    Picasso.Builder builder = new Picasso.Builder(this);
    builder.downloader(okHttpDownloader);

    Picasso picasso = builder.build();
    //picasso.setIndicatorsEnabled(true);
    //picasso.setLoggingEnabled(true);
    Picasso.setSingletonInstance(picasso);
  }

  public StorageComponent getStorageComponent() {
    return storageComponent;
  }
}
