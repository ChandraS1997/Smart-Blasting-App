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

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.HoleDetailItem;
import com.mineexcellence.sblastingapp.databinding.DialogChargingDataViewBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
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

            ((HoleDetail3DModelActivity) mContext).updateEditedDataForPreSplitIntoDb(allTablesData, true);

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
