package com.example.human_bean_routine;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditCategory extends AppCompatActivity {

    // list of categories data to be used by the UI
    private CategoriesViewModel categoriesViewModel;
    // could be used for both adding a new category or editing existing category
    private boolean isEditing;
    // if editing, store category values that were passed in by intent
    private int categoryId;
    private String categoryName;
    private String iconPath;
    // need to track what icon file was selected and the new category name
    private String newIconFile;
    private String newCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_category);
        categoriesViewModel = new CategoriesViewModel(this /*getPackageName(), getResources() */);

        // check if category id and remaining  values were passed in through the intent
        Intent i = getIntent();
        categoryId = i.getIntExtra("Id", -1);
        isEditing = categoryId != -1;
        // if we are editing, store those values to
        if(isEditing){
            categoryName = i.getStringExtra("Name");
            iconPath = i.getStringExtra("IconPath");
            loadEditCategoryValues();
            newIconFile = iconPath;
        }
        else{
            newIconFile ="category_star_icon";
        }
    }

    // on saving a category, check if there no duplicate names and save to database
    public void onSaveCategory (View view){
        newCategoryName = ((EditText) findViewById(R.id.edNameInput)).getText().toString();

        // check for duplicate category name and create toast warning message if so
        if(categoriesViewModel.checkIfCategoryNameIsDuplicate(newCategoryName)){
            Toast.makeText(this, "You cant have duplicate category names!", Toast.LENGTH_SHORT).show();
        }
        else{
            if(isEditing){
                categoriesViewModel.editCategory(categoryId, newCategoryName, newIconFile, true );
            }
            else{
                categoriesViewModel.addNewCategory(newCategoryName, newIconFile, true );
            }
            Intent i = new Intent(getApplicationContext(), CategoriesActivity.class);
            startActivity(i);
        }
    }

    // on cancel adding/editing a category, go back to the categories screen
    public void onCancel(View view){
        Intent i = new Intent(getApplicationContext(), CategoriesActivity.class);
        startActivity(i);
    }

    // create a dialog when clicking the icon image button
    public void onChangeIconImage(View view){
        // get icon image button from view param
        ImageButton iconSelectButton = (ImageButton) view;

        // get popup dialogue layout from xml
        ScrollView iconSelectPopupView = (ScrollView) getLayoutInflater().inflate(R.layout.category_icon_select_popup, null);
        GridLayout iconGridLayout = (GridLayout) iconSelectPopupView.getChildAt(0);

        // create alert dialog and alert dialog builder with the icon select popup layout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(iconSelectPopupView);
        AlertDialog alertDialog = alertDialogBuilder.create();

        // get resources and package name need to access resources
        Resources res = getResources();
        String packName = getPackageName();

        // create dynamically all the icon buttons from the filename string array in resources
        String[] iconFilenames = getResources().getStringArray(R.array.category_icon_filenames);
        for (int  i =0; i < iconFilenames.length; i++) {
            // create icon select button and set properties
            ImageButton iconBtn = new ImageButton(this);
            LinearLayout.LayoutParams iconBtnParams = new LinearLayout.LayoutParams(250, 250);
            iconBtnParams.setMargins(20, 20, 20, 20);
            iconBtn.setLayoutParams(iconBtnParams);
            iconBtn.setBackgroundColor(Color.WHITE);

            // get associated category icon from drawable resources and set it to icon select button
            String iconFile = iconFilenames[i];
            int resourceId = res.getIdentifier(iconFile, "drawable", packName);
            Drawable iconImage = getDrawable(resourceId);
            iconBtn.setImageDrawable(iconImage);

            // set on click listener for each select button
            iconBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iconSelectButton.setImageDrawable(iconImage);
                    newIconFile = iconFile;
                    alertDialog.dismiss();
                }
            });

            // add the button to the grid view
            iconGridLayout.addView(iconBtn);
        }

        // after the setup, show toe dialog
        alertDialog.show();
    }



    // if we are editing a category, should load its values to input widgets by default
    private void loadEditCategoryValues(){
        // change the screen header to Edit Categories
        ((TextView) findViewById(R.id.tvAddEditCategoriesHeader)).setText(R.string.edit_category);

        // change the text input to the existing category name
        ((EditText) findViewById(R.id.edNameInput)).setText(categoryName);

        // get associated category icon for the icon button from drawable resources
        Resources res = getResources();
        int resId = res.getIdentifier(iconPath, "drawable", getPackageName());
        ImageButton iconButton = ((ImageButton) findViewById(R.id.btIconPictureInput));
        iconButton.setImageDrawable(res.getDrawable(resId));
    }
}