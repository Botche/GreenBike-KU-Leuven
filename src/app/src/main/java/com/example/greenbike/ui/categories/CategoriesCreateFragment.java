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
import com.example.greenbike.database.services.CategoryService;
import com.example.greenbike.databinding.FragmentCategoriesCreateBinding;

public class CategoriesCreateFragment extends Fragment {
    private FragmentCategoriesCreateBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCategoriesCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        EditText nameInput = root.findViewById(R.id.bikeCreateCategoryName);

        Button createButton = root.findViewById(R.id.createBikeCategoryButton);
        createButton.setTag(root);
        createButton.setOnClickListener(v -> CategoryService.create(nameInput.getText().toString(), CategoriesCreateFragment.this.getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}