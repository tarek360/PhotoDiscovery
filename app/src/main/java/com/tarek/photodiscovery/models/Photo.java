package com.tarek.photodiscovery.models;

import org.parceler.Parcel;

/**
 * Created by tarek on 11/7/15.
 */

@Parcel
public class Photo {

    int width;
    int height;
    String url;

    public Photo() {
    }

    public Photo(String url, int width, int height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public float getAspectRatio() {

        if (width <= 0)
            throw new ArithmeticException("width must be more than zero");

        if (height <= 0)
            throw new ArithmeticException("height must be more than zero");

        return (float) height / width;
    }
}
