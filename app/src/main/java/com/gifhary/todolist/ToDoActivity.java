package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ToDoActivity extends AppCompatActivity {
    private static final String TAG = "ToDoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        //null check
        assert getSupportActionBar() != null;
        //show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //remove title in action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView todayDateTextView = findViewById(R.id.todayDateTextView);
        todayDateTextView.setText(getTodayDate());

        //TODO add task button
        //TODO display created task

    }

    private String getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");

        return dateFormat.format(calendar.getTime());
    }

    //back arrow function
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
