package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    CategoriesViewModel categoriesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        categoriesViewModel = new CategoriesViewModel(getPackageName(), getResources());
        createCategoryButtons();
    }

    public void onAddOrEditCategory(View view){
        Button categoryBtn = ((Button) view);
        String categoryName = categoryBtn.getText().toString();
        Intent i = new Intent(getApplicationContext(), AddEditCategory.class);

        Category category = categoriesViewModel.getCategoryByName(categoryName);
        if(category != null){
            i.putExtra("Id", category.getCategoryID());
            i.putExtra("Name", category.getName());
            i.putExtra("IconPath", category.getIconPath());
            i.putExtra("Active", category.getActive());
        }

        startActivity(i);

    }

    private void createCategoryButtons(){
        // get the grid layout we are on
        GridLayout gridLayout = (GridLayout) findViewById(R.id.glCategoriesGridLayout);

        List<Category> categoryList = categoriesViewModel.getCategories();
        for(Category category : categoryList) {

            // create base frame layout for the category button and delete button
            FrameLayout frameLayout = new FrameLayout(this);
            FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            frameLayout.setLayoutParams(frameLp);

            // create category button and set layout properties
            Button categoryBtn = new Button(this);
            LinearLayout.LayoutParams categoryBtnParams = new LinearLayout.LayoutParams(375, 375);
            categoryBtnParams.setMargins(20, 20, 20, 20);
            categoryBtn.setLayoutParams(categoryBtnParams);

            // get associated category icon from drawable resources
            int resourceId = getResources().getIdentifier(category.getIconPath(), "drawable", getPackageName());
            Log.d("categoryNameButton", category.getIconPath() + " id:" + resourceId);
            categoryBtn.setCompoundDrawablesWithIntrinsicBounds(0 , resourceId, 0, 0);

            // set on click listener for category button
            categoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddOrEditCategory(v);
                }
            });

            // set remaining category button properties
            categoryBtn.setText(category.getName());
            categoryBtn.setBackgroundColor(Color.WHITE);

            // create delete button
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

            frameLayout.addView(categoryBtn);
            frameLayout.addView(deleteBtn);

            gridLayout.addView(frameLayout);
        }
    }

    private void loadCategories(){
        // TODO: load categories from database
    }


}