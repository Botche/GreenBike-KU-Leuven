package com.example.greenbike.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeMaterialAdapter;
import com.example.greenbike.database.models.bike.BikeMaterial;

import java.util.ArrayList;

public class BikeMaterialFragment extends Fragment {
    private ArrayList<BikeMaterial> bikeMaterials;

    public BikeMaterialFragment() {
        this.bikeMaterials = new ArrayList<BikeMaterial>();
    }

    public BikeMaterialFragment(ArrayList<BikeMaterial> bikeMaterials) {
        this.bikeMaterials = bikeMaterials;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bike_material, container, false);

        BikeMaterialAdapter adapter = new BikeMaterialAdapter(this.getContext(), this.bikeMaterials);

        ListView listView = (ListView) view.findViewById(R.id.bikeMaterialList);
        listView.setAdapter(adapter);

        return view;
    }
}