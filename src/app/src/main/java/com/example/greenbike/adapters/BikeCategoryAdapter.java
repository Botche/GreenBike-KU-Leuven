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
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.services.CategoryService;

import java.util.ArrayList;

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
        TextView bikeCategoryName = convertView.findViewById(R.id.bikeCategoryNameValue);

        bikeCategoryId.setText(item.getId());
        bikeCategoryName.setText(item.getName());

        Button deleteButton = convertView.findViewById(R.id.deleteBikeCategoryButton);
        deleteButton.setTag(position);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryService.delete(bikeCategoryId.getText().toString(), (Activity)BikeCategoryAdapter.this.getContext());
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

    private void onEditBikeCategory(View v, BikeCategory bikeCategory) {
        Activity origin = (Activity)this.getContext();

        Bundle bundle = new Bundle();
        bundle.putSerializable("BikeCategory", bikeCategory);

        NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.nav_categories_edit, bundle);

    }
}
