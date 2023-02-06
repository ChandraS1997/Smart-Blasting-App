package com.smart_blasting_drilling.android.ui.adapter;

import static com.smart_blasting_drilling.android.helper.Constants.DATABASE_NAME;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.ProjectListViewBinding;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.Project2DBladesDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.Project3DBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.Project2DBladesEntity;
import com.smart_blasting_drilling.android.room_database.entities.Project3DBladesEntity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class ProjectDialogListAdapter extends BaseRecyclerAdapter {
    Context context;
    List<ResponseBladesRetrieveData> projectList;
    boolean is3DBlades;

    public ProjectDialogListAdapter(Context context, List<ResponseBladesRetrieveData> projectList, boolean is3DBlades) {
        this.context = context;
        this.projectList = projectList;
        this.is3DBlades = is3DBlades;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        ProjectListViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.project_list_view, group, false);
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

            if (getAdapterPosition() == projectList.size() - 1) {
                binding.viewLineBottom.setVisibility(View.GONE);
            }

            binding.icnDownloadProject.setOnClickListener(view -> {
                AppDatabase appDatabase = BaseApplication.getAppDatabase(context, DATABASE_NAME);
                if (is3DBlades) {
                    Project3DBladesDao bladesDao = appDatabase.project3DBladesDao();
                    Project3DBladesEntity entity = new Gson().fromJson(new Gson().toJson(data), Project3DBladesEntity.class);
                    if (!bladesDao.isExistProject(data.getDesignId())) {
                        bladesDao.insertProject(entity);
                    } else {
                        bladesDao.updateProject(entity);
                    }
                } else {
                    Project2DBladesDao bladesDao = appDatabase.project2DBladesDao();
                    Project2DBladesEntity entity = new Gson().fromJson(new Gson().toJson(data), Project2DBladesEntity.class);
                    if (!bladesDao.isExistProject(data.getDesignId())) {
                        bladesDao.insertProject(entity);
                    } else {
                        bladesDao.updateProject(entity);
                    }
                }
            });

        }

    }
}