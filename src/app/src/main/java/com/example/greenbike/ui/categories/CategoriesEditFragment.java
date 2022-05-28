package com.example.greenbike.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.greenbike.R;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.database.services.CategoryService;
import com.example.greenbike.databinding.FragmentCategoriesEditBinding;

public class CategoriesEditFragment extends Fragment {
    private FragmentCategoriesEditBinding binding;

    private EditText nameInput;
    private BikeCategory bikeCategory;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoriesEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if(bundle != null) {
            this.bikeCategory = (BikeCategory) bundle.getSerializable("BikeCategory");
        }

        this.nameInput = root.findViewById(R.id.editActivityBikeCategoryName);
        this.nameInput.setText(bikeCategory.getName());

        Button editButton = root.findViewById(R.id.editActivityBikeCategoryButton);
        editButton.setTag(root);
        editButton.setOnClickListener(v -> CategoryService.update(nameInput.getText().toString(), bikeCategory.getId(), CategoriesEditFragment.this.getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}