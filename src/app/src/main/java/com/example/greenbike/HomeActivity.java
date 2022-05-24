package com.example.greenbike;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.adapters.UserBikeAdapter;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private final ArrayList<Bike> allBikes;
    private final ArrayList<BikeMaterial> bikeMaterials;
    private final ArrayList<BikeCategory> bikeCategories;
    private final ArrayList<BikeBrand> bikeBrands;

    public HomeActivity() {
        this.allBikes = new ArrayList<>();
        this.bikeMaterials = new ArrayList<>();
        this.bikeCategories = new ArrayList<>();
        this.bikeBrands = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.getAllBikeBrands();
        this.getAllBikeMaterials();
        this.getAllBikeCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.logoutLink) {
            Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(myIntent);
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllBikes() {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_BIKES_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            allBikes.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                Bike data = gson.fromJson(String.valueOf(jsonObject), Bike.class);

                                if (jsonObject.getString("is_taken").equals("1")) {
                                    continue;
                                }

                                data.setImageURL(jsonObject.getString("image_url"));
                                data.setBrandId(jsonObject.getString("brand_id"));
                                data.setMaterialId(jsonObject.getString("material_id"));
                                data.setCategoryId(jsonObject.getString("category_id"));
                                data.setIsForRent(jsonObject.getString("is_for_rent").equals("1"));
                                data.setTaken(jsonObject.getString("is_taken").equals("1"));

                                allBikes.add(data);
                            }

                            fillFragments();
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
                        Toast.makeText(HomeActivity.this, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
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

        UserBikeAdapter adapter = new UserBikeAdapter(HomeActivity.this, this.allBikes);

        ListView listView = findViewById(R.id.userBikesList);
        listView.setAdapter(adapter);
    }

    private void getAllBikeBrands() {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_BRANDS_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            bikeBrands.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeBrand data = gson.fromJson(String.valueOf(jsonObject), BikeBrand.class);

                                bikeBrands.add(data);
                            }

                            getAllBikes();
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
                        Toast.makeText(HomeActivity.this, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    private void getAllBikeMaterials() {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_MATERIALS_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            bikeMaterials.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                                bikeMaterials.add(data);
                            }

                            getAllBikes();
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
                        Toast.makeText(HomeActivity.this, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    private void getAllBikeCategories() {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_CATEGORIES_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            bikeCategories.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                                bikeCategories.add(data);
                            }

                            getAllBikes();
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
                        Toast.makeText(HomeActivity.this, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}