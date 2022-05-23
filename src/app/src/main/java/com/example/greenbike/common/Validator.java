package com.example.greenbike.common;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isEmailValid(String email){

        return Pattern.compile("^(.+)@(.+)\\.(.+)$").matcher(email).matches();
    }

    public static boolean isBikeMaterialInvalid(String name) {
        boolean isInvalid = name.isEmpty();

        return isInvalid;
    }

    public static boolean isBikeCategoryInvalid(String name) {
        boolean isInvalid = name.isEmpty();

        return isInvalid;
    }
}
