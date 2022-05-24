package com.example.greenbike.adapters;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.R;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeBrand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BikeBrandAdapter extends ArrayAdapter<BikeBrand> {

    public BikeBrandAdapter(Context context, ArrayList<BikeBrand> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BikeBrand item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bike_brand_item, parent, false);
        }

        TextView bikeBrandId = convertView.findViewById(R.id.bikeBrandId);
        TextView bikeBrandName = convertView.findViewById(R.id.bikeBrandName);

        bikeBrandId.setText(item.getId());
        bikeBrandName.setText(item.getName());

        Button deleteButton = convertView.findViewById(R.id.deleteBikeBrandButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeBrandAdapter.this.onDeleteBikeBrand(v, item.getId());
            }
        });

        Button editButton = convertView.findViewById(R.id.editBikeBrandButton);
        editButton.setTag(position);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeBrandAdapter.this.onEditBikeBrand(v, item);
            }
        });

        return convertView;
    }

    private void onDeleteBikeBrand(View v, String id) {
        Activity origin = (Activity)this.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.DELETE_BRAND_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_brands);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.DELETE_BRAND_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    private void onEditBikeBrand(View v, BikeBrand bikeBrand) {
        Activity origin = (Activity)this.getContext();

        Bundle bundle = new Bundle();
        bundle.putSerializable("BikeBrand", bikeBrand);

        NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_brands_edit, bundle);

    }
}
