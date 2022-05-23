package com.example.greenbike.ui.categories;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.R;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeCategory;
import com.example.greenbike.databinding.FragmentCategoriesEditBinding;

import java.util.HashMap;
import java.util.Map;

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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriesEditFragment.this.onEdit(v);
            }
        });

        return root;
    }

    public void onEdit(View v)
    {
        Activity origin = (Activity)this.getContext();

        String name = this.nameInput.getText().toString();
        String id = this.bikeCategory.getId();

        if (Validator.isBikeCategoryInvalid(name)) {
            Toast.makeText(origin, ExceptionMessages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.EDIT_CATEGORY_URL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_categories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(origin, ExceptionMessages.EDIT_CATEGORY_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("name", name);

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}