package com.example.greenbike.adapters;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.greenbike.R;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.services.BrandService;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;


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
        TextView bikeCategoryName = convertView.findViewById(R.id.bikeItemCategoryName);
        TextView bikeBrandName = convertView.findViewById(R.id.bikeItemBrandName);
        TextView bikeMaterialName = convertView.findViewById(R.id.bikeItemMaterialName);
        TextView bikeIsForRent = convertView.findViewById(R.id.bikeItemIsForRent);
        TextView bikePrice = convertView.findViewById(R.id.bikeItemPrice);

        bikeId.setText(item.getId());
        bikeModel.setText("Model: " + item.getModel());
        Uri uri = Uri.parse(item.getImageURL());
        image.setImageURI(uri);
        bikeCategoryName.setText("Category: " + item.getBikeCategory().getName());
        bikeBrandName.setText("Brand: " + item.getBikeBrand().getName());
        bikeMaterialName.setText("Material: " + item.getBikeMaterial().getName());
        bikeIsForRent.setText("Is for rent: " + item.getIsForRent().toString());
        bikePrice.setText("Price: " + item.getPrice());

        Button deleteButton = convertView.findViewById(R.id.deleteBikeItemButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandService.delete(item.getId(), (Activity)BikeAdapter.this.getContext());
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

    private void onEditBike(View v, Bike bike) {
        Activity origin = (Activity)this.getContext();

        Bundle bundle = new Bundle();
        bundle.putSerializable("Bike", bike);

        NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_bikes_edit, bundle);

    }
}
