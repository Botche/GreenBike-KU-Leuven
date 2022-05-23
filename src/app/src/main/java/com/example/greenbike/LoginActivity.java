package com.example.greenbike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.greenbike.common.ExceptionMessages;
import com.example.greenbike.common.Global;
import com.example.greenbike.database.common.Constatants;
import com.example.greenbike.database.models.user.User;
import com.example.greenbike.database.models.user.UserRole;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    private final ArrayList<UserRole> userRoles = new ArrayList<UserRole>();

    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.emailInput = findViewById(R.id.email);
        this.passwordInput = findViewById(R.id.password);

        this.getUserRoles();

        TextView registerLink = findViewById(R.id.registerLink);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void onLoginUser(View v) {
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();

        boolean isInvalid = email.isEmpty() || password.isEmpty();
        if(isInvalid) {
            Toast.makeText(LoginActivity.this, ExceptionMessages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        String requestUrl = String.format(Constatants.LOGIN_USER, email);
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, null,
            new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response)
                {
                    try {
                        boolean isInvalid = response.length() != 1;

                        if (isInvalid) {
                            Toast.makeText(LoginActivity.this, ExceptionMessages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        JSONObject jsonObject = response.getJSONObject(0);

                        String userPassword = jsonObject.getString("password");
                        isInvalid = userPassword.equals(password) == false;
                        if (isInvalid) {
                            Toast.makeText(LoginActivity.this, ExceptionMessages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        Gson gson = new Gson();
                        User user = gson.fromJson(String.valueOf(jsonObject), User.class);

                        String userRoleId = jsonObject.getString("role_id");

                        UserRole userRole = LoginActivity.this.userRoles.stream()
                                .filter(ur -> ur.getId().equals(userRoleId))
                                .findFirst()
                                .get();

                        user.setUserRole(userRole);

                        Global.currentUser = user;

                       Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                       startActivity(myIntent);
                    }
                    catch(JSONException e)
                    {
                        Log.e(ExceptionMessages.DATABASE_ERROR_TAG, e.getMessage(), e);
                    }
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(LoginActivity.this, ExceptionMessages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show();
                }
            }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);

    }

    private void getUserRoles() {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constatants.GET_ALL_USER_ROLES, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {

                            for (int index = 0; index < response.length(); index++) {
                                JSONObject jsonObject = response.getJSONObject(index);

                                Gson gson = new Gson();
                                UserRole data = gson.fromJson(String.valueOf(jsonObject), UserRole.class);

                                LoginActivity.this.userRoles.add(data);
                            }
                        }
                        catch(JSONException e)
                        {
                            Log.e(ExceptionMessages.DATABASE_ERROR_TAG, e.getMessage(), e);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(LoginActivity.this, ExceptionMessages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}