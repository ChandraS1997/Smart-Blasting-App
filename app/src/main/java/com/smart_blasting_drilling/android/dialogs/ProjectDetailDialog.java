package com.smart_blasting_drilling.android.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseSiteDetail;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.ProjectInfoDialogBinding;
import com.smart_blasting_drilling.android.databinding.ProjectInfoLayoutBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProjectDetailDialog extends BaseDialogFragment {

    ProjectInfoLayoutBinding binding;
    public static final String TAG = "ProjectDetailDialog";
    Dialog dialog;
    ProjectDetailDialog _self;
    private ProjectInfoDialogListener mListener;
    public ResponseBladesRetrieveData bladesRetrieveData;
    public ResponseBladesRetrieveData updateBladesData;
    AppDatabase appDatabase;
    List<String> siteName = new ArrayList<>();

    public ProjectDetailDialog() {
        _self = this;
        appDatabase = BaseApplication.getAppDatabase(mContext, Constants.DATABASE_NAME);
    }

    public static ProjectDetailDialog getInstance(ResponseBladesRetrieveData bladesRetrieveData) {
        ProjectDetailDialog frag = new ProjectDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bladesRetrieveData", bladesRetrieveData);
        frag.setArguments(bundle);
        return frag;
    }

    public void setUpListener(ProjectInfoDialogListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bladesRetrieveData = (ResponseBladesRetrieveData) getArguments().getSerializable("bladesRetrieveData");
            updateBladesData = bladesRetrieveData;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_info_layout, container, false);
        return binding.getRoot();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

    }

    @Override
    public void setupClickListener() {

    }

    @Override
    public void loadData() {
        if (!Constants.isListEmpty(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject())) {
            if (appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0) != null) {
                Type siteList = new TypeToken<List<ResponseSiteDetail>>(){}.getType();
                List<ResponseSiteDetail> siteDetailList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonArray.class).get(0), siteList);
                Log.e("Data : ", new Gson().toJson(siteDetailList));
            }
        }


        if (bladesRetrieveData != null) {
            binding.projectName.setText(StringUtill.getString(bladesRetrieveData.getDesignName()));
            binding.spinnerSiteName.setText(StringUtill.getString(bladesRetrieveData.getMineName()));
            binding.spinnerSiteName.setText(StringUtill.getString(bladesRetrieveData.getMineName()));
        }

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

        binding.saveAndProceedBtn.setOnClickListener(view -> {
            UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
            AppDatabase appDatabase = BaseApplication.getAppDatabase(mContext, Constants.DATABASE_NAME);
            UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();

            if (!updateProjectBladesDao.isExistProject(bladesEntity.getDesignId())) {
                updateProjectBladesDao.insertProject(bladesEntity);
            } else {
                updateProjectBladesDao.updateProject(bladesEntity);
            }
            dismiss();
        });

    }

    private ProjectInfoDialogListener getListener() {
        ProjectInfoDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof ProjectInfoDialogListener)
            listener = (ProjectInfoDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof ProjectInfoDialogListener)
            listener = (ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(ProjectDetailDialog dialogFragment);

        default void onCancel(ProjectDetailDialog dialogFragment) {
        }
    }

}
