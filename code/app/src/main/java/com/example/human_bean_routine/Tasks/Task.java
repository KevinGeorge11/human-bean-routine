package com.example.human_bean_routine.Tasks;

import java.util.Random;

public class Task {
    private Integer taskId;
    private String taskName;
    private String description;
    private Integer categoryID;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String repeat;
    private String reminderDate;
    private String reminderTime;
    private Boolean complete;
    private String categoryName;

    public Task(String name, String description, Integer categoryID,
                String startDate, String startTime, String endDate, String endTime,
                String repeat, String reminderDate, String reminderTime, Boolean complete,
                String categoryName) {
        Random rand = new Random();
        this.taskId = rand.nextInt(100000000);
        this.taskName = name;
        this.description = description;
        this.categoryID = categoryID;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.repeat = repeat;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.complete = complete;
        this.categoryName = categoryName;
    }

    public Task(Integer taskID, String name, String description, Integer categoryID,
                String startDate, String startTime, String endDate, String endTime,
                String repeat, String reminderDate, String reminderTime, Boolean complete,
                String categoryName) {
        this.taskId = taskID;
        this.taskName = name;
        this.description = description;
        this.categoryID = categoryID;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.repeat = repeat;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.complete = complete;
        this.categoryName = categoryName;
    }

    public Task(String taskName, String categoryName, int categoryID, String description, String startDate) {
        Random rand = new Random();
        this.taskId = rand.nextInt(100000000);
        this.taskName = taskName;
        this.categoryName = categoryName;
        this.categoryID = categoryID;
        this.complete = false;
        this.description = description;
        this.startDate = startDate;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setName(String name) {
        this.taskName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public void editTaskName(String newTaskName) {
        this.taskName = newTaskName;
    }
}
