package com.example.human_bean_routine;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CategoriesViewModel {
    // the user selected categories data
    private List<Category> categories;
    private Context context;

    public CategoriesViewModel(Context context){
        this.context = context;
        loadCategoriesList(this.context);
    }

    // get all the categories or reload them if the data is not already existing
    public List<Category> getCategories(Context context) {
        if(categories == null){
            loadCategoriesList(this.context);
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

        DataBaseHelper db = DataBaseHelper.getDbInstance(context);
        db.addCategory(category);
    }

    // edit existing category on list data and update category database
    public void editCategory(int id, String name, String iconFileName, boolean active) {
        Category category = categories.get(id-1);
        category.setName(name);
        category.setIconPath(iconFileName);
        category.setActive(active);

        DataBaseHelper db = DataBaseHelper.getDbInstance(context);
        db.updateCategory(category);
    }

    // remove existing category from list data and delete it in category database
    public void deleteCategory(Category deletedCategory) {
        categories.remove(deletedCategory);
    }


    public void removeInactiveCategories(Context context) {
        DataBaseHelper db = DataBaseHelper.getDbInstance(context);

        for(Category category : categories) {
            if(!category.getActive()) {
                db.deleteCategory(category.getName());
                categories.remove(category);
            }
        }
    }

    // load all the user selected categories from category database
    private void loadCategoriesList(Context context) {
        DataBaseHelper db = DataBaseHelper.getDbInstance(context);
        categories = db.getAllCategories();
        /*Field[] fields = R.string.class.getFields();
        Field[] fields = R.string.class.getFields();
        for (int  i =0; i < fields.length; i++) {
            String stringKeyName = fields[i].getName();
            if(stringKeyName.startsWith("category")) {
                String stringValue = res.getString(res.getIdentifier(stringKeyName, "string", packageName));
                Category category = new Category(i, stringValue, stringKeyName + "_icon"  , false);
                categories.add(category);
            }
        }*/
    }

    // checking the new name does not overlap any other existing category names
    public boolean checkIfCategoryNameIsDuplicate(String newName, int currentCategoryId){
        for(Category category : categories) {
            if(currentCategoryId != category.getCategoryID()
                    && newName.equalsIgnoreCase(category.getName())){
                return true;
            }
        }
        return false;
    }


}
