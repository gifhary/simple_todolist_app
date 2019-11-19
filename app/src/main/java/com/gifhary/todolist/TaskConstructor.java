package com.gifhary.todolist;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskConstructor implements Parcelable {
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

    private TaskConstructor(Parcel in) {
        taskId = in.readInt();
        taskName = in.readString();
        taskDate = in.readString();
        taskTime = in.readString();
        taskReminder = in.readInt();
        taskImportance = in.readInt();
    }

    public static final Creator<TaskConstructor> CREATOR = new Creator<TaskConstructor>() {
        @Override
        public TaskConstructor createFromParcel(Parcel in) {
            return new TaskConstructor(in);
        }

        @Override
        public TaskConstructor[] newArray(int size) {
            return new TaskConstructor[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(taskId);
        dest.writeString(taskName);
        dest.writeString(taskDate);
        dest.writeString(taskTime);
        dest.writeInt(taskReminder);
        dest.writeInt(taskImportance);
    }
}
