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
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            isAdd = extras.getBoolean("isAddable");

        } else {
            isAdd = (Boolean) savedInstanceState.getSerializable("isAddable");
        }
        // On clicking save button
        Button saveButton = findViewById(R.id.btnSave);
        EditText taskName = findViewById(R.id.editName);
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