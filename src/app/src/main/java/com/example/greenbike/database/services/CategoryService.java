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
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constants;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kotlin.jvm.functions.Function3;

public class CategoryService {
    public static void create(String name, Activity origin) {
        if (Validator.isNullOrEmpty(name)) {
            Toast.makeText(origin, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.CREATE_CATEGORY_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_categories);
        }, error -> Toast.makeText(origin, Messages.CREATE_CATEGORY_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                UUID id = UUID.randomUUID();

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.toString());
                params.put("name", name);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void getAll(View root, Integer listId, Function3<View, ArrayList<BikeCategory>, Integer, View> callBackFunction) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, null,
                response -> {
                    try {
                        ArrayList<BikeCategory> allBikeCategories = new ArrayList<>();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                            allBikeCategories.add(data);
                        }

                        callBackFunction.invoke(root, allBikeCategories, listId);
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

    public static void update(String name, String id, Activity origin) {
        if (Validator.isNullOrEmpty(name)) {
            Toast.makeText(origin, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.EDIT_CATEGORY_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_categories);
        }, error -> Toast.makeText(origin, Messages.EDIT_CATEGORY_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("name", name);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void delete(String id, Activity origin) {
        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.DELETE_CATEGORY_URL, response -> {
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_categories);
        }, error -> Toast.makeText(origin, Messages.DELETE_CATEGORY_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", id);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}
