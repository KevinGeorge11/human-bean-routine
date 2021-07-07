package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

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
        TextView tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog picker = openDatePicker(v);
                picker.show();
            }
            });

    }

    public void returnToDashboard (View view) {
        Intent intent = new Intent(AddEditTask.this, TaskDashboard.class);
        startActivity(intent);
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            String year = String.valueOf(selectedYear);
            String month = String.valueOf(selectedMonth + 1);
            String day = String.valueOf(selectedDay);
            TextView tvDt = (TextView) findViewById(R.id.tvStartDate);
            tvDt.setText(day + "/" + month + "/" + year);
        }
    };

    public DatePickerDialog openDatePicker (View view) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog datePicker = new DatePickerDialog(this,
                R.style.Theme_Humanbeanroutine,
                datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");

        return datePicker;
    }
}