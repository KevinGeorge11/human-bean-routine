package com.example.human_bean_routine;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TaskDashboard extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener, PopupMenu.OnMenuItemClickListener {

    MyRecyclerViewAdapter adapter;
    ArrayList<String> taskNames = new ArrayList<>();
    RecyclerView recyclerView;
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


        taskNames.add("Eat 3-5 fruits/vegetables.");
        taskNames.add("Limit junk food.");

        this.recyclerView = findViewById(R.id.taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, taskNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        ArrayList<String> taskNames2 = new ArrayList<>();
        taskNames2.add("Sleep by 10pm.");
        taskNames2.add("Sleep at least 7 hours.");

        RecyclerView recyclerView2 = findViewById(R.id.taskList2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, taskNames2);
        adapter.setClickListener(this);
        recyclerView2.setAdapter(adapter);

        final FloatingActionButton addButton = findViewById(R.id.floatingAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddEditTask.class);
                startActivity(i);
            }
        });

    }

    public void ellipses(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(TaskDashboard.this);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.show();
    }

    public void edit(MenuItem item) {
        Intent i = new Intent(getApplicationContext(),AddEditTask.class);
        startActivity(i);
    }

    public void delete(MenuItem item) {
        taskNames.remove(0);
        adapter = new MyRecyclerViewAdapter(this, taskNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.i("onMenuItemClick", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.edit_dropdown:
                edit(item);
                return true;
            case R.id.delete_dropdown:
                delete(item);
                return true;
            default:
                return false;
        }
    }
}