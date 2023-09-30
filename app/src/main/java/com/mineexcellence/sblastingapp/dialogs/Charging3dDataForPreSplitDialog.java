package com.mineexcellence.sblastingapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.HoleDetailItem;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.Response3DTable18PreSpilitDataModel;
import com.mineexcellence.sblastingapp.app.AppDelegate;
import com.mineexcellence.sblastingapp.databinding.DialogChargingDataViewBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.mineexcellence.sblastingapp.room_database.entities.ProjectHoleDetailRowColEntity;
import com.mineexcellence.sblastingapp.room_database.entities.UpdateProjectBladesEntity;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.ui.adapter.Charging3dDataListAdapter;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class Charging3dDataForPreSplitDialog extends BaseDialogFragment {

    DialogChargingDataViewBinding binding;
    public static final String TAG = "Charging3dDataForPreSplitDialog";
    Dialog dialog;
    Charging3dDataForPreSplitDialog _self;
    Charging3dDataForPreSplitDialog.ProjectInfoDialogListener mListener;
    ProjectHoleDetailRowColDao entity;

    List<ChargeTypeArrayItem> chargingDataModelList = new ArrayList<>();
    Charging3dDataListAdapter adapter;

    HoleDetailItem allTablesData;
    List<Response3DTable18PreSpilitDataModel> preSpilitDataModelList;

    public Charging3dDataForPreSplitDialog() {
        _self = this;
    }

    public static Charging3dDataForPreSplitDialog getInstance(HoleDetailItem allTablesData) {
        Charging3dDataForPreSplitDialog frag = new Charging3dDataForPreSplitDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("all_charge_data", allTablesData);
        frag.setArguments(bundle);
        return frag;
    }

    public void setUpListener(Charging3dDataForPreSplitDialog.ProjectInfoDialogListener listener) {
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
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            allTablesData = (HoleDetailItem) getArguments().getSerializable("all_charge_data");
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

        binding.pageTitle.setText(String.format("%s (%s)", mContext.getString(R.string.charging_param), allTablesData.getHoleId()));
        entity = appDatabase.projectHoleDetailRowColDao();
        if (Constants.isListEmpty( allTablesData.getChargingArray()))
            chargingDataModelList.add(new ChargeTypeArrayItem());
        else {
            chargingDataModelList.addAll( allTablesData.getChargingArray());
        }

        adapter = new Charging3dDataListAdapter(mContext, chargingDataModelList);
        binding.chargingList.setAdapter(adapter);

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

        binding.addChargingFab.setOnClickListener(view -> {
            if (adapter != null)
                adapter.addItemInArray();
            chargingDataModelList.add(new ChargeTypeArrayItem());
            adapter.notifyItemChanged(chargingDataModelList.size() - 1);
            binding.chargingList.scrollToPosition(chargingDataModelList.size() - 1);
        });

        binding.saveProceedBtn.setOnClickListener(view -> {
            allTablesData.setChargingArray(adapter.getJsonArray());

            double totalCharge = 0.0, chargeLen = 0.0;
            for (int i = 0; i < adapter.getJsonArray().size(); i++) {
                totalCharge = totalCharge + adapter.getJsonArray().get(i).getWeight();

                if (StringUtill.getString(adapter.getJsonArray().get(i).getType()).equals("Bulk") || StringUtill.getString(adapter.getJsonArray().get(i).getType()).equals("Cartridge")) {
                    chargeLen = chargeLen + adapter.getJsonArray().get(i).getLength();
                }
            }
            allTablesData.setTotalCharge(String.valueOf(totalCharge));
            allTablesData.setChargeLength(String.valueOf(chargeLen));

            UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
            UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();

            bladesEntity.setData(new Gson().toJson(allTablesData));
//            bladesEntity.setHoleId(Integer.parseInt(String.valueOf(allTablesData.getHoleNo())));
//            bladesEntity.setRowId(Integer.parseInt(String.valueOf(allTablesData.getRowNo())));
            bladesEntity.setDesignId(String.valueOf(allTablesData.getHoleId()));

            if (!updateProjectBladesDao.isExistProject(String.valueOf(allTablesData.getHoleId()))) {
                updateProjectBladesDao.insertProject(bladesEntity);
            } else {
                updateProjectBladesDao.updateProject(bladesEntity);
            }

//            JsonElement element = AppDelegate.getInstance().getHole3DDataElement();
            JsonElement element = new Gson().fromJson(entity.getAllBladesProject(allTablesData.getDesignId()).getProjectHole(), JsonElement.class);
            List<Response3DTable16PilotDataModel> allTablesDataChild = new ArrayList<>();
            if (element != null) {
                // GetAll3DDesignInfoResult for test
                JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) element).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                List<Response3DTable18PreSpilitDataModel> holeDetailDataList = new ArrayList<>();
                JsonArray jsonArray = new JsonArray();
                if (array.get(18) instanceof JsonArray) {
                    jsonArray = new Gson().fromJson(array.get(18), JsonArray.class);
                } else {
                    jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(18), String.class), JsonArray.class);
                }
                for (JsonElement e : jsonArray) {
                    Response3DTable18PreSpilitDataModel model = new Gson().fromJson(e, Response3DTable18PreSpilitDataModel.class);
                    model.setHoleDetailStr(model.getHoleDetailStr());
                    model.setHolePointsStr(model.getHolePointsStr());
                    model.setLinePointsStr(model.getLinePointsStr());
                    holeDetailDataList.add(model);
                }
                if (!Constants.isListEmpty(holeDetailDataList)) {
                    for (int ind = 0; ind < holeDetailDataList.size(); ind++) {
                        Response3DTable18PreSpilitDataModel model = holeDetailDataList.get(ind);
                        List<HoleDetailItem> itemList = new Gson().fromJson(holeDetailDataList.get(ind).getHoleDetailStr(), new TypeToken<List<HoleDetailItem>>() {
                        }.getType());
                        for (int i = 0; i < itemList.size(); i++) {
                            HoleDetailItem item = itemList.get(i);
                            if (item.getHoleId().equals(allTablesData.getHoleId())) {
                                itemList.set(i, allTablesData);
                            }
                        }
                        model.setHoleDetailStr(new Gson().toJson(itemList));
                        holeDetailDataList.set(ind, model);
                    }

                    array.set(18, new Gson().fromJson(new Gson().toJson(holeDetailDataList), JsonElement.class));
                    JsonObject jsonObject = new JsonObject();
                    JsonPrimitive primitive = new JsonPrimitive(new Gson().toJson(array));
                    jsonObject.add(Constants._3D_TBALE_NAME, primitive);
                    preSpilitDataModelList = holeDetailDataList;

                    if (!entity.isExistProject(String.valueOf(allTablesData.getDesignId()))) {
                        entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(allTablesData.getDesignId()), true, new Gson().toJson(jsonObject)));
                    } else {
                        entity.updateProject(String.valueOf(allTablesData.getDesignId()), new Gson().toJson(jsonObject));
                    }

                }
            }

            try {
                ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
                ProjectHoleDetailRowColEntity colEntity = dao.getAllBladesProject(allTablesData.getDesignId());

                JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) new Gson().fromJson(colEntity.getProjectHole(), JsonElement.class)).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                List<Response3DTable18PreSpilitDataModel> holeDetailDataList = new ArrayList<>();
                JsonArray jsonArray = new JsonArray();
                if (array.get(16) instanceof JsonArray) {
                    jsonArray = new Gson().fromJson(array.get(18), JsonArray.class);
                } else {
                    jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(18), String.class), JsonArray.class);
                }
                for (JsonElement e : jsonArray) {
                    holeDetailDataList.add(new Gson().fromJson(e, Response3DTable18PreSpilitDataModel.class));
                }

                if (((HoleDetail3DModelActivity) mContext).rowItemDetail != null)
                    ((HoleDetail3DModelActivity) mContext).rowItemDetail.setRowOfTableForPreSplit(((HoleDetail3DModelActivity) mContext).rowPageVal, holeDetailDataList, false);

                if (((HoleDetail3DModelActivity) mContext).mapViewDataUpdateLiveData != null)
                    ((HoleDetail3DModelActivity) mContext).mapViewDataUpdateLiveData.setValue(true);

                if (Constants.holeBgListener != null)
                    Constants.holeBgListener.setBackgroundRefresh();
                if (Constants.hole3DBgListener != null) {
                    Constants.hole3DBgListener.setBackgroundRefresh();
                }

                AppDelegate.getInstance().setPreSpilitDataModelList(holeDetailDataList);
                ((HoleDetail3DModelActivity) mContext).preSplitTableData = holeDetailDataList;
                if (((HoleDetail3DModelActivity) mContext).mapPilotCallBackListener != null)
                    ((HoleDetail3DModelActivity) mContext).mapPilotCallBackListener.saveAndCloseHoleDetailForPilotCallBack();

            } catch (Exception e) {
                e.getLocalizedMessage();
            }

            showToast(mContext.getString(R.string.charging_added_msg));
            dismiss();

        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((BaseActivity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    private Charging3dDataForPreSplitDialog.ProjectInfoDialogListener getListener() {
        Charging3dDataForPreSplitDialog.ProjectInfoDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof Charging3dDataForPreSplitDialog.ProjectInfoDialogListener)
            listener = (Charging3dDataForPreSplitDialog.ProjectInfoDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof Charging3dDataForPreSplitDialog.ProjectInfoDialogListener)
            listener = (Charging3dDataForPreSplitDialog.ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(Charging3dDataForPreSplitDialog dialogFragment, String designId);

        default void onCancel(HoleDetailDialog dialogFragment) {
        }
    }

}
