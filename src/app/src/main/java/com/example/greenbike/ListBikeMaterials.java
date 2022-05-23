package com.example.greenbike;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.database.models.user.UserRole;
import com.example.greenbike.fragments.BikeMaterialFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListBikeMaterials extends AppCompatActivity {
    private RequestQueue requestQueue;
    private ArrayList<BikeMaterial> allBikeMaterials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bike_materials);

        this.allBikeMaterials = new ArrayList<BikeMaterial>();

        this.getAllBikeMaterials();
    }

    private void getAllBikeMaterials() {
        requestQueue = Volley.newRequestQueue( this );
        String requestURL = Constatants.BASE_URL + "/getAllBikeMaterials";

        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                BikeMaterial data = gson.fromJson(String.valueOf(jsonObject), BikeMaterial.class);

                                ListBikeMaterials.this.allBikeMaterials.add(data);
                            }

                            ListBikeMaterials.this.fillFragments();
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
                        Toast.makeText(ListBikeMaterials.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(submitRequest);
    }

    private void fillFragments() {
        BikeMaterialFragment selectorFragment = new BikeMaterialFragment(this.allBikeMaterials);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.listBikeMaterials, selectorFragment)
                .commitAllowingStateLoss();

    }
}