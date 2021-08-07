package com.example.human_bean_routine;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CategoryListAdapter
        extends RecyclerView
        .Adapter<CategoryListAdapter.CategoryViewHolder> {

    // An object of RecyclerView.RecycledViewPool
    // is created to share the Views
    // between the child and
    // the parent RecyclerViews
    private RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView.RecycledViewPool();
    private List<CategoryTaskList> itemList;
    private Context context;
    private static RecyclerViewClickListener recyclerViewClickListener;
    private TaskListAdapter childItemAdapter;
    private RecyclerViewClickListener childRecyclerViewClickListener;

    CategoryListAdapter(List<CategoryTaskList> itemList, Context context, RecyclerViewClickListener recyclerViewClickListener)
    {
        this.context = context;
        this.itemList = itemList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.childItemAdapter = null;
    }

    public void notifyNewData() {
        notifyDataSetChanged();
        if (this.childItemAdapter != null) {
            this.childItemAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup,
            int i)
    {

        // Here we inflate the corresponding
        // layout of the parent item
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

        // Create an instance of the ParentItem
        // class for the given position
        CategoryTaskList parentItem
                = itemList.get(position);

        // For the created instance,
        // get the title and set it
        // as the text for the TextView
        parentViewHolder
                .ParentItemTitle
                .setText(parentItem.getCategoryName());

        // Create a layout manager
        // to assign a layout
        // to the RecyclerView.

        // Here we have assigned the layout
        // as LinearLayout with vertical orientation
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                parentViewHolder
                        .ChildRecyclerView
                        .getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        // Since this is a nested layout, so
        // to define how many child items
        // should be prefetched when the
        // child RecyclerView is nested
        // inside the parent RecyclerView,
        // we use the following method
        layoutManager
                .setInitialPrefetchItemCount(
                        parentItem.getTasks().size());

        // Create an instance of the child
        // item view adapter and set its
        // adapter, layout manager and RecyclerViewPool
        this.childItemAdapter = new TaskListAdapter(this.context,
                this.recyclerViewClickListener, parentItem.getTasks());
        this.childItemAdapter.SetOnItemClickListener(recyclerViewClickListener);
        parentViewHolder
                .ChildRecyclerView
                .setLayoutManager(layoutManager);
        parentViewHolder
                .ChildRecyclerView
                .setAdapter(this.childItemAdapter);
        parentViewHolder
                .ChildRecyclerView
                .setRecycledViewPool(viewPool);
    }

    // This method returns the number
    // of items we have added in the
    // ParentItemList i.e. the number
    // of instances we have created
    // of the ParentItemList
    @Override
    public int getItemCount()
    {
        return itemList.size();
    }

    public void SetOnItemClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
     //   this.childItemAdapter.SetOnItemClickListener(recyclerViewClickListener);
    }

 /*   public String getItem(int position) {
    } */

    // This class is to initialize
    // the Views present in
    // the parent RecyclerView
    class CategoryViewHolder
            extends RecyclerView.ViewHolder {

        private TextView ParentItemTitle;
        private RecyclerView ChildRecyclerView;

        CategoryViewHolder(final View itemView)
        {
            super(itemView);

            ParentItemTitle
                    = itemView
                    .findViewById(
                            R.id.CategoryList);
            ChildRecyclerView
                    = itemView
                    .findViewById(
                            R.id.taskList);
      //      itemView.setOnClickListener(this);
        }

    }
}