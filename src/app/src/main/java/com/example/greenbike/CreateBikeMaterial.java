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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateBikeMaterial extends AppCompatActivity {

    private RequestQueue requestQueue;

    private EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bike_material);

        this.nameInput = findViewById(R.id.name);
    }

    public void onCreate(View v)
    {
        String name = this.nameInput.getText().toString();

        if (Validator.isBikeMaterialInvalid(name)) {
            Toast.makeText(CreateBikeMaterial.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();

            return;
        }

        requestQueue = Volley.newRequestQueue( this );
        String requestURL = Constatants.BASE_URL + "/createBikeMaterial";

        StringRequest submitRequest = new StringRequest (Request.Method.POST, requestURL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent myIntent = new Intent(CreateBikeMaterial.this, LoginActivity.class);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();

                Toast.makeText(CreateBikeMaterial.this, "Create bike material faild!", Toast.LENGTH_SHORT).show();
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

        requestQueue.add(submitRequest);


    }
}