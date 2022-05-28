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
import com.example.greenbike.database.services.MaterialService;
import com.example.greenbike.databinding.FragmentMaterialsCreateBinding;

public class MaterialsCreateFragment extends Fragment {
    private FragmentMaterialsCreateBinding binding;

    private EditText nameInput;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMaterialsCreateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.nameInput = root.findViewById(R.id.name);

        Button createButton = root.findViewById(R.id.createBikeMaterialButton);
        createButton.setTag(root);
        createButton.setOnClickListener(v -> MaterialService.create(nameInput.getText().toString(), MaterialsCreateFragment.this.getActivity()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}