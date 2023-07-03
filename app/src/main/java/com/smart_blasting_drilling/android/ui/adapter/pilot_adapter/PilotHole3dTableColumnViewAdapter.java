package com.smart_blasting_drilling.android.ui.adapter.pilot_adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.pre_spilit_table.HoleDetailItem;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.HoleTableColumnViewBinding;
import com.smart_blasting_drilling.android.dialogs.ChangingDataDialog;
import com.smart_blasting_drilling.android.dialogs.Charging3dDataDialog;
import com.smart_blasting_drilling.android.dialogs.HoleStatusDialog;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class PilotHole3dTableColumnViewAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableEditModel> editModelArrayList;
    Response3DTable16PilotDataModel holeDetailData;
    boolean isHeader, isSelected;

    public PilotHole3dTableColumnViewAdapter(Context context, List<TableEditModel> editModelArrayList, Response3DTable16PilotDataModel holeDetailData, boolean isHeader, boolean isSelected) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.holeDetailData = holeDetailData;
        this.isHeader = isHeader;
        this.isSelected = isSelected;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        return null;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class PilotColumnViewHolder extends RecyclerView.ViewHolder {

        HoleTableColumnViewBinding binding;

        public PilotColumnViewHolder(@NonNull HoleTableColumnViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setValueOfData(TableEditModel model) {
            if (setViewOfTitle(model.getTitleVal())) {
                if (isHeader) {
                    binding.holeIdValTxt.setVisibility(View.VISIBLE);
                    binding.holeIdVal.setVisibility(View.GONE);
                    binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
                } else {
                    binding.holeIdVal.setVisibility(View.VISIBLE);
                    binding.holeIdValTxt.setVisibility(View.GONE);
                    binding.holeIdVal.setText(StringUtill.getString(model.getCheckBox()));
                }
            } else {
                binding.holeIdValTxt.setVisibility(View.VISIBLE);
                binding.holeIdVal.setVisibility(View.GONE);
                binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
            }
        }

        private boolean setViewOfTitle(String title) {
            return StringUtill.getString(title).equals("Hole Depth")
                    || StringUtill.getString(title).equals("VerticalDip")
                    /*|| StringUtill.getString(title).equals("Burden")
                    || StringUtill.getString(title).equals("Spacing")*/;
        }

        void setDataBind(TableEditModel model) {

            if (((HoleDetail3DModelActivity) context).isTableHeaderFirstTimeLoad) {
                setValueOfData(model);
            } else {
                if (isSelected) {
                    if (model.isSelected()) {
                        setValueOfData(model);
                    } else {
                        binding.holeIdValTxt.setVisibility(View.GONE);
                        binding.holeIdVal.setVisibility(View.GONE);
                    }
                } else {
                    if (!setViewOfTitle(model.getTitleVal())){
                        binding.holeIdValTxt.setVisibility(View.VISIBLE);
                        binding.holeIdVal.setVisibility(View.GONE);
                        binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
                    } else {
                        binding.holeIdValTxt.setVisibility(View.GONE);
                        binding.holeIdVal.setVisibility(View.VISIBLE);
                        binding.holeIdVal.setText(StringUtill.getString(model.getCheckBox()));
                    }
                }
                /*if (model.isSelected()) {
                    setValueOfData(model);
                } else {
                    if (!setViewOfTitle(model.getTitleVal())){
                        binding.holeIdValTxt.setVisibility(View.VISIBLE);
                        binding.holeIdVal.setVisibility(View.GONE);
                        binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
                    } else {
                        binding.holeIdValTxt.setVisibility(View.GONE);
                        binding.holeIdVal.setVisibility(View.VISIBLE);
                        binding.holeIdVal.setText(StringUtill.getString(model.getCheckBox()));
                    }
                }*/
            }

            LinearLayout.LayoutParams layoutParams;
            if (isHeader) {
                layoutParams = new LinearLayout.LayoutParams(300, 100);
            } else {
                layoutParams = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.MATCH_PARENT);
            }

            if (setViewOfTitle(model.getTitleVal())) {
                if (!isHeader) {
                    binding.holeIdVal.setEnabled(true);
                    binding.holeIdVal.setCursorVisible(true);
                    binding.holeIdVal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    binding.holeIdVal.setBackgroundResource(R.drawable.table_cell_border_white_bg);
                }
            }

            binding.holeIdValTxt.setOnClickListener(view -> {
                if (getBindingAdapterPosition() > 0 && !binding.holeIdValTxt.getText().toString().equals("Charging")) {
                    if (model.getTitleVal().equals("Charging")) {
                        /*FragmentManager manager = ((BaseActivity) context).getSupportFragmentManager();
                        Charging3dDataDialog dataDialog = Charging3dDataDialog.getInstance(holeDetailData);
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.add(dataDialog, ChangingDataDialog.TAG);
                        ft.commitAllowingStateLoss();*/
                    }
                }
            });

            binding.holeIdVal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    if (StringUtill.validateDoubleVal(charSequence.toString())) {
                    if (StringUtill.getString(model.getTitleVal()).equals("Hole Depth")) {
                        holeDetailData.setHoleDepth(binding.holeIdVal.getText().toString());
                    }
                    /*if (StringUtill.getString(model.getTitleVal()).equals("Hole Status")) {
                        holeDetailData.setHoleStatus(binding.holeIdVal.getText().toString());
                    }*/
                    if (StringUtill.getString(model.getTitleVal()).equals("VerticalDip")) {
                        holeDetailData.setVerticalDip(binding.holeIdVal.getText().toString());
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Burden")) {
                        holeDetailData.setBurden(binding.holeIdVal.getText().toString());
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Spacing")) {
                        holeDetailData.setSpacing(binding.holeIdVal.getText().toString());
                    }
                    model.setCheckBox(binding.holeIdVal.getText().toString());
                    editModelArrayList.set(getBindingAdapterPosition(), model);
//                    ((HoleDetail3DModelActivity) context).updateEditedDataIntoDb(holeDetailData, true);
                    /*} else {
                        ((BaseActivity) context).showSnackBar(binding.getRoot(), "Please enter only number format");
                    }*/
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    binding.holeIdVal.setFocusable(true);
                    binding.holeIdVal.setFocusableInTouchMode(true);
                    binding.holeIdVal.setCursorVisible(true);
                    binding.holeIdVal.requestFocus();
                }
            });

            binding.holeIdVal.setLayoutParams(layoutParams);
            binding.holeIdValTxt.setLayoutParams(new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.MATCH_PARENT));

        }

    }

}
