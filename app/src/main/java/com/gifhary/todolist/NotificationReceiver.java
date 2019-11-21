package com.gifhary.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;

import java.util.HashSet;
import java.util.Set;

import static com.gifhary.todolist.MainActivity.PREFERENCES;


public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Set<String> idSet = getTaskId(context);
        if (!idSet.isEmpty()){
            for (String id : idSet){
                NotificationHelper notificationHelper = new NotificationHelper(context, Integer.parseInt(id));
                NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
                notificationHelper.getManager().notify(Integer.parseInt(id), nb.build());
            }
        }
    }

    private Set<String> getTaskId(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        return prefs.getStringSet("notifyId", new HashSet<String>());
    }

}