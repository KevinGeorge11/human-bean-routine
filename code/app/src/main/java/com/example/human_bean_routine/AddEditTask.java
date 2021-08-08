package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        if (isAdd == false) {
            Task editableTask = db.getTaskByID(extras.getInt("taskId"));
            taskName.setText(editableTask.getTaskName());
            db.deleteTask(extras.getInt("taskId"));
        }
        // On clicking save button

       // taskName.setText(editableTask.get);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  Intent i = new Intent(getApplicationContext(),TaskDashboard.class);
              //  startActivity(i);
                String x = taskName.getText().toString();
                Task newTask = new Task(x);
                db.addTask(newTask);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        // setResult(Activity.RESULT_CANCELED, returnIntent);


    }


}