package com.gifhary.todolist;

public class ListInfo {
    private String iconName;
    private String menuName;
    private int taskCount;

    public ListInfo(String iconName, String menuName, int taskCount) {
        this.iconName = iconName;
        this.menuName = menuName;
        this.taskCount = taskCount;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}
