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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.databinding.DialogSelectionFieldLayoutBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetailActivity;
import com.mineexcellence.sblastingapp.ui.adapter.AdapterEditTableFields;
import com.mineexcellence.sblastingapp.ui.models.TableEditModel;

import java.util.ArrayList;
import java.util.List;

public class HoleEditTableFieldSelectionDialog extends BaseDialogFragment {
    public static final String TAG = "HoleEditTableFieldSelectionDialog";
    DialogSelectionFieldLayoutBinding binding;
    Dialog dialog;
    List<TableEditModel> editModelArrayList = new ArrayList<>();
    AdapterEditTableFields adapterEditTableFields;

    public static HoleEditTableFieldSelectionDialog getInstance() {
        return new HoleEditTableFieldSelectionDialog();
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
            ((HoleDetailActivity) mContext).isTableHeaderFirstTimeLoad = false;
            if (Constants.onDataEditTable != null && adapterEditTableFields != null) {
                boolean isSelected = false;
                for (TableEditModel model : adapterEditTableFields.getSelectedData()) {
                    if (model.isSelected()) {
                        isSelected = true;
                        break;
                    }
                }
                if (isSelected) {
                    manger.setTableField(adapterEditTableFields.getSelectedData());
                    Constants.onDataEditTable.editDataTable(manger.getTableField(), true);
                } else {
                    manger.setTableField(null);
                    Constants.onDataEditTable.editDataTable(((HoleDetailActivity) mContext).getTableModel(), false);
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

        editModelArrayList.clear();
        editModelArrayList.addAll(((HoleDetailActivity) mContext).getTableModel());

        if (Constants.getScreenHeightResolution(mContext) < 800) {
            /*
            * ConstraintLayout.LayoutParams layoutParamsMsg = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            layoutParamsMsg.setMargins(20, 20, 20, 20);

            ConstraintLayout constraintLayout = binding.stackConst;
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.applyToLayoutParams(binding.msgView.getId(), layoutParamsMsg);
            constraintSet.connect(R.id.msgView, ConstraintSet.TOP, R.id.statusView, ConstraintSet.BOTTOM, 20);
            constraintSet.connect(R.id.msgView, ConstraintSet.START, R.id.stackConst, ConstraintSet.START, 0);
            constraintSet.connect(R.id.msgView, ConstraintSet.END, R.id.stackConst, ConstraintSet.END, 0);

            constraintSet.connect(R.id.statusView, ConstraintSet.TOP, R.id.stackConst, ConstraintSet.TOP, 10);
            constraintSet.connect(R.id.statusView, ConstraintSet.START, R.id.stackConst, ConstraintSet.START, 0);
            constraintSet.connect(R.id.statusView, ConstraintSet.END, R.id.stackConst, ConstraintSet.END, 0);
            constraintSet.applyTo(constraintLayout);
            * */

            ConstraintLayout.LayoutParams layoutParamsMsg = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsMsg.height = ((BaseActivity) mContext).dp2px(mContext.getResources(), 150);
            layoutParamsMsg.width = binding.listContainerView.getWidth();
//            binding.listContainerView.setLayoutParams(layoutParamsMsg);
            binding.tableEditRecycler.setLayoutParams(layoutParamsMsg);
        }

        adapterEditTableFields = new AdapterEditTableFields(mContext, editModelArrayList);
        binding.tableEditRecycler.setLayoutManager(new LinearLayoutManager(mContext));
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
