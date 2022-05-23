package com.example.greenbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.databinding.MainScreenBinding;

import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private EditText repeatPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.emailInput = findViewById(R.id.email);
        this.passwordInput = findViewById(R.id.password);
        this.repeatPasswordInput = findViewById(R.id.repeatPassword);

        TextView loginLink = findViewById(R.id.loginLink);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void onRegisterUser(View v)
    {
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        String repeatPassword = this.repeatPasswordInput.getText().toString();

        boolean isInvalid = email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty();
        if(isInvalid) {
            Toast.makeText(RegisterActivity.this, ExceptionMessages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        isInvalid = Validator.isEmailValid(email) == false;
        if (isInvalid) {
            Toast.makeText(RegisterActivity.this, ExceptionMessages.NOT_VALID_EMAIL,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        isInvalid = password.length() <= 3;
        if (isInvalid) {
            Toast.makeText(RegisterActivity.this, ExceptionMessages.PASSWORD_TOO_SMALL,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        isInvalid = password.equals(repeatPassword) == false;
        if (isInvalid) {
            Toast.makeText(RegisterActivity.this, ExceptionMessages.PASSWORDS_DID_NOT_MATCH,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.REGISTER_USER,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, ExceptionMessages.REGISTER_FAILED, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                UUID id = UUID.randomUUID();

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.toString());
                params.put("email", email);
                params.put("password", password);
                params.put("roleid", "a12b5beb-b42e-11ec-baa2-422590d84e63");

                return params;
            }
        };

        Global.requestQueue.addToRequestQueue(submitRequest);
    }

}