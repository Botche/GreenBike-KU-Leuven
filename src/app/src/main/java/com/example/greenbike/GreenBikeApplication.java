package com.example.greenbike;

import android.app.Application;
import android.content.Context;

public class GreenBikeApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        GreenBikeApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GreenBikeApplication.context;
    }

}
