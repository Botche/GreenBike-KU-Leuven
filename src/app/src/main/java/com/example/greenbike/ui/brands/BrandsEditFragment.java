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
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.services.BrandService;
import com.example.greenbike.databinding.FragmentBrandsEditBinding;

public class BrandsEditFragment extends Fragment {
    private FragmentBrandsEditBinding binding;

    private EditText nameInput;
    private BikeBrand bikeBrand;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrandsEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if(bundle != null) {
            this.bikeBrand = (BikeBrand) bundle.getSerializable("BikeBrand");
        }

        this.nameInput = root.findViewById(R.id.editActivityBikeBrandName);
        this.nameInput.setText(bikeBrand.getName());

        Button editButton = root.findViewById(R.id.editActivityBrandButton);
        editButton.setTag(root);
        editButton.setOnClickListener(v -> BrandService.update(nameInput.getText().toString(), bikeBrand.getId(), BrandsEditFragment.this.getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}