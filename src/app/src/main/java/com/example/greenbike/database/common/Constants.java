package com.example.greenbike.database.common;

public class Constants {

    public static String BASE_URL = "https://studev.groept.be/api/a21pt104";

    public static String GET_ALL_USER_ROLES = Constants.BASE_URL + "/getAllUserRoles";
    public static String REGISTER_USER = Constants.BASE_URL + "/register";
    public static String LOGIN_USER = Constants.BASE_URL + "/login/%s";

    public static String GET_ALL_USER_BIKES = Constants.BASE_URL + "/getAllUserBikes/%s";

    public static String GET_BRANDS_URL = Constants.BASE_URL + "/getAllBikeBrands";
    public static String CREATE_BRAND_URL = Constants.BASE_URL + "/createBikeBrand";
    public static String EDIT_BRAND_URL = Constants.BASE_URL + "/editBikeBrand";
    public static String DELETE_BRAND_URL = Constants.BASE_URL + "/deleteBikeBrand";

    public static String GET_CATEGORIES_URL = Constants.BASE_URL + "/getAllBikeCategories";
    public static String CREATE_CATEGORY_URL = Constants.BASE_URL + "/createBikeCategory";
    public static String EDIT_CATEGORY_URL = Constants.BASE_URL + "/editBikeCategory";
    public static String DELETE_CATEGORY_URL = Constants.BASE_URL + "/deleteBikeCategory";

    public static String GET_MATERIALS_URL = Constants.BASE_URL + "/getAllBikeMaterials";
    public static String CREATE_MATERIAL_URL = Constants.BASE_URL + "/createBikeMaterial";
    public static String EDIT_MATERIAL_URL = Constants.BASE_URL + "/editBikeMaterial";
    public static String DELETE_MATERIAL_URL = Constants.BASE_URL + "/deleteBikeMaterial";

    public static String GET_BIKES_URL = Constants.BASE_URL + "/getAllBikes";
    public static String CREATE_BIKE_URL = Constants.BASE_URL + "/createBike";
    public static String EDIT_BIKE_URL = Constants.BASE_URL + "/editBike";
    public static String DELETE_BIKE_URL = Constants.BASE_URL + "/deleteBike";
    public static String BUY_BIKE_URL = Constants.BASE_URL + "/buyBike";
    public static String RENT_BIKE_URL = Constants.BASE_URL + "/rentBike";
    public static String RETURN_BIKE_URL = Constants.BASE_URL + "/returnBike";

    public static String ADMIN_ROLE = "admin";
}
