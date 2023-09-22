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
import androidx.databinding.DataBindingUtil;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.databinding.DialogSelectionFieldLayoutBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.ui.adapter.AdapterEditTableFields;
import com.mineexcellence.sblastingapp.ui.models.TableEditModel;

import java.util.ArrayList;
import java.util.List;

public class Hole3dEditFieldTableForPilotSelectionDialog extends BaseDialogFragment {
    public static final String TAG = "Hole3dEditFieldTableForPilotSelectionDialog";
    DialogSelectionFieldLayoutBinding binding;
    Dialog dialog;
    List<TableEditModel> editModelArrayList = new ArrayList<>();
    AdapterEditTableFields adapterEditTableFields;

    public static Hole3dEditFieldTableForPilotSelectionDialog getInstance() {
        return new Hole3dEditFieldTableForPilotSelectionDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_selection_field_layout, container, false);

        return binding.getRoot();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

    }

    @Override
    public void setupClickListener() {
        binding.saveBtn.setOnClickListener(view -> {
            dismiss();
            ((HoleDetail3DModelActivity) mContext).isTableHeaderFirstTimeLoad = false;

            boolean isSelected = false;
            for (TableEditModel model : adapterEditTableFields.getSelectedData()) {
                if (model.isSelected()) {
                    isSelected = true;
                    break;
                }
            }
            if (Constants.onDataEditTable != null) {
                if (isSelected) {
                    manger.set3dPilotTableField(adapterEditTableFields.getSelectedData());
                    Constants.onDataEditTable.pilotEditDataTable(manger.get3dPilotTableField(), true);
                } else {
                    manger.set3dPilotTableField(null);
                    Constants.onDataEditTable.pilotEditDataTable(((HoleDetail3DModelActivity) mContext).getPilotTableModel(), false);
                }
            }
        });
        binding.cancelBtn.setOnClickListener(view -> dismiss());
    }

    @Override
    public void loadData() {
        binding.headerLayHole.pageTitle.setVisibility(View.VISIBLE);
        binding.headerLayHole.pageTitle.setText(mContext.getString(R.string.choose_fields));
        binding.headerLayHole.projectInfo.setVisibility(View.GONE);
        binding.headerLayHole.camIcon.setVisibility(View.GONE);
        binding.headerLayHole.homeBtn.setVisibility(View.GONE);
        binding.headerLayHole.editTable.setVisibility(View.GONE);
        binding.headerLayHole.refreshIcn.setVisibility(View.GONE);
        binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
        binding.headerLayHole.spinnerHoleType.setVisibility(View.GONE);

        editModelArrayList.clear();
        editModelArrayList.addAll(((HoleDetail3DModelActivity) mContext).getPilotTableModel());

        adapterEditTableFields = new AdapterEditTableFields(mContext, editModelArrayList);
        binding.tableEditRecycler.setAdapter(adapterEditTableFields);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        return dialog;
    }

    public interface HoleEditTableFieldSelectionDialogListener {
        void onOk(boolean is3DBlades, DownloadListDialog dialogFragment);

        default void onCancel(DownloadListDialog dialogFragment) {
        }
    }

}