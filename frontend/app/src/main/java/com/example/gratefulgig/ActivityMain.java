package com.example.gratefulgig;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityMain extends AppCompatActivity {

    private RequestQueue requestQueue; // Volley request queue for network operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Set up the bottom navigation
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
                    Intent intent = new Intent(ActivityMain.this, ActivityFriends.class); // Likely a mistake: should navigate to a profile activity
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Fetch Grats from the backend
        fetchGrats();

        // Set up the post gratitude button
        findViewById(R.id.postGratButton).setOnClickListener(v -> showPostGratDialog());
    }

    /**
     * Fetches the list of Grats from the backend and displays them.
     */
    public void fetchGrats() {
        String username = ActivityLogin.getUsername(); // Retrieve current user's username
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

                            // Parse Grat object
                            Grat grat = new Grat();
                            grat.setId(gratJson.getLong("id"));
                            grat.setGratName(gratJson.getString("gratName"));
                            grat.setGratDescription(gratJson.getString("gratDescription"));
                            grat.setGratDate(gratJson.getString("gratDate"));

                            // Parse associated users
                            List<User> users = new ArrayList<>();
                            JSONArray usersJsonArray = gratJson.getJSONArray("users");
                            for (int j = 0; j < usersJsonArray.length(); j++) {
                                JSONObject userJson = usersJsonArray.getJSONObject(j);

                                User user = new User();
                                user.setUsername(userJson.getString("username"));
                                user.setPassword(userJson.getString("password"));
                                user.setEmail(userJson.getString("email"));
                                user.setBirthdate(userJson.optString("birthdate", null));

                                users.add(user);
                            }
                            grat.setUsers(users);

                            grats.add(grat);
                        }

                        // Display the fetched Grats
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

    /**
     * Displays the list of Grats in a ListView.
     *
     * @param grats The list of Grats to display.
     */
    public void displayGrats(List<Grat> grats) {
        ListView gratsListView = findViewById(R.id.gratsListView);

        // Create a custom ArrayAdapter to display Grats
        ArrayAdapter<Grat> adapter = new ArrayAdapter<Grat>(this, android.R.layout.simple_list_item_2, android.R.id.text1, grats) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_grat, parent, false);
                }

                Grat grat = getItem(position);

                // Bind Grat data to the view
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

    /**
     * Displays a dialog to post a new Gratitude entry.
     */
    private void showPostGratDialog() {
        // Retrieve the current date
        String currentDate = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O
                ? LocalDate.now().toString()
                : new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Create a dialog for user input
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post Gratitude");

        // Fetch the text entered in the "What are you grateful for today?" field
        EditText gratInputField = findViewById(R.id.gratInput); // Assuming this is the ID of the text box
        String initialGratName = gratInputField.getText().toString().trim();

        // Set up the input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Grat name input
        EditText nameInput = new EditText(ActivityMain.this);
        nameInput.setHint("Enter Grat Name");
        nameInput.setText(initialGratName); // Prefill with the input from "What are you grateful for today?"
        layout.addView(nameInput);

        // Description input
        EditText descriptionInput = new EditText(this);
        descriptionInput.setHint("Enter Grat Description");
        layout.addView(descriptionInput);

        // Date input (read-only)
        EditText dateInput = new EditText(this);
        dateInput.setHint("Date");
        dateInput.setText(currentDate);
        dateInput.setInputType(InputType.TYPE_NULL);
        layout.addView(dateInput);

        nameInput.setInputType(InputType.TYPE_CLASS_TEXT); // Ensure it's for text input
        builder.setView(layout);

        // Set up dialog buttons
        builder.setPositiveButton("Submit", (dialog, which) -> {
            String gratName = nameInput.getText().toString().trim();
            String gratDescription = descriptionInput.getText().toString();
            String gratDate = dateInput.getText().toString();

            addGratToBackend(gratName, gratDescription, gratDate);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Sends a POST request to add a Grat to the backend.
     *
     * @param gratName        The name of the Grat.
     * @param gratDescription The description of the Grat.
     * @param gratDate        The date of the Grat.
     */
    private void addGratToBackend(String gratName, String gratDescription, String gratDate) {
        String username = ActivityLogin.getUsername(); // Current logged-in user
        String url = "http://10.0.2.2:8080/grat/addGrat/" + username;

        JSONObject gratJson = new JSONObject();
        try {
            gratJson.put("gratName", gratName);
            gratJson.put("gratDescription", gratDescription);
            gratJson.put("gratDate", gratDate);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating Grat JSON.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                gratJson,
                response -> {
                    Toast.makeText(this, "Grat added successfully!", Toast.LENGTH_SHORT).show();
                    fetchGrats(); // Refresh the list
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error adding Grat.", Toast.LENGTH_SHORT).show();
                }
        );

        requestQueue.add(request);
    }
}
