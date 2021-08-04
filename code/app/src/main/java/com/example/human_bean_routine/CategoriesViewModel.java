package com.example.human_bean_routine;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel {
    // the user selected categories data
    private List<Category> categories;
    // TODO : remove the packageName and res fields
    private final String packageName;
    private final Resources res;

    // TODO : remove this constructor as it wont be needed
    public CategoriesViewModel(String packageName, Resources res){
        this.packageName = packageName;
        this.res = res;
        loadCategories();
    }

    // get all the categories or reload them if the data is not already existing
    public List<Category> getCategories() {
        if(categories == null){
            loadCategories();
        }
        return categories;
    }

    // get the category by name from list data
    public Category getCategoryByName(String name) {
        for(Category category : categories) {
            if(name.equalsIgnoreCase(category.getName())) {
                return category;
            }
        }
        return null;
    }

    // add new category to list data and save to category database
    public void addNewCategory(String name, String iconFileName, boolean active) {
        Category category = new Category(categories.size(), name, iconFileName, active);
        categories.add(category);

        // TODO: save new category in database
    }

    // edit existing category on list data and update category database
    public void editCategory(int id, String name, String iconFileName, boolean active) {
        Category category = categories.get(id);
        category.setName(name);
        category.setIconPath(iconFileName);
        category.setActive(active);

        // TODO: save edited category in database
    }

    // remove existing category from list data and delete it in category database
    public void deleteCategory(Category deletedCategory) {
        categories.remove(deletedCategory);

        // TODO: delete category from database
    }

    // load all the user selected categories from category database
    public void loadCategories() {
        categories = new ArrayList<Category>();
        // TODO: load categories from database

        // TODO: remove this selection after integrated with database
        Field[] fields = R.string.class.getFields();
        for (int  i =0; i < fields.length; i++) {
            String stringKeyName = fields[i].getName();
            if(stringKeyName.startsWith("category")) {
                String stringValue = res.getString(res.getIdentifier(stringKeyName, "string", packageName));
                Category category = new Category(i, stringValue, stringKeyName + "_icon"  , false);
                categories.add(category);
            }
        }
        // TODO: remove this selection after integrated with database
    }

    // checking the new name does not overlap any other existing category names
    public boolean checkIfCategoryNameIsDuplicate(String newName){
        for(Category category : categories) {
            if(newName.equalsIgnoreCase(category.getName())) {
                return true;
            }
        }
        return false;
    }


}
