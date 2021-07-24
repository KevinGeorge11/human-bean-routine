package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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