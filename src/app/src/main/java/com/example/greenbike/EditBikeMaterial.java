package com.example.greenbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.bike.BikeMaterial;
import com.example.greenbike.ui.materials.MaterialsFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditBikeMaterial extends AppCompatActivity {
    private RequestQueue requestQueue;
    private EditText nameInput;
    private BikeMaterial bikeMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bike_material);
        this.bikeMaterial = (BikeMaterial)getIntent().getSerializableExtra("BikeMaterial");

        this.nameInput = findViewById(R.id.editActivityBikeMaterialName);
        this.nameInput.setText(bikeMaterial.getName());
    }

    public void onEdit(View v)
    {
        String name = this.nameInput.getText().toString();
        String id = this.bikeMaterial.getId();

        if (Validator.isBikeMaterialInvalid(name)) {
            Toast.makeText(EditBikeMaterial.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();

            return;
        }

        requestQueue = Volley.newRequestQueue( this );
        String requestURL = Constatants.BASE_URL + "/editBikeMaterial";

        StringRequest submitRequest = new StringRequest (Request.Method.POST, requestURL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent myIntent = new Intent(EditBikeMaterial.this, MaterialsFragment.class);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();

                Toast.makeText(EditBikeMaterial.this, "Edit bike material faild!", Toast.LENGTH_SHORT).show();
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

        requestQueue.add(submitRequest);
    }
}