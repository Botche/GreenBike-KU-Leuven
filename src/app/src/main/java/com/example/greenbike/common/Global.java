package com.example.greenbike.common;

import com.example.greenbike.GreenBikeApplication;
import com.example.greenbike.database.models.user.User;

import java.time.format.DateTimeFormatter;

public class Global {
    public static User currentUser = null;

    public static final VolleyRequestQueue requestQueue = VolleyRequestQueue.getInstance(GreenBikeApplication.getAppContext());
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
}
