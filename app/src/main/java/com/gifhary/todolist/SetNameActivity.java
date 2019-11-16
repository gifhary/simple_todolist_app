package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.gifhary.todolist.MainActivity.PREFERENCES;

public class SetNameActivity extends AppCompatActivity {
    private EditText yourNameEditText;
    private static final String TAG = "SetNameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        yourNameEditText = findViewById(R.id.yourNameEditText);
    }

    public void setName(View view){
        String userName = yourNameEditText.getText().toString();

        if (!userName.equals("")){
            SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
            SharedPreferences.Editor prefsEditor = prefs.edit();

            Log.d(TAG, "userName : " + userName);

            prefsEditor.putString("userName", userName);
            prefsEditor.apply();

            moveToHome();
        }else {
            Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_LONG).show();
        }
    }

    private void moveToHome(){
        Intent toHome = new Intent(SetNameActivity.this, HomeActivity.class);
        startActivity(toHome);
        finish();
    }
}
