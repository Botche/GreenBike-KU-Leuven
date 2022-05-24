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
        boolean isInvalid = name == null || name.isEmpty();

        return isInvalid;
    }

    public static boolean checkCredentialsForLogin(JSONObject jsonObject, String password) throws JSONException {
        String userPassword = jsonObject.getString("password");
        boolean isValid = Validator.equalsTo(userPassword, password);

        return isValid;
    }

    public static boolean checkIfResponseIsCorrect(JSONArray response) {
        boolean isValid = response.length() == 1;

        return isValid;
    }

    public static boolean isLowerThan(String text, Integer numberToCheck) {
        boolean isLowerThan = text.length() < numberToCheck;

        return isLowerThan;
    }

    public static boolean equalsTo(String firstString, String secondString) {
        boolean areEqual = firstString.equals(secondString);

        return areEqual;
    }


}
