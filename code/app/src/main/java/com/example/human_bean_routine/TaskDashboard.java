package com.example.human_bean_routine;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDashboard extends AppCompatActivity implements RecyclerViewClickListener, PopupMenu.OnMenuItemClickListener {
    DataBaseHelper db;
    PopupWindow popupWindow;
    List<CategoryTaskList> parentItemList;
    int current;
    CategoryListAdapter parentAdapter;
    String selectedDay;
    ActivityResultLauncher<Intent> launchSomeActivity;
    RecyclerViewClickListener recyclerViewClickListener = new RecyclerViewClickListener() {
        @Override
        public void recyclerViewListClicked(View v, int position) {
            PopupMenu popup = new PopupMenu(getApplicationContext(), v);
            popup.setOnMenuItemClickListener(TaskDashboard.this);
            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
            popup.show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_dashboard);

        // Bottom navbar code
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bnvMenu);
        navigation.setSelectedItemId(R.id.miTasks);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miCategories:
                        Intent categoriesIntent = new Intent(TaskDashboard.this, CategoriesActivity.class);
                        startActivity(categoriesIntent);
                        break;
                    case R.id.miPuzzle:
                        Intent puzzleIntent = new Intent(TaskDashboard.this, PuzzleActivity.class);
                        startActivity(puzzleIntent);
                        break;
                    case R.id.miSettings:
                        Intent settingsIntent = new Intent (TaskDashboard.this, SettingsActivity.class);
                        startActivity(settingsIntent);
                        break;
                }
                return false;
            }
        });

        this.db = DataBaseHelper.getDbInstance(this);

        RecyclerView
                categoryRecyclerView
                = findViewById(
                R.id.parent_recyclerview);

        LinearLayoutManager
                layoutManager
                = new LinearLayoutManager(this);

        parentItemList = new ArrayList<CategoryTaskList>();
        CategoryListAdapter
                parentItemAdapter
                = new CategoryListAdapter(parentItemList, getApplicationContext(), recyclerViewClickListener);
        this.parentAdapter = parentItemAdapter;
        parentItemAdapter.SetOnItemClickListener(recyclerViewClickListener);
        categoryRecyclerView
                .setAdapter(parentItemAdapter);
        categoryRecyclerView
                .setLayoutManager(layoutManager);

        final FloatingActionButton addButton = findViewById(R.id.floatingAddButton);

        ImageButton nextButton = findViewById(R.id.nextButton);
        ImageButton prevButton = findViewById(R.id.prevButton);
        prevButton.setVisibility(View.INVISIBLE);
        TextView day = findViewById(R.id.tasksText);
        selectedDay = "today";
        getCurrentTasks(selectedDay);
        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            getCurrentTasks(selectedDay);
                        }
                    }
                });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddEditTask.class);
                i.putExtra("isAddable", true);
                launchSomeActivity.launch(i);
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(selectedDay == "this week") {
                    selectedDay = "tomorrow";
                    getCurrentTasks(selectedDay);
                    day.setText("tomorrow");
                    nextButton.setVisibility(View.VISIBLE);
                } else if (selectedDay == "tomorrow") {
                    selectedDay = "today";
                    getCurrentTasks(selectedDay);
                    day.setText("today");
                    prevButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if(selectedDay == "today") {
                    selectedDay = "tomorrow";
                    getCurrentTasks(selectedDay);
                    day.setText("tomorrow");
                    prevButton.setVisibility(View.VISIBLE);
                } else if (selectedDay == "tomorrow") {
                    selectedDay = "this week";
                    getCurrentTasks(selectedDay);
                    day.setText("this week");
                    nextButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getCurrentTasks(String selectedDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter);
        boolean singleDay = true;
    //    Date dt = new Date();
        if (selectedDay == "tomorrow") {
            date = LocalDate.now().plus(1, ChronoUnit.DAYS).format(formatter);
        } else if (selectedDay == "this week") {
            date = LocalDate.now().plus(1, ChronoUnit.DAYS).format(formatter);
            singleDay = false;
        }
        List<Category> allCategories = db.getAllCategories();
        parentItemList.clear();
        for (int i = 0; i < allCategories.size(); i++) {
            allCategories.get(i).setCategoryID(db.getCategoryIdByName(allCategories.get(i).getName()));
            CategoryTaskList newTaskList = new CategoryTaskList(allCategories.get(i).getName(), db.getTasksbyDate(allCategories.get(i).getCategoryID(), date, singleDay));
            if (newTaskList.getTasks().size() > 0) {
                parentItemList.add(newTaskList);
            }
        }
        parentAdapter.setItemList(parentItemList);
        parentAdapter.notifyDataSetChanged();
    }

    public Task getTask() {

        List<Category> allCategories = db.getAllCategories();
    //    List<CategoryTaskList> categoryLists = new ArrayList<CategoryTaskList>();
    //    categoryLists.clear();
    /*    for (int i = 0; i < allCategories.size(); i++) {
            allCategories.get(i).setCategoryID(db.getCategoryIdByName(allCategories.get(i).getName()));
            CategoryTaskList newTaskList = new CategoryTaskList(allCategories.get(i).getName(), db.getTasks(allCategories.get(i).getCategoryID()));
            if (newTaskList.getTasks().size() > 0) {
                categoryLists.add(newTaskList);
            }
        } */
        CategoryTaskList categoryTaskList = this.parentItemList.get(KeepTrack.currentCategoryPosition);
        List<Task> tasks = categoryTaskList.getTasks();
        Task currentTask = tasks.get(KeepTrack.currentTaskPosition);
        return currentTask;
    }

    public void checkBoxMethod(View v) {
        Task task = getTask();
        task.setComplete(!task.getComplete());
        db.updateTask(task);
    }

    @Override
    public void recyclerViewListClicked(View v, int position){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(TaskDashboard.this);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        current = position;
        popup.show();
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
        Button deleteButton = findViewById(R.id.deleteButton);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteThis(View v) {

        Task task = getTask();
        int taskId = task.getTaskId();
        db.deleteTask(taskId);
        this.parentAdapter.notifyDataSetChanged();
        popupWindow.dismiss();
        getCurrentTasks(selectedDay);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void edit(MenuItem item) {
        getCurrentTasks(selectedDay);
        Task task = getTask();
        int taskId = task.getTaskId();
        Intent i = new Intent(getApplicationContext(),AddEditTask.class);
        Bundle extras = new Bundle();
        extras.putBoolean("isAddable", false);
        extras.putInt("taskId", taskId);
        i.putExtras(extras);
        launchSomeActivity.launch(i);
        this.parentAdapter.notifyDataSetChanged();
        getCurrentTasks(selectedDay);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
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