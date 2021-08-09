//Source referenced:
// bottom navbar code: https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView

package com.example.human_bean_routine.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.human_bean_routine.Categories.CategoriesActivity;
import com.example.human_bean_routine.Puzzles.PuzzleActivity;
import com.example.human_bean_routine.R;
import com.example.human_bean_routine.Tasks.TaskDashboard;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                    case R.id.miCategories:
                        Intent categoriesIntent = new Intent (SettingsActivity.this, CategoriesActivity.class);
                        startActivity(categoriesIntent);
                        break;
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