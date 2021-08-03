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

        categories = new ArrayList<Category>();
        initCategoriesList();
    }

    public List<Category> getCategories(Context context) {
        if(categories == null || categories.isEmpty()){
            DataBaseHelper db = DataBaseHelper.getDbInstance(context);
            categories = db.getAllCategories();
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

    public Category getCategoryByName(String name) {
        for(Category category : categories) {
            if(name.equalsIgnoreCase(category.getName())) {
                return category;
            }
        }
        return null;
    }

    public void addNewCategory(String name, String iconFileName, boolean active, Context context) {
        Category category = new Category(categories.size(), name, iconFileName, active);
        categories.add(category);

        DataBaseHelper db = DataBaseHelper.getDbInstance(context);
        db.addCategory(category);
    }

    public void editCategory(int id, String name, String iconFileName, boolean active, Context context) {
        Category category = categories.get(id);
        category.setName(name);
        category.setIconPath(iconFileName);
        category.setActive(active);

        DataBaseHelper db = DataBaseHelper.getDbInstance(context);
        db.updateCategory(category);
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

    // initialize the default list of categories
    private void initCategoriesList() {
        Field[] fields = R.string.class.getFields();

        for (int  i =0; i < fields.length; i++) {
            String stringKeyName = fields[i].getName();
            if(stringKeyName.startsWith("category")) {
                String stringValue = res.getString(res.getIdentifier(stringKeyName, "string", packageName));
                Category category = new Category(i, stringValue, stringKeyName + "_icon"  , true); // TODO change later
                categories.add(category);
            }
        }
    }
}
