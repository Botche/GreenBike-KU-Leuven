package com.example.greenbike.ui.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.R;
import com.example.greenbike.adapters.UserBikeAdapter;
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
import com.example.greenbike.databinding.FragmentMineBikesBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MineBikesFragment extends Fragment {
    private static final ArrayList<BikeMaterial> bikeMaterials = new ArrayList<>();
    private static final ArrayList<BikeCategory> bikeCategories =  new ArrayList<>();
    private static final ArrayList<BikeBrand> bikeBrands = new ArrayList<>();
    private static final ArrayList<UserBikeMapping> userBikes = new ArrayList<>();

    private FragmentMineBikesBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMineBikesBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        getUserBikes();
        getAllBikeBrands();
        getAllBikeCategories();
        getAllBikeMaterials();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static View fillFragments(View root, ArrayList<Bike> allBikes, Integer bikeListId) {
        Context context = root.getContext();
        for (int index = 0; index < allBikes.size(); index++) {
            Bike bike = allBikes.get(index);

            bike.setBikeBrand(bikeBrands.stream()
                    .filter(brand -> brand.getId().equals(bike.getBrandId()))
                    .findFirst()
                    .get());

            bike.setBikeCategory(bikeCategories.stream()
                    .filter(bc -> bc.getId().equals(bike.getCategoryId()))
                    .findFirst()
                    .get());

            bike.setBikeMaterial(bikeMaterials.stream()
                    .filter(bm -> bm.getId().equals(bike.getMaterialId()))
                    .findFirst()
                    .get());
        }

        UserBikeAdapter adapter = new UserBikeAdapter(context, allBikes);

        ListView listView = root.findViewById(bikeListId);
        listView.setAdapter(adapter);

        return root;
    }

    private void getUserBikes() {
        Activity origin = (Activity)this.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, String.format(Constants.GET_ALL_USER_BIKES, Global.currentUser.getId()), null,
                response -> {
                    try {
                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            UserBikeMapping data = gson.fromJson(String.valueOf(jsonObject), UserBikeMapping.class);
                            data.setUserId(jsonObject.getString("user_id"));
                            data.setBikeId(jsonObject.getString("bike_id"));

                            userBikes.add(data);
                        }

                        boolean isDataReady = bikeBrands.size() != 0 &&
                                bikeMaterials.size() != 0 &&
                                bikeCategories.size() != 0 &&
                                userBikes.size() != 0;

                        if (isDataReady) {
                            BikeService.getAll(userBikes, root, R.id.userBikeList, MineBikesFragment::fillFragments);
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

    private void getAllBikeBrands() {
        Activity origin = (Activity)this.getContext();

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

                        boolean isDataReady = bikeBrands.size() != 0 &&
                                bikeMaterials.size() != 0 &&
                                bikeCategories.size() != 0 &&
                                userBikes.size() != 0;

                        if (isDataReady) {
                            BikeService.getAll(userBikes, root, R.id.userBikeList, MineBikesFragment::fillFragments);
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

    private void getAllBikeMaterials() {
        Activity origin = (Activity)this.getContext();

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

                        boolean isDataReady = bikeBrands.size() != 0 &&
                                bikeMaterials.size() != 0 &&
                                bikeCategories.size() != 0 &&
                                userBikes.size() != 0;

                        if (isDataReady) {
                            BikeService.getAll(userBikes, root, R.id.userBikeList, MineBikesFragment::fillFragments);
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

    private void getAllBikeCategories() {
        Activity origin = (Activity)this.getContext();

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

                        boolean isDataReady = bikeBrands.size() != 0 &&
                                bikeMaterials.size() != 0 &&
                                bikeCategories.size() != 0 &&
                                userBikes.size() != 0;

                        if (isDataReady) {
                            BikeService.getAll(userBikes, root, R.id.userBikeList, MineBikesFragment::fillFragments);
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