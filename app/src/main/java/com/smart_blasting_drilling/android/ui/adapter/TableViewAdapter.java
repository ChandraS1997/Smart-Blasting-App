package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app_utils.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.TableViewBinding;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;

import java.util.ArrayList;
import java.util.List;

public class TableViewAdapter extends BaseRecyclerAdapter {

    Context ctx;
    List<String> tableList;
    ArrayList<TableEditModel> editModelArrayList ;

    public TableViewAdapter(Context ctx, List<String> tableList, ArrayList<TableEditModel> arrayList) {
        this.ctx = ctx;
        this.tableList = tableList;
        this.editModelArrayList = arrayList;
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
        viewHolder.setDataBind(tableList.get(position));
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TableViewBinding binding;

        public ViewHolder(@NonNull TableViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(String fieldName) {
            if (getAdapterPosition() == 0) {
                binding.tableHeadingRow.setVisibility(View.VISIBLE);
            } else {
                binding.tableHeadingRow.setVisibility(View.GONE);
            }
        }

    }
}
