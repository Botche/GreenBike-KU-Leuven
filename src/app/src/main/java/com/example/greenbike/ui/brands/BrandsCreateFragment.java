package com.example.greenbike.ui.brands;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.MainActivity;
import com.example.greenbike.R;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.services.BrandService;
import com.example.greenbike.databinding.FragmentBrandsCreateBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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