package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String hbrPrefs = "hbrPreferences";
    public static final String launched = "launched";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Launch and preferences management
        SharedPreferences sharedPreferences = getSharedPreferences(hbrPrefs, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains(launched)) {
            Intent intent = new Intent(MainActivity.this, TaskDashboard.class);
            startActivity(intent);
        } else {
            // TODO: add method to populate preferences
            editor.putBoolean(launched, true);
            editor.apply();
            // TODO: show a startup screen
            // Intent intent = new Intent((MainActivity.this, CategoriesSetupActivity));
            // startActivity(intent);
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bnvMenu);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miPuzzle:
                        Intent puzzleIntent = new Intent(MainActivity.this, PuzzleActivity.class);
                        startActivity(puzzleIntent);
                        break;
                    case R.id.miTasks:
                        Intent taskIntent = new Intent(MainActivity.this, TaskDashboard.class);
                        startActivity(taskIntent);
                        break;
                    // TODO: add case for categories and settings
                }
                return false;
            }
        });
    }
}