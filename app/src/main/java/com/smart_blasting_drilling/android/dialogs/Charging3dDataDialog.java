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
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.databinding.DialogChargingDataViewBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.ui.adapter.Charging3dDataListAdapter;
import com.smart_blasting_drilling.android.ui.adapter.ChargingDataListAdapter;
import com.smart_blasting_drilling.android.ui.models.ChargingDataModel;

import java.util.ArrayList;
import java.util.List;

public class Charging3dDataDialog extends BaseDialogFragment {

    DialogChargingDataViewBinding binding;
    public static final String TAG = "Charging3dDataDialog";
    Dialog dialog;
    Charging3dDataDialog _self;
    Charging3dDataDialog.ProjectInfoDialogListener mListener;
    ProjectHoleDetailRowColDao entity;

    List<ChargeTypeArrayItem> chargingDataModelList = new ArrayList<>();
    Charging3dDataListAdapter adapter;

    Response3DTable4HoleChargingDataModel allTablesData = new Response3DTable4HoleChargingDataModel();

    public Charging3dDataDialog() {
        _self = this;
    }

    public static Charging3dDataDialog getInstance(Response3DTable4HoleChargingDataModel allTablesData) {
        Charging3dDataDialog frag = new Charging3dDataDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("all_charge_data", allTablesData);
        frag.setArguments(bundle);
        return frag;
    }

    public void setUpListener(Charging3dDataDialog.ProjectInfoDialogListener listener) {
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
            allTablesData = (Response3DTable4HoleChargingDataModel) getArguments().getSerializable("all_charge_data");
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
        entity = appDatabase.projectHoleDetailRowColDao();
        if (Constants.isListEmpty( allTablesData.getChargeTypeArray()))
            chargingDataModelList.add(new ChargeTypeArrayItem());
        else {
            chargingDataModelList.addAll( allTablesData.getChargeTypeArray());
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
            adapter.notifyDataSetChanged();
            binding.chargingList.scrollToPosition(chargingDataModelList.size() - 1);
        });

        binding.saveProceedBtn.setOnClickListener(view -> {
            Log.e("Item List : ", new Gson().toJson(adapter.getJsonArray()));
        });

    }

    private Charging3dDataDialog.ProjectInfoDialogListener getListener() {
        Charging3dDataDialog.ProjectInfoDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof Charging3dDataDialog.ProjectInfoDialogListener)
            listener = (Charging3dDataDialog.ProjectInfoDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof Charging3dDataDialog.ProjectInfoDialogListener)
            listener = (Charging3dDataDialog.ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(Charging3dDataDialog dialogFragment, String designId);

        default void onCancel(HoleDetailDialog dialogFragment) {
        }
    }

}