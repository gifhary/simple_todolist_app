package com.gifhary.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private String userName;
    private TextView userNameTextView;
    private ImageView userAvatarImageView;
    private ListView avatarListView;
    private View changeAvatarView;
    private String[] assetFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //get username from preferences and display to username text view
        userName = loadName();
        userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText(userName);

        //Load user avatar file name with loadAvatarFile function
        String avatarFile = loadAvatarFile();
        userAvatarImageView = findViewById(R.id.userAvatarImageView);
        //if preferences is empty, default image displayed
        if (!avatarFile.equals("")){
            try {
                AssetManager assetManager = this.getAssets();

                InputStream inputStream = assetManager.open("avatars/" + avatarFile);
                Drawable drawable = Drawable.createFromStream(inputStream, null);

                userAvatarImageView.setImageDrawable(drawable);
            }catch (IOException e){
                e.printStackTrace();
                Log.d(TAG, "ERROR : LOAD IMAGE FROM ASSETS");
            }
        }

        //set user name clickable to change name
        userNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNameDialog();
            }
        });

        userAvatarImageView = findViewById(R.id.userAvatarImageView);
        userAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeAvatarDialog();
            }
        });

        //Home page menu items
        String[] menuNames = new String[]{"ToDo", "Important", "Planned", "Today"};
        ListView menuListView = findViewById(R.id.menuListView);
        //show menu items to list view
        addToListView(menuListView, menuNames);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        changeAvatarView = inflater.inflate(R.layout.change_avatar_layout, null, false);
        avatarListView = changeAvatarView.findViewById(R.id.avatarListView);
        avatarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateAvatar(assetFiles[position]);
            }
        });


    }

    private void updateAvatar(String filename){
        try {
            AssetManager assetManager = this.getAssets();

            InputStream inputStream = assetManager.open("avatars/" + filename);
            Drawable drawable = Drawable.createFromStream(inputStream, null);

            userAvatarImageView.setImageDrawable(drawable);
        }catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "ERROR : LOAD IMAGE FROM ASSETS");
        }

        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        Log.d(TAG, "avatar file : " + filename);

        prefsEditor.putString("avatarFile", filename);
        prefsEditor.apply();
    }

    //loadAvatarFile function read from preferences and return file name in string
    private String loadAvatarFile(){
        Log.d(TAG, "loadAvatar function");
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        String loadedAvatarFile = prefs.getString("avatarFile", "");

        Log.d(TAG, "avatar file name : " + loadedAvatarFile);
        return loadedAvatarFile;
    }

    private void showChangeAvatarDialog(){
        assetFiles = listAssetFiles();
        //show avatar img to avatar list view
        addToListView(avatarListView, assetFiles);

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        alertDialog.setTitle("Select Avatar");
        alertDialog.setView(changeAvatarView);

        alertDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((ViewGroup)changeAvatarView.getParent()).removeView(changeAvatarView);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((ViewGroup)changeAvatarView.getParent()).removeView(changeAvatarView);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();

        //without fixed size for alertDialog, getView method
        //in ListAdapter class will be called too many times
        //resulting more time to display alertDialog
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.9);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.65);

        Objects.requireNonNull(alert.getWindow()).setLayout(width, height);
    }

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
        }
        return list;
    }

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

                if (isNameEdited(editNameEditText.getText().toString()) && !editNameEditText.getText().toString().equals("")){
                    updateUserName(String.valueOf(editNameEditText.getText()));
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

    private boolean isNameEdited(String newName){
        return !newName.equals(userName);
    }

    private void updateUserName(String newName){
        userName = newName;
        userNameTextView.setText(userName);

        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("userName", newName);
        prefsEditor.apply();

        Log.d(TAG, "new name : " + newName);
    }

    //TODO task count should come from actual task count in database, except for avatar list view
    private void addToListView(ListView listView, String[] listNames){
        ArrayList<ListInfo> list = new ArrayList<>();

        Log.d(TAG, "addToMenuList function");
        ListInfo listInfo;
        for (String listName : listNames) {
            listInfo = new ListInfo(listName.toLowerCase(), listName, 0);
            list.add(listInfo);

            Log.d(TAG, "menu name : " + listName);
        }

        showListView(listView, list);
    }

    private void showListView(ListView listView, ArrayList<ListInfo> arrayList){
        Log.d(TAG, "show List function");

        ListAdapter menuAdapter = new ListAdapter(HomeActivity.this, R.layout.adapter_layout, arrayList);
        listView.setAdapter(menuAdapter);
    }

    private String loadName(){
        Log.d(TAG, "loadName function");
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        String loadedUserName = prefs.getString("userName", "Your Name");

        Log.d(TAG, "user name : " + loadedUserName);
        return loadedUserName;
    }
}
