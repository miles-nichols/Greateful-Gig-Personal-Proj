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
    private static String USERNAME;
    private static String PASSWORD;
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
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                USERNAME = username;
                PASSWORD = password;
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginA.this, SignupA.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Makes a POST request to the backend to authenticate the user with the provided username and password.
     * If the login is successful, the user is redirected to the home screen.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    private void makeJsonArrayReq(String username, String password) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String responseMessage = response.getString("message");
                            if(responseMessage.equals("Login successful")) {
                                Intent intent = new Intent(LoginA.this, MainA.class);
                                startActivity(intent);
                            } else if (responseMessage.equals("Incorrect password")) {
                                Toast.makeText(LoginA.this, "Login failed.", Toast.LENGTH_SHORT).show();
                            } else if (responseMessage.equals("User not found with username: " + USERNAME)) {
                                Toast.makeText(LoginA.this, "This account does not exist, please make an account!", Toast.LENGTH_SHORT).show();
                            }
                            else if(responseMessage.equals("Too many strikes")){
                                Toast.makeText(LoginA.this, "This account has been banned!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        );
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}