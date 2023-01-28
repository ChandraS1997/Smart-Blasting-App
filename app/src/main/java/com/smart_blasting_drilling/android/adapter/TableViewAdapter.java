package com.smart_blasting_drilling.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.app_utils.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.TableViewBinding;

import java.util.List;

public class TableViewAdapter extends BaseRecyclerAdapter {

    Context ctx;
    List<String> tableList;

    public TableViewAdapter(Context ctx, List<String> tableList) {
        this.ctx = ctx;
        this.tableList = tableList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        TableViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.table_view, group, false);
        return new TableViewAdapter.ViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(ctx), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setDataBind();
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TableViewBinding binding;

        public ViewHolder(@NonNull TableViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind() {
            if (getAdapterPosition() == 0) {
                binding.tableHeadingRow.setVisibility(View.VISIBLE);
            } else {
                binding.tableHeadingRow.setVisibility(View.GONE);
            }
        }

    }
}
