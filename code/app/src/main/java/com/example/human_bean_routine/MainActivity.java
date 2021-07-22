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
        BottomNavigationItemView miPuzzle = findViewById(R.id.miPuzzle);

        // Bottom navigation on click listeners
        miTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(new Intent(MainActivity.this, TaskDashboard.class), miTasks);
            }
        });

        miPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(new Intent(MainActivity.this, PuzzleActivity.class), miPuzzle);
            }
        });
    }

    /* starts activity [intent] on clicking [menuItem] */
    private void navigate(Intent intent, BottomNavigationItemView menuItem) {
        // TODO: make menuItem highlighted (change color)
        startActivity(intent);
    }
}