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

    // list of categories data to be used by the UI
    CategoriesViewModel categoriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_setup);
        categoriesViewModel = new CategoriesViewModel(getPackageName(), getResources());
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
            categoriesViewModel.selectCategory(categoryName, true);
        }
        else {
            checkmark.setVisibility(View.INVISIBLE);
            categoriesViewModel.selectCategory(categoryName, false);
        }
    }

    public void saveCategories(View v) {
        categoriesViewModel.removeInactiveCategories();
        // TODO: save categoriesViewModel to database
    }
}