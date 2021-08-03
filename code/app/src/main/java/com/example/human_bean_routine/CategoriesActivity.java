package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

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

        List<Category> categoryList = categoriesViewModel.getCategories(this);
        for(Category category : categoryList) {

            // create category button and set layout properties
            Button btn= new Button(this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(375, 375);
            btnParams.setMargins(20, 20, 20, 20);
            btn.setLayoutParams(btnParams);

            // get associated category icon from drawable resources
            int resourceId = getResources().getIdentifier(category.getIconPath(), "drawable", getPackageName());
            Log.d("categoryNameButton", category.getIconPath() + " id:" + resourceId);
            btn.setCompoundDrawablesWithIntrinsicBounds(0 , resourceId, 0, 0);

            // set on click listener for button
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddOrEditCategory(v);
                }
            });


            // set remaining button properties
            btn.setText(category.getName());
            btn.setBackgroundColor(Color.WHITE);
            gridLayout.addView(btn);
        }
    }

    private void loadCategories(){
        // TODO: load categories from database
        // Please verify if this is correct
        categoriesViewModel.getCategories(this);
    }


}