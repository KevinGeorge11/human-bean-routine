package com.example.human_bean_routine;

public class Category {
    private String name; //must be unique
    private String iconPath;
    private Boolean active;


    public Category(String name, String iconPath, Boolean active) {
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
}
