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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.mineexcellence.sblastingapp.app.AppDelegate;
import com.mineexcellence.sblastingapp.databinding.HoleParameteres3dLayoutBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.mineexcellence.sblastingapp.room_database.entities.AllProjectBladesModelEntity;
import com.mineexcellence.sblastingapp.room_database.entities.ProjectHoleDetailRowColEntity;
import com.mineexcellence.sblastingapp.room_database.entities.UpdateProjectBladesEntity;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleDetail3dDialog extends BaseDialogFragment {

    HoleParameteres3dLayoutBinding binding;
    public static final String TAG = "HoleDetail3dDialog";
    Dialog dialog;
    HoleDetail3dDialog _self;
    private HoleDetail3dDialog.ProjectInfoDialogListener mListener;
    public Response3DTable4HoleChargingDataModel holeDetailData;
    public Response3DTable4HoleChargingDataModel updateHoleDetailData;
    Response3DTable1DataModel bladesRetrieveData;

    ProjectHoleDetailRowColDao entity;

    public HoleDetail3dDialog() {
        _self = this;
    }

    public static HoleDetail3dDialog getInstance(Response3DTable4HoleChargingDataModel holeDetailData, Response3DTable1DataModel data) {
        HoleDetail3dDialog frag = new HoleDetail3dDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("holeDetail", holeDetailData);
        bundle.putSerializable("blades_data", data);
        frag.setArguments(bundle);
        return frag;
    }

    public void setUpListener(HoleDetail3dDialog.ProjectInfoDialogListener listener) {
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
            holeDetailData = (Response3DTable4HoleChargingDataModel) getArguments().getSerializable("holeDetail");
            bladesRetrieveData = (Response3DTable1DataModel) getArguments().getSerializable("blades_data");
            updateHoleDetailData = holeDetailData;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.hole_parameteres_3d_layout, container, false);

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
            binding.holeDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDepth())));
            String[] status = new String[]{"Pending Hole", "Work in Progress", "Completed", "Deleted/ Blocked holes/ Do not blast"};
            binding.holeStatusSpinner.setAdapter(Constants.getAdapter(mContext, status));

            if (StringUtill.isEmpty(holeDetailData.getHoleStatus())) {
                binding.holeStatusSpinner.setText("Pending Hole");
            } else {
                binding.holeStatusSpinner.setText(StringUtill.getString(holeDetailData.getHoleStatus()));
            }

            binding.holeAngleEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getVerticalDip())));
            binding.diameterEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDiameter())));
            binding.burdenEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBurden())));
            binding.spacingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSpacing())));
            binding.xEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopX())));
            binding.yTxtEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopY())));
            binding.zEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopZ())));
            binding.bottomXEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomX())));
            binding.bottomYEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomY())));
            binding.bottomZEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomZ())));

            binding.totalChargeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTotalCharge())));
            binding.chargeLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getChargeLength())));
            binding.stemmingLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getStemmingLength())));
            binding.deckLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getDeckLength())));
            binding.blockEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBlock())));
            binding.blockLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBlockLength())));
            binding.holeDelayEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDelay())));
            binding.inHoleDelayEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getInHoleDelay())));
            binding.waterDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getWaterDepth())));
            binding.subgradeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSubgrade())));
        }

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

        binding.saveProceedBtn.setOnClickListener(view -> {
            UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
            UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();

            updateHoleDetailData.setHoleDepth(StringUtill.isEmpty(binding.holeDepthEt.getText().toString()) ? "0" : binding.holeDepthEt.getText().toString());
            updateHoleDetailData.setVerticalDip(StringUtill.isEmpty(binding.holeAngleEt.getText().toString()) ? "0" : binding.holeAngleEt.getText().toString());
            updateHoleDetailData.setHoleDiameter(StringUtill.isEmpty(binding.diameterEt.getText().toString()) ? "0" : binding.diameterEt.getText().toString());
            updateHoleDetailData.setBurden(StringUtill.isEmpty(binding.burdenEt.getText().toString()) ? "0" : binding.burdenEt.getText().toString());
            updateHoleDetailData.setSpacing(StringUtill.isEmpty(binding.spacingEt.getText().toString()) ? "0" : binding.spacingEt.getText().toString());
            updateHoleDetailData.setTopX(StringUtill.isEmpty(binding.xEt.getText().toString()) ? "0" : binding.xEt.getText().toString());
            updateHoleDetailData.setTopY(StringUtill.isEmpty(binding.yTxtEt.getText().toString()) ? "0" : binding.yTxtEt.getText().toString());
            updateHoleDetailData.setTopZ(StringUtill.isEmpty(binding.zEt.getText().toString()) ? "0" : binding.zEt.getText().toString());
            updateHoleDetailData.setBottomX(StringUtill.isEmpty(binding.bottomXEt.getText().toString()) ? "0" : binding.bottomXEt.getText().toString());
            updateHoleDetailData.setBottomY(StringUtill.isEmpty(binding.bottomYEt.getText().toString()) ? "0" : binding.bottomYEt.getText().toString());
            updateHoleDetailData.setBottomZ(StringUtill.isEmpty(binding.bottomZEt.getText().toString()) ? "0" : binding.bottomZEt.getText().toString());

            updateHoleDetailData.setSubgrade(StringUtill.isEmpty(binding.subgradeEt.getText().toString()) ? "0" : binding.bottomYEt.getText().toString());
            updateHoleDetailData.setWaterDepth(StringUtill.isEmpty(binding.waterDepthEt.getText().toString()) ? "0" : binding.waterDepthEt.getText().toString());
            updateHoleDetailData.setTotalCharge(StringUtill.isEmpty(binding.totalChargeEt.getText().toString()) ? "0" : binding.totalChargeEt.getText().toString());
            updateHoleDetailData.setChargeLength(StringUtill.isEmpty(binding.chargeLengthEt.getText().toString()) ? "0" : binding.chargeLengthEt.getText().toString());
            updateHoleDetailData.setStemmingLength(StringUtill.isEmpty(binding.stemmingLengthEt.getText().toString()) ? "0" : binding.stemmingLengthEt.getText().toString());
            updateHoleDetailData.setDeckLength(StringUtill.isEmpty(binding.deckLengthEt.getText().toString()) ? "0" : binding.deckLengthEt.getText().toString());
            updateHoleDetailData.setBlock(StringUtill.isEmpty(binding.blockEt.getText().toString()) ? "0" : binding.blockEt.getText().toString());
            updateHoleDetailData.setBlockLength(StringUtill.isEmpty(binding.blockLengthEt.getText().toString()) ? "0" : binding.blockLengthEt.getText().toString());
            updateHoleDetailData.setHoleDelay(StringUtill.isEmpty(binding.holeDelayEt.getText().toString()) ? "0" : binding.holeDelayEt.getText().toString());
            updateHoleDetailData.setInHoleDelay(StringUtill.isEmpty(binding.inHoleDelayEt.getText().toString()) ? "0" : binding.inHoleDelayEt.getText().toString());

            updateHoleDetailData.setHoleStatus(StringUtill.getString(binding.holeStatusSpinner.getText().toString()));

            bladesEntity.setData(new Gson().toJson(updateHoleDetailData));
            bladesEntity.setHoleId(Integer.parseInt(String.valueOf(holeDetailData.getHoleNo())));
            bladesEntity.setRowId(Integer.parseInt(String.valueOf(holeDetailData.getRowNo())));
            bladesEntity.setDesignId(String.valueOf(updateHoleDetailData.getDesignId()));

            if (!updateProjectBladesDao.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()), Integer.parseInt(String.valueOf(holeDetailData.getRowNo())), Integer.parseInt(String.valueOf(holeDetailData.getHoleNo())))) {
                updateProjectBladesDao.insertProject(bladesEntity);
            } else {
                updateProjectBladesDao.updateProject(bladesEntity);
            }

            String dataStr = "";

            JsonElement element = AppDelegate.getInstance().getHole3DDataElement();
            List<Response3DTable4HoleChargingDataModel> allTablesData = new ArrayList<>();
            if (element != null) {
                JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) element).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
                JsonArray jsonArray = new JsonArray();
                if (array.get(15) instanceof JsonArray) {
                    jsonArray = new Gson().fromJson(array.get(15), JsonArray.class);
                } else {
                    jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(15), String.class), JsonArray.class);
                }
                for (JsonElement e : jsonArray) {
                    holeDetailDataList.add(new Gson().fromJson(e, Response3DTable4HoleChargingDataModel.class));
                }
                if (!Constants.isListEmpty(holeDetailDataList)) {
                    for (int i = 0; i < holeDetailDataList.size(); i++) {
                        if (String.valueOf(updateHoleDetailData.getRowNo()).equals(String.valueOf(holeDetailDataList.get(i).getRowNo())) && holeDetailDataList.get(i).getHoleID().equals(updateHoleDetailData.getHoleID())) {
                            holeDetailDataList.set(i, updateHoleDetailData);
                        } else {
                            holeDetailDataList.set(i, holeDetailDataList.get(i));
                        }
                    }

                    array.set(15, new Gson().fromJson(new Gson().toJson(holeDetailDataList), JsonElement.class));
                    JsonObject jsonObject = new JsonObject();
                    JsonPrimitive primitive = new JsonPrimitive(new Gson().toJson(array));
                    jsonObject.add(Constants._3D_TBALE_NAME, primitive);
                    allTablesData = holeDetailDataList;
                    dataStr = new Gson().toJson(allTablesData);

                    if (!entity.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
                        entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), bladesRetrieveData.isIs3dBlade(), new Gson().toJson(jsonObject)));
                    } else {
                        entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), new Gson().toJson(jsonObject));
                    }

                }
            }

            AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(updateHoleDetailData.getDesignId()));

//            ((BaseActivity) mContext).setInsertUpdateHoleDetailSync(((HoleDetailActivity) mContext).bladesRetrieveData, updateHoleDetailData, modelEntity != null ? modelEntity.getProjectCode() : "0");

            HoleDetail3dDialog.ProjectInfoDialogListener listener = getListener();
            if (listener != null) {
                listener.onOk(_self, String.valueOf(updateHoleDetailData.getDesignId()));
            } else {
                dismiss();
            }
        });

    }

    private HoleDetail3dDialog.ProjectInfoDialogListener getListener() {
        HoleDetail3dDialog.ProjectInfoDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof HoleDetail3dDialog.ProjectInfoDialogListener)
            listener = (HoleDetail3dDialog.ProjectInfoDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof HoleDetail3dDialog.ProjectInfoDialogListener)
            listener = (HoleDetail3dDialog.ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(HoleDetail3dDialog dialogFragment, String designId);

        default void onCancel(HoleDetail3dDialog dialogFragment) {
        }
    }

}