package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

//splash screen page
public class MainActivity extends AppCompatActivity {
    public static final String PREFERENCES = "prefs";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //two second splash screen
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //if user has set their name, move directly to HomeActivity
                if (hasSetName()) {
                    setTaskCount();
                    setImportantTaskCount();
                    setPlannedTaskCount();
                    setTodayTaskCount();

                    Intent nameHasSet = new Intent(MainActivity.this, HomeActivity.class);
                    nameHasSet.putParcelableArrayListExtra("taskData", getAllTask());
                    startActivity(nameHasSet);
                    finish();

                }
                //for newly installed app, user need to set their name first
                else {
                    Intent nameNotSet = new Intent(MainActivity.this, SetNameActivity.class);
                    startActivity(nameNotSet);
                    finish();
                }
            }
        }, 2000);
    }

    private boolean hasSetName(){
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        String userName = prefs.getString("userName", "");
        Log.d(TAG, "has user set their name? : " + userName);
        assert userName != null;
        return !userName.equals("");
    }

    private ArrayList<TaskConstructor> getAllTask(){
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        ArrayList<TaskConstructor> taskLists = new ArrayList<>();

        //get all task from database
        Log.d(TAG, "getAllTask");
        Cursor cursor = databaseHelper.getAllData();
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String taskName = cursor.getString(cursor.getColumnIndex("task_name"));
                String taskDate = cursor.getString(cursor.getColumnIndex("task_date"));
                String taskTime = cursor.getString(cursor.getColumnIndex("task_time"));
                int taskReminder = cursor.getInt(cursor.getColumnIndex("task_reminder"));
                int taskImportance = cursor.getInt(cursor.getColumnIndex("task_importance"));

                Log.d(TAG, "Loaded data from database : "+id+"|"+taskName+"|"+taskDate+"|"+taskTime+"|"+taskReminder+"|"+taskImportance);

                //insert all data in tasksList with TaskConstructor object
                taskLists.add(new TaskConstructor(id, taskName, taskDate, taskTime, taskReminder, taskImportance));
            }
        }
        return taskLists;
    }

    private void setTaskCount(){
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        int count = (int) databaseHelper.getTaskCount();

        setIntPrefs("ToDo", count);
    }

    private void setImportantTaskCount(){
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        int count = (int) databaseHelper.getImportantCount();

        setIntPrefs("Important", count);
    }

    private void setPlannedTaskCount(){
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        int count = (int) databaseHelper.getPlannedTaskCount();

        setIntPrefs("Planned", count);
    }

    private void setTodayTaskCount(){
        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
        int count = (int) databaseHelper.getTodayTaskCount(getTodayDate());

        setIntPrefs("Today", count);
    }

    private String getTodayDate(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return day+ "/" + month + "/" + year;
    }

    private void setIntPrefs(String key, int value){
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        Log.d(TAG, key +" value : " + value);
        prefsEditor.putInt(key, value);
        prefsEditor.apply();
    }
}
