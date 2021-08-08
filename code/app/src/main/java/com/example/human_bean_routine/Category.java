package com.example.human_bean_routine;

import java.util.Random;

public class Category {
    private Integer categoryID;
    private String name; //must be unique
    private String iconPath;
    private Boolean active;


    public Category(String name, String iconPath, Boolean active) {
        Random rand = new Random();
        this.categoryID = rand.nextInt(100000000);
        this.name = name;
        this.iconPath = iconPath;
        this.active = active;
    }

    public Category(Integer categoryID,String name, String iconPath, Boolean active) {
    //    Random rand = new Random();
        this.categoryID = categoryID;
        this.name = name;
        this.iconPath = iconPath;
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }
}
