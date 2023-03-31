package com.smart_blasting_drilling.android.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.DialogChargingDataViewBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.ChargingDataListAdapter;
import com.smart_blasting_drilling.android.ui.models.ChargingDataModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
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

    List<ChargeTypeArrayItem> chargingDataModelList = new ArrayList<>();
    ChargingDataListAdapter adapter;


    public ChangingDataDialog() {
        _self = this;
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
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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

    private List<ChargeTypeArrayItem> setChargeArray() {
        List<ChargeTypeArrayItem> arrayItemList = new ArrayList<>();

        try {
            if (!Constants.isListEmpty(holeDetailData.getChargeTypeArray())) {
                arrayItemList = holeDetailData.getChargeTypeArray();
                return arrayItemList;
            }
            ChargeTypeArrayItem arrayItem = new ChargeTypeArrayItem();
            arrayItem.setName(holeDetailData.getName());
            arrayItem.setColor(holeDetailData.getColor());
            arrayItem.setCost(holeDetailData.getUnitCost());
            if (holeDetailData.getExpCode() != null)
                arrayItem.setProdId(((Double) holeDetailData.getExpCode()).intValue());
            if (holeDetailData.getExpType() != null)
                arrayItem.setProdType(((Double) holeDetailData.getExpType()).intValue());
            arrayItemList.add(arrayItem);

            arrayItem = new ChargeTypeArrayItem();
            arrayItem.setName(String.valueOf(holeDetailData.getName1()));
            arrayItem.setColor(String.valueOf(holeDetailData.getColor1()));
            arrayItem.setCost(holeDetailData.getUnitCost1());
            if (holeDetailData.getExpCode1() != null)
                arrayItem.setProdId(((Double) holeDetailData.getExpCode1()).intValue());
            if (holeDetailData.getExpType1() != null)
                arrayItem.setProdType(((Double) holeDetailData.getExpType1()).intValue());
            arrayItemList.add(arrayItem);

            arrayItem = new ChargeTypeArrayItem();
            arrayItem.setName(holeDetailData.getName2());
            arrayItem.setColor(holeDetailData.getColor2());
            arrayItem.setCost(holeDetailData.getUnitCost2());
            if (holeDetailData.getExpCode2() != null)
                arrayItem.setProdId(((Double) holeDetailData.getExpCode2()).intValue());
            if (holeDetailData.getExpType2() != null)
                arrayItem.setProdType(((Double) holeDetailData.getExpType2()).intValue());
            arrayItemList.add(arrayItem);

            arrayItem = new ChargeTypeArrayItem();
            arrayItem.setLength(holeDetailData.getStemLngth());
            arrayItemList.add(arrayItem);

            arrayItem = new ChargeTypeArrayItem();
            arrayItem.setLength(holeDetailData.getDeckLength1());
            arrayItemList.add(arrayItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayItemList;
    }

    @Override
    public void loadData() {
        entity = appDatabase.projectHoleDetailRowColDao();
        /*if (Constants.isListEmpty(chargingDataModelList))
            chargingDataModelList.add(new ChargeTypeArrayItem());
        else {
            chargingDataModelList.clear();
            chargingDataModelList.addAll(setChargeArray());
        }*/

        chargingDataModelList.clear();
        chargingDataModelList.addAll(setChargeArray());

        adapter = new ChargingDataListAdapter(mContext, chargingDataModelList);
        binding.chargingList.setAdapter(adapter);

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

        binding.addChargingFab.setVisibility(View.GONE);

        binding.addChargingFab.setOnClickListener(view -> {
            if (adapter != null)
                adapter.addItemInArray();
            chargingDataModelList.add(new ChargeTypeArrayItem());
            adapter.notifyItemChanged(chargingDataModelList.size() - 1);
            binding.chargingList.scrollToPosition(chargingDataModelList.size() - 1);
        });

        binding.saveProceedBtn.setOnClickListener(view -> {
            try {
                updateHoleDetailData.setChargeTypeArray(new Gson().toJson(adapter.getJsonArray()));

                UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
                UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();

                List<ChargeTypeArrayItem> chargeTypeArrayItemList = adapter.getJsonArray();
                for (int i = 0; i < chargeTypeArrayItemList.size(); i++) {
//                    if (chargeTypeArrayItemList.get(i).)
                    if ((i + 1) == 1) {
                        updateHoleDetailData.setExpCode(chargeTypeArrayItemList.get(i).getProdId());
                        updateHoleDetailData.setName(chargeTypeArrayItemList.get(i).getName());
                        updateHoleDetailData.setColor(chargeTypeArrayItemList.get(i).getColor());
                        updateHoleDetailData.setExpType(chargeTypeArrayItemList.get(i).getProdType());
                        updateHoleDetailData.setUnitCost(chargeTypeArrayItemList.get(i).getCost());
                    }
                    if ((i + 1) == 2) {
                        updateHoleDetailData.setExpCode1(chargeTypeArrayItemList.get(i).getProdId());
                        updateHoleDetailData.setName1(chargeTypeArrayItemList.get(i).getName());
                        updateHoleDetailData.setColor1(chargeTypeArrayItemList.get(i).getColor());
                        updateHoleDetailData.setExpType1(chargeTypeArrayItemList.get(i).getProdType());
                        updateHoleDetailData.setUnitCost1(chargeTypeArrayItemList.get(i).getCost());
                    }
                    if ((i + 1) == 3) {
                        updateHoleDetailData.setBsterName(chargeTypeArrayItemList.get(i).getName());
                        updateHoleDetailData.setBsterLength(((Double) chargeTypeArrayItemList.get(i).getLength()).intValue());
                        updateHoleDetailData.setBsterWt(chargeTypeArrayItemList.get(i).getWeight());
                        updateHoleDetailData.setExpCode2(chargeTypeArrayItemList.get(i).getProdId());
                        updateHoleDetailData.setName2(chargeTypeArrayItemList.get(i).getName());
                        updateHoleDetailData.setColor2(chargeTypeArrayItemList.get(i).getColor());
                        updateHoleDetailData.setExpType2(chargeTypeArrayItemList.get(i).getProdType());
                        updateHoleDetailData.setUnitCost2(chargeTypeArrayItemList.get(i).getCost());
                    }
                    if ((i + 1) == 4) {
                        updateHoleDetailData.setStemLngth(chargeTypeArrayItemList.get(i).getLength());
                    }
                    if ((i + 1) == 5) {
                        updateHoleDetailData.setDeckLength1(((Double) chargeTypeArrayItemList.get(i).getLength()).intValue());
                    }
                }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((BaseActivity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
