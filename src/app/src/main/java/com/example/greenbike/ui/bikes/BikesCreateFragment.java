package com.example.greenbike.ui.bikes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeBrandAdapter;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.databinding.FragmentBikesBinding;
import com.example.greenbike.databinding.FragmentBikesCreateBinding;
import com.example.greenbike.ui.brands.BrandsCreateFragment;
import com.example.greenbike.ui.brands.BrandsFragment;
import com.example.greenbike.ui.categories.CategoriesFragment;
import com.example.greenbike.ui.materials.MaterialsFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BikesCreateFragment extends Fragment {
    private FragmentBikesCreateBinding binding;
    private ArrayList<BikeMaterial> bikeMaterials;
    private ArrayList<BikeCategory> bikeCategories;
    private ArrayList<BikeBrand> bikeBrands;

    private Spinner bikeBrandsSelect;
    private TextView bikeBrandsSelectedIdInput;

    private Spinner bikeMaterialsSelect;
    private TextView bikeMaterialsSelectedIdInput;

    private Spinner bikeCategoriesSelect;
    private TextView bikeCategoriesSelectedIdInput;

    private Switch isForRentInput;
    private EditText modelInput;
    private EditText createBikeURLInput;
    private EditText priceInput;

    public BikesCreateFragment() {
        this.bikeMaterials = new ArrayList<BikeMaterial>();
        this.bikeCategories = new ArrayList<BikeCategory>();
        this.bikeBrands = new ArrayList<BikeBrand>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBikesCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.bikeBrandsSelect = root.findViewById(R.id.spinnerBikeBrands);
        this.bikeBrandsSelectedIdInput = root.findViewById(R.id.createBikeSelectedBrandId);

        this.bikeMaterialsSelect = root.findViewById(R.id.spinnerBikeMaterials);
        this.bikeMaterialsSelectedIdInput = root.findViewById(R.id.createBikeSelectedMaterialId);

        this.bikeCategoriesSelect = root.findViewById(R.id.spinnerBikeCategories);
        this.bikeCategoriesSelectedIdInput = root.findViewById(R.id.createBikeSelectedCategoryId);

        this.isForRentInput = root.findViewById(R.id.bikeCreateIsForRent);
        this.modelInput = root.findViewById(R.id.createBikeModel);
        this.createBikeURLInput = root.findViewById(R.id.createBikeURL);
        this.priceInput = root.findViewById(R.id.createBikePrice);

        this.getAllBikeBrands();
        this.getAllBikeMaterials();
        this.getAllBikeCategories();

        Button createButton = root.findViewById(R.id.createBikeButton);
        createButton.setTag(root);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikesCreateFragment.this.onCreate(v);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onCreate(View v) {
        String model = this.modelInput.getText().toString();
        String imageURL = this.createBikeURLInput.getText().toString();
        Boolean isForRent = this.isForRentInput.isChecked();
        String brandId = this.bikeBrandsSelectedIdInput.getText().toString();
        String materialId = this.bikeMaterialsSelectedIdInput.getText().toString();
        String categoryId = this.bikeCategoriesSelectedIdInput.getText().toString();
        String price = this.priceInput.getText().toString();

        Activity origin = (Activity)this.getContext();

        boolean isInvalid = Validator.isNullOrEmpty(model) || Validator.isNullOrEmpty(imageURL) ||
                Validator.isNullOrEmpty(brandId) || Validator.isNullOrEmpty(materialId) ||
                Validator.isNullOrEmpty(categoryId);

        if (isInvalid) {
            Toast.makeText(origin, ExceptionMessages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.CREATE_BIKE_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_bikes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, ExceptionMessages.CREATE_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
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
                params.put("isforrent", isForRent.toString());
                params.put("price", price);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
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
                            BikesCreateFragment.this.bikeBrands.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeBrand data = gson.fromJson(String.valueOf(jsonObject), BikeBrand.class);

                                BikesCreateFragment.this.bikeBrands.add(data);
                            }

                            BikesCreateFragment.this.fillBikeBrandsFragments();
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

    private void fillBikeBrandsFragments() {
        final BikeBrand items[] = this.bikeBrands.toArray(new BikeBrand[0]);

        ArrayAdapter<BikeBrand> adapter = new ArrayAdapter<BikeBrand>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.bikeBrandsSelect.setAdapter(adapter);

        this.bikeBrandsSelect.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BikeBrand bikeBrand = items[position];
                        bikeBrandsSelectedIdInput.setText(bikeBrand.getId());
                    }

                    public void onNothingSelected(AdapterView<?> parent) { }
                }
        );
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
                            BikesCreateFragment.this.bikeMaterials.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                                BikesCreateFragment.this.bikeMaterials.add(data);
                            }

                            BikesCreateFragment.this.fillBikeMaterialsFragments();
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

    private void fillBikeMaterialsFragments() {
        final BikeMaterial items[] = this.bikeMaterials.toArray(new BikeMaterial[0]);

        ArrayAdapter<BikeMaterial> adapter = new ArrayAdapter<BikeMaterial>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.bikeMaterialsSelect.setAdapter(adapter);

        this.bikeMaterialsSelect.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BikeMaterial bikeMaterial = items[position];
                        bikeMaterialsSelectedIdInput.setText(bikeMaterial.getId());
                    }

                    public void onNothingSelected(AdapterView<?> parent) { }
                }
        );
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
                            BikesCreateFragment.this.bikeCategories.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                                BikesCreateFragment.this.bikeCategories.add(data);
                            }

                            BikesCreateFragment.this.fillBikeCategoriesFragments();
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

    private void fillBikeCategoriesFragments() {
        final BikeCategory items[] = this.bikeCategories.toArray(new BikeCategory[0]);

        ArrayAdapter<BikeCategory> adapter = new ArrayAdapter<BikeCategory>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.bikeCategoriesSelect.setAdapter(adapter);

        this.bikeCategoriesSelect.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BikeCategory bikeCategory = items[position];
                        bikeCategoriesSelectedIdInput.setText(bikeCategory.getId());
                    }

                    public void onNothingSelected(AdapterView<?> parent) { }
                }
        );
    }
}