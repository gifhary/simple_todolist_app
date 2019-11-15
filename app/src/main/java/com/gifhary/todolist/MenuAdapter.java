package com.gifhary.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MenuAdapter extends ArrayAdapter<MenuInfo> {
    private static final String TAG = "MenuAdapter";

    private Context context;
    private int resource;

    public MenuAdapter(Context context, int resource, ArrayList<MenuInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String iconName = getItem(position).getIconName();
        String menuName = getItem(position).getMenuName();
        int taskCount = getItem(position).getTaskCount();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        ImageView menuIconImageView = convertView.findViewById(R.id.menuIconImageView);
        TextView menuNameTextView = convertView.findViewById(R.id.menuNameTextView);
        TextView taskCountTextView = convertView.findViewById(R.id.taskCountTextView);

        int drawableId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        menuIconImageView.setImageResource(drawableId);
        menuNameTextView.setText(menuName);
        taskCountTextView.setText(String.valueOf(taskCount));

        return convertView;
    }
}
