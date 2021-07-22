package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation buttons
        MenuItem miTasks = findViewById(R.id.miTasks);
        MenuItem miPuzzle = findViewById(R.id.miPuzzle);
        MenuItem miSettings = findViewById(R.id.miSettings);

        // Bottom navigation on click listeners
        miTasks.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                navigate(TaskDashboard.class, miTasks);
                return true;
            }
        });
    }

    /* navigates to the [activity] on clicking [menuItem] */
    private void navigate(Class activity, MenuItem menuItem) {
        // TODO: make menuItem highlighted (change color)
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}