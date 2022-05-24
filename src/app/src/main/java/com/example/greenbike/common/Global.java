package com.example.greenbike.common;

import android.app.Application;

import com.example.greenbike.GreenBikeApplication;
import com.example.greenbike.MainActivity;
import com.example.greenbike.database.models.user.User;
import com.example.greenbike.database.models.user.UserRole;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Global {
    public static User currentUser = null;

    public static VolleyRequestQueue requestQueue = VolleyRequestQueue.getInstance(GreenBikeApplication.getAppContext());
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
