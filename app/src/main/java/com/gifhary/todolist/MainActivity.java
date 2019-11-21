package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

        //database task data
        DatabaseHelper db = new DatabaseHelper(this);
        final ArrayList<TaskConstructor> tasks = db.getAllData();

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
                    if (tasks.size()>0){
                        nameHasSet.putParcelableArrayListExtra("taskData", tasks);
                    }
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

        assert userName != null;
        Log.d(TAG, "has user set their name? : " + !userName.equals(""));
        return !userName.equals("");
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

        Log.d(TAG, "Today : "+day+ "/" + month + "/" + year);
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
