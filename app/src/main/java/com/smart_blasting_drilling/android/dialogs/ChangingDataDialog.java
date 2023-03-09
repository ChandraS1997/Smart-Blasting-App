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

import com.google.gson.Gson;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.DialogChargingDataViewBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class ChangingDataDialog extends BaseDialogFragment {

    DialogChargingDataViewBinding binding;
    public static final String TAG = "ChangingDataDialog";
    Dialog dialog;
    ChangingDataDialog _self;
    public ResponseHoleDetailData holeDetailData;
    public ResponseHoleDetailData updateHoleDetailData;
    ProjectInfoDialogListener mListener;
    ProjectHoleDetailRowColDao entity;
    ResponseBladesRetrieveData bladesRetrieveData;

    public ChangingDataDialog() {
        _self = this;
        entity = appDatabase.projectHoleDetailRowColDao();
    }

    public static ChangingDataDialog getInstance(ResponseHoleDetailData holeDetailData, ResponseBladesRetrieveData data) {
        ChangingDataDialog frag = new ChangingDataDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("holeDetail", holeDetailData);
        bundle.putSerializable("blades_data", data);
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
            holeDetailData = (ResponseHoleDetailData) getArguments().getSerializable("holeDetail");
            bladesRetrieveData = (ResponseBladesRetrieveData) getArguments().getSerializable("blades_data");
            updateHoleDetailData = holeDetailData;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_charging_data_view, container, false);
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
            binding.spinnerColumn.setText(holeDetailData.getColName());
            binding.columnLengthEt.setText(String.valueOf(holeDetailData.getColLength()));
            binding.columnWeightEt.setText(String.valueOf(holeDetailData.getColWt()));

            binding.spinnerbottom.setText(String.valueOf(holeDetailData.getBtmName()));
            binding.bottomLengthEt.setText(String.valueOf(holeDetailData.getBtmLength()));
            binding.bottomWeightEt.setText(String.valueOf(holeDetailData.getBtmWt()));

            binding.spinnerbuster.setText(holeDetailData.getBsterName());
            binding.busterLengthEt.setText(String.valueOf(holeDetailData.getBsterLength()));
            binding.busterWeightEt.setText(String.valueOf(holeDetailData.getBsterWt()));

            binding.spinnerStem.setText(holeDetailData.getStemLngth() != null ? String.valueOf(holeDetailData.getStemLngth()) : "0");
        }

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

        binding.saveProceedBtn.setOnClickListener(view -> {
            UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
            UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();

            updateHoleDetailData.setStemLngth(StringUtill.isEmpty(binding.spinnerStem.getText().toString()) ? 0 : Integer.parseInt(binding.spinnerStem.getText().toString()));
            updateHoleDetailData.setColName(StringUtill.getString(binding.spinnerColumn.getText().toString()));
            updateHoleDetailData.setColLength(StringUtill.getString(binding.columnLengthEt.getText().toString()));
            updateHoleDetailData.setColWt(StringUtill.getString(binding.columnWeightEt.getText().toString()));
            updateHoleDetailData.setBtmName(StringUtill.getString(binding.bottomName.getText().toString()));
            updateHoleDetailData.setBtmLength(Integer.parseInt(StringUtill.getString(binding.bottomLengthEt.getText().toString())));
            updateHoleDetailData.setBtmWt(Integer.parseInt(StringUtill.getString(binding.bottomWeightEt.getText().toString())));
            updateHoleDetailData.setBsterName(StringUtill.getString(binding.busterName.getText().toString()));
            updateHoleDetailData.setBsterLength(Integer.parseInt(StringUtill.getString(binding.busterLengthEt.getText().toString())));
            updateHoleDetailData.setBsterWt(Integer.parseInt(StringUtill.getString(binding.busterWeightEt.getText().toString())));

            bladesEntity.setData(new Gson().toJson(updateHoleDetailData));
            bladesEntity.setDesignId(String.valueOf(updateHoleDetailData.getDesignId()));

            if (!updateProjectBladesDao.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
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
                        entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), false, dataStr));
                    } else {
                        entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), dataStr);
                    }

                }
            }
            ProjectInfoDialogListener listener = getListener();
            if (listener != null) {
                listener.onOk(_self, String.valueOf(updateHoleDetailData.getDesignId()));
            } else {
                dismiss();
            }
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
        void onOk(ChangingDataDialog dialogFragment, String designId);

        default void onCancel(HoleDetailDialog dialogFragment) {
        }
    }

}
