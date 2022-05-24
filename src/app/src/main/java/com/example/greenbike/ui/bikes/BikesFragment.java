package com.example.greenbike.ui.bikes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeAdapter;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.databinding.FragmentBikesBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BikesFragment extends Fragment {
    private FragmentBikesBinding binding;
    private final ArrayList<Bike> allBikes;
    private ArrayList<BikeMaterial> bikeMaterials;
    private ArrayList<BikeCategory> bikeCategories;
    private ArrayList<BikeBrand> bikeBrands;

    private View root;

    public BikesFragment() {
        this.allBikes = new ArrayList<Bike>();
        this.bikeMaterials = new ArrayList<BikeMaterial>();
        this.bikeCategories = new ArrayList<BikeCategory>();
        this.bikeBrands = new ArrayList<BikeBrand>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBikesBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        this.getAllBikeBrands();
        this.getAllBikeMaterials();
        this.getAllBikeCategories();

        Button createButton = root.findViewById(R.id.createBikePlusButton);
        createButton.setTag(root);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity origin = (Activity)root.getContext();
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_bikes_create);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getAllBikes() {
        boolean isDataNotReady = this.bikeBrands.size() == 0 ||
                this.bikeMaterials.size() == 0 ||
                this.bikeCategories.size() == 0;

        if (isDataNotReady) {
            return;
        }

        Activity origin = (Activity)this.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_BIKES_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            BikesFragment.this.allBikes.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                Bike data = gson.fromJson(String.valueOf(jsonObject), Bike.class);
                                data.setImageURL(jsonObject.getString("image_url"));
                                data.setBrandId(jsonObject.getString("brand_id"));
                                data.setMaterialId(jsonObject.getString("material_id"));
                                data.setCategoryId(jsonObject.getString("category_id"));
                                data.setIsForRent(jsonObject.getString("is_for_rent").equals("1"));

                                BikesFragment.this.allBikes.add(data);
                            }

                            BikesFragment.this.fillFragments();
                        }
                        catch(JSONException e)
                        {
                            Log.e(ExceptionMessages.DATABASE_ERROR_TAG, e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(origin, ExceptionMessages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    private void fillFragments() {
        for (int index = 0; index < this.allBikes.size(); index++) {
            Bike bike = this.allBikes.get(index);

            bike.setBikeBrand(this.bikeBrands.stream()
                    .filter(brand -> brand.getId().equals(bike.getBrandId()))
                    .findFirst()
                    .get());

            bike.setBikeCategory(this.bikeCategories.stream()
                    .filter(bc -> bc.getId().equals(bike.getCategoryId()))
                    .findFirst()
                    .get());

            bike.setBikeMaterial(this.bikeMaterials.stream()
                    .filter(bm -> bm.getId().equals(bike.getMaterialId()))
                    .findFirst()
                    .get());
        }

        BikeAdapter adapter = new BikeAdapter(this.getContext(), this.allBikes);

        ListView listView = this.root.findViewById(R.id.adminBikesList);
        listView.setAdapter(adapter);
    }

    private void getAllBikeBrands() {
        Activity origin = (Activity)this.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_BRANDS_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            BikesFragment.this.bikeBrands.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeBrand data = gson.fromJson(String.valueOf(jsonObject), BikeBrand.class);

                                BikesFragment.this.bikeBrands.add(data);
                            }

                            BikesFragment.this.getAllBikes();
                        }
                        catch(JSONException e)
                        {
                            Log.e(ExceptionMessages.DATABASE_ERROR_TAG, e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(origin, ExceptionMessages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    private void getAllBikeMaterials() {
        Activity origin = (Activity)this.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_MATERIALS_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            BikesFragment.this.bikeMaterials.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                                BikesFragment.this.bikeMaterials.add(data);
                            }

                            BikesFragment.this.getAllBikes();
                        }
                        catch(JSONException e)
                        {
                            Log.e(ExceptionMessages.DATABASE_ERROR_TAG, e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(origin, ExceptionMessages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    private void getAllBikeCategories() {
        Activity origin = (Activity)this.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_CATEGORIES_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            BikesFragment.this.bikeCategories.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                                BikesFragment.this.bikeCategories.add(data);
                            }

                            BikesFragment.this.getAllBikes();
                        }
                        catch(JSONException e)
                        {
                            Log.e(ExceptionMessages.DATABASE_ERROR_TAG, e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(origin, ExceptionMessages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}