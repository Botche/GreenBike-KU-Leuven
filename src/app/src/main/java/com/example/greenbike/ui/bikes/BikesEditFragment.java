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
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BaseBike;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.databinding.FragmentBikesEditBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BikesEditFragment extends Fragment {
    private FragmentBikesEditBinding binding;
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
    private EditText editBikeURLInput;
    private EditText priceInput;

    private Bike bike;

    private int spinnerPosition = 2;

    public BikesEditFragment() {
        this.bikeMaterials = new ArrayList<BikeMaterial>();
        this.bikeCategories = new ArrayList<BikeCategory>();
        this.bikeBrands = new ArrayList<BikeBrand>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBikesEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.bikeBrandsSelect = root.findViewById(R.id.spinnerBikeBrandsEdit);
        this.bikeBrandsSelectedIdInput = root.findViewById(R.id.editBikeSelectedBrandId);

        this.bikeMaterialsSelect = root.findViewById(R.id.spinnerBikeMaterialsEdit);
        this.bikeMaterialsSelectedIdInput = root.findViewById(R.id.editBikeSelectedMaterialId);

        this.bikeCategoriesSelect = root.findViewById(R.id.spinnerBikeCategoriesEdit);
        this.bikeCategoriesSelectedIdInput = root.findViewById(R.id.editBikeSelectedCategoryId);

        this.isForRentInput = root.findViewById(R.id.bikeEditIsForRent);
        this.modelInput = root.findViewById(R.id.editBikeModel);
        this.editBikeURLInput = root.findViewById(R.id.editBikeURL);
        this.priceInput = root.findViewById(R.id.editBikePrice);

        this.getAllBikeBrands();
        this.getAllBikeMaterials();
        this.getAllBikeCategories();

        Bundle bundle = getArguments();
        if(bundle != null) {
            this.bike = (Bike) bundle.getSerializable("Bike");
        }

        this.modelInput.setText(bike.getModel());
        this.isForRentInput.setChecked(bike.getIsForRent());
        this.editBikeURLInput.setText(bike.getImageURL());
        this.priceInput.setText(String.valueOf(bike.getPrice()));
        this.bikeBrandsSelectedIdInput.setText(bike.getBrandId());
        this.bikeMaterialsSelectedIdInput.setText(bike.getMaterialId());
        this.bikeCategoriesSelectedIdInput.setText(bike.getCategoryId());


        Button createButton = root.findViewById(R.id.editBikeButton);
        createButton.setTag(root);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikesEditFragment.this.onEdit(v);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onEdit(View v) {
        String id = this.bike.getId();
        String model = this.modelInput.getText().toString();
        String imageURL = this.editBikeURLInput.getText().toString();
        String isForRent = String.valueOf(this.isForRentInput.isChecked() ? 1 : 0);
        String brandId = this.bikeBrandsSelectedIdInput.getText().toString();
        String materialId = this.bikeMaterialsSelectedIdInput.getText().toString();
        String categoryId = this.bikeCategoriesSelectedIdInput.getText().toString();
        String price = this.priceInput.getText().toString();

        Activity origin = (Activity)this.getContext();

        boolean isInvalid = Validator.isNullOrEmpty(model) || Validator.isNullOrEmpty(imageURL) ||
                Validator.isNullOrEmpty(brandId) || Validator.isNullOrEmpty(materialId) ||
                Validator.isNullOrEmpty(categoryId);

        if (isInvalid) {
            Toast.makeText(origin, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.EDIT_BIKE_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_bikes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.EDIT_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
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

    private void getAllBikeBrands() {
        Activity origin = (Activity)this.getContext();

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_BRANDS_URL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            BikesEditFragment.this.bikeBrands.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeBrand data = gson.fromJson(String.valueOf(jsonObject), BikeBrand.class);

                                BikesEditFragment.this.bikeBrands.add(data);
                            }

                            BikesEditFragment.this.fillBikeBrandsFragments();
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

        int spinnerPosition = this.indexOfAdapter(adapter, this.bike.getBikeBrand());
        this.bikeBrandsSelect.setSelection(spinnerPosition);
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
                            BikesEditFragment.this.bikeMaterials.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                                BikesEditFragment.this.bikeMaterials.add(data);
                            }

                            BikesEditFragment.this.fillBikeMaterialsFragments();
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

        int spinnerPosition = this.indexOfAdapter(adapter, this.bike.getBikeMaterial());
        this.bikeBrandsSelect.setSelection(spinnerPosition);
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
                            BikesEditFragment.this.bikeCategories.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                                BikesEditFragment.this.bikeCategories.add(data);
                            }

                            BikesEditFragment.this.fillBikeCategoriesFragments();
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

        int spinnerPosition = this.indexOfAdapter(adapter, this.bike.getBikeCategory());
        this.bikeBrandsSelect.setSelection(spinnerPosition);
    }

    private int indexOfAdapter(ArrayAdapter adapter, BaseBike baseBike)
    {
        int indexOf = -1;

        for (int index = 0, count = adapter.getCount(); index < count; ++index)
        {
            BaseBike baseBikeAdapterItem = (BaseBike)adapter.getItem(index);

            if (baseBikeAdapterItem.getId().equals(baseBike.getId()))
            {
                indexOf = index;
                break;
            }
        }

        return indexOf;
    }
}