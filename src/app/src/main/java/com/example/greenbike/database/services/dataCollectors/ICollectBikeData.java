package com.example.greenbike.database.services.dataCollectors;

import android.view.View;

import com.example.greenbike.common.BikeFilterOptions;
import com.example.greenbike.database.models.bike.Bike;

import java.util.ArrayList;

public interface ICollectBikeData {
    void getAllData(View root);
    void getAllData(View root, BikeFilterOptions filterOptions);
    boolean checkIfDataIsReady();
    View fillFragments(View root, ArrayList<Bike> allBikes, Integer bikeListId);
}
