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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.greenbike.R;


public class BikeMaterialAdapter extends ArrayAdapter<BikeMaterial> {

    public BikeMaterialAdapter(Context context, ArrayList<BikeMaterial> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BikeMaterial item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bike_material_item, parent, false);
        }

        TextView bikeMaterialId = convertView.findViewById(R.id.bikeMaterialId);
        TextView bikeMaterialName = convertView.findViewById(R.id.bikeMaterialName);

        bikeMaterialId.setText(item.getId());
        bikeMaterialName.setText(item.getName());

        Button deleteButton = convertView.findViewById(R.id.deleteBikeMaterialButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeMaterialAdapter.this.onDeleteBikeMaterial(v, item.getId());
            }
        });

        Button editButton = convertView.findViewById(R.id.editBikeMaterialButton);
        editButton.setTag(position);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeMaterialAdapter.this.onEditBikeMaterial(v, item);
            }
        });

        return convertView;
    }

    private void onDeleteBikeMaterial(View v, String id) {
        Activity origin = (Activity)this.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.DELETE_MATERIAL_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_materials);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, ExceptionMessages.DELETE_MATERIAL_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
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

    private void onEditBikeMaterial(View v, BikeMaterial bikeMaterial) {
        Activity origin = (Activity)this.getContext();

        Bundle bundle = new Bundle();
        bundle.putSerializable("BikeMaterial", bikeMaterial);

        NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_materials_edit, bundle);

    }
}
