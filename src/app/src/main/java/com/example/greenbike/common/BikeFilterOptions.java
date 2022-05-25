package com.example.greenbike.common;

public enum BikeFilterOptions {
    All,
    Rent,
    Buy;

    public String getStringValue() {
        switch (this) {
            case Rent:
                return "1";
            case Buy:
                return "0";
            case All:
            default:
                return "-1";
        }
    }
}
