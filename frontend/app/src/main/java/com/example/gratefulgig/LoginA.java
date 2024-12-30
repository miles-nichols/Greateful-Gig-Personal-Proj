package com.example.gratefulgig;

import static androidx.compose.ui.semantics.SemanticsPropertiesKt.setText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginA extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button signupButton;
    private String url = "http://10.0.2.2:8080/api/auth/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);

        //set text boxes to empty
        usernameEditText.setText("");
        passwordEditText.setText("");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create user to access across the rest of the app
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                UserO user = new UserO();
                user.setUsername(username);
                user.setPassword(password);
                if (username.isEmpty()) {
                    Toast.makeText(LoginA.this, "Username is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendLoginRequest(username, password);
            }
        });

            // Signup button click listener
        signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginA.this, SignupA.class);
                    startActivity(intent);
                }
            });
        }

    private void sendLoginRequest(String username, String password) {
        JSONObject requestBody = createRequestBody(username, password);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response as a plain string
                        if ("Login successful".equals(response)) {
                            // Navigate to the main screen on successful login
                            Intent intent = new Intent(LoginA.this, MainA.class);
                            startActivity(intent);
                            finish();  // Close the login screen
                        } else if (response.contains("User not found")) {
                            Toast.makeText(LoginA.this, "Account does not exist. Please sign up.", Toast.LENGTH_SHORT).show();
                        } else if ("Too many strikes".equals(response)) {
                            Toast.makeText(LoginA.this, "This account has been banned.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginA.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                        Toast.makeText(LoginA.this, "Error occurred, please try again later", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return requestBody.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json"; // Set the content type to application/json
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }


    private JSONObject createRequestBody(String username, String password) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

}