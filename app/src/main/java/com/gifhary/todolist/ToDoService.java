package com.gifhary.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static com.gifhary.todolist.MainActivity.PREFERENCES;

public class ToDoService extends Service {

    public ToDoService(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Set<String> idSet = getTaskId(this);
        if (!idSet.isEmpty()){
            for (String id : idSet){
                //scheduled notification will cleared after phone restarted
                //reschedule all notification after phone restarted
                setAlarm(Integer.parseInt(id), getAlarmSchedule(id));
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private Calendar getAlarmSchedule(String id){
        DatabaseHelper db = new DatabaseHelper(this);
        return db.getDateTime(id);
    }

    private void setAlarm(int id, Calendar calendar) {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent,PendingIntent.FLAG_ONE_SHOT);

        manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private Set<String> getTaskId(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        return prefs.getStringSet("notifyId", new HashSet<String>());
    }
}
