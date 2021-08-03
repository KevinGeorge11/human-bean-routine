package com.example.human_bean_routine;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TaskDashboard extends AppCompatActivity implements RecyclerViewClickListener, PopupMenu.OnMenuItemClickListener {

 //   MyRecyclerViewAdapter adapter;
    RecyclerView parentRecyclerView;
    RecyclerView recyclerView;
    PopupWindow popupWindow;
    RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            PopupMenu popup = new PopupMenu(getApplicationContext(), v);
            popup.setOnMenuItemClickListener(TaskDashboard.this);
            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
            popup.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#355D52"));

        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Human Bean Routine");

        RecyclerView
                categoryRecyclerView
                = findViewById(
                R.id.parent_recyclerview);

        // Initialise the Linear layout manager
        LinearLayoutManager
                layoutManager
                = new LinearLayoutManager(this);

        CategoryListAdapter
                parentItemAdapter
                = new CategoryListAdapter(parentItemList(), getApplicationContext(), recyclerViewClickListener);
        parentItemAdapter.SetOnItemClickListener(recyclerViewClickListener);
        categoryRecyclerView
                .setAdapter(parentItemAdapter);
        categoryRecyclerView
                .setLayoutManager(layoutManager);

        final FloatingActionButton addButton = findViewById(R.id.floatingAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddEditTask.class);
                startActivity(i);
            }
        });

    }
    // hardcoded until Database is running
    private List<CategoryTaskList> parentItemList() {
        CategoryTaskList foodTaskList = new CategoryTaskList("Food");
        Task task1 = new Task("Eat 3-5 fruits/vegetables");
        Task task2 = new Task("Limit junk food");
        foodTaskList.addTask(task1);
        foodTaskList.addTask(task2);

        CategoryTaskList sleepTaskList = new CategoryTaskList("Sleep");
        Task task3 = new Task("Sleep by 10pm");
        Task task4 = new Task("Sleep at least 7 hours");
        sleepTaskList.addTask(task3);
        sleepTaskList.addTask(task4);

        List<CategoryTaskList> categories = new ArrayList<>();
        categories.add(foodTaskList);
        categories.add(sleepTaskList);
        return categories;
    }

    @Override
    public void recyclerViewListClicked(View v, int position){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(TaskDashboard.this);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.show();
    }

    public void edit(MenuItem item) {
        Intent i = new Intent(getApplicationContext(),AddEditTask.class);
        i.putExtra("editName", "Limit Junk Food.");
        i.putExtra("category", "Food");
        startActivity(i);
    }

    public void delete(MenuItem item) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(findViewById(R.id.taskList), Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
/*
    public void deleteButton(View v) {
        //     taskNames.remove(0);
        //     adapter = new MyRecyclerViewAdapter(this, taskNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        popupWindow.dismiss();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
*/
    @Override
    public boolean onMenuItemClick(MenuItem item) {
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