package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
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

    List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_setup);
        initCategoriesList();
    }

    public void onClickCategory(View v) {
        // get the category button's associated checkmark
        ViewGroup vg = (ViewGroup) v.getParent();
        ImageView checkmark = (ImageView) vg.getChildAt(1);

        // get the category button's category name
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
        for(Category category : categories) {
            // TODO: save active categories to database
            if(category.getActive()){

                //Log.d("category","name: " + category.getName());
            }
        }

        // go to Task dashboard after saving
        Intent i = new Intent(getApplicationContext(), TaskDashboard.class);
        startActivity(i);
    }

    // initialize the default list of categories
    private void initCategoriesList() {
        categories = new ArrayList<Category>();
        Field[] fields = R.string.class.getFields();

        // try to get default list from the string resources
        for (int  i =0; i < fields.length; i++) {
            String stringKeyName = fields[i].getName();
            if(stringKeyName.startsWith("category")) {
                String stringValue = getResources().getString(getResources().getIdentifier(stringKeyName, "string", getPackageName()));
                Category category = new Category(i, stringValue, stringKeyName + "_icon"  , false);
                categories.add(category);
                //Log.d("category loaded","loaded name: " + category.getName());
            }
        }
    }

    private void selectCategory(String name, boolean select) {
        for(Category category : categories) {
            if(category.getName().equals(name)) {
                category.setActive(select);
                return;
            }
        }
    }
}