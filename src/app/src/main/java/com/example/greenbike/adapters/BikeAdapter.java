package com.example.greenbike.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.R;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.Bike;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;


public class BikeAdapter extends ArrayAdapter<Bike> {

    public BikeAdapter(Context context, ArrayList<Bike> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bike item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bike_item, parent, false);
        }

        TextView bikeId = convertView.findViewById(R.id.bikeItemId);
        TextView bikeModel = convertView.findViewById(R.id.bikeItemModel);
        SimpleDraweeView image = convertView.findViewById(R.id.bikeItemImage);

        bikeId.setText(item.getId());
        bikeModel.setText(item.getModel());

        Uri uri = Uri.parse(item.getImageURL());
        image.setImageURI(uri);

        Button deleteButton = convertView.findViewById(R.id.deleteBikeItemButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeAdapter.this.onDeleteBike(v, item.getId());
            }
        });

        Button editButton = convertView.findViewById(R.id.editBikeItemButton);
        editButton.setTag(position);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BikeAdapter.this.onEditBike(v, item);
            }
        });

        return convertView;
    }

    private void onDeleteBike(View v, String id) {
        Activity origin = (Activity)this.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.DELETE_BIKE_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_bikes);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, ExceptionMessages.DELETE_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
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

    private void onEditBike(View v, Bike bike) {
        Activity origin = (Activity)this.getContext();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Bike", bike);

        NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
        //navController.navigate(R.id.nav_brands_edit, bundle);

    }
}
