package com.example.human_bean_routine;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel {
    private List<Category> categories;
    private final String packageName;
    private final Resources res;

    // needs the package name and resource object from activity's context
    //  to dynamically load the default categories that are in the strings resource
    public CategoriesViewModel(String packageName, Resources res){
        this.packageName = packageName;
        this.res = res;

    }

    public List<Category> getCategories() {
        if(categories == null){
            loadCategories();
        }
        return categories;
    }


    public Category getCategoryByName(String name) {
        for(Category category : categories) {
            if(name.equalsIgnoreCase(category.getName())) {
                return category;
            }
        }
        return null;
    }

    public void addNewCategory(String name, String iconFileName, boolean active) {
        Category category = new Category(categories.size(), name, iconFileName, active);
        categories.add(category);

        // TODO: save new category in database
    }

    public void editCategory(int id, String name, String iconFileName, boolean active) {
        Category category = categories.get(id);
        category.setName(name);
        category.setIconPath(iconFileName);
        category.setActive(active);

        // TODO: save edited category in database
    }

    public void deleteCategory(Category deletedCategory) {
        categories.remove(deletedCategory);

        // TODO: delete category from database
    }

    public void loadCategories() {
        categories = new ArrayList<Category>();
        // TODO: load categories from database
        // TODO: remove this later

        Field[] fields = R.string.class.getFields();

        // try to get default list from the string resources
        for (int  i =0; i < fields.length; i++) {
            String stringKeyName = fields[i].getName();
            if(stringKeyName.startsWith("category")) {
                String stringValue = res.getString(res.getIdentifier(stringKeyName, "string", packageName));
                Category category = new Category(i, stringValue, stringKeyName + "_icon"  , false);
                categories.add(category);
                //Log.d("category loaded","loaded name: " + category.getName());
            }
        }

    }


}
