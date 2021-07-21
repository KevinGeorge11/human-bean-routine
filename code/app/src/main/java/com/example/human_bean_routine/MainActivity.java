package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* navigates to the [activity] on clicking [menuItem] */
    private void navigate(Activity activity, MenuItem menuItem) {
        // TODO: make menuItem highlighted (change color)
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }
}