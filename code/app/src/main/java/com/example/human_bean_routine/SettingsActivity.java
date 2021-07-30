package com.example.human_bean_routine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

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


        // Get shared preferences (settings)
        SharedPreferences sh = getSharedPreferences(String.valueOf(R.string.hbrPrefs),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();

        // Confirm before delete settings
        Boolean confirmBeforeDelete =
                sh.getBoolean(String.valueOf(R.string.confirmBeforeDelete), true);
        Switch switchConfirmDelete = findViewById(R.id.switchConfirmDelete);
        switchConfirmDelete.setChecked(confirmBeforeDelete);
        switchConfirmDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(String.valueOf(R.string.confirmBeforeDelete), isChecked);
                editor.apply();
            }
        });

    }
}