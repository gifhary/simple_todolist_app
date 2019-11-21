package com.gifhary.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "to_do_list";

    private static final String TABLE_NAME = "tasks";

    private static final String COLUMN1 = "id";
    private static final String COLUMN2 = "task_name";
    private static final String COLUMN3 = "task_date";
    private static final String COLUMN4 = "task_time";
    private static final String COLUMN5 = "task_reminder";
    private static final String COLUMN6 = "task_importance" ;

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN2 + " TEXT, "
                + COLUMN3 + " TEXT, "
                + COLUMN4 + " TEXT, "
                + COLUMN5 + " INTEGER DEFAULT 0, "
                + COLUMN6 + " INTEGER DEFAULT 0);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    long addData(String taskName, String taskDate, String taskTime, int taskReminder, int taskImportance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN2, taskName);
        contentValues.put(COLUMN3, taskDate);
        contentValues.put(COLUMN4, taskTime);
        contentValues.put(COLUMN5, taskReminder);
        contentValues.put(COLUMN6, taskImportance);

        return db.insert(TABLE_NAME, null, contentValues);
    }

    ArrayList<TaskConstructor> getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<TaskConstructor> taskLists = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN1));
                    String taskName = cursor.getString(cursor.getColumnIndex(COLUMN2));
                    String taskDate = cursor.getString(cursor.getColumnIndex(COLUMN3));
                    String taskTime = cursor.getString(cursor.getColumnIndex(COLUMN4));
                    int taskReminder = cursor.getInt(cursor.getColumnIndex(COLUMN5));
                    int taskImportance = cursor.getInt(cursor.getColumnIndex(COLUMN6));

                    //insert all data in tasksList with TaskConstructor object
                    taskLists.add(new TaskConstructor(id, taskName, taskDate, taskTime, taskReminder, taskImportance));
                }
            }
        }
        return taskLists;
    }

    Calendar getDateTime(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        try (Cursor cursor = db.rawQuery("SELECT " +COLUMN3+ ", " +COLUMN4+ " WHERE "+COLUMN1+ " = " +id , null)) {
            String date = cursor.getString(cursor.getColumnIndex(COLUMN3));
            String time = cursor.getString(cursor.getColumnIndex(COLUMN4));
            return stringToCalendar(date, time);
        }
    }

    private Calendar stringToCalendar(String date, String time){
        String[] dateArr = date.split("/");
        String[] timeArr = time.split(":");

        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArr[0]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArr[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArr[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArr[1]));
        calendar.set(Calendar.YEAR,Integer.parseInt(dateArr[2]));

        return calendar;
    }

    int deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN1+" = "+ id, null);
    }

    int updateData(String id, ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME, values, COLUMN1+" = "+ id, null);
    }
    long getTaskCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    long getImportantCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, COLUMN6+" = 1");
    }

    long getPlannedTaskCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, "NOT " +COLUMN3+" = ''");
    }

    long getTodayTaskCount(String todayDate){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, COLUMN3+" = '" + todayDate + "'");
    }

}
