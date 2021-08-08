package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddEditTask extends AppCompatActivity {

    Boolean isAdd;
    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        this.db = DataBaseHelper.getDbInstance(this);
        Bundle extras = getIntent().getExtras();
        if (savedInstanceState == null) {
            isAdd = extras.getBoolean("isAddable");

        } else {
            isAdd = (Boolean) savedInstanceState.getSerializable("isAddable");
        }

        Button saveButton = findViewById(R.id.btnSave);
        EditText taskName = findViewById(R.id.editName);
        Spinner category = findViewById(R.id.spinnerCategory);
        List<String> ListSpinner = new ArrayList<String>();

        List<Category> allCategories = db.getAllCategories();
        for (int i = 0; i < allCategories.size(); i++) {
            ListSpinner.add(allCategories.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, ListSpinner);
        category.setAdapter(adapter);
        if (isAdd == false) {
            Task editableTask = db.getTaskByID(extras.getInt("taskId"));
            taskName.setText(editableTask.getTaskName());
            Category currentCategory = db.getCategoryByID(editableTask.getCategoryID());
            category.setSelection(((ArrayAdapter<String>)category.getAdapter()).getPosition(currentCategory.getName()));
            db.deleteTask(extras.getInt("taskId"));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String x = taskName.getText().toString();
                String categoryName = category.getSelectedItem().toString();
                Task newTask = new Task(x, categoryName, db.getCategoryIdByName(categoryName));
                db.addTask(newTask);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


    }


}