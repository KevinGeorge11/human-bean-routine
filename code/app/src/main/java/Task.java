public class Task {
    private Integer taskId;
    private String name;
    private String description;
    private String category;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String repeat;
    private String reminderDate;
    private String reminderTime;
    private Boolean complete;

    public Task(String name, String description, String category,
                String startDate, String startTime, String endDate, String endTime,
                String repeat, String reminderDate, String reminderTime, Boolean complete) {
        this.taskId = -1;
        this.name = name;
        this.description = description;
        this.category = category;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.repeat = repeat;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.complete = complete;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
