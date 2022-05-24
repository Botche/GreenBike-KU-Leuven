package com.example.greenbike;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class GreenBikeApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        GreenBikeApplication.context = getApplicationContext();
        Fresco.initialize(this);
    }

    public static Context getAppContext() {
        return GreenBikeApplication.context;
    }

}
