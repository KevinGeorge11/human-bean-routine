package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class add_edit_task extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
    }

    public void returnToDashboard () {
        Intent intent = new Intent(add_edit_task.this, task_dashboard.class);
        startActivity(intent);
    }
}