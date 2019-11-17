package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class TodayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        //null check
        assert getSupportActionBar() != null;
        //show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //remove title in action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
