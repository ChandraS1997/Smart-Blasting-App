package com.smart_blasting_drilling.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.ProjectListViewBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectDialogListAdapter extends BaseRecyclerAdapter
{
    ProjectListViewBinding binding;
    Context context;
    List<String> projectList = new ArrayList<>();

    public ProjectDialogListAdapter(Context context, List<String> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_list_view,group,false);
        return new ViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(context),parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ViewHolder viewHolder = (ViewHolder) holder;
    }

    @Override
    public int getItemCount() {
        return 8;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ProjectListViewBinding binding;
        public ViewHolder(@NonNull ProjectListViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
