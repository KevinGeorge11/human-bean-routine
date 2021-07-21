package com.example.human_bean_routine;

import android.content.Context;
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
    private Context context;
    private static RecyclerViewClickListener recyclerViewClickListener;

    TaskListAdapter(Context context, RecyclerViewClickListener recyclerViewClickListener, List<Task> tasks) {
        this.tasks = tasks;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.context = context;
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
    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView ChildItemTitle;

        TaskViewHolder(View itemView)
        {
            super(itemView);
            ChildItemTitle
                    = itemView.findViewById(
                    R.id.singleCheckList);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewClickListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }
}


