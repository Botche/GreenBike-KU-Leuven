package com.example.greenbike.common;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isEmailValid(String email){

        return Pattern.compile("^(.+)@(.+)\\.(.+)$").matcher(email).matches();
    }
}
