package com.example.greenbike.ui.materials;

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
import com.example.greenbike.adapters.BikeMaterialAdapter;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.database.services.MaterialService;
import com.example.greenbike.databinding.FragmentMaterialsBinding;

import java.util.ArrayList;

public class MaterialsFragment extends Fragment {
    private FragmentMaterialsBinding binding;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMaterialsBinding.inflate(inflater, container, false);
        this.root = binding.getRoot();

        MaterialService.getAll(root, R.id.bikeMaterialList, this::fillFragments);

        Button createButton = root.findViewById(R.id.createBikeMaterialPlusButton);
        createButton.setTag(root);
        createButton.setOnClickListener(v -> {
            Activity origin = (Activity)root.getContext();
            NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_materials_create);
        });

        return this.root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View fillFragments(View root, ArrayList<BikeMaterial> allBikeMaterials, Integer bikeMaterialListId) {
        Context context = root.getContext();
        BikeMaterialAdapter adapter = new BikeMaterialAdapter(context, allBikeMaterials);

        ListView listView = root.findViewById(bikeMaterialListId);
        listView.setAdapter(adapter);

        return root;
    }
}