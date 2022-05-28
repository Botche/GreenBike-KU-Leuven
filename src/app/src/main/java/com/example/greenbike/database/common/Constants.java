package com.example.greenbike.database.common;

public class Constants {

    public static final String BASE_URL = "https://studev.groept.be/api/a21pt104";

    public static final String GET_ALL_USER_ROLES = Constants.BASE_URL + "/getAllUserRoles";
    public static final String REGISTER_USER = Constants.BASE_URL + "/register";
    public static final String LOGIN_USER = Constants.BASE_URL + "/login/%s";

    public static String GET_ALL_USER_BIKES = Constants.BASE_URL + "/getAllUserBikes/%s";

    public static final String GET_BRANDS_URL = Constants.BASE_URL + "/getAllBikeBrands";
    public static final String CREATE_BRAND_URL = Constants.BASE_URL + "/createBikeBrand";
    public static final String EDIT_BRAND_URL = Constants.BASE_URL + "/editBikeBrand";
    public static final String DELETE_BRAND_URL = Constants.BASE_URL + "/deleteBikeBrand";

    public static final String GET_CATEGORIES_URL = Constants.BASE_URL + "/getAllBikeCategories";
    public static final String CREATE_CATEGORY_URL = Constants.BASE_URL + "/createBikeCategory";
    public static final String EDIT_CATEGORY_URL = Constants.BASE_URL + "/editBikeCategory";
    public static final String DELETE_CATEGORY_URL = Constants.BASE_URL + "/deleteBikeCategory";

    public static final String GET_MATERIALS_URL = Constants.BASE_URL + "/getAllBikeMaterials";
    public static final String CREATE_MATERIAL_URL = Constants.BASE_URL + "/createBikeMaterial";
    public static final String EDIT_MATERIAL_URL = Constants.BASE_URL + "/editBikeMaterial";
    public static final String DELETE_MATERIAL_URL = Constants.BASE_URL + "/deleteBikeMaterial";

    public static final String GET_BIKES_URL = Constants.BASE_URL + "/getAllBikes";
    public static final String CREATE_BIKE_URL = Constants.BASE_URL + "/createBike";
    public static final String EDIT_BIKE_URL = Constants.BASE_URL + "/editBike";
    public static final String DELETE_BIKE_URL = Constants.BASE_URL + "/deleteBike";
    public static final String BUY_BIKE_URL = Constants.BASE_URL + "/buyBike";
    public static final String RENT_BIKE_URL = Constants.BASE_URL + "/rentBike";
    public static final String RETURN_BIKE_URL = Constants.BASE_URL + "/returnBike";

    public static final String ADMIN_ROLE = "admin";
}
