package com.example.greenbike.ui.categories;

import android.app.Activity;
import android.os.Bundle;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeCategoryAdapter;
import com.example.greenbike.common.VolleyRequestQueue;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.databinding.FragmentCategoriesBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    private FragmentCategoriesBinding binding;
    private ArrayList<BikeCategory> allBikeCategories;
    private View root;
    private RequestQueue requestQueue;

    public CategoriesFragment() {
        this.allBikeCategories = new ArrayList<BikeCategory>();
    }

    public CategoriesFragment(ArrayList<BikeCategory> allBikeCategories) {
        this.allBikeCategories = allBikeCategories;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        this.getAllBikeCategories();

        Button createButton = root.findViewById(R.id.createBikeCategoryPlusButton);
        createButton.setTag(root);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity origin = (Activity)root.getContext();

                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_categories_create);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getAllBikeCategories() {
        Activity origin = (Activity)this.getContext();

        VolleyRequestQueue volleyRequestQueue = VolleyRequestQueue.getInstance(origin);
        requestQueue = volleyRequestQueue.getRequestQueue();

        String requestURL = Constatants.BASE_URL + "/getAllBikeCategories";

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            CategoriesFragment.this.allBikeCategories.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeCategory data = gson.fromJson(String.valueOf(jsonObject), BikeCategory.class);

                                CategoriesFragment.this.allBikeCategories.add(data);
                            }

                            CategoriesFragment.this.fillFragments();
                        }
                        catch(JSONException e)
                        {
                            Log.e("Database", e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(origin, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    private void fillFragments() {
        BikeCategoryAdapter adapter = new BikeCategoryAdapter(this.getContext(), this.allBikeCategories);

        ListView listView = this.root.findViewById(R.id.bikeCategoryList);
        listView.setAdapter(adapter);
    }
}