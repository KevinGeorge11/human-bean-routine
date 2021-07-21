package com.example.human_bean_routine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
// https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android/
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private List<Task> tasks;

    TaskListAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,
                                                     int i)
    {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(
                        R.layout.task_layout,
                        viewGroup, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TaskViewHolder childViewHolder,
            int position)
    {
        Task task
                = tasks.get(position);

        childViewHolder
                .ChildItemTitle
                .setText(task.getTaskName());
    }

    @Override
    public int getItemCount()
    {
        return tasks.size();
    }
    class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView ChildItemTitle;

        TaskViewHolder(View itemView)
        {
            super(itemView);
            ChildItemTitle
                    = itemView.findViewById(
                    R.id.singleCheckList);
        }
    }
}


