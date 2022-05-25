package com.example.greenbike.ui.brands;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.greenbike.R;
import com.example.greenbike.database.services.BrandService;
import com.example.greenbike.databinding.FragmentBrandsCreateBinding;

public class BrandsCreateFragment extends Fragment {
    private FragmentBrandsCreateBinding binding;

    private EditText nameInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrandsCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.nameInput = root.findViewById(R.id.name);

        Button createButton = root.findViewById(R.id.createBikeBrandButton);
        createButton.setTag(root);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandService.create(nameInput.getText().toString(), BrandsCreateFragment.this.getActivity());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}