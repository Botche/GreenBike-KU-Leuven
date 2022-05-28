package com.example.greenbike.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.greenbike.R;
import com.example.greenbike.adapters.UserBikeAdapter;
import com.example.greenbike.common.BikeFilterOptions;
import com.example.greenbike.database.services.dataCollectors.CollectBikeData;
import com.example.greenbike.database.services.dataCollectors.ICollectBikeData;
import com.example.greenbike.databinding.FragmentBuyBikesBinding;

public class BuyBikesFragment extends Fragment {
    private FragmentBuyBikesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuyBikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        UserBikeAdapter adapter = new UserBikeAdapter(root.getContext());
        ICollectBikeData dataCollector = new CollectBikeData(R.id.buyBikesList, adapter);
        dataCollector.getAllData(root, BikeFilterOptions.Buy);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}