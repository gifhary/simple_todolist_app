package com.gifhary.todolist;

public class TaskConstructor {
    private int taskId;
    private String taskName;
    private String taskDate;
    private String taskTime;
    private int taskReminder;
    private int taskImportance;

    public TaskConstructor(int taskId, String taskName, String taskDate, String taskTime, int taskReminder, int taskImportance){
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskReminder = taskReminder;
        this.taskImportance = taskImportance;
    }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public int getTaskReminder() {
        return taskReminder;
    }

    public void setTaskReminder(int taskReminder) {
        this.taskReminder = taskReminder;
    }

    public int getTaskImportance() {
        return taskImportance;
    }

    public void setTaskImportance(int taskImportance) {
        this.taskImportance = taskImportance;
    }
}
