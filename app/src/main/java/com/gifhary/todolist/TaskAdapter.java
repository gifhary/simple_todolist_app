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
            holder.cardImportantIcon.setImageResource(R.drawable.ic_star_on); }

        if (!taskConstructor.getTaskDate().equals("")){
            holder.cardDate.setText(taskConstructor.getTaskDate());
        }else {
            holder.cardDateIcon.setVisibility(View.INVISIBLE);
            holder.cardDate.setVisibility(View.INVISIBLE);
        }

        if (taskConstructor.getTaskReminder() != 1){
            holder.cardReminderIcon.setVisibility(View.INVISIBLE);
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
                    imageView.setImageResource(R.drawable.ic_star_on);
                    //value to update database
                    contentValues.put("task_importance", 1);
                    //update database
                    result = dh.updateData(id, contentValues);
                    //update data in taskList
                    taskLists.get(position).setTaskImportance(1);
                    Log.d(TAG, "Updated task id : " + id + " result : " +result);
                }else {
                    imageView.setImageResource(R.drawable.important);
                    //value to update database
                    contentValues.put("task_importance", 0);
                    //update database
                    result = dh.updateData(id, contentValues);
                    //update data in taskList
                    taskLists.get(position).setTaskImportance(0);
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

                prefsEditor.putBoolean("isTaskEdited", value);
                prefsEditor.apply();
            }
        }).start();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView cardTaskId;
        ImageView cardCheckBox;
        TextView cardTaskName;
        ImageView cardImportantIcon;
        ImageView cardDateIcon;
        TextView cardDate;
        ImageView cardReminderIcon;

        TaskViewHolder(View v) {
            super(v);
            cardTaskId = v.findViewById(R.id.cardTaskId);
            cardCheckBox = v.findViewById(R.id.cardCheckBox);
            cardTaskName = v.findViewById(R.id.cardTaskName);
            cardImportantIcon = v.findViewById(R.id.cardImportantIcon);
            cardDateIcon = v.findViewById(R.id.cardDateIcon);
            cardDate = v.findViewById(R.id.cardDate);
            cardReminderIcon = v.findViewById(R.id.cardReminderIcon);
        }
    }
}
