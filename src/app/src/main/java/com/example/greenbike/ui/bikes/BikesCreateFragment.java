package com.example.greenbike.ui.bikes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.greenbike.R;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.database.services.BikeService;
import com.example.greenbike.database.services.BrandService;
import com.example.greenbike.database.services.CategoryService;
import com.example.greenbike.database.services.MaterialService;
import com.example.greenbike.databinding.FragmentBikesCreateBinding;

import java.util.ArrayList;

public class BikesCreateFragment extends Fragment {
    private FragmentBikesCreateBinding binding;

    private TextView bikeBrandsSelectedIdInput;

    private TextView bikeMaterialsSelectedIdInput;

    private TextView bikeCategoriesSelectedIdInput;

    private Switch isForRentInput;
    private EditText modelInput;
    private EditText createBikeURLInput;
    private EditText priceInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBikesCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.bikeBrandsSelectedIdInput = root.findViewById(R.id.createBikeSelectedBrandId);
        this.bikeMaterialsSelectedIdInput = root.findViewById(R.id.createBikeSelectedMaterialId);
        this.bikeCategoriesSelectedIdInput = root.findViewById(R.id.createBikeSelectedCategoryId);

        this.isForRentInput = root.findViewById(R.id.bikeCreateIsForRent);
        this.modelInput = root.findViewById(R.id.createBikeModel);
        this.createBikeURLInput = root.findViewById(R.id.createBikeURL);
        this.priceInput = root.findViewById(R.id.createBikePrice);

        BrandService.getAll(root, android.R.layout.simple_spinner_dropdown_item, BikesCreateFragment::fillBikeBrandsFragments);
        MaterialService.getAll(root, android.R.layout.simple_spinner_dropdown_item, BikesCreateFragment::fillBikeMaterialsFragments);
        CategoryService.getAll(root, android.R.layout.simple_spinner_dropdown_item, BikesCreateFragment::fillBikeCategoriesFragments);

        Button createButton = root.findViewById(R.id.createBikeButton);
        createButton.setTag(root);
        createButton.setOnClickListener(v -> {
            String model = modelInput.getText().toString();
            String imageURL = createBikeURLInput.getText().toString();
            String isForRent = String.valueOf(isForRentInput.isChecked() ? 1 : 0);
            String brandId = bikeBrandsSelectedIdInput.getText().toString();
            String materialId = bikeMaterialsSelectedIdInput.getText().toString();
            String categoryId = bikeCategoriesSelectedIdInput.getText().toString();
            String price = priceInput.getText().toString();

            BikeService.create(model, imageURL, isForRent, brandId, materialId, categoryId, price, BikesCreateFragment.this.getActivity());
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static View fillBikeBrandsFragments(View root, ArrayList<BikeBrand> bikeBrands, Integer simpleSpinnerDropdownItemId) {
        Context context = root.getContext();
        final BikeBrand items[] = bikeBrands.toArray(new BikeBrand[0]);

        Spinner bikeBrandsSelect = root.findViewById(R.id.spinnerBikeBrands);
        TextView bikeBrandsSelectedIdInput = root.findViewById(R.id.createBikeSelectedBrandId);

        ArrayAdapter<BikeBrand> adapter = new ArrayAdapter<>(context, simpleSpinnerDropdownItemId, items);
        adapter.setDropDownViewResource(simpleSpinnerDropdownItemId);
        bikeBrandsSelect.setAdapter(adapter);

        bikeBrandsSelect.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BikeBrand bikeBrand = items[position];
                        bikeBrandsSelectedIdInput.setText(bikeBrand.getId());
                    }

                    public void onNothingSelected(AdapterView<?> parent) { }
                }
        );

        return root;
    }

    public static View fillBikeMaterialsFragments(View root, ArrayList<BikeMaterial> bikeMaterials, Integer simpleSpinnerDropdownItemId) {
        Context context = root.getContext();
        final BikeMaterial items[] = bikeMaterials.toArray(new BikeMaterial[0]);

        Spinner bikeMaterialsSelect = root.findViewById(R.id.spinnerBikeMaterials);
        TextView bikeMaterialsSelectedIdInput = root.findViewById(R.id.createBikeSelectedMaterialId);

        ArrayAdapter<BikeMaterial> adapter = new ArrayAdapter<>(context, simpleSpinnerDropdownItemId, items);
        adapter.setDropDownViewResource(simpleSpinnerDropdownItemId);
        bikeMaterialsSelect.setAdapter(adapter);

        bikeMaterialsSelect.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BikeMaterial bikeMaterial = items[position];
                        bikeMaterialsSelectedIdInput.setText(bikeMaterial.getId());
                    }

                    public void onNothingSelected(AdapterView<?> parent) { }
                }
        );

        return root;
    }

    public static View fillBikeCategoriesFragments(View root, ArrayList<BikeCategory> bikeCategories, Integer simpleSpinnerDropdownItemId) {
        Context context = root.getContext();
        final BikeCategory items[] = bikeCategories.toArray(new BikeCategory[0]);

        Spinner bikeCategoriesSelect = root.findViewById(R.id.spinnerBikeCategories);
        TextView bikeCategoriesSelectedIdInput = root.findViewById(R.id.createBikeSelectedCategoryId);

        ArrayAdapter<BikeCategory> adapter = new ArrayAdapter<>(context, simpleSpinnerDropdownItemId, items);
        adapter.setDropDownViewResource(simpleSpinnerDropdownItemId);
        bikeCategoriesSelect.setAdapter(adapter);

        bikeCategoriesSelect.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BikeCategory bikeCategory = items[position];
                        bikeCategoriesSelectedIdInput.setText(bikeCategory.getId());
                    }

                    public void onNothingSelected(AdapterView<?> parent) { }
                }
        );

        return root;
    }
}