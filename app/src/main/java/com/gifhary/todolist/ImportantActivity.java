package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class ImportantActivity extends AppCompatActivity {
    //important tasks data are here
    private ArrayList<TaskConstructor> importantTasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important);
        //null check
        assert getSupportActionBar() != null;
        //show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //remove title in action bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //get intent extra from splash screen
        Intent intent = getIntent();
        importantTasks = intent.getParcelableArrayListExtra("taskData");

        //show task list in cardView list
        showTaskCardViewList();

    }

    private void showTaskCardViewList(){
        RecyclerView recTaskList = findViewById(R.id.importantTaskList);
        recTaskList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recTaskList.setLayoutManager(linearLayoutManager);

        TaskAdapter taskAdapter = new TaskAdapter(ImportantActivity.this, R.layout.card_view_adapter_layout, importantTasks);
        recTaskList.setAdapter(taskAdapter);
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
