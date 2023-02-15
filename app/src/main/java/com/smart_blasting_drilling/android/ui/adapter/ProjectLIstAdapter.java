package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.app_utils.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.AdapterDrillingBlastingItemBinding;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class ProjectLIstAdapter extends BaseRecyclerAdapter {

    Context ctx;
    List<ResponseBladesRetrieveData> projectList;
    OnItemClickCallBack itemClickCallBack;

    public ProjectLIstAdapter(Context ctx, List<ResponseBladesRetrieveData> projectList) {
        this.ctx = ctx;
        this.projectList = projectList;
    }

    public void setItemClickCallBack(OnItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
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
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setDataBind(projectList.get(position));
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public interface OnItemClickCallBack {
        void onClick(ResponseBladesRetrieveData data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterDrillingBlastingItemBinding binding;

        public ViewHolder(@NonNull AdapterDrillingBlastingItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setDataBind(ResponseBladesRetrieveData data) {
            binding.titleNameTv.setText(StringUtill.getString(data.getDesignName()));
            binding.MinevalTv.setText(StringUtill.getString(data.getMineName()));
            binding.pit.setText(StringUtill.getString(data.getPitName()));
            binding.zone.setText(StringUtill.getString(data.getZoneName()));
//            binding.StatusTv.setText(StringUtill.getString(data.get()));
            binding.Bench.setText(StringUtill.getString(data.getBenchName()));
            binding.CreatedDateval.setText(StringUtill.getString(DateUtils.getFormattedTime(data.getDesignDateTime(), "dd/MM/yyyy HH:mm:ss a", "dd/MM/yyyy")));

            itemView.setOnClickListener(view -> {
                if (itemClickCallBack != null)
                    itemClickCallBack.onClick(data);
            });

        }
    }
}
