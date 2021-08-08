package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
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
        EditText description = findViewById(R.id.editDescription);

        EditText etStartDate = findViewById(R.id.etStartDate);
        final Calendar endDateCal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener etEndDateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        myOnDateSet(etStartDate, view, year, month, day);
                    }
                };
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog picker = openDatePicker(v, etEndDateListener,endDateCal);
                picker.show();
            }

        });
        
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
            description.setText(editableTask.getDescription());
            etStartDate.setText(editableTask.getStartDate());
            Category currentCategory = db.getCategoryByID(editableTask.getCategoryID());
            category.setSelection(((ArrayAdapter<String>)category.getAdapter()).getPosition(currentCategory.getName()));
            db.deleteTask(extras.getInt("taskId"));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String x = taskName.getText().toString();
                String categoryName = category.getSelectedItem().toString();
                String desc = description.getText().toString();
                String startDate = etStartDate.getText().toString();
                Task newTask = new Task(x, categoryName, db.getCategoryIdByName(categoryName), desc, startDate);
                db.addTask(newTask);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }

    public DatePickerDialog openDatePicker (View view, DatePickerDialog.OnDateSetListener datePickerListener,
                                            Calendar cal) {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                R.style.Theme_Humanbeanroutine,
                datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        return datePicker;
    }

    // Sets date to the Edittext tv
    public void myOnDateSet(EditText et, DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
        String year = String.valueOf(selectedYear);
        String month = String.valueOf(selectedMonth + 1);
        String day = String.valueOf(selectedDay);
        et.setText(day + "/" + month + "/" + year);
    }


}