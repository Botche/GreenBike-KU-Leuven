package com.example.greenbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.greenbike.database.common.Constatants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    private RequestQueue requestQueue;

    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.emailInput = (EditText)findViewById(R.id.email);
        this.passwordInput = (EditText)findViewById(R.id.password);
    }

    public void onLoginUser(View v) {
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();

        boolean isInvalid = email.isEmpty() || password.isEmpty();
        if(isInvalid) {
            Toast.makeText(LoginActivity.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();

            return;
        }

        requestQueue = Volley.newRequestQueue( this );
        String requestURL = Constatants.BASE_URL + "/login/" + email;

        JsonObjectRequest  submitRequest = new JsonObjectRequest(Request.Method.GET, requestURL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  response) {
                //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();

                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();

                Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(submitRequest);

    }

}