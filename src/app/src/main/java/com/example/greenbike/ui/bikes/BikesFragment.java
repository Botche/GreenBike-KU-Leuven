package com.example.greenbike.ui.bikes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeAdapter;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.Bike;
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
    private View root;

    public BikesFragment() {
        this.allBikes = new ArrayList<Bike>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBikesBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        this.getAllBikes();

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
                                //Bitmap a = BikesFragment.this.getImageBitmap(data.getImageURL());

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
        BikeAdapter adapter = new BikeAdapter(this.getContext(), this.allBikes);

        ListView listView = this.root.findViewById(R.id.adminBikesList);
        listView.setAdapter(adapter);
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            //Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }
}