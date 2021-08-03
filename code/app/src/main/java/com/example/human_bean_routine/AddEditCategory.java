package com.example.human_bean_routine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class AddEditCategory extends AppCompatActivity {

    private boolean isEditing;
    private int categoryId;
    private String oldCategoryName;
    private String oldIconPath;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);

        // check if id and category values were passed in through the intent
        Intent i = getIntent();
        categoryId = i.getIntExtra("Id", -1);
        isEditing = categoryId != -1;
        if(isEditing){
            oldCategoryName = i.getStringExtra("Name");
            oldIconPath = i.getStringExtra("IconPath");
            loadEditCategoryValues();
        }
    }

    public void onClickIcon(View view){

    }

    public void onClickChangeIcon(View view){

    }

    public void onSaveCategory (View view){
        String newName = ((EditText) findViewById(R.id.edNameInput)).getText().toString();

        String newFilePath =((Button) findViewById(R.id.btIconPictureInput)).getCompoundDrawables()[0].toString();
    }


    public void onCancel(View view){
        Intent i = new Intent(getApplicationContext(), CategoriesActivity.class);
        startActivity(i);
    }

    public void createNewContactDialog(){
        alertDialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.category_icon_select_popup, null);
    }

    private void loadEditCategoryValues(){
        ((TextView) findViewById(R.id.tvAddEditCategoriesHeader)).setText(R.string.edit_category);

        ((EditText) findViewById(R.id.edNameInput)).setText(oldCategoryName);

        // get associated category icon from drawable resources
        int resourceId = getResources().getIdentifier(oldIconPath, "drawable", getPackageName());
        ((Button) findViewById(R.id.btIconPictureInput)).setCompoundDrawablesWithIntrinsicBounds(0 , resourceId, 0, 0);
    }


}