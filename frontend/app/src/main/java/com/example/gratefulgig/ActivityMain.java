package com.example.gratefulgig;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }
}
