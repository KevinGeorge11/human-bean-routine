package com.example.human_bean_routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
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

    private void createCategoryButtons(){
        // get the grid layout we are on
        GridLayout gridLayout = (GridLayout) findViewById(R.id.glCategoriesGridLayout);

        List<Category> categoryList = categoriesViewModel.getCategories();
        int categoryIndex = 0;
        for(Category category : categoryList) {

            // create category button and set layout properties
            Button btn= new Button(this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(375, 375);
            btnParams.setMargins(20, 20, 20, 20);
            btn.setLayoutParams(btnParams);

            // get associated category icon from drawable resources
            String categoryType = categoriesViewModel.getCategoryTypeAtIndex(categoryIndex);
            int resourceId = getResources().getIdentifier(categoryType + "_icon", "drawable", getPackageName());
            Log.d("categoryNameButton", categoryType + " id:" + resourceId);
            btn.setCompoundDrawablesWithIntrinsicBounds(0 , resourceId, 0, 0);

            // set remaining button properties
            btn.setText(category.getName());
            btn.setBackgroundColor(Color.WHITE);
            gridLayout.addView(btn);

            categoryIndex++;
        }


    }

    private void loadCategories(){
        // TODO: load categories
    }


}