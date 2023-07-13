package com.mineexcellence.sblastingapp.dialogs;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseBladesRetrieveData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseHoleDetailData;
import com.mineexcellence.sblastingapp.api.apis.response.hole_tables.AllTablesData;
import com.mineexcellence.sblastingapp.databinding.HoleParameteresLayoutBinding;
import com.mineexcellence.sblastingapp.databinding.ProjectInfoLayoutBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.mineexcellence.sblastingapp.room_database.entities.AllProjectBladesModelEntity;
import com.mineexcellence.sblastingapp.room_database.entities.ProjectHoleDetailRowColEntity;
import com.mineexcellence.sblastingapp.room_database.entities.UpdateProjectBladesEntity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetailActivity;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.List;

public class HoleDetailDialog extends BaseDialogFragment {

    HoleParameteresLayoutBinding binding;
    public static final String TAG = "HoleDetailDialog";
    Dialog dialog;
    HoleDetailDialog _self;
    private HoleDetailDialog.ProjectInfoDialogListener mListener;
    public ResponseHoleDetailData holeDetailData;
    public ResponseHoleDetailData updateHoleDetailData;
    ResponseBladesRetrieveData bladesRetrieveData;

    ProjectHoleDetailRowColDao entity;

    public HoleDetailDialog() {
        _self = this;
    }

    public static HoleDetailDialog getInstance(ResponseHoleDetailData holeDetailData, ResponseBladesRetrieveData data) {
        HoleDetailDialog frag = new HoleDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("holeDetail", holeDetailData);
        bundle.putSerializable("blades_data", data);
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
            bladesRetrieveData = (ResponseBladesRetrieveData) getArguments().getSerializable("blades_data");
            updateHoleDetailData = holeDetailData;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.hole_parameteres_layout, container, false);

        binding.mainContainerView.setBackgroundColor(mContext.getColor(R.color._80000000));

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(20, 20, 20, 20);
        binding.childContainerView.setLayoutParams(layoutParams);

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
        entity = appDatabase.projectHoleDetailRowColDao();
        if (holeDetailData != null) {
            binding.holeDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDepthDouble())));
            String[] status = new String[]{"Pending Hole", "Work in Progress", "Completed", "Deleted/ Blocked holes/ Do not blast"};
            binding.holeStatusSpinner.setAdapter(Constants.getAdapter(mContext, status));

            if (StringUtill.isEmpty(holeDetailData.getHoleStatus())) {
                binding.holeStatusSpinner.setText("Pending Hole");
            } else {
                binding.holeStatusSpinner.setText(StringUtill.getString(holeDetailData.getHoleStatus()));
            }

            binding.holeAngleEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleAngle())));
            binding.diameterEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDiameter())));
            binding.burdenEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBurdenDouble())));
            binding.spacingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSpacingDouble())));
            binding.xEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getX())));
            binding.yTxtEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getY())));
            binding.zEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getZ())));
        }

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

        binding.saveProceedBtn.setOnClickListener(view -> {
            UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
            UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();

            updateHoleDetailData.setHoleDepth(StringUtill.isEmpty(binding.holeDepthEt.getText().toString()) ? 0 : Integer.parseInt(binding.holeDepthEt.getText().toString()));
            updateHoleDetailData.setHoleAngle(StringUtill.isEmpty(binding.holeAngleEt.getText().toString()) ? 0 : Integer.parseInt(binding.holeAngleEt.getText().toString()));
            updateHoleDetailData.setHoleDiameter(StringUtill.isEmpty(binding.diameterEt.getText().toString()) ? 0 : Integer.parseInt(binding.diameterEt.getText().toString()));
            updateHoleDetailData.setBurden(StringUtill.isEmpty(binding.burdenEt.getText().toString()) ? 0 : binding.burdenEt.getText().toString());
            updateHoleDetailData.setSpacing(StringUtill.isEmpty(binding.spacingEt.getText().toString()) ? 0 : binding.spacingEt.getText().toString());
            updateHoleDetailData.setX(StringUtill.isEmpty(binding.xEt.getText().toString()) ? 0 : binding.xEt.getText().toString());
            updateHoleDetailData.setY(StringUtill.isEmpty(binding.yTxtEt.getText().toString()) ? 0 : binding.yTxtEt.getText().toString());
            updateHoleDetailData.setZ(StringUtill.isEmpty(binding.zEt.getText().toString()) ? 0 : binding.zEt.getText().toString());
            updateHoleDetailData.setHoleStatus(StringUtill.getString(binding.holeStatusSpinner.getText().toString()));

            bladesEntity.setData(new Gson().toJson(updateHoleDetailData));
            bladesEntity.setHoleId(holeDetailData.getHoleNo());
            bladesEntity.setRowId((int) holeDetailData.getRowNo());
            bladesEntity.setDesignId(String.valueOf(updateHoleDetailData.getDesignId()));

            if (!updateProjectBladesDao.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()), (int) holeDetailData.getRowNo(), holeDetailData.getHoleNo())) {
                updateProjectBladesDao.insertProject(bladesEntity);
            } else {
                updateProjectBladesDao.updateProject(bladesEntity);
            }

            String dataStr = "";

            AllTablesData allTablesData = ((HoleDetailActivity) mContext).allTablesData;
            if (allTablesData != null) {
                List<ResponseHoleDetailData> holeDetailDataList = allTablesData.getTable2();
                if (!Constants.isListEmpty(holeDetailDataList)) {
                    for (int i = 0; i < holeDetailDataList.size(); i++) {
                        if (holeDetailDataList.get(i).getRowNo() == updateHoleDetailData.getRowNo() && holeDetailDataList.get(i).getHoleID().equals(updateHoleDetailData.getHoleID())) {
                            holeDetailDataList.set(i, updateHoleDetailData);
                        } else {
                            holeDetailDataList.set(i, holeDetailDataList.get(i));
                        }
                    }

                    allTablesData.setTable2(holeDetailDataList);
                    dataStr = new Gson().toJson(allTablesData);

                    if (!entity.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
                        entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), bladesRetrieveData.isIs3dBlade(), dataStr));
                    } else {
                        entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), dataStr);
                    }

                }
            }

            AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(updateHoleDetailData.getDesignId()));

//            ((BaseActivity) mContext).setInsertUpdateHoleDetailSync(((HoleDetailActivity) mContext).bladesRetrieveData, updateHoleDetailData, modelEntity != null ? modelEntity.getProjectCode() : "0");

            ProjectInfoDialogListener listener = getListener();
            if (listener != null) {
                listener.onOk(_self, String.valueOf(updateHoleDetailData.getDesignId()));
            } else {
                dismiss();
            }
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
        void onOk(HoleDetailDialog dialogFragment, String designId);

        default void onCancel(HoleDetailDialog dialogFragment) {
        }
    }

}
