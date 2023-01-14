package com.smart_blasting_drilling.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.FragmentProjectDialogBinding;
import com.smart_blasting_drilling.android.databinding.ProjectListViewBinding;

public class ProjectListAdapter extends BaseRecyclerAdapter {
    ProjectListViewBinding binding;
    Context context;
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
        return 5;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ProjectListViewBinding binding;
        public ViewHolder(@NonNull ProjectListViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
