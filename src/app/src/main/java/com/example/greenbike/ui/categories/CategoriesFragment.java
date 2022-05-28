package com.example.greenbike.ui.categories;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.greenbike.R;
import com.example.greenbike.adapters.BikeCategoryAdapter;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.services.CategoryService;
import com.example.greenbike.databinding.FragmentCategoriesBinding;

import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    private FragmentCategoriesBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        CategoryService.getAll(root, R.id.bikeCategoryList, this::fillFragments);

        Button createButton = root.findViewById(R.id.createBikeCategoryPlusButton);
        createButton.setTag(root);
        createButton.setOnClickListener(v -> {
            Activity origin = (Activity)root.getContext();

            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_categories_create);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View fillFragments(View root, ArrayList<BikeCategory> allBikeCategories, Integer bikeCategoryListId) {
        Context context = root.getContext();
        BikeCategoryAdapter adapter = new BikeCategoryAdapter(context, allBikeCategories);

        ListView listView = root.findViewById(bikeCategoryListId);
        listView.setAdapter(adapter);

        return root;
    }
}