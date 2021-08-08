package com.example.human_bean_routine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
// https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android/
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private Context context;
    private static RecyclerViewClickListener recyclerViewClickListener;
    KeepTrack tracker;
    private int categoryPosition;

    TaskListAdapter(Context context, RecyclerViewClickListener recyclerViewClickListener, List<Task> tasks, int categoryPosition) {
        this.tasks = tasks;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.context = context;
        this.categoryPosition = categoryPosition;
    }

 /*   public RecyclerViewClickListener getRecyclerViewClickListener() {
        return this.recyclerViewClickListener;
    }*/

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

        TaskViewHolder viewHolder = new TaskViewHolder(view);
        viewHolder.ellipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickListener.recyclerViewListClicked(viewHolder.ellipses, viewHolder.getAdapterPosition());
            }
        });
        
        return viewHolder;
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

        childViewHolder.checkbox.setChecked(task.getComplete());
        childViewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            DataBaseHelper db = DataBaseHelper.getDbInstance(context);

            @Override
            public void onClick(View v) {
                tracker.currentTaskPosition = position;
                tracker.currentCategoryPosition = categoryPosition;
                task.setComplete(!task.getComplete());
                db.completeTask(task);
            }
        });

        childViewHolder.ellipses.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tracker.currentTaskPosition = position;
                tracker.currentCategoryPosition = categoryPosition;
            }
        });
        childViewHolder.ellipses.setOnClickListener(childViewHolder);

    }

    @Override
    public int getItemCount()
    {
        return tasks.size();
    }

    public void SetOnItemClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView ChildItemTitle;
        CheckBox checkbox;
        ImageButton ellipses;
        KeepTrack tracker;

        TaskViewHolder(View itemView)
        {
            super(itemView);
            ChildItemTitle
                    = itemView.findViewById(
                    R.id.singleCheckList);

            checkbox = itemView.findViewById(R.id.singleCheckList);
        //    ChildItemTitle.setOnClickListener();
            ellipses = itemView.findViewById(R.id.ellipses);

            ellipses.setOnClickListener(this);

        //    ChildItemTitle.setOnClickListener(this);
        //    int x = this.getLayoutPosition();
        //    recyclerViewClickListener.recyclerViewListClicked(itemView, this.getLayoutPosition());

        /*    ellipses.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    tracker.currentTaskPosition = x;
                }
            });*/
        }

        @Override
        public void onClick(View v) {
            recyclerViewClickListener.recyclerViewListClicked(v, this.getLayoutPosition());
            KeepTrack.currentTaskPosition = this.getLayoutPosition();
            KeepTrack.currentCategoryPosition = categoryPosition;
        }
    }
}


