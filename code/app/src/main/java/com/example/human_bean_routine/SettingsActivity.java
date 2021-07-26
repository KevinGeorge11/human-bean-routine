package com.example.human_bean_routine;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ComponentActivity;

import android.view.MenuItem;
import android.view.View;

import com.example.human_bean_routine.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bnvMenu);
        navigation.setSelectedItemId(R.id.miSettings);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miPuzzle:
                        Intent puzzleIntent = new Intent(SettingsActivity.this, PuzzleActivity.class);
                        startActivity(puzzleIntent);
                        break;
                    case R.id.miTasks:
                        Intent taskIntent = new Intent(SettingsActivity.this, TaskDashboard.class);
                        startActivity(taskIntent);
                        break;
                    // TODO: add case for categories
                }
                return false;
            }
        });

    }
}