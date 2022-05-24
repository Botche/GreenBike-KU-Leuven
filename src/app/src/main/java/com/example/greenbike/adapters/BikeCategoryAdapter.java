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
import com.example.greenbike.database.models.bike.BikeCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BikeCategoryAdapter extends ArrayAdapter<BikeCategory> {

    public BikeCategoryAdapter(Context context, ArrayList<BikeCategory> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BikeCategory item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bike_category_item, parent, false);
        }

        TextView bikeCategoryId = convertView.findViewById(R.id.bikeCategoryId);
        TextView bikeCategoryName = convertView.findViewById(R.id.bikeCategoryName);

        bikeCategoryId.setText(item.getId());
        bikeCategoryName.setText(item.getName());

        Button deleteButton = convertView.findViewById(R.id.deleteBikeCategoryButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeCategoryAdapter.this.onDeleteBikeCategory(v, item.getId());
            }
        });

        Button editButton = convertView.findViewById(R.id.editBikeCategoryButton);
        editButton.setTag(position);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeCategoryAdapter.this.onEditBikeCategory(v, item);
            }
        });

        return convertView;
    }

    private void onDeleteBikeCategory(View v, String id) {
        Activity origin = (Activity)this.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.DELETE_CATEGORY_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_categories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.DELETE_CATEGORY_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
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

    private void onEditBikeCategory(View v, BikeCategory bikeCategory) {
        Activity origin = (Activity)this.getContext();

        Bundle bundle = new Bundle();
        bundle.putSerializable("BikeCategory", bikeCategory);

        NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_categories_edit, bundle);

    }
}
