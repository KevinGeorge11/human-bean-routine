package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    }

    public void returnToDashboard (View view) {
        Intent intent = new Intent(AddEditTask.this, TaskDashboard.class);
        startActivity(intent);
    }
}