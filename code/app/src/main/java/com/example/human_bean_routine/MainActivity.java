package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
            final Handler screenTimerHandler = new Handler(Looper.getMainLooper());
            screenTimerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, TaskDashboard.class);
                    startActivity(intent);
                }
            }, 2000);
        } else {
            // TODO: add method to populate preferences
            editor.putBoolean(launched, true);
            editor.apply();
            Button btnStart = findViewById(R.id.btnStart);
            btnStart.setVisibility(View.VISIBLE);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Intent intent = new Intent((MainActivity.this, CategoriesSetupActivity));
                    // startActivity(intent);
                }
            });

        }
    }
}