package com.example.human_bean_routine.Tasks;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.human_bean_routine.Categories.CategoriesActivity;
import com.example.human_bean_routine.Categories.Category;
import com.example.human_bean_routine.Database.DataBaseHelper;
import com.example.human_bean_routine.Puzzles.PuzzleActivity;
import com.example.human_bean_routine.R;
import com.example.human_bean_routine.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TaskDashboard extends AppCompatActivity implements RecyclerViewClickListener, PopupMenu.OnMenuItemClickListener {
    DataBaseHelper db;
    PopupWindow popupWindow;
    List<CategoryTaskList> parentItemList;
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

        // Adapter initialization
        this.db = DataBaseHelper.getDbInstance(this);
        RecyclerView categoryRecyclerView = findViewById(R.id.parent_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.parentItemList = new ArrayList<CategoryTaskList>();
        CategoryListAdapter
                parentItemAdapter
                = new CategoryListAdapter(parentItemList, getApplicationContext(), recyclerViewClickListener);
        this.parentAdapter = parentItemAdapter;
        parentItemAdapter.SetOnItemClickListener(recyclerViewClickListener);
        categoryRecyclerView
                .setAdapter(parentItemAdapter);
        categoryRecyclerView
                .setLayoutManager(layoutManager);

        buttonInitialization();

    }

    // Initializes floating plus button, next day and previous day buttons
    private void buttonInitialization() {
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
                Intent i = new Intent(getApplicationContext(), AddEditTask.class);
                i.putExtra("isAddable", true);
                launchSomeActivity.launch(i);
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
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

    // Given parameter "today", "tomorrow" or "this week",
    // it will take the category and task position from the AdapterHelper
    // and fetch the currentTasks in the TaskDashboard
    private void getCurrentTasks(String selectedDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDateTime.now().format(formatter);
        boolean singleDay = true;
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

    // Gets the task and category positions as per the AdapterHelper
    // return the necessary Task
    public Task getTask() {
        List<Category> allCategories = db.getAllCategories();
        CategoryTaskList categoryTaskList = this.parentItemList.get(AdapterHelper.currentCategoryPosition);
        List<Task> tasks = categoryTaskList.getTasks();
        Task currentTask = tasks.get(AdapterHelper.currentTaskPosition);
        return currentTask;
    }

    // Sets complete to task and updates database on checkbox check
    public void checkBoxMethod(View v) {
        Task task = getTask();
        task.setComplete(!task.getComplete());
        db.updateTask(task);
    }

    // Creates the menu popup when the ellipses is clicked
    @Override
    public void recyclerViewListClicked(View v, int position){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(TaskDashboard.this);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.show();
    }

    // Will open the Delete popup if the SharedPreferences has allowed confirmation
    // Otherwise will proceed to delete the desired task
    public void delete(MenuItem item) {
        SharedPreferences sh = getSharedPreferences(String.valueOf(R.string.hbrPrefs), Context.MODE_PRIVATE);
        Boolean confirmBeforeDelete = sh.getBoolean(String.valueOf(R.string.confirmBeforeDelete), true);
        if (confirmBeforeDelete) { // Show popup before deleting task
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
        } else { // Delete task
            Task task = getTask();
            int taskId = task.getTaskId();
            db.deleteTask(taskId);
            this.parentAdapter.notifyDataSetChanged();
            getCurrentTasks(selectedDay);
        }
    }

    // Delete the task for the position of the ellipses
    public void deleteThis(View v) {
        Task task = getTask();
        int taskId = task.getTaskId();
        db.deleteTask(taskId);
        this.parentAdapter.notifyDataSetChanged();
        popupWindow.dismiss();
        getCurrentTasks(selectedDay);
    }

    // Cancel button will dismiss the popup for Delete/Cancel
    public void cancelThis(View v) {
        popupWindow.dismiss();
    }

    // If the edit is clicked on the Menu popup, launch the activity and send details about task
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

    // Functions for different item clicks on the Edit/Delete Menu popup
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