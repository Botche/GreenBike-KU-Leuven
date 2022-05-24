package com.example.greenbike.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.HomeActivity;
import com.example.greenbike.R;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.Bike;
import com.facebook.drawee.view.SimpleDraweeView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserBikeAdapter extends ArrayAdapter<Bike> {

    public UserBikeAdapter(Context context, ArrayList<Bike> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bike item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_bike_item, parent, false);
        }

        TextView bikeId = convertView.findViewById(R.id.bikeId);
        TextView bikeModel = convertView.findViewById(R.id.bikeModel);
        SimpleDraweeView image = convertView.findViewById(R.id.bikeImage);
        TextView bikeCategoryName = convertView.findViewById(R.id.bikeCategoryName);
        TextView bikeBrandName = convertView.findViewById(R.id.bikeBrandName);
        TextView bikeMaterialName = convertView.findViewById(R.id.bikeMaterialName);
        TextView bikeIsForRent = convertView.findViewById(R.id.bikeIsForRent);
        TextView bikePrice = convertView.findViewById(R.id.bikePrice);

        bikeId.setText(item.getId());
        bikeModel.setText("Model: " + item.getModel());
        Uri uri = Uri.parse(item.getImageURL());
        image.setImageURI(uri);
        bikeCategoryName.setText("Category: " + item.getBikeCategory().getName());
        bikeBrandName.setText("Brand: " + item.getBikeBrand().getName());
        bikeMaterialName.setText("Material: " + item.getBikeMaterial().getName());
        bikeIsForRent.setText("Is for rent: " + item.getIsForRent());
        bikePrice.setText("Price: " + item.getPrice());

        Button actionBikeButton = convertView.findViewById(R.id.actionBikeButton);
        String actionBikeButtonText = item.getIsForRent() ? "Rent" : "Buy";
        actionBikeButton.setText(actionBikeButtonText);
        actionBikeButton.setTag(position);
        actionBikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionBikeButtonText.equals("Rent")) {
                    UserBikeAdapter.this.onRentBike(v, item.getId());
                } else {
                    UserBikeAdapter.this.onBuyBike(v, item.getId());
                }

            }
        });

        return convertView;
    }

    private void onBuyBike(View v, String bikeId) {
        Activity origin = (Activity)this.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.BUY_BIKE_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent myIntent = new Intent(origin, HomeActivity.class);
                origin.startActivity(myIntent);
                origin.finish();

                Toast.makeText(origin, Messages.SUCCESSFULLY_BOUGHT_BIKE, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.BUY_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                UUID id = UUID.randomUUID();
                LocalDateTime now = LocalDateTime.now();

                Map<String, String> params = new HashMap<>();

                params.put("id", id.toString());
                params.put("userid", Global.currentUser.getId());
                params.put("bikeid", bikeId);
                params.put("buydate", Global.dateTimeFormatter.format(now));

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    private void onRentBike(View v, String bikeId) {
        Activity origin = (Activity)this.getContext();

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.RENT_BIKE_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent myIntent = new Intent(origin, HomeActivity.class);
                origin.startActivity(myIntent);
                origin.finish();

                Toast.makeText(origin, Messages.SUCCESSFULLY_RENTED_BIKE, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, Messages.RENT_BIKE_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                UUID id = UUID.randomUUID();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime nowPlusOneMonth = LocalDateTime.now().plusMonths(1);

                Map<String, String> params = new HashMap<>();

                params.put("id", id.toString());
                params.put("userid", Global.currentUser.getId());
                params.put("bikeid", bikeId);
                params.put("rentstartdate", Global.dateTimeFormatter.format(now));
                params.put("rentenddate", Global.dateTimeFormatter.format(nowPlusOneMonth));

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}
