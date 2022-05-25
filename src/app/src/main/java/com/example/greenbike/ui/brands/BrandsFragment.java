package com.example.greenbike.ui.brands;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeBrandAdapter;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.services.BrandService;
import com.example.greenbike.databinding.FragmentBrandsBinding;

import java.util.ArrayList;

public class BrandsFragment extends Fragment {
    private FragmentBrandsBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrandsBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        BrandService.getAll(root, R.id.bikeBrandList, this::fillFragments);

        Button createButton = root.findViewById(R.id.createBikeBrandPlusButton);
        createButton.setTag(root);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity origin = (Activity)root.getContext();
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_brands_create);
            }
        });

        return this.root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View fillFragments(View root, ArrayList<BikeBrand> allBikeBrands, Integer bikeBrandListId) {
        Context context = root.getContext();
        BikeBrandAdapter adapter = new BikeBrandAdapter(context, allBikeBrands);

        ListView listView = root.findViewById(bikeBrandListId);
        listView.setAdapter(adapter);

        return root;
    }
}