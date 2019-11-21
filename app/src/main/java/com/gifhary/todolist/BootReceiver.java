package com.gifhary.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //phone boot complete receiver
        Intent i = new Intent(context, ToDoService.class);
        context.startService(i);
    }
}
