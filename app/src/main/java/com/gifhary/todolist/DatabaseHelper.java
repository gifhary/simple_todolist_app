package com.gifhary.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "to_do_list";

    private static final String TABLE_NAME = "tasks";

    private static final String COLUMN1 = "id";
    private static final String COLUMN2 = "task_name";
    private static final String COLUMN3 = "task_date";
    private static final String COLUMN4 = "task_time";
    private static final String COLUMN5 = "task_importance" ;

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
                + COLUMN5 + " INTEGER DEFAULT 0);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addData(String taskName, String taskDate, String taskTime, int taskImportance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN2, taskName);
        contentValues.put(COLUMN3, taskDate);
        contentValues.put(COLUMN4, taskTime);
        contentValues.put(COLUMN5, taskImportance);

        return db.insert(TABLE_NAME, null, contentValues);
    }

    public ArrayList<TaskConstructor> getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<TaskConstructor> taskLists = new ArrayList<>();
        try (Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN1));
                    String taskName = cursor.getString(cursor.getColumnIndex(COLUMN2));
                    String taskDate = cursor.getString(cursor.getColumnIndex(COLUMN3));
                    String taskTime = cursor.getString(cursor.getColumnIndex(COLUMN4));
                    int taskImportance = cursor.getInt(cursor.getColumnIndex(COLUMN5));

                    //insert all data in tasksList with TaskConstructor object
                    taskLists.add(new TaskConstructor(id, taskName, taskDate, taskTime, taskImportance));
                }
            }
        }
        return taskLists;
    }

    public int deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN1+" = "+ id, null);
    }

    public int updateData(String id, ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_NAME, values, COLUMN1+" = "+ id, null);
    }
    public long getTaskCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public long getImportantCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, COLUMN5+" = 1");
    }

    public long getPlannedTaskCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, "NOT " +COLUMN3+" = ''");
    }

    public long getTodayTaskCount(String todayDate){
        SQLiteDatabase db = this.getWritableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME, COLUMN3+" = '" + todayDate + "'");
    }

}
