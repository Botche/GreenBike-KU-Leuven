package com.example.greenbike.database.services;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.R;
import com.example.greenbike.common.BikeFilterOptions;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constants;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.mapping.UserBikeMapping;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kotlin.jvm.functions.Function3;

public class BikeService {
    public static void create(String model, String imageURL, String isForRent, String brandId, String materialId, String categoryId,String price, Activity origin) {
        boolean isInvalid = Validator.isNullOrEmpty(model) || Validator.isNullOrEmpty(imageURL) ||
                Validator.isNullOrEmpty(brandId) || Validator.isNullOrEmpty(materialId) ||
                Validator.isNullOrEmpty(categoryId);

        if (isInvalid) {
            Toast.makeText(origin, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.CREATE_BIKE_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_bikes);
        }, error -> Toast.makeText(origin, Messages.CREATE_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                UUID id = UUID.randomUUID();

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.toString());
                params.put("model", model);
                params.put("brandid", brandId);
                params.put("materialid", materialId);
                params.put("categoryid", categoryId);
                params.put("imageurl", imageURL);
                params.put("isforrent", isForRent);
                params.put("price", price);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void getAll(BikeFilterOptions filterOptions, View root, Integer listId, Function3<View, ArrayList<Bike>, Integer, View> callBackFunction) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_BIKES_URL, null,
                response -> {
                    try {
                        ArrayList<Bike> allBikes = new ArrayList<>();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            Bike data = gson.fromJson(String.valueOf(jsonObject), Bike.class);
                            data.setImageURL(jsonObject.getString("image_url"));
                            data.setBrandId(jsonObject.getString("brand_id"));
                            data.setMaterialId(jsonObject.getString("material_id"));
                            data.setCategoryId(jsonObject.getString("category_id"));
                            data.setIsForRent(jsonObject.getString("is_for_rent").equals("1"));
                            data.setTaken(jsonObject.getString("is_taken").equals("1"));

                            if (filterOptions == BikeFilterOptions.All) {
                                allBikes.add(data);
                                continue;
                            }

                            if (!data.getTaken() && filterOptions.getStringValue().equals("1") == data.getIsForRent()) {
                                allBikes.add(data);
                            }
                        }

                        callBackFunction.invoke(root, allBikes, listId);
                    }
                    catch(JSONException e)
                    {
                        Log.e(Messages.DATABASE_ERROR_TAG, e.getMessage(), e);
                    }
                },
                error -> Toast.makeText(origin, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void getAll(ArrayList<UserBikeMapping> userBikeMappings, View root, Integer listId, Function3<View, ArrayList<Bike>, Integer, View> callBackFunction) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_BIKES_URL, null,
                response -> {
                    try {
                        ArrayList<Bike> allBikes = new ArrayList<>();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            Bike data = gson.fromJson(String.valueOf(jsonObject), Bike.class);
                            data.setImageURL(jsonObject.getString("image_url"));
                            data.setBrandId(jsonObject.getString("brand_id"));
                            data.setMaterialId(jsonObject.getString("material_id"));
                            data.setCategoryId(jsonObject.getString("category_id"));
                            data.setIsForRent(jsonObject.getString("is_for_rent").equals("1"));
                            data.setTaken(jsonObject.getString("is_taken").equals("1"));

                            for (UserBikeMapping userBikeMapping : userBikeMappings) {
                                if (data.getTaken()
                                    && userBikeMapping.getBikeId().equals(data.getId())
                                    && userBikeMapping.getUserId().equals(Global.currentUser.getId())) {
                                    allBikes.add(data);
                                    break;
                                }
                            }
                        }

                        callBackFunction.invoke(root, allBikes, listId);
                    }
                    catch(JSONException e)
                    {
                        Log.e(Messages.DATABASE_ERROR_TAG, e.getMessage(), e);
                    }
                },
                error -> Toast.makeText(origin, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void update(String id, String model, String imageURL, String isForRent, String brandId, String materialId, String categoryId,String price, Activity origin) {
        boolean isInvalid = Validator.isNullOrEmpty(model) || Validator.isNullOrEmpty(imageURL) ||
                Validator.isNullOrEmpty(brandId) || Validator.isNullOrEmpty(materialId) ||
                Validator.isNullOrEmpty(categoryId);

        if (isInvalid) {
            Toast.makeText(origin, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.EDIT_BIKE_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_bikes);
        }, error -> Toast.makeText(origin, Messages.EDIT_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("model", model);
                params.put("brandid", brandId);
                params.put("materialid", materialId);
                params.put("categoryid", categoryId);
                params.put("imageurl", imageURL);
                params.put("isforrent", isForRent);
                params.put("price", price);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void delete(String id, Activity origin) {
        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.DELETE_BIKE_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_bikes);
        }, error -> Toast.makeText(origin, Messages.DELETE_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void buyBike(View view, String bikeId) {
        Activity origin = (Activity)view.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.BUY_BIKE_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_user_home);
            navController.navigate(R.id.navigation_for_buy);

            Toast.makeText(origin, Messages.SUCCESSFULLY_BOUGHT_BIKE, Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(origin, Messages.BUY_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                UUID id = UUID.randomUUID();
                LocalDateTime now = LocalDateTime.now();

                Map<String, String> params = new HashMap<>();

                params.put("id", id.toString());
                params.put("userid", Global.currentUser.getId());
                params.put("bikeid", bikeId);
                params.put("buydate", Global.dateTimeFormatter.format(now));

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void rentBike(View view, String bikeId) {
        Activity origin = (Activity)view.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.RENT_BIKE_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_user_home);
            navController.navigate(R.id.navigation_for_rent);

            Toast.makeText(origin, Messages.SUCCESSFULLY_RENTED_BIKE, Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(origin, Messages.RENT_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                UUID id = UUID.randomUUID();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime nowPlusOneMonth = LocalDateTime.now().plusMonths(1);

                Map<String, String> params = new HashMap<>();

                params.put("id", id.toString());
                params.put("userid", Global.currentUser.getId());
                params.put("bikeid", bikeId);
                params.put("rentstartdate", Global.dateTimeFormatter.format(now));
                params.put("rentenddate", Global.dateTimeFormatter.format(nowPlusOneMonth));

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void returnBike(View view, String bikeId) {
        Activity origin = (Activity)view.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.RETURN_BIKE_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_user_home);
            navController.navigate(R.id.navigation_mine);

            Toast.makeText(origin, Messages.SUCCESSFULLY_RETURNED_BIKE, Toast.LENGTH_LONG).show();
        }, error -> Toast.makeText(origin, Messages.RETURN_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("bikeid", bikeId);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}
