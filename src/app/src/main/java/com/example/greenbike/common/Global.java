package com.example.greenbike.common;

import android.app.Application;

import com.example.greenbike.GreenBikeApplication;
import com.example.greenbike.MainActivity;
import com.example.greenbike.database.models.user.User;
import com.example.greenbike.database.models.user.UserRole;

import java.util.ArrayList;

public class Global {
    public static User currentUser = null;

    public static VolleyRequestQueue requestQueue = VolleyRequestQueue.getInstance(GreenBikeApplication.getAppContext());
}
