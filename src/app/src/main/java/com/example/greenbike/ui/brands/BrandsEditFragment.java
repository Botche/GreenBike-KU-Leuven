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
import com.example.greenbike.R;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeBrand;
import com.example.greenbike.database.services.BrandService;
import com.example.greenbike.databinding.FragmentBrandsEditBinding;

import java.util.HashMap;
import java.util.Map;

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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandService.update(nameInput.getText().toString(), bikeBrand.getId(), BrandsEditFragment.this.getActivity());
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