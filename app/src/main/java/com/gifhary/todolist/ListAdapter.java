package com.gifhary.todolist;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class ListAdapter extends ArrayAdapter<ListInfo> {
    private static final String TAG = "ListAdapter";

    private Context context;
    private int resource;

    ListAdapter(Context context, int resource, ArrayList<ListInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String iconName = Objects.requireNonNull(getItem(position)).getIconName();
        String menuName = Objects.requireNonNull(getItem(position)).getMenuName();
        int taskCount = Objects.requireNonNull(getItem(position)).getTaskCount();

        String taskCountStr = "";
        if (taskCount > 0){
            taskCountStr = String.valueOf(taskCount);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        ImageView menuIconImageView = convertView.findViewById(R.id.menuIconImageView);
        TextView menuNameTextView = convertView.findViewById(R.id.menuNameTextView);
        TextView taskCountTextView = convertView.findViewById(R.id.taskCountTextView);

        if (iconName.contains("avatar")){
            try {
                AssetManager assetManager = context.getAssets();

                InputStream inputStream = assetManager.open("avatars/" + iconName);
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                inputStream.close();

                menuIconImageView.setImageDrawable(drawable);
                menuNameTextView.setText(menuName);
                taskCountTextView.setText(taskCountStr);

                Log.d(TAG, "icon : " + iconName + "|| name : " + menuName + "|| task count : " + taskCount);
            }catch (IOException e){
                e.printStackTrace();
                Log.d(TAG, "ERROR : LOAD IMAGE FROM ASSETS");
            }
            return convertView;
        }else {
            int drawableId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

            menuIconImageView.setImageResource(drawableId);
            menuNameTextView.setText(menuName);
            taskCountTextView.setText(taskCountStr);

            Log.d(TAG, "icon : " + iconName + "|| name : " + menuName + "|| task count : " + taskCount);
            return convertView;
        }
    }
}
