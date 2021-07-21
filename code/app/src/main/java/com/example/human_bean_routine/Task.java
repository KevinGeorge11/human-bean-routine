package com.example.human_bean_routine;

public class Task {
    private String taskName;
    private String description;
    private String category;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void editTaskName(String newTaskName) {
        this.taskName = newTaskName;
    }
}
