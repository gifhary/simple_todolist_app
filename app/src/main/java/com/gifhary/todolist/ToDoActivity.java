package com.gifhary.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class ToDoActivity extends AppCompatActivity {
    private static final String TAG = "ToDoActivity";

    private DatabaseHelper databaseHelper;

    //all tasks data are here
    private ArrayList<TaskConstructor> taskLists = new ArrayList<>();

    private String taskDate = "";
    private String taskTime = "";
    private int taskReminder = 0;
    private int taskImportance = 0;

    private View addTaskView;
    private EditText taskNameEditText;
    private ConstraintLayout setDueDateBtn;
    private ConstraintLayout setReminderBtn;
    private ImageView importantImageView;
    private TextView saveTextView;
    private TextView cancelTextView;

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
        //database class
        databaseHelper = new DatabaseHelper(ToDoActivity.this);
        getAllTask();

        TextView todayDateTextView = findViewById(R.id.todayDateTextView);
        //get today date and display
        todayDateTextView.setText(getTodayDate());

        FloatingActionButton addTaskButton = findViewById(R.id.addTaskButton);
        //floating button for new task
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskDialog();
            }
        });

        //layout inflater for new task dialog
        LayoutInflater inflater = LayoutInflater.from(ToDoActivity.this);
        addTaskView = inflater.inflate(R.layout.add_task_layout, null, false);

        //all components in new task dialog
        taskNameEditText = addTaskView.findViewById(R.id.taskNameEditText);
        importantImageView = addTaskView.findViewById(R.id.importantImageView);
        setDueDateBtn = addTaskView.findViewById(R.id.setDueDateBtn);
        setReminderBtn = addTaskView.findViewById(R.id.setReminderBtn);
        saveTextView = addTaskView.findViewById(R.id.saveTextView);
        cancelTextView = addTaskView.findViewById(R.id.cancelTextView);
        //all components in new task dialog

    }

    private void showAddTaskDialog(){
        setDialogContentClickable();

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ToDoActivity.this);
        alertDialog.setTitle("New Task");
        alertDialog.setView(addTaskView);

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((ViewGroup)addTaskView.getParent()).removeView(addTaskView);
            }
        });

        final AlertDialog alert = alertDialog.create();

        //use textView as button instead of button provided by dialog
        //dialog provided button will dismiss the dialog even input data is not valid
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameEditText.getText().toString();
                //keep alertDialog when user hasn't enter task name
                if (!taskName.equals("")){
                    saveTask(taskName);
                    alert.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(), "Enter task name", Toast.LENGTH_LONG).show();
                }
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void setDialogContentClickable(){
        importantImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskImportance == 0){
                    taskImportance = 1;
                }else {
                    taskImportance = 0;
                }
                updateImportanceBtn();
            }
        });
        setDueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        setReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskReminder == 0){
                    showTimePicker();
                }else {
                    taskReminder = 0;
                    taskTime = "";
                    changeBtnColor(setReminderBtn, taskTime);
                }
            }
        });
    }

    private void updateImportanceBtn(){
        if (taskImportance == 1){
            importantImageView.setImageResource(R.drawable.ic_star_on);
        }else {
            importantImageView.setImageResource(R.drawable.important);
        }
    }

    private void saveTask(String taskName){
        //add data method will return row id of data just inserted
        long result = databaseHelper.addData(taskName, taskDate, taskTime, taskReminder, taskImportance);

        Log.d(TAG, "task to save : " +taskName+" | " +taskDate+ " | " +taskTime+ " | " +taskReminder+ " | " +taskImportance);
        Log.d(TAG, "Just inserted row id : " + result);

        if (result == -1){
            //insert data will return -1 if its failed
            Toast.makeText(getApplicationContext(), "Error saving task", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_LONG).show();
            //immediately added to taskLists array instead of get newly added data from database
            taskLists.add(new TaskConstructor((int) result, taskName, taskDate, taskTime, taskReminder, taskImportance));

            //set all new task variable to default value
            setVarToDefault();
        }
    }

    private void setVarToDefault(){
        taskDate = "";
        taskTime = "";
        taskReminder = 0;
        taskImportance = 0;
    }

    private void getAllTask(){
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
    }

    private void showDatePicker(){
        Log.d(TAG, "Date picker dialog");
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        taskDate = dayOfMonth + "/" + monthOfYear + "/" + year;
                        changeBtnColor(setDueDateBtn, taskDate);
                        Log.d(TAG, "User pick date : " + dayOfMonth +"/"+monthOfYear+"/"+year);
                    }
                }, currentYear, currentMonth, currentDay);

        //disable date before current date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    //TODO future time only can be selected
    //this code now allow any time to be selected
    private void showTimePicker(){
        Log.d(TAG, "Time picker dialog");
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        taskTime = hourOfDay + ":" + minute;
                        //set taskReminder to 1 or equal to TRUE when user has set the reminder time
                        taskReminder = 1;
                        changeBtnColor(setReminderBtn, taskTime);

                        Log.d(TAG,"User pick time : "+hourOfDay + ":" + minute);
                    }
                }, currentHour, currentMinute, true);
        timePickerDialog.show();
    }

    //change button color for set due date and set reminder button
    private void changeBtnColor(ConstraintLayout layout, String value){
        if (!value.equals("")){
            layout.setBackgroundColor(ContextCompat.getColor(ToDoActivity.this, R.color.green));
        }else {
            layout.setBackgroundColor(ContextCompat.getColor(ToDoActivity.this, R.color.lightGrey));
        }
    }

    private String getTodayDate(){
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
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
