package com.gifhary.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import static com.gifhary.todolist.MainActivity.PREFERENCES;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    //all tasks data are here
    private ArrayList<TaskConstructor> taskLists = new ArrayList<>();

    private String userName;
    private TextView userNameTextView;
    private ImageView userAvatarImageView;
    private ListView avatarListView;
    private View changeAvatarView;
    private String[] assetFiles;

    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //get intent extra from splash screen
        Intent intent = getIntent();
        taskLists = intent.getParcelableArrayListExtra("taskData");

        //get username from preferences and display to username text view
        userName = getStringPrefs("userName");
        if (userName.equals("")){
            //name not empty assurance. if empty go back to set name activity
            Intent backToSetName = new Intent(HomeActivity.this, SetNameActivity.class);
            startActivity(backToSetName);
            finish();
        }
        userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText(userName);
        //set user name clickable to change name
        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNameDialog();
            }
        });

        //Home page menu items
        final String[] menuNames = new String[]{"ToDo", "Important", "Planned", "Today"};
        ListView menuListView = findViewById(R.id.menuListView);
        //show menu items to list view
        addToListView(menuListView, menuNames);

        //selected menu listView click listener
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(menuNames[position]);
            }
        });

        //Load user avatar file name with loadAvatarFile function
        String avatarFile = getStringPrefs("avatarFile");
        userAvatarImageView = findViewById(R.id.userAvatarImageView);
        //if preferences is empty for avatar file name, default image displayed
        if (!avatarFile.equals("")){
            try {
                AssetManager assetManager = this.getAssets();

                InputStream inputStream = assetManager.open("avatars/" + avatarFile);
                Drawable drawable = Drawable.createFromStream(inputStream, null);

                userAvatarImageView.setImageDrawable(drawable);
            }catch (IOException e){
                e.printStackTrace();
                Log.d(TAG, "ERROR : load image first open");
                Toast.makeText(getApplicationContext(), "ERROR : load image first open", Toast.LENGTH_LONG).show();
            }
        }

        //make user avatar clickable
        userAvatarImageView = findViewById(R.id.userAvatarImageView);
        userAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show a listView of all image file inside assets/avatars/ folder
                showChangeAvatarDialog();
            }
        });

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        changeAvatarView = inflater.inflate(R.layout.change_avatar_layout, null, false);
        avatarListView = changeAvatarView.findViewById(R.id.avatarListView);

        //selected avatar listView click listener
        avatarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //dismiss alertDialog when user selected new avatar
                alert.dismiss();
                //update user avatar when user selected new avatar
                updateAvatar(assetFiles[position]);
            }
        });
    }

    //open activity with class name in string
    private void openActivity(String activityName){
        Log.d(TAG, "openActivity");
        try {
            Class<?> aClass = Class.forName("com.gifhary.todolist." + activityName + "Activity");
            Intent intent = new Intent(this, aClass);

            if (activityName.equals("ToDo")){
                intent.putParcelableArrayListExtra("taskData", taskLists);
            }

            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR : CLASS NOT FOUND");
            Toast.makeText(getApplicationContext(), "ERROR : class not found", Toast.LENGTH_LONG).show();
        }
    }

    private void updateAvatar(String filename){
        try {
            AssetManager assetManager = this.getAssets();
            InputStream inputStream = assetManager.open("avatars/" + filename);
            Drawable drawable = Drawable.createFromStream(inputStream, null);

            //update avatar in imageView
            userAvatarImageView.setImageDrawable(drawable);

            //update avatar in shared preferences
            setStringPrefs("avatarFile", filename);
        }catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "ERROR : update image");
            Toast.makeText(getApplicationContext(), "ERROR : update image", Toast.LENGTH_LONG).show();
        }
    }

    private void showChangeAvatarDialog(){
        assetFiles = listAssetFiles();
        //show avatar images to avatar list view
        addToListView(avatarListView, assetFiles);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Select Avatar");
        alertDialog.setView(changeAvatarView);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((ViewGroup)changeAvatarView.getParent()).removeView(changeAvatarView);
            }
        });
        alert = alertDialog.create();
        alert.show();

        //without fixed size for alertDialog, getView method
        //in ListAdapter class will be called too many times
        //resulting more time to display alertDialog
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.9);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.65);

        Objects.requireNonNull(alert.getWindow()).setLayout(width, height);
    }

    //get all file name in assets/avatars/ folder and return as array
    private String[] listAssetFiles() {
        final AssetManager assetManager = getAssets();
        String [] list = new String[0];

        try {
            list = assetManager.list("avatars");
            if (list != null) {
                for (String file : list) {
                    Log.d(TAG, "asset file name : " + file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR : list avatar files");
            Toast.makeText(getApplicationContext(), "ERROR : list avatar files", Toast.LENGTH_LONG).show();
        }
        return list;
    }

    //display edit name dialog box
    private void showEditNameDialog(){
        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        final View view = inflater.inflate(R.layout.edit_name_layout, null, false);

        final EditText editNameEditText = view.findViewById(R.id.editNameEditText);
        editNameEditText.setText(userName);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Edit User Name");
        alertDialog.setView(view);

        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String newName = editNameEditText.getText().toString();
                if (isNameEdited(newName) && !newName.equals("")){
                    //update user name in textView
                    userName = newName;
                    userNameTextView.setText(userName);
                    //update user name in shared preferences
                    setStringPrefs("userName", newName);
                    Log.d(TAG, "new name : " + newName);
                }
                else if (String.valueOf(editNameEditText.getText()).equals("")){
                    Toast.makeText(getApplicationContext(), "Enter your name", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    //compare new name and saved username
    private boolean isNameEdited(String newName){
        return !newName.equals(userName);
    }

    /*
    Store menu names into ArrayList with ListInfo object
    with data of icon file name (file name must use menu name in lowercase),
    menu name itself from string array and item count from sharedPreferences
    with menu name as the pref key
     */
    private void addToListView(ListView listView, String[] listNames){
        ArrayList<ListInfo> list = new ArrayList<>();

        Log.d(TAG, "addToMenuList function");
        ListInfo listInfo;
        for (String listName : listNames) {
            listInfo = new ListInfo(listName.toLowerCase(), listName, getIntPrefs(listName));
            list.add(listInfo);

            Log.d(TAG, "menu name : " + listName);
        }

        showListView(listView, list);
    }

    //display listView with custom adapter
    private void showListView(ListView listView, ArrayList<ListInfo> arrayList){
        Log.d(TAG, "show List function");

        ListAdapter menuAdapter = new ListAdapter(HomeActivity.this, R.layout.adapter_layout, arrayList);
        listView.setAdapter(menuAdapter);
    }

    private int getIntPrefs(String key){
        Log.d(TAG, "getIntPrefs function");
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);

        return prefs.getInt(key, 0);
    }

    private String getStringPrefs(String key){
        Log.d(TAG, "getStringPrefs function");
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);

        return prefs.getString(key, "");
    }

    private void setStringPrefs(final String key, final String value){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "setStringPrefs function");
                SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
                SharedPreferences.Editor prefsEditor = prefs.edit();

                prefsEditor.putString(key, value);
                prefsEditor.apply();
            }
        }).start();
    }
}
