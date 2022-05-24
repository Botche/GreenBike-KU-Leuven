package com.example.greenbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;

import java.util.HashMap;
import java.util.UUID;

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
                finish();
            }
        });
    }

    public void onRegisterUser(View v)
    {
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        String repeatPassword = this.repeatPasswordInput.getText().toString();

        if(Validator.isNullOrEmpty(email) || Validator.isNullOrEmpty((password)) || Validator.isNullOrEmpty((repeatPassword))) {
            Toast.makeText(RegisterActivity.this, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        if (Validator.isEmailValid(email) == false) {
            Toast.makeText(RegisterActivity.this, Messages.NOT_VALID_EMAIL,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if (Validator.isLowerThan(password, 3)) {
            Toast.makeText(RegisterActivity.this, Messages.PASSWORD_TOO_SMALL,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if (Validator.equalsTo(password, repeatPassword)) {
            Toast.makeText(RegisterActivity.this, Messages.PASSWORDS_DID_NOT_MATCH,
                    Toast.LENGTH_SHORT).show();

            return;
        }

        StringRequest submitRequest = new StringRequest (Request.Method.POST, Constatants.REGISTER_USER,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, Messages.REGISTER_FAILED, Toast.LENGTH_SHORT).show();
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