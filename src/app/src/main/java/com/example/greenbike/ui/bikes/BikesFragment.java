package com.example.greenbike.ui.bikes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeAdapter;
import com.example.greenbike.common.BikeFilterOptions;
import com.example.greenbike.database.services.dataCollectors.CollectBikeData;
import com.example.greenbike.database.services.dataCollectors.ICollectBikeData;
import com.example.greenbike.databinding.FragmentBikesBinding;

public class BikesFragment extends Fragment {
    private FragmentBikesBinding binding;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBikesBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        BikeAdapter adapter = new BikeAdapter(this.root.getContext());
        ICollectBikeData dataCollector = new CollectBikeData(R.id.adminBikesList, adapter);
        dataCollector.getAllData(root, BikeFilterOptions.All);

        Button createButton = root.findViewById(R.id.createBikePlusButton);
        createButton.setTag(root);
        createButton.setOnClickListener(v -> {
            Activity origin = (Activity)root.getContext();
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_bikes_create);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}