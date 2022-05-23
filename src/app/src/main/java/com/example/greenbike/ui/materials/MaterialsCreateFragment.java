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
import com.example.greenbike.databinding.FragmentMaterialsCreateBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialsCreateFragment.this.onCreate(v);
            }
        });

        return root;
    }

    public void onCreate(View v)
    {
        Activity origin = (Activity)this.getContext();

        String name = this.nameInput.getText().toString();

        if (Validator.isBikeMaterialInvalid(name)) {
            Toast.makeText(origin, "Some fields are empty!", Toast.LENGTH_SHORT).show();

            return;
        }

        String requestURL = Constatants.BASE_URL + "/createBikeMaterial";

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

                Toast.makeText(origin, "Create bike material failed!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                UUID id = UUID.randomUUID();

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.toString());
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