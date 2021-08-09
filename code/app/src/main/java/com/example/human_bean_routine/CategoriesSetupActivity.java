package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.*;
import java.util.stream.*;

public class CategoriesSetupActivity extends AppCompatActivity {
    // keep track a list of default categories
    private List<Category> defaultCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_setup);
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);
        // should clear all the categories in the database on setup
        db.clearCategoriesTable();

        initDefaultCategoriesList();
    }

    // when clicking a category toggle button update its status is the list
    public void onClickCategory(View v) {
        // get the category toggle button's checkmark image view
        ViewGroup vg = (ViewGroup) v.getParent();
        ImageView checkmark = (ImageView) vg.getChildAt(1);

        // get the category toggle button's  name
        ToggleButton toggleBtn = ((ToggleButton) v);
        String categoryName = toggleBtn.getText().toString();

        // on select/deselect set the category to active/inactive and show/hide the checkmark
        if(toggleBtn.isChecked()) {
            checkmark.setVisibility(View.VISIBLE);
            selectCategory(categoryName, true);
        }
        else {
            checkmark.setVisibility(View.INVISIBLE);
            selectCategory(categoryName, false);
        }
    }

    // save the user's selected category list
    public void saveCategories(View v) {
        List<Category> activeCategories = new ArrayList<Category>();
        DataBaseHelper db = DataBaseHelper.getDbInstance(this);

        for(Category category : defaultCategories) {
            if(category.getActive()){
                activeCategories.add(category);
            }
        }

        db.setupCategories(activeCategories, this);

        // go to Task dashboard after saving
        Intent i = new Intent(getApplicationContext(),TaskDashboard.class);
        startActivity(i);
    }

    // initialize dynamically the default list of categories from string resources
    private void initDefaultCategoriesList() {
        defaultCategories = new ArrayList<Category>();

        // get resources and package name need to access resources
        Resources res = getResources();
        String packName = getPackageName();

        // get all the field values from string resources
        Field[] fields = R.string.class.getFields();

        // create the categories and include them into the default category list
        for (int  i =0; i < fields.length; i++) {
            // get only the field values starting with category
            String stringKeyName = fields[i].getName();
            if(stringKeyName.startsWith("category")) {
                String stringValue = res.getString(res.getIdentifier(stringKeyName, "string", packName));
                Category category = new Category(i, stringValue, stringKeyName + "_icon"  , false);
                defaultCategories.add(category);
            }
        }
    }

    // select the category from the list and update its status based on select/deselect
    private void selectCategory(String name, boolean select) {
        for(Category category : defaultCategories) {
            if(category.getName().equals(name)) {
                category.setActive(select);
                return;
            }
        }
    }
}