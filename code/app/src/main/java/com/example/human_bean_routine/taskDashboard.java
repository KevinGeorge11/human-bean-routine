package com.example.human_bean_routine;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class taskDashboard extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#355D52"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Human Bean Routine");

        ArrayList<String> taskNames = new ArrayList<>();
        taskNames.add("Go to sleep by 12am.");
        taskNames.add("Eat healthy food.");

        RecyclerView recyclerView = findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, taskNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerView2 = findViewById(R.id.taskList2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, taskNames);
        adapter.setClickListener(this);
        recyclerView2.setAdapter(adapter);

        final FloatingActionButton addButton = findViewById(R.id.floatingAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),add_edit_task.class);
                startActivity(i);
            }
        });



    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}