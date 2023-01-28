package com.smart_blasting_drilling.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.app_utils.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.AdapterDrillingBlastingItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectLIstAdapter extends BaseRecyclerAdapter {

    Context ctx;
    List<String> projectList;

    public ProjectLIstAdapter(Context ctx, List<String> projectList) {
        this.ctx = ctx;
        this.projectList = projectList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        AdapterDrillingBlastingItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_drilling_blasting_item, group, false);
        return new ProjectLIstAdapter.ViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(ctx), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vholder = (ViewHolder) holder;
        vholder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(ctx, HoleDetailActivity.class);
            ctx.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AdapterDrillingBlastingItemBinding binding;

        public ViewHolder(@NonNull AdapterDrillingBlastingItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
