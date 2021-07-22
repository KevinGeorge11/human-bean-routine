package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation buttons
        BottomNavigationItemView miTasks = findViewById(R.id.miTasks);

        // Bottom navigation on click listeners
        miTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(TaskDashboard.class, miTasks);
            }
        });
    }

    /* navigates to the [activity] on clicking [menuItem] */
    private void navigate(Class activity, BottomNavigationItemView menuItem) {
        // TODO: make menuItem highlighted (change color)
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}