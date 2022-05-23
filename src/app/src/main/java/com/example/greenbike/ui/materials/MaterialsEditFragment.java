package com.example.greenbike.ui.materials;

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
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.databinding.FragmentMaterialsEditBinding;

import java.util.HashMap;
import java.util.Map;

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
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialsEditFragment.this.onEdit(v);
            }
        });

        return root;
    }


    public void onEdit(View v)
    {
        Activity origin = (Activity)this.getContext();

        String name = this.nameInput.getText().toString();
        String id = this.bikeMaterial.getId();

        if (Validator.isBikeMaterialInvalid(name)) {
            Toast.makeText(origin, "Some fields are empty!", Toast.LENGTH_SHORT).show();

            return;
        }

        String requestURL = Constatants.BASE_URL + "/editBikeMaterial";

        StringRequest submitRequest = new StringRequest (Request.Method.POST, requestURL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NavController navController = Navigation.findNavController(origin, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.nav_materials);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();

                Toast.makeText(origin, "Edit bike material failed!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

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