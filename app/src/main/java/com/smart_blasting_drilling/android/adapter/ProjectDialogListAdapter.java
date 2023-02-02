package com.smart_blasting_drilling.android.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.ProjectListViewBinding;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class ProjectDialogListAdapter extends BaseRecyclerAdapter {
    ProjectListViewBinding binding;
    Context context;
    List<ResponseBladesRetrieveData> projectList;

    public ProjectDialogListAdapter(Context context, List<ResponseBladesRetrieveData> projectList) {
        this.context = context;
        this.projectList = projectList;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_list_view, group, false);
        return new ViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(context), parent);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProjectListViewBinding binding;

        public ViewHolder(@NonNull ProjectListViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBind(ResponseBladesRetrieveData data) {
            binding.projectListTitle.setText(StringUtill.getString(data.getDesignName()));
            Log.e("51456454564", StringUtill.getString(data.getDesignName()));

            if (getAdapterPosition() == projectList.size() - 1) {
                binding.viewLineBottom.setVisibility(View.GONE);
            }
        }

    }
}
