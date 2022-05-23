package com.example.greenbike.ui.materials;

import android.app.Activity;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeMaterialAdapter;
import com.example.greenbike.common.VolleyRequestQueue;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.databinding.FragmentMaterialsBinding;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MaterialsFragment extends Fragment {
    private FragmentMaterialsBinding binding;
    private ArrayList<BikeMaterial> allBikeMaterials;
    private View root;

    private RequestQueue requestQueue;

    public MaterialsFragment() {
        this.allBikeMaterials = new ArrayList<BikeMaterial>();
    }

    public MaterialsFragment(ArrayList<BikeMaterial> bikeMaterials) {
        this.allBikeMaterials = bikeMaterials;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMaterialsBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        this.getAllBikeMaterials();

        return this.root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getAllBikeMaterials() {
        Activity origin = (Activity)this.getContext();

        VolleyRequestQueue volleyRequestQueue = VolleyRequestQueue.getInstance(origin);
        requestQueue = volleyRequestQueue.getRequestQueue();

        String requestURL = Constatants.BASE_URL + "/getAllBikeMaterials";

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            MaterialsFragment.this.allBikeMaterials.clear();

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                                MaterialsFragment.this.allBikeMaterials.add(data);
                            }

                            MaterialsFragment.this.fillFragments();
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
        BikeMaterialAdapter adapter = new BikeMaterialAdapter(this.getContext(), this.allBikeMaterials);

        ListView listView = this.root.findViewById(R.id.bikeMaterialList);
        listView.setAdapter(adapter);
    }
}