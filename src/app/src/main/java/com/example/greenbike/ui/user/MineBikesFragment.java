package com.example.greenbike.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.greenbike.R;
import com.example.greenbike.adapters.UserBikeAdapter;
import com.example.greenbike.database.services.dataCollectors.CollectUserBikeData;
import com.example.greenbike.database.services.dataCollectors.ICollectBikeData;
import com.example.greenbike.databinding.FragmentMineBikesBinding;

public class MineBikesFragment extends Fragment {
    private FragmentMineBikesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMineBikesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        UserBikeAdapter adapter = new UserBikeAdapter(root.getContext());
        ICollectBikeData dataCollector = new CollectUserBikeData(R.id.userBikeList, adapter);
        dataCollector.getAllData(root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}