package com.smart_blasting_drilling.android.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.HoleParameteresLayoutBinding;
import com.smart_blasting_drilling.android.databinding.ProjectInfoLayoutBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.utils.StringUtill;

public class HoleDetailDialog extends BaseDialogFragment {

    HoleParameteresLayoutBinding binding;
    public static final String TAG = "HoleDetailDialog";
    Dialog dialog;
    HoleDetailDialog _self;
    private HoleDetailDialog.ProjectInfoDialogListener mListener;
    public ResponseHoleDetailData holeDetailData;
    public ResponseHoleDetailData updateHoleDetailData;

    public static HoleDetailDialog getInstance(ResponseHoleDetailData holeDetailData) {
        HoleDetailDialog frag = new HoleDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("holeDetail", holeDetailData);
        frag.setArguments(bundle);
        return frag;
    }

    public void setUpListener(HoleDetailDialog.ProjectInfoDialogListener listener) {
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
            holeDetailData = (ResponseHoleDetailData) getArguments().getSerializable("holeDetail");
            updateHoleDetailData = holeDetailData;
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
        if (holeDetailData != null) {

        }

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

        binding.saveProceedBtn.setOnClickListener(view -> {
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

    private HoleDetailDialog.ProjectInfoDialogListener getListener() {
        HoleDetailDialog.ProjectInfoDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof HoleDetailDialog.ProjectInfoDialogListener)
            listener = (HoleDetailDialog.ProjectInfoDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof HoleDetailDialog.ProjectInfoDialogListener)
            listener = (HoleDetailDialog.ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(HoleDetailDialog dialogFragment);

        default void onCancel(HoleDetailDialog dialogFragment) {
        }
    }

}
