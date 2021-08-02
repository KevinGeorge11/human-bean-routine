package com.example.human_bean_routine;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel {
    private List<Category> categories;
    private List<String> categoryTypes;
    private String packageName;
    private Resources res;

    // needs the package name and resource object from activity's context
    //  to dynamically load the default categories that are in the strings resource
    public CategoriesViewModel(String packageName, Resources res){
        this.packageName = packageName;
        this.res = res;

        categories = new ArrayList<Category>();
        categoryTypes = new ArrayList<String>();
        initCategoriesList();
    }

    public List<Category> getCategories() {
        if(categories == null || categories.isEmpty()){
            // TODO: load categories from database
        }
        return categories;
    }

    public void selectCategory(String name, boolean select) {
        for(Category category : categories) {
            if(category.getName().equals(name)) {
                category.setActive(select);
                return;
            }
        }
    }

    public void removeInactiveCategories() {
        for(Category category : categories) {
            if(!category.getActive()) {
                categories.remove(category);
            }
        }
    }

    public String getCategoryTypeAtIndex(int num) {
        return categoryTypes.get(num);
    }

    // initialize the default list of categories
    private void initCategoriesList() {
        Field[] fields = R.string.class.getFields();

        for (int  i =0; i < fields.length; i++) {
            String stringKey = fields[i].getName();
            if(stringKey.startsWith("category")) {
                categoryTypes.add(stringKey);
                String stringValue = res.getString(res.getIdentifier(stringKey, "string", packageName));
                Category category = new Category(i, stringValue, "/res/drawable/" + stringKey + ".xml" , false);
                categories.add(category);
            }
        }
    }
}
