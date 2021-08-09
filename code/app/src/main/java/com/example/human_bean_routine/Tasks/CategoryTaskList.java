package com.example.human_bean_routine.Tasks;

import com.example.human_bean_routine.Tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class CategoryTaskList {

    private String categoryName;
    private List<Task> tasks;

    public CategoryTaskList(String categoryName, List<Task> tasks) {
        this.categoryName = categoryName;
        this.tasks = tasks;
    }

    public CategoryTaskList(String categoryName) {
        this.categoryName = categoryName;
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public String getCategoryName() {
        return this.categoryName;
    }
}
