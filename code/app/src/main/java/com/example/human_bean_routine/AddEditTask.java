package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

public class AddEditTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Filling out category spinner. TODO: Change to dynamic array
        Spinner category_spinner = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> category_adapter =
                ArrayAdapter.createFromResource(this, R.array.categories_array,
                        android.R.layout.simple_spinner_item);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_adapter);

        // Filling out repetition spinner. TODO: Change to dynamic array
        Spinner repetition_spinner = (Spinner) findViewById(R.id.spinnerRepeat);
        ArrayAdapter<CharSequence> repetition_adapter =
                ArrayAdapter.createFromResource(this, R.array.task_repetition_array,
                        android.R.layout.simple_spinner_item);
        repetition_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repetition_spinner.setAdapter(repetition_adapter);



        // Setting tvStartDate on click listener
        EditText editEndDate = (EditText) findViewById(R.id.editEndDate);
        final Calendar endDateCal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener etEndDateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        endDateCal.set(Calendar.YEAR, year);
                        endDateCal.set(Calendar.MONTH, month);
                        endDateCal.set(Calendar.DAY_OF_MONTH, day);
                        editEndDate.setText( String.valueOf(day) + "/" +  String.valueOf(month)
                                + "/" +  String.valueOf(year));
                    }
                };
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog picker = new DatePickerDialog( AddEditTask.this, etEndDateListener,
                        endDateCal.get(Calendar.YEAR), endDateCal.get(Calendar.MONTH),
                        endDateCal.get(Calendar.DAY_OF_MONTH));
                picker.show();
            }
            });


        // Setting etStartDate on click listener
        EditText etStartDate = (EditText) findViewById(R.id.etStartDate);
        final Calendar startDateCal = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener etStartDateListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        myOnDateSet(etStartDate, view, year, month, day);
                    }
                };
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = openDatePicker(v, etStartDateListener, startDateCal);
                picker.show();
            }
        });

        // On clicking save button
        Button saveButton = findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),TaskDashboard.class);
                startActivity(i);
            }
        });

    }


    // Sets date to the Edittext tv
    public void myOnDateSet(EditText et, DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
        String year = String.valueOf(selectedYear);
        String month = String.valueOf(selectedMonth + 1);
        String day = String.valueOf(selectedDay);
        et.setText(day + "/" + month + "/" + year);
    }


    // Returns a date picker
    public DatePickerDialog openDatePicker (View view, DatePickerDialog.OnDateSetListener datePickerListener,
                                            Calendar cal) {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                R.style.Theme_Humanbeanroutine,
                datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

      //  datePicker.setCancelable(false);
       // datePicker.setTitle("Select the date");

        return datePicker;
    }
}