package com.example.human_bean_routine.Tasks;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.human_bean_routine.Categories.Category;
import com.example.human_bean_routine.Database.DataBaseHelper;
import com.example.human_bean_routine.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEditTask extends AppCompatActivity {


    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
        this.db = DataBaseHelper.getDbInstance(this);
        Bundle extras = getIntent().getExtras();
        Boolean isAdd;
        if (savedInstanceState == null) {
            isAdd = extras.getBoolean("isAddable");

        } else {
            isAdd = (Boolean) savedInstanceState.getSerializable("isAddable");
        }

        // Initialize all buttons, spinners, fields
        Button saveButton = findViewById(R.id.btnSave);
        Button cancelButton = findViewById(R.id.btnCancel);
        EditText taskName = findViewById(R.id.editName);
        Spinner category = findViewById(R.id.spinnerCategory);
        EditText description = findViewById(R.id.editDescription);
        EditText etStartDate = findViewById(R.id.etStartDate);

        // Initialize calendar for startDate
        final Calendar endDateCal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener etEndDateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        myOnDateSet(etStartDate, view, year, month, day);
                    }
                };

        // Initialize start date field
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog picker = openDatePicker(v, etEndDateListener,endDateCal);
                picker.show();
            }

        });

        // Attach adapter to Categories Spinner
        List<String> ListSpinner = getListOfCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, ListSpinner);
        category.setAdapter(adapter);

        // if the task is not newly added, then define the AddEditTask screen fields with the values in the database
        if (isAdd == false) {
            Task editableTask = db.getTaskByID(extras.getInt("taskId"));
            taskName.setText(editableTask.getTaskName());
            description.setText(editableTask.getDescription());
            etStartDate.setText(editableTask.getStartDate());
            Category currentCategory = db.getCategoryByID(editableTask.getCategoryID());
            category.setSelection(((ArrayAdapter<String>)category.getAdapter()).getPosition(currentCategory.getName()));
        }

        // Initialize onClickListener for save button
        // Will take all the values in the field and create a task out of it, then push it to the database
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTask(extras.getInt("taskId"));
                String x = taskName.getText().toString();
                String categoryName = category.getSelectedItem().toString();
                String desc = description.getText().toString();
                String startDate = etStartDate.getText().toString();
                if (startDate.equals("")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    startDate = LocalDateTime.now().format(formatter);
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date y = formatter.parse(startDate);
                        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                        startDate = formatter2.format(y);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                Task newTask = new Task(x, categoryName, db.getCategoryIdByName(categoryName), desc, startDate);
                db.addTask(newTask);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        // on Cancel, return back to TaskDashboard activity
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }

    // Gets list of category names
    private List<String> getListOfCategories() {
        List<String> ListSpinner = new ArrayList<String>();
        List<Category> allCategories = db.getAllCategories();
        for (int i = 0; i < allCategories.size(); i++) {
            ListSpinner.add(allCategories.get(i).getName());
        }
        return ListSpinner;
    }

    // Opens the DatePickerDialog box
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

    // Converts the date selected into a parsable string
    public void myOnDateSet(EditText et, DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
        String year = String.valueOf(selectedYear);
        String month = String.valueOf(selectedMonth + 1);
        String day = String.valueOf(selectedDay);
        et.setText(day + "/" + month + "/" + year);
    }


}