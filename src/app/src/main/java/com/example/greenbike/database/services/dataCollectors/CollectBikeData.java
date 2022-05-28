package com.example.greenbike.database.services.dataCollectors;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.common.BikeFilterOptions;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Messages;
import com.example.greenbike.database.common.Constants;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.database.models.mapping.UserBikeMapping;
import com.example.greenbike.database.services.BikeService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CollectBikeData implements ICollectBikeData {
    protected final ArrayList<BikeMaterial> bikeMaterials;
    protected final ArrayList<BikeCategory> bikeCategories;
    protected final ArrayList<BikeBrand> bikeBrands;

    protected final int listId;

    protected final ArrayAdapter<Bike> bikeAdapter;

    public CollectBikeData(int listId, ArrayAdapter<Bike> bikeAdapter) {
        this.listId = listId;
        this.bikeAdapter = bikeAdapter;

        this.bikeBrands = new ArrayList<>();
        this.bikeCategories = new ArrayList<>();
        this.bikeMaterials = new ArrayList<>();
    }

    public View fillFragments(View root, ArrayList<Bike> allBikes, Integer bikeListId) {
        ArrayList<Bike> filledAllBikes = this.fillDataInBikes(allBikes);

        this.bikeAdapter.addAll(filledAllBikes);
        ListView listView = root.findViewById(bikeListId);
        listView.setAdapter(this.bikeAdapter);

        return root;
    }

    public void getAllData(View root) {
        this.getAllBikeBrands(root, BikeFilterOptions.All);
        this.getAllBikeCategories(root, BikeFilterOptions.All);
        this.getAllBikeMaterials(root, BikeFilterOptions.All);
    };

    public void getAllData(View root, BikeFilterOptions filterOptions) {
        this.getAllBikeBrands(root, filterOptions);
        this.getAllBikeCategories(root, filterOptions);
        this.getAllBikeMaterials(root, filterOptions);
    }

    public boolean checkIfDataIsReady() {
        return this.bikeBrands.size() != 0
                && this.bikeMaterials.size() != 0
                && this.bikeCategories.size() != 0;
    }

    protected ArrayList<Bike> fillDataInBikes(ArrayList<Bike> allBikes) {
        ArrayList<Bike> copiedAllBikes = new ArrayList<>(allBikes);

        for (int index = 0; index < copiedAllBikes.size(); index++) {
            Bike bike = copiedAllBikes.get(index);

            this.bikeBrands.stream()
                    .filter(brand -> brand.getId().equals(bike.getBrandId()))
                    .findFirst()
                    .ifPresent(bike::setBikeBrand);

            this.bikeCategories.stream()
                    .filter(bc -> bc.getId().equals(bike.getCategoryId()))
                    .findFirst()
                    .ifPresent(bike::setBikeCategory);

            this.bikeMaterials.stream()
                    .filter(bm -> bm.getId().equals(bike.getMaterialId()))
                    .findFirst()
                    .ifPresent(bike::setBikeMaterial);
        }

        return copiedAllBikes;
    }

    protected void getAllBikeBrands(View root, BikeFilterOptions filterOptions) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_BRANDS_URL, null,
                response -> {
                    try {
                        this.bikeBrands.clear();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            BikeBrand data = gson.fromJson(String.valueOf(jsonObject), BikeBrand.class);

                            this.bikeBrands.add(data);
                        }

                        if (this.checkIfDataIsReady()) {
                            BikeService.getAll(filterOptions, root, this.listId, this::fillFragments);
                        }
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

    protected void getAllBikeBrands(View root, ArrayList<UserBikeMapping> userBikes) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_BRANDS_URL, null,
                response -> {
                    try {
                        bikeBrands.clear();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            BikeBrand data = gson.fromJson(String.valueOf(jsonObject), BikeBrand.class);

                            bikeBrands.add(data);
                        }

                        if (checkIfDataIsReady()) {
                            BikeService.getAll(userBikes, root, listId, this::fillFragments);
                        }
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

    protected void getAllBikeMaterials(View root, BikeFilterOptions filterOptions) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_MATERIALS_URL, null,
                response -> {
                    try {
                        this.bikeMaterials.clear();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                            this.bikeMaterials.add(data);
                        }

                        if (this.checkIfDataIsReady()) {
                            BikeService.getAll(filterOptions, root, this.listId, this::fillFragments);
                        }
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

    protected void getAllBikeMaterials(View root, ArrayList<UserBikeMapping> userBikes) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_MATERIALS_URL, null,
                response -> {
                    try {
                        bikeMaterials.clear();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                            bikeMaterials.add(data);
                        }

                        if (checkIfDataIsReady()) {
                            BikeService.getAll(userBikes, root, listId, this::fillFragments);
                        }
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

    protected void getAllBikeCategories(View root, BikeFilterOptions filterOptions) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, null,
                response -> {
                    try {
                        this.bikeCategories.clear();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                            this.bikeCategories.add(data);
                        }

                        if (this.checkIfDataIsReady()) {
                            BikeService.getAll(filterOptions, root, this.listId, this::fillFragments);
                        }
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

    protected void getAllBikeCategories(View root, ArrayList<UserBikeMapping> userBikes) {
        Activity origin = (Activity)root.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_CATEGORIES_URL, null,
                response -> {
                    try {
                        bikeCategories.clear();

                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                            bikeCategories.add(data);
                        }

                        if (checkIfDataIsReady()) {
                            BikeService.getAll(userBikes, root, listId, this::fillFragments);
                        }
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
}
