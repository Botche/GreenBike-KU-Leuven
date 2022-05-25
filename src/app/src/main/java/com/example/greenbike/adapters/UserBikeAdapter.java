package com.example.greenbike.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.greenbike.R;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.services.BikeService;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

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
        actionBikeButton.setTag(position);
        if (item.getTaken() && !item.getIsForRent()) {
            actionBikeButton.setVisibility(View.GONE);
        } else {
            String actionBikeButtonText = item.getTaken() ? "Return" : item.getIsForRent() ? "Rent" : "Buy";
            actionBikeButton.setText(actionBikeButtonText);
            actionBikeButton.setVisibility(View.VISIBLE);
            actionBikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getTaken()) {
                        BikeService.returnBike(v, item.getId());
                    } else if(actionBikeButtonText.equals("Rent")) {
                        BikeService.rentBike(v, item.getId());
                    } else {
                        BikeService.buyBike(v, item.getId());
                    }

                }
            });
        }

        return convertView;
    }
}
