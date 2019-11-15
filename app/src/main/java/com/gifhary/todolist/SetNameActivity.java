package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.gifhary.todolist.MainActivity.PREFERENCES;

public class SetNameActivity extends AppCompatActivity {
    EditText yourNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        yourNameEditText = findViewById(R.id.yourNameEditText);
    }

    public void setName(View view){
        String userName = yourNameEditText.getText().toString();

        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        prefsEditor.putString("userName", userName);
        prefsEditor.putBoolean("hasSetName", true);
        prefsEditor.apply();

        moveToHome();
    }

    private void moveToHome(){
        Intent toHome = new Intent(SetNameActivity.this, HomeActivity.class);
        startActivity(toHome);
        finish();
    }
}
