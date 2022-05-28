package com.example.greenbike.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isEmailValid(String email){
        return Pattern.compile("^(.+)@(.+)\\.(.+)$").matcher(email).matches();
    }

    public static boolean isNullOrEmpty(String name) {
        return name == null || name.isEmpty();
    }

    public static boolean checkCredentialsForLogin(JSONObject jsonObject, String password) throws JSONException {
        String userPassword = jsonObject.getString("password");

        return Validator.equalsTo(userPassword, password);
    }

    public static boolean checkIfResponseIsCorrect(JSONArray response) {
        return response.length() == 1;
    }

    public static boolean isLowerThan(String text, Integer numberToCheck) {
        return text.length() < numberToCheck;
    }

    public static boolean equalsTo(String firstString, String secondString) {
        return firstString.equals(secondString);
    }


}
