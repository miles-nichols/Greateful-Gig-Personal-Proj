package com.example.gratefulgig;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupA extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final String BASE_URL = "http://10.0.2.2:8080/user/register";
    private EditText etUsername, etPassword, etConfirmPassword, etEmail;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etNewUsername);
        etPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> handleSignup());
    }

    private void handleSignup() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (isInputValid(username, password, confirmPassword, email)) {
            makeRequest(BASE_URL, username, password, email);
        }
    }

    private boolean isInputValid(String username, String password, String confirmPassword, String email) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            showToast("Please fill all fields");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }
        return true;
    }

    private void makeRequest(String url, String username, String password, String email) {
        JSONObject requestBody = createRequestBody(username, password, email);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                this::handleResponse,
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody;
                        try {
                            responseBody = new String(error.networkResponse.data, "UTF-8");
                            if (error.networkResponse.statusCode == 409) {
                                // Conflict error, show the plain message
                                showToast(responseBody);
                            } else {
                                // Handle other errors
                                showToast("Error: " + responseBody);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response: ", e);
                            showToast("An error occurred, please try again.");
                        }
                    } else {
                        showToast("Network error, please check your connection.");
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private JSONObject createRequestBody(String username, String password, String email) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
            requestBody.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    private void handleResponse(JSONObject response) {
        try {
            String responseMessage = response.getString("message");
            showToast(responseMessage);
            Intent intent = new Intent(SignupA.this, LoginA.class);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Unexpected response from server.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(SignupA.this, message, Toast.LENGTH_SHORT).show();
    }
}
