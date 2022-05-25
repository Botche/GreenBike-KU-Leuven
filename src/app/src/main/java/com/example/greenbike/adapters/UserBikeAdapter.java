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

import java.text.MessageFormat;
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
        TextView bikeModel = convertView.findViewById(R.id.bikeModelValue);
        SimpleDraweeView image = convertView.findViewById(R.id.bikeImage);
        TextView bikeCategoryName = convertView.findViewById(R.id.bikeCategoryNameValue);
        TextView bikeBrandName = convertView.findViewById(R.id.bikeBrandNameValue);
        TextView bikeMaterialName = convertView.findViewById(R.id.bikeMaterialNameValue);
        TextView bikeIsForRent = convertView.findViewById(R.id.bikeIsForRentValue);
        TextView bikePrice = convertView.findViewById(R.id.bikePriceValue);

        bikeId.setText(item.getId());
        bikeModel.setText(item.getModel());
        Uri uri = Uri.parse(item.getImageURL());
        image.setImageURI(uri);
        bikeCategoryName.setText(item.getBikeCategory().getName());
        bikeBrandName.setText(item.getBikeBrand().getName());
        bikeMaterialName.setText(item.getBikeMaterial().getName());
        bikeIsForRent.setText(String.format("%s", item.getIsForRent()));
        bikePrice.setText(String.format("%s", item.getPrice()));

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
