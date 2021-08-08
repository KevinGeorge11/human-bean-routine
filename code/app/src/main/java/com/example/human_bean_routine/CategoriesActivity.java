package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    // list of categories data to be used by the UI
    private CategoriesViewModel categoriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        categoriesViewModel = new CategoriesViewModel(this /*getPackageName(), getResources()*/);
        createCategoryButtons();
    }

    // when clicking the + button or a category button, go to AddEditCategory activity
    public void onAddOrEditCategory(View view){
        Button categoryBtn = ((Button) view);
        String categoryName = categoryBtn.getText().toString();
        Category category = categoriesViewModel.getCategoryByName(categoryName);

        // if we clicked on category button, pass in its values to the intent for editing
        Intent i = new Intent(getApplicationContext(), AddEditCategory.class);
        if(category != null){
            i.putExtra("Id", category.getCategoryID());
            i.putExtra("Name", category.getName());
            i.putExtra("IconPath", category.getIconPath());
        }
        startActivity(i);
    }

    // dynamically create all the ui category buttons from data
    private void createCategoryButtons(){
        // get the grid layout we are on
        GridLayout gridLayout = (GridLayout) findViewById(R.id.glCategoriesGridLayout);

        // get resources and package name need to access resources
        Resources res = getResources();
        String packName = getPackageName();

        // get the category list data and for each one create the category button
        List<Category> categoryList = categoriesViewModel.getCategories(this);
        for(Category category : categoryList) {

            // create base frame layout for both the category button and delete button
            FrameLayout frameLayout = new FrameLayout(this);
            FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayout.setLayoutParams(frameLp);

            // create category button and set properties
            Button categoryBtn = new Button(this);
            LinearLayout.LayoutParams categoryBtnParams = new LinearLayout.LayoutParams(400, 400);
            categoryBtnParams.setMargins(20, 20, 20, 20);
            categoryBtn.setLayoutParams(categoryBtnParams);
            categoryBtn.setText(category.getName());
            categoryBtn.setBackgroundColor(Color.WHITE);

            // get associated category icon from drawable resources
            int resourceId = res.getIdentifier(category.getIconPath(), "drawable", packName);
            categoryBtn.setCompoundDrawablesWithIntrinsicBounds(0 , resourceId, 0, 0);

            // set on click listener for category button
            categoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddOrEditCategory(v);
                }
            });

            // create delete button and set properties
            Button deleteBtn = new Button(this);
            deleteBtn.setBackground(getDrawable(R.drawable.ic_baseline_cancel_24));
            LinearLayout.LayoutParams deleteBtnParams = new LinearLayout.LayoutParams(100, 100);
            deleteBtn.setLayoutParams(deleteBtnParams);
            deleteBtn.setVisibility(View.INVISIBLE);

            // set on click listener for close button
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridLayout.removeView(frameLayout);
                    categoriesViewModel.deleteCategory(category);
                }
            });

            // set on hold listener for category button to show the close button
            categoryBtn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteBtn.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            // add the category button and delete button to the frame layout
            frameLayout.addView(categoryBtn);
            frameLayout.addView(deleteBtn);

            // add the frame layout to the grid layout
            gridLayout.addView(frameLayout);
        }
    }
}