package com.tarek.photodiscovery.utils;

import android.view.View;
import android.view.animation.Animation;

import com.tarek.photodiscovery.R;

/**
 * Created by tarek on 11/8/15.
 */
public class AnimationUtils {


    public static void fadeIn(View view){
        Animation fadeInAnimation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in_anim);
        view.startAnimation(fadeInAnimation);
    }

}
