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
import com.example.greenbike.common.Validator;
import com.example.greenbike.database.common.Constatants;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue requestQueue;

    private EditText emailInput;
    private EditText passwordInput;
    private EditText repeatPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.emailInput = (EditText)findViewById(R.id.email);
        this.passwordInput = (EditText)findViewById(R.id.password);
        this.repeatPasswordInput = (EditText)findViewById(R.id.repeatPassword);
    }

    public void onRegisterUser(View v)
    {
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();
        String repeatPassword = this.repeatPasswordInput.getText().toString();

        boolean isInvalid = email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty();
        if(isInvalid) {
            Toast.makeText(RegisterActivity.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();

            return;
        }

        isInvalid = Validator.isEmailValid(email) == false;
        if (isInvalid) {
            Toast.makeText(RegisterActivity.this, "Email is not valid!",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        isInvalid = password.length() <= 3;
        if (isInvalid) {
            Toast.makeText(RegisterActivity.this, "Password should be longer than 3 symbols!",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        isInvalid = password.equals(repeatPassword) == false;
        if (isInvalid) {
            Toast.makeText(RegisterActivity.this, "Password should match repeat password!",
                    Toast.LENGTH_SHORT).show();

            return;
        }


        requestQueue = Volley.newRequestQueue( this );
        String requestURL = Constatants.BASE_URL + "/register";

        StringRequest  submitRequest = new StringRequest (Request.Method.POST, requestURL,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myIntent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();

                Toast.makeText(RegisterActivity.this, "Register faild!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                UUID id = UUID.randomUUID();

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id.toString());
                params.put("email", email);
                params.put("password", email);
                params.put("roleid", "a12b5beb-b42e-11ec-baa2-422590d84e63");

                return params;
            }
        };

        requestQueue.add(submitRequest);

//        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.POST, requestURL, null,
//                new Response.Listener<JSONArray>()
//                {
//                    @Override
//                    public void onResponse(JSONArray response)
//                    {
//                        try {
//                            String responseString = "";
//                            for(int index = 0; index < response.length(); index++)
//                            {
//                                JSONObject curObject = response.getJSONObject(index);
//
//                                responseString += curObject.getString( "id" ) + " : " + curObject.getString( "name" ) + "\n";
//                            }
//                            //txtResponse.setText(responseString);
//                        }
//                        catch(JSONException e)
//                        {
//                            Log.e("Database", e.getMessage(), e);
//                        }
//                    }
//                },
//
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        //txtResponse.setText(error.getLocalizedMessage());
//                    }
//                }
//        );
//
//        requestQueue.add(submitRequest);
    }

}