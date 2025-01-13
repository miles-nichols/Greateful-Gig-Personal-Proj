package com.example.gratefulgig;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import java.util.ArrayList;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    private RequestQueue requestQueue;
    private Button postGrat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    return true;
                } else if (id == R.id.navigation_friends) {
                    Intent intent = new Intent(ActivityMain.this, ActivityFriends.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.navigation_profile) {
                    Intent intent = new Intent(ActivityMain.this, ActivityFriends.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        fetchGrats();
    }

    public void fetchGrats() {
        String username = ActivityLogin.getUsername();
        String url = "http://10.0.2.2:8080/grat/userfriends/" + username;

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    List<Grat> grats = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject gratJson = response.getJSONObject(i);

                            Grat grat = new Grat();
                            grat.setId(gratJson.getLong("id"));
                            grat.setGratName(gratJson.getString("gratName"));
                            grat.setGratDescription(gratJson.getString("gratDescription"));
                            grat.setGratDate(gratJson.getString("gratDate"));

                            // Parse users list
                            List<User> users = new ArrayList<>();
                            JSONArray usersJsonArray = gratJson.getJSONArray("users");
                            for (int j = 0; j < usersJsonArray.length(); j++) {
                                JSONObject userJson = usersJsonArray.getJSONObject(j);

                                User user = new User();
                                user.setUsername(userJson.getString("username"));
                                user.setPassword(userJson.getString("password"));
                                user.setEmail(userJson.getString("email"));
                                user.setBirthdate(userJson.optString("birthdate", null)); // Handle nullable field

                                users.add(user);
                            }
                            grat.setUsers(users);

                            grats.add(grat);
                        }

                        // Display the grats
                        displayGrats(grats);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing Grats!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error fetching Grats!", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }


    public void displayGrats(List<Grat> grats) {
        ListView gratsListView = findViewById(R.id.gratsListView);

        // Create an ArrayAdapter or custom adapter to display the Grats
        ArrayAdapter<Grat> adapter = new ArrayAdapter<Grat>(this, android.R.layout.simple_list_item_2, android.R.id.text1, grats) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_grat, parent, false);
                }

                Grat grat = getItem(position);

                TextView nameView = convertView.findViewById(R.id.grat_name);
                TextView descriptionView = convertView.findViewById(R.id.grat_description);
                TextView userView = convertView.findViewById(R.id.grat_user);
                TextView dateView = convertView.findViewById(R.id.grat_date);

                nameView.setText(grat.getGratName());
                descriptionView.setText(grat.getGratDescription());
                userView.setText(grat.getUsers().isEmpty() ? "Unknown" : grat.getUsers().get(0).getUsername());
                dateView.setText(grat.getGratDate());

                return convertView;
            }
        };

        gratsListView.setAdapter(adapter);
    }

}
