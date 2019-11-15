package com.gifhary.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.gifhary.todolist.MainActivity.PREFERENCES;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<MenuInfo> menuList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //get username from preferences and display to username text view
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        userNameTextView.setText(loadName());

        //Home page menu items
        String[] menuNames = new String[]{"ToDo", "Important", "Planned", "Today"};
        addToMenuList(menuNames);

    }

    private void addToMenuList(String[] menuNames){
        MenuInfo menuInfo;
        for (String menuName : menuNames) {
            menuInfo = new MenuInfo("ic_" + menuName.toLowerCase(), menuName, 0);
            menuList.add(menuInfo);
        }

        showMenuList();
    }

    private void showMenuList(){
        ListView menuListView = findViewById(R.id.menuListView);

        MenuAdapter menuAdapter = new MenuAdapter(HomeActivity.this, R.layout.adapter_layout, menuList);
        menuListView.setAdapter(menuAdapter);
    }

    private String loadName(){
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);
        return prefs.getString("userName", "Your Name");
    }
}
