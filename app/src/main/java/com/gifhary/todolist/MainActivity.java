package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public static final String PREFERENCES = "prefs";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasSetName()) {
                    Intent nameHasSet = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(nameHasSet);
                    finish();

                } else {
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

        Log.d(TAG, "has user set their name? : " + userName);
        return !userName.equals("");
    }
}
