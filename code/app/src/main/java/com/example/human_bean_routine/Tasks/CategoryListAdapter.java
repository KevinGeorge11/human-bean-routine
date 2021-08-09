package com.example.human_bean_routine.Tasks;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.human_bean_routine.R;

import java.util.List;

// To implement the Nested Adapters, a tutorial was viewed from this site, to learn how multiple adapters can be linked together:
// https://www.geeksforgeeks.org/how-to-create-a-nested-recyclerview-in-android/
// However, many additional fields and some additional methods were added.
public class CategoryListAdapter
        extends RecyclerView
        .Adapter<CategoryListAdapter.CategoryViewHolder> {

    private RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView.RecycledViewPool();
    private List<CategoryTaskList> itemList;
    private Context context;
    private static RecyclerViewClickListener recyclerViewClickListener;
    private TaskListAdapter taskItemAdapter;

    CategoryListAdapter(List<CategoryTaskList> itemList, Context context, RecyclerViewClickListener recyclerViewClickListener)
    {
        this.context = context;
        this.itemList = itemList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.taskItemAdapter = null;
    }

    public void setItemList(List<CategoryTaskList> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int i)
    {

        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(
                        R.layout.parent_item,
                        viewGroup, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CategoryViewHolder parentViewHolder,
            int position)
    {

        CategoryTaskList parentItem
                = itemList.get(position);

        parentViewHolder
                .categoryItemTitle
                .setText(parentItem.getCategoryName());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                parentViewHolder
                        .taskRecyclerView
                        .getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        layoutManager
                .setInitialPrefetchItemCount(
                        parentItem.getTasks().size());

        this.taskItemAdapter = new TaskListAdapter(this.context,
                this.recyclerViewClickListener, parentItem.getTasks(), position);
        this.taskItemAdapter.SetOnItemClickListener(recyclerViewClickListener);
        parentViewHolder
                .taskRecyclerView
                .setLayoutManager(layoutManager);
        parentViewHolder
                .taskRecyclerView
                .setAdapter(this.taskItemAdapter);
        parentViewHolder
                .taskRecyclerView
                .setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    public void SetOnItemClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    class CategoryViewHolder
            extends RecyclerView.ViewHolder {

        private TextView categoryItemTitle;
        private RecyclerView taskRecyclerView;

        CategoryViewHolder(final View itemView)
        {
            super(itemView);

            categoryItemTitle = itemView.findViewById(R.id.CategoryList);
            taskRecyclerView = itemView.findViewById(R.id.taskList);
        }

    }
}