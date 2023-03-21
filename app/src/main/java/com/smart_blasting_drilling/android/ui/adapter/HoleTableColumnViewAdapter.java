package com.smart_blasting_drilling.android.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.HoleTableColumnViewBinding;
import com.smart_blasting_drilling.android.dialogs.ChangingDataDialog;
import com.smart_blasting_drilling.android.dialogs.HoleStatusDialog;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.KeyboardUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class HoleTableColumnViewAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableEditModel> editModelArrayList;
    boolean isHeader;
    ResponseHoleDetailData holeDetailData;

    public HoleTableColumnViewAdapter(Context context, List<TableEditModel> editModelArrayList, boolean isHeader, ResponseHoleDetailData holeDetailData) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.isHeader = isHeader;
        this.holeDetailData = holeDetailData;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        HoleTableColumnViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.hole_table_column_view, group, false);
        return new HoleColumnViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HoleColumnViewHolder viewHolder = (HoleColumnViewHolder) holder;
        viewHolder.setDataBind(editModelArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return editModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class HoleColumnViewHolder extends RecyclerView.ViewHolder {
        HoleTableColumnViewBinding binding;

        public HoleColumnViewHolder(@NonNull HoleTableColumnViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setValueOfData(TableEditModel model) {
            if (setViewOfTitle(model.getTitleVal())) {
                if (isHeader) {
                    binding.holeIdValTxt.setVisibility(View.VISIBLE);
                    binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
                } else {
                    binding.holeIdVal.setVisibility(View.VISIBLE);
                    binding.holeIdVal.setText(StringUtill.getString(model.getCheckBox()));
                }
            } else {
                binding.holeIdValTxt.setVisibility(View.VISIBLE);
                binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
            }
        }

        void setDataBind(TableEditModel model) {

            if (((HoleDetailActivity) context).isTableHeaderFirstTimeLoad) {
                setValueOfData(model);
            } else {
                if (model.isSelected()) {
                    setValueOfData(model);
                } else {
                    if (setViewOfTitle(model.getTitleVal()))
                        binding.holeIdVal.setVisibility(View.GONE);
                    else
                        binding.holeIdValTxt.setVisibility(View.GONE);
                }
            }

            LinearLayout.LayoutParams layoutParams;
            if (isHeader) {
                layoutParams = new LinearLayout.LayoutParams(300, 100);
            } else {
                layoutParams = new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.MATCH_PARENT);
            }

            binding.holeIdVal.setLayoutParams(layoutParams);
            binding.holeIdValTxt.setLayoutParams(layoutParams);

            if (setViewOfTitle(model.getTitleVal())) {
                if (!isHeader) {
                    binding.holeIdVal.setEnabled(true);
                    binding.holeIdVal.setCursorVisible(true);
                    binding.holeIdVal.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    binding.holeIdVal.setBackgroundResource(R.drawable.table_cell_border_white_bg);
                }
            }

            binding.holeIdValTxt.setOnClickListener(view -> {
                if (!isHeader && !binding.holeIdVal.getText().toString().equals("Charging")) {
                    if (model.getTitleVal().equals("Hole Status")) {
                        FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        HoleStatusDialog holeStatusDialog = HoleStatusDialog.getInstance();
                        holeStatusDialog.setupListener(new HoleStatusDialog.HoleStatusListener() {
                            @Override
                            public void holeStatusCallBack(String status) {
                                binding.holeIdVal.setText(StringUtill.getString(status));
                            }
                        });
                        transaction.add(holeStatusDialog, HoleStatusDialog.TAG);
                        transaction.commitAllowingStateLoss();
                    } else if (model.getTitleVal().equals("Charging")) {
                        FragmentManager manager = ((BaseActivity) context).getSupportFragmentManager();
                        ChangingDataDialog dataDialog = ChangingDataDialog.getInstance(holeDetailData, ((HoleDetailActivity) context).bladesRetrieveData);
                        FragmentTransaction ft = manager.beginTransaction();
                        ft.add(dataDialog, ChangingDataDialog.TAG);
                        ft.commitAllowingStateLoss();
                    }
                }
            });

            binding.holeIdVal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (StringUtill.getString(model.getTitleVal()).equals("Hole Depth")) {
                        if (!StringUtill.isEmpty(binding.holeIdVal.getText().toString()))
                            holeDetailData.setHoleDepth(Double.parseDouble(binding.holeIdVal.getText().toString()));
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Hole Status")) {
                        holeDetailData.setHoleStatus(binding.holeIdVal.getText().toString());
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Hole Angle")) {
                        if (!StringUtill.isEmpty(binding.holeIdVal.getText().toString()))
                            holeDetailData.setHoleAngle(Double.parseDouble(binding.holeIdVal.getText().toString()));
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Burden")) {
                        if (!StringUtill.isEmpty(binding.holeIdVal.getText().toString()))
                            holeDetailData.setBurden(Double.parseDouble(binding.holeIdVal.getText().toString()));
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Spacing")) {
                        if (!StringUtill.isEmpty(binding.holeIdVal.getText().toString()))
                            holeDetailData.setSpacing(Double.parseDouble(binding.holeIdVal.getText().toString()));
                    }
                    ((HoleDetailActivity)context).updateEditedDataIntoDb(holeDetailData);
//                    KeyboardUtils.hideSoftKeyboard((Activity) context);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    binding.holeIdVal.setFocusable(true);
                    binding.holeIdVal.setFocusableInTouchMode(true);
                    binding.holeIdVal.setCursorVisible(true);
                    binding.holeIdVal.requestFocus();
                }
            });

        }

        private boolean setViewOfTitle(String title) {
            return StringUtill.getString(title).equals("Hole Depth")
                    || StringUtill.getString(title).equals("Hole Angle")
                    || StringUtill.getString(title).equals("Burden")
                    || StringUtill.getString(title).equals("Spacing");
        }

    }

}
