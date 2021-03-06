package com.example.greenbike;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.greenbike.common.Global;
import com.example.greenbike.common.Messages;
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constants;
import com.example.greenbike.database.models.user.User;
import com.example.greenbike.database.models.user.UserRole;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private final ArrayList<UserRole> userRoles = new ArrayList<>();

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
        registerLink.setOnClickListener(v -> {
            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(myIntent);
            finish();
        });
    }

    public void onLoginUser(View v) {
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();

        if(Validator.isNullOrEmpty(email) || Validator.isNullOrEmpty((password))) {
            Toast.makeText(LoginActivity.this, Messages.EMPTY_FIELDS, Toast.LENGTH_SHORT).show();

            return;
        }

        String requestUrl = String.format(Constants.LOGIN_USER, email);
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    try {
                        if (!Validator.checkIfResponseIsCorrect(response)) {
                            Toast.makeText(LoginActivity.this, Messages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show();

                            return;
                        }

                        JSONObject jsonObject = response.getJSONObject(0);
                        if (!Validator.checkCredentialsForLogin(jsonObject, password)) {
                            Toast.makeText(LoginActivity.this, Messages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show();

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

                        Class<? extends AppCompatActivity> activityToRedirect = UserHomeActivity.class;
                        if (user.getUserRole().getName().equals(Constants.ADMIN_ROLE)) {
                            activityToRedirect =  MainActivity.class;
                        }

                        Intent myIntent = new Intent(LoginActivity.this, activityToRedirect);

                        startActivity(myIntent);
                        finish();
                    }
                    catch(JSONException e)
                    {
                        Log.e(Messages.DATABASE_ERROR_TAG, e.getMessage(), e);
                    }
                },
                error -> Toast.makeText(LoginActivity.this, Messages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show()
        );

        Global.requestQueue.addToRequestQueue(submitRequest);

    }

    private void getUserRoles() {
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, Constants.GET_ALL_USER_ROLES, null,
                response -> {
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
                        Log.e(Messages.DATABASE_ERROR_TAG, e.getMessage(), e);
                    }
                },
                error -> Toast.makeText(LoginActivity.this, Messages.INVALID_CREDENTIALS, Toast.LENGTH_SHORT).show()
        );

        Global.requestQueue.addToRequestQueue(submitRequest);
    }
}