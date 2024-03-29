package com.gifhary.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.gifhary.todolist.MainActivity.PREFERENCES;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private static final String TAG = "TaskAdapter";

    private Context context;
    private int resource;
    private ArrayList<TaskConstructor> taskLists;

    TaskAdapter(Context context, int resource, ArrayList<TaskConstructor> taskLists) {
        this.context = context;
        this.resource = resource;
        this.taskLists = taskLists;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(resource, parent, false);

        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, int position) {
        final TaskConstructor taskConstructor = taskLists.get(position);

        holder.cardTaskId.setText(String.valueOf(taskConstructor.getTaskId()));
        holder.cardCheckBox.setImageResource(R.drawable.ic_box);
        holder.cardTaskName.setText(taskConstructor.getTaskName());

        if (taskConstructor.getTaskImportance() == 1){
            holder.cardImportantIcon.setImageResource(R.drawable.ic_star_on);
        }else {
            holder.cardImportantIcon.setImageResource(R.drawable.important);
        }

        if (taskConstructor.getTaskDate().equals("")){
            holder.cardDateIcon.setVisibility(View.INVISIBLE);
            holder.cardDate.setVisibility(View.INVISIBLE);
        }else {
            holder.cardDate.setText(taskConstructor.getTaskDate());
        }

        if (taskConstructor.getTaskTime().equals("")){
            holder.cardReminderIcon.setVisibility(View.INVISIBLE);
            holder.cardTime.setVisibility(View.INVISIBLE);
        }else {
            holder.cardTime.setText(taskConstructor.getTaskTime());
        }

        setCheckListener(holder.cardCheckBox, holder.cardTaskId.getText().toString(), position);
        setImportantListener(holder.cardImportantIcon,
                taskConstructor.getTaskImportance(),
                holder.cardTaskId.getText().toString(),
                position);
    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }

    private void setCheckListener(final ImageView imageView, final String id, final int position){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.ic_check);
                DatabaseHelper dh = new DatabaseHelper(context);
                int result = dh.deleteData(id);
                Log.d(TAG, "Deleted task id : " + id + " result : " +result);
                taskLists.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, taskLists.size());
                setBoolPrefs("isTaskEdited", true);

                if (result > 0){
                    Toast.makeText(imageView.getContext(), "Task done", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setImportantListener(final ImageView imageView, final int currentValue, final String id, final int position){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dh = new DatabaseHelper(context);
                ContentValues contentValues = new ContentValues();
                int result;
                if (currentValue == 0){
                    //value to update database
                    contentValues.put("task_importance", 1);
                    //update database
                    result = dh.updateData(id, contentValues);
                    //update data in taskList
                    taskLists.get(position).setTaskImportance(1);
                    notifyItemChanged(position);
                    Log.d(TAG, "Updated task id : " + id + " result : " +result);
                }else {
                    //value to update database
                    contentValues.put("task_importance", 0);
                    //update database
                    result = dh.updateData(id, contentValues);
                    //update data in taskList
                    taskLists.get(position).setTaskImportance(0);
                    notifyItemChanged(position);
                    Log.d(TAG, "Updated task id : " + id + " result : " +result);
                }
                setBoolPrefs("isTaskEdited", true);
            }
        });

    }

    private void setBoolPrefs(final String key, final boolean value){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "setStringPrefs function");
                SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
                SharedPreferences.Editor prefsEditor = prefs.edit();

                prefsEditor.putBoolean(key, value);
                prefsEditor.apply();
            }
        }).start();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        private TextView cardTaskId;
        private ImageView cardCheckBox;
        private TextView cardTaskName;
        private ImageView cardImportantIcon;
        private ImageView cardDateIcon;
        private TextView cardDate;
        private ImageView cardReminderIcon;
        private TextView cardTime;

        TaskViewHolder(View v) {
            super(v);
            cardTaskId = v.findViewById(R.id.cardTaskId);
            cardCheckBox = v.findViewById(R.id.cardCheckBox);
            cardTaskName = v.findViewById(R.id.cardTaskName);
            cardImportantIcon = v.findViewById(R.id.cardImportantIcon);
            cardDateIcon = v.findViewById(R.id.cardDateIcon);
            cardDate = v.findViewById(R.id.cardDate);
            cardReminderIcon = v.findViewById(R.id.cardReminderIcon);
            cardTime = v.findViewById(R.id.cardTime);
        }
    }
}
