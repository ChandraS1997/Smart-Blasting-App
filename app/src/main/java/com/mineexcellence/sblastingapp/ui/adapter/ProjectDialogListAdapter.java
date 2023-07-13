package com.mineexcellence.sblastingapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseBladesRetrieveData;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.ProjectListViewBinding;
import com.mineexcellence.sblastingapp.room_database.AppDatabase;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.BlastCodeDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.BlastPerformanceDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.InitiatingDeviceDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.MediaUploadDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.Project2DBladesDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.Project3DBladesDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.UpdatedProjectDataDao;
import com.mineexcellence.sblastingapp.room_database.entities.AllProjectBladesModelEntity;
import com.mineexcellence.sblastingapp.room_database.entities.Project2DBladesEntity;
import com.mineexcellence.sblastingapp.room_database.entities.Project3DBladesEntity;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
            if (data.isDownloaded()) {
                binding.icnDownloadProject.setBackgroundColor(context.getColor(R.color.transparent));
                binding.icnDownloadProject.setImageResource(R.mipmap.icn_refresh_btn);
            } else {
                binding.icnDownloadProject.setBackgroundColor(context.getColor(R.color.black));
                binding.icnDownloadProject.setImageResource(R.drawable.img_downlode);
            }

            if (getAdapterPosition() == projectList.size() - 1) {
                binding.viewLineBottom.setVisibility(View.GONE);
            }

            binding.icnDownloadProject.setOnClickListener(view -> {
                boolean isAlreadyDownloaded = data.isDownloaded();
                ((BaseActivity) context).showLoader();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((BaseActivity) context).runOnUiThread(new TimerTask() {
                                @Override
                                public void run() {
                                    AppDatabase appDatabase = ((BaseActivity) context).appDatabase;
                                    if (is3DBlades) {
                                        Project3DBladesDao bladesDao = appDatabase.project3DBladesDao();
                                        Project3DBladesEntity entity = new Gson().fromJson(new Gson().toJson(data), Project3DBladesEntity.class);
                                        entity.setIs3dBlade(true);
                                        if (!bladesDao.isExistProject(data.getDesignId())) {
                                            bladesDao.insertProject(entity);
                                        } else {
                                            bladesDao.updateProject(entity);
                                        }
                                    } else {
                                        Project2DBladesDao bladesDao = appDatabase.project2DBladesDao();
                                        Project2DBladesEntity entity = new Gson().fromJson(new Gson().toJson(data), Project2DBladesEntity.class);
                                        entity.setIs3dBlade(false);
                                        if (!bladesDao.isExistProject(data.getDesignId())) {
                                            bladesDao.insertProject(entity);
                                        } else {
                                            bladesDao.updateProject(entity);
                                        }
                                    }
                                    AllProjectBladesModelEntity allProjectBladesModelEntity = new AllProjectBladesModelEntity();
                                    allProjectBladesModelEntity.setData(new Gson().toJson(data));
                                    allProjectBladesModelEntity.setIs2dBlade(is3DBlades);
                                    allProjectBladesModelEntity.setDesignId(data.getDesignId());
                                    if (!appDatabase.allProjectBladesModelDao().isExistItem(data.getDesignId())) {
                                        appDatabase.allProjectBladesModelDao().insertItem(allProjectBladesModelEntity);
                                    } else {
                                        appDatabase.allProjectBladesModelDao().updateItem(data.getDesignId(), "0", new Gson().toJson(data));
                                    }

                                    ((BaseActivity) context).hideLoader();
                                    data.setDownloaded(true);
                                    projectList.set(getBindingAdapterPosition(), data);
                                    notifyDataSetChanged();

                                    if (isAlreadyDownloaded) {
                                        deleteProjectIfAlreadyDownload(data.getDesignId());
                                    }
                                }
                            });
                        }
                    }, 1000);
                /*} else {
                    ((BaseActivity) context).showToast("Project Already Downloaded.");
                }*/
            });

        }

        private void deleteProjectIfAlreadyDownload(String designId) {
            AppDatabase appDatabase = ((BaseActivity) context).appDatabase;
            ProjectHoleDetailRowColDao projectHoleDetailRowColDao = appDatabase.projectHoleDetailRowColDao();
            if (projectHoleDetailRowColDao.isExistProject(designId)) {
                projectHoleDetailRowColDao.deleteProjectById(designId);
            }
            BlastCodeDao blastCodeDao = appDatabase.blastCodeDao();
            if (blastCodeDao.isExistItem(designId)) {
                blastCodeDao.deleteSingleItem(designId);
            }
            InitiatingDeviceDao initiatingDeviceDao = appDatabase.initiatingDeviceDao();
            if (initiatingDeviceDao.isExistItem(designId)) {
                initiatingDeviceDao.deleteItemById(designId);
            }
            BlastPerformanceDao blastPerformanceDao = appDatabase.blastPerformanceDao();
            if (blastPerformanceDao.isExistItem(designId)) {
                blastPerformanceDao.deleteItemById(designId);
            }
            MediaUploadDao mediaUploadDao = appDatabase.mediaUploadDao();
            if (mediaUploadDao.isExistItem(designId)) {
                mediaUploadDao.deleteItemById(designId);
            }
            UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();
            if (updateProjectBladesDao.isExistProject(designId)) {
                updateProjectBladesDao.deleteProjectById(designId);
            }
            UpdatedProjectDataDao updatedProjectDataDao = appDatabase.updatedProjectDataDao();
            if (updatedProjectDataDao.isExistItem(designId)) {
                updatedProjectDataDao.deleteItemById(designId);
            }
        }

    }
}
