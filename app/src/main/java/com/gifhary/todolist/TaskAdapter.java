package com.gifhary.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private int resource;
    private ArrayList<TaskConstructor> taskLists;

    public TaskAdapter(Context context, int resource, ArrayList<TaskConstructor> taskLists) {
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
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskConstructor taskConstructor = taskLists.get(position);

        holder.cardCheckBox.setImageResource(R.drawable.ic_box);
        holder.cardTaskName.setText(taskConstructor.getTaskName());

        if (taskConstructor.getTaskImportance() == 1){ holder.cardImportantIcon.setImageResource(R.drawable.ic_star_on); }
        else { holder.cardImportantIcon.setImageResource(R.drawable.important); }

        if (!taskConstructor.getTaskDate().equals("")){
            holder.cardDateIcon.setImageResource(R.drawable.ic_date);
            holder.cardDate.setText(taskConstructor.getTaskDate());
        }else {
            holder.cardDateIcon.setVisibility(View.INVISIBLE);
            holder.cardDate.setVisibility(View.INVISIBLE);
        }

        if (taskConstructor.getTaskReminder() == 1){
            holder.cardReminderIcon.setImageResource(R.drawable.ic_reminder);
        }else {
            holder.cardReminderIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        protected ImageView cardCheckBox;
        protected TextView cardTaskName;
        protected ImageView cardImportantIcon;
        protected ImageView cardDateIcon;
        protected TextView cardDate;
        protected ImageView cardReminderIcon;

        public TaskViewHolder(View v) {
            super(v);
            cardCheckBox = v.findViewById(R.id.cardCheckBox);
            cardTaskName = v.findViewById(R.id.cardTaskName);
            cardImportantIcon = v.findViewById(R.id.cardImportantIcon);
            cardDateIcon = v.findViewById(R.id.cardDateIcon);
            cardDate = v.findViewById(R.id.cardDate);
            cardReminderIcon = v.findViewById(R.id.cardReminderIcon);
        }
    }
}
