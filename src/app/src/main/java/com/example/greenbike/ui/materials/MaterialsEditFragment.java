package com.example.greenbike.ui.materials;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.greenbike.R;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.database.services.MaterialService;
import com.example.greenbike.databinding.FragmentMaterialsEditBinding;

public class MaterialsEditFragment extends Fragment {
    private FragmentMaterialsEditBinding binding;

    private EditText nameInput;
    private BikeMaterial bikeMaterial;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMaterialsEditBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if(bundle != null) {
            this.bikeMaterial = (BikeMaterial) bundle.getSerializable("BikeMaterial");
        }

        this.nameInput = root.findViewById(R.id.editActivityBikeMaterialName);
        this.nameInput.setText(bikeMaterial.getName());

        Button editButton = root.findViewById(R.id.editActivityBikeMaterialButton);
        editButton.setTag(root);
        editButton.setOnClickListener(v -> MaterialService.update(nameInput.getText().toString(), bikeMaterial.getId(), MaterialsEditFragment.this.getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}