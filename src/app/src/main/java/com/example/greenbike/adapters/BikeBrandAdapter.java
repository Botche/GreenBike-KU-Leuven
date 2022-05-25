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

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.greenbike.R;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.services.BrandService;

import java.util.ArrayList;


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
                BrandService.delete(item.getId(), (Activity)BikeBrandAdapter.this.getContext());
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

    private void onEditBikeBrand(View v, BikeBrand bikeBrand) {
        Activity origin = (Activity)this.getContext();

        Bundle bundle = new Bundle();
        bundle.putSerializable("BikeBrand", bikeBrand);

        NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_brands_edit, bundle);

    }
}
