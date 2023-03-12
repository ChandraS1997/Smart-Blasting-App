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
import com.smart_blasting_drilling.android.databinding.DialogSelectionFieldLayoutBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.AdapterEditTableFields;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;

import java.util.ArrayList;
import java.util.List;

public class Hole3dEditTableFieldSelectionDialog extends BaseDialogFragment {
    public static final String TAG = "Hole3dEditTableFieldSelectionDialog";
    DialogSelectionFieldLayoutBinding binding;
    Dialog dialog;
    List<TableEditModel> editModelArrayList = new ArrayList<>();
    AdapterEditTableFields adapterEditTableFields;

    public static Hole3dEditTableFieldSelectionDialog getInstance() {
        return new Hole3dEditTableFieldSelectionDialog();
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
            if (Constants.onDataEditTable != null && adapterEditTableFields != null) {
                manger.set3dTableField(adapterEditTableFields.getSelectedData());
                Constants.onDataEditTable.editDataTable(manger.get3dTableField());
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

        editModelArrayList.clear();
        editModelArrayList.addAll(((HoleDetail3DModelActivity) mContext).getTableModel());

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