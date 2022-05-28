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
import com.example.greenbike.database.models.bike.BaseBike;
import com.example.greenbike.database.models.bike.Bike;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.database.services.BikeService;
import com.example.greenbike.database.services.BrandService;
import com.example.greenbike.database.services.CategoryService;
import com.example.greenbike.database.services.MaterialService;
import com.example.greenbike.databinding.FragmentBikesEditBinding;

import java.util.ArrayList;

public class BikesEditFragment extends Fragment {
    private FragmentBikesEditBinding binding;

    private TextView bikeBrandsSelectedIdInput;
    private TextView bikeMaterialsSelectedIdInput;
    private TextView bikeCategoriesSelectedIdInput;

    private Switch isForRentInput;
    private EditText modelInput;
    private EditText editBikeURLInput;
    private EditText priceInput;

    private Bike bike;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBikesEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.bikeBrandsSelectedIdInput = root.findViewById(R.id.editBikeSelectedBrandId);
        this.bikeMaterialsSelectedIdInput = root.findViewById(R.id.editBikeSelectedMaterialId);
        this.bikeCategoriesSelectedIdInput = root.findViewById(R.id.editBikeSelectedCategoryId);

        this.isForRentInput = root.findViewById(R.id.bikeEditIsForRent);
        this.modelInput = root.findViewById(R.id.editBikeModel);
        this.editBikeURLInput = root.findViewById(R.id.editBikeURL);
        this.priceInput = root.findViewById(R.id.editBikePrice);

        BrandService.getAll(root, android.R.layout.simple_spinner_dropdown_item, this::fillBikeBrandsFragments);
        MaterialService.getAll(root, android.R.layout.simple_spinner_dropdown_item, this::fillBikeMaterialsFragments);
        CategoryService.getAll(root, android.R.layout.simple_spinner_dropdown_item, this::fillBikeCategoriesFragments);

        Bundle bundle = getArguments();
        if(bundle != null) {
            this.bike = (Bike) bundle.getSerializable("Bike");
        }

        this.modelInput.setText(bike.getModel());
        this.isForRentInput.setChecked(bike.getIsForRent());
        this.editBikeURLInput.setText(bike.getImageURL());
        this.priceInput.setText(String.valueOf(bike.getPrice()));
        this.bikeBrandsSelectedIdInput.setText(bike.getBrandId());
        this.bikeMaterialsSelectedIdInput.setText(bike.getMaterialId());
        this.bikeCategoriesSelectedIdInput.setText(bike.getCategoryId());

        Button createButton = root.findViewById(R.id.editBikeButton);
        createButton.setTag(root);
        createButton.setOnClickListener(v -> {
            String id = bike.getId();
            String model = modelInput.getText().toString();
            String imageURL = editBikeURLInput.getText().toString();
            String isForRent = String.valueOf(isForRentInput.isChecked() ? 1 : 0);
            String brandId = bikeBrandsSelectedIdInput.getText().toString();
            String materialId = bikeMaterialsSelectedIdInput.getText().toString();
            String categoryId = bikeCategoriesSelectedIdInput.getText().toString();
            String price = priceInput.getText().toString();

            BikeService.update(id, model, imageURL, isForRent, brandId, materialId, categoryId, price, BikesEditFragment.this.getActivity());
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View fillBikeBrandsFragments(View root, ArrayList<BikeBrand> bikeBrands, Integer simpleSpinnerDropdownItemId) {
        Context context = root.getContext();
        final BikeBrand items[] = bikeBrands.toArray(new BikeBrand[0]);

        Spinner bikeBrandsSelect = root.findViewById(R.id.spinnerBikeBrandsEdit);

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

        int bikeBrandsSelectIndex = this.indexOfAdapter(adapter, this.bike.getBikeBrand());
        bikeBrandsSelect.setSelection(bikeBrandsSelectIndex);

        return root;
    }

    public View fillBikeMaterialsFragments(View root, ArrayList<BikeMaterial> bikeMaterials, Integer simpleSpinnerDropdownItemId) {
        Context context = root.getContext();
        final BikeMaterial items[] = bikeMaterials.toArray(new BikeMaterial[0]);

        Spinner bikeMaterialsSelect = root.findViewById(R.id.spinnerBikeMaterialsEdit);

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

        int bikeMaterialsSelectIndex = this.indexOfAdapter(adapter, this.bike.getBikeMaterial());
        bikeMaterialsSelect.setSelection(bikeMaterialsSelectIndex);

        return root;
    }

    public View fillBikeCategoriesFragments(View root, ArrayList<BikeCategory> bikeCategories, Integer simpleSpinnerDropdownItemId) {
        Context context = root.getContext();
        final BikeCategory items[] = bikeCategories.toArray(new BikeCategory[0]);

        Spinner bikeCategoriesSelect = root.findViewById(R.id.spinnerBikeCategoriesEdit);

        ArrayAdapter<BikeCategory> adapter = new ArrayAdapter<BikeCategory>(context, simpleSpinnerDropdownItemId, items);
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

        int bikeCategoriesSelectIndex = this.indexOfAdapter(adapter, this.bike.getBikeCategory());
        bikeCategoriesSelect.setSelection(bikeCategoriesSelectIndex);

        return root;
    }

    private int indexOfAdapter(ArrayAdapter adapter, BaseBike baseBike)
    {
        int indexOf = -1;

        for (int index = 0, count = adapter.getCount(); index < count; ++index)
        {
            BaseBike baseBikeAdapterItem = (BaseBike)adapter.getItem(index);

            if (baseBikeAdapterItem.getId().equals(baseBike.getId()))
            {
                indexOf = index;
                break;
            }
        }

        return indexOf;
    }
}