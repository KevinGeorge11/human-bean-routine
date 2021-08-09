package com.example.human_bean_routine.Tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.human_bean_routine.Database.DataBaseHelper;
import com.example.human_bean_routine.R;

import java.util.List;
// To implement the Nested Adapters, a tutorial was viewed from this site, to learn how multiple adapters can be linked together:
// https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android/
// However, many additional fields and some additional methods were added in respect to our project.
public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private Context context;
    private static RecyclerViewClickListener recyclerViewClickListener;
    AdapterHelper tracker;
    private int categoryPosition;

    TaskListAdapter(Context context, RecyclerViewClickListener recyclerViewClickListener, List<Task> tasks, int categoryPosition) {
        this.tasks = tasks;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.context = context;
        this.categoryPosition = categoryPosition;
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

        TaskViewHolder viewHolder = new TaskViewHolder(view);
        
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(
            @NonNull TaskViewHolder taskViewHolder,
            int position)
    {
        Task task
                = tasks.get(position);

        taskViewHolder
                .taskItemTitle
                .setText(task.getTaskName());

        taskViewHolder.checkbox.setChecked(task.getComplete());
        taskViewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            DataBaseHelper db = DataBaseHelper.getDbInstance(context);

            @Override
            public void onClick(View v) {
                tracker.currentTaskPosition = position;
                tracker.currentCategoryPosition = categoryPosition;
                task.setComplete(!task.getComplete());
                db.completeTask(task);
            }
        });

        taskViewHolder.ellipses.setOnClickListener(taskViewHolder);

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

        TextView taskItemTitle;
        CheckBox checkbox;
        ImageButton ellipses;
        AdapterHelper tracker;

        TaskViewHolder(View itemView)
        {
            super(itemView);
            taskItemTitle
                    = itemView.findViewById(
                    R.id.singleCheckList);

            checkbox = itemView.findViewById(R.id.singleCheckList);
            ellipses = itemView.findViewById(R.id.ellipses);

            ellipses.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            recyclerViewClickListener.recyclerViewListClicked(v, this.getLayoutPosition());
            AdapterHelper.currentTaskPosition = this.getLayoutPosition();
            AdapterHelper.currentCategoryPosition = categoryPosition;
        }
    }
}


