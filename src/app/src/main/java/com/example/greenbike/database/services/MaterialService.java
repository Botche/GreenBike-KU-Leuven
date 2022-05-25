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
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kotlin.jvm.functions.Function3;

public class MaterialService {
    public static void create(String name, Activity origin) {
        if (Validator.isNullOrEmpty(name)) {
            Toast.makeText(origin, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.CREATE_MATERIAL_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_materials);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.CREATE_MATERIAL_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
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

    public static void getAll(View root, Integer listId, Function3<View, ArrayList<BikeMaterial>, Integer, View> callBackFunction) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_MATERIALS_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            ArrayList<BikeMaterial> allBikeMaterials = new ArrayList<>();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                                allBikeMaterials.add(data);
                            }

                            callBackFunction.invoke(root, allBikeMaterials, listId);
                        }
                        catch(JSONException e)
                        {
                            Log.e(Messages.DATABASE_ERROR_TAG, e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(origin, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void update(String name, String id, Activity origin) {
        if (Validator.isNullOrEmpty(name)) {
            Toast.makeText(origin, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.EDIT_MATERIAL_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_materials);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.EDIT_MATERIAL_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("name", name);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    public static void delete(String id, Activity origin) {
        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constants.DELETE_MATERIAL_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_materials);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.DELETE_MATERIAL_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}
