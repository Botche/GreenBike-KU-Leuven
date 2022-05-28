package com.example.greenbike.database.services.dataCollectors;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Messages;
import com.example.greenbike.database.common.Constants;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.mapping.UserBikeMapping;
import com.example.greenbike.database.services.BikeService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CollectUserBikeData extends CollectBikeData {
    private final ArrayList<UserBikeMapping> userBikes;

    public CollectUserBikeData(int listId, ArrayAdapter<Bike> adapter) {
        super(listId, adapter);

        this.userBikes = new ArrayList<>();
    }

    @Override
    public void getAllData(View root) {
        this.getAllBikeBrands(root, userBikes);
        this.getAllBikeCategories(root, userBikes);
        this.getAllBikeMaterials(root, userBikes);
        this.getUserBikes(root);
    }

    @Override
    public boolean checkIfDataIsReady() {
        return super.checkIfDataIsReady()
                && this.userBikes.size() != 0;
    }

    private void getUserBikes(View root) {
        Activity origin = (Activity)root.getContext();

        String url = String.format(Constants.GET_ALL_USER_BIKES, Global.currentUser.getId());
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int index = 0; index < response.length(); index++) {
                            JSONObject jsonObject = response.getJSONObject(index);

                            Gson gson = new Gson();
                            UserBikeMapping data = gson.fromJson(String.valueOf(jsonObject), UserBikeMapping.class);
                            data.setUserId(jsonObject.getString("user_id"));
                            data.setBikeId(jsonObject.getString("bike_id"));

                            userBikes.add(data);
                        }

                        if (checkIfDataIsReady()) {
                            BikeService.getAll(userBikes, root, listId, this::fillFragments);
                        }
                    }
                    catch(JSONException e)
                    {
                        Log.e(Messages.DATABASE_ERROR_TAG, e.getMessage(), e);
                    }
                },
                error -> Toast.makeText(origin, Messages.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}
