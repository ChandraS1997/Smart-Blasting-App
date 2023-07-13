package com.mineexcellence.sblastingapp.ui.adapter;

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

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseBladesRetrieveData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseHoleDetailData;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.HoleTableColumnViewBinding;
import com.mineexcellence.sblastingapp.dialogs.ChangingDataDialog;
import com.mineexcellence.sblastingapp.dialogs.HoleStatusDialog;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetailActivity;
import com.mineexcellence.sblastingapp.ui.models.TableEditModel;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleTableColumnViewAdapter extends BaseRecyclerAdapter {

    Context context;
    List<TableEditModel> editModelArrayList;
    boolean isHeader;
    ResponseHoleDetailData holeDetailData;
    boolean isSelected;

    public HoleTableColumnViewAdapter(Context context, List<TableEditModel> editModelArrayList, boolean isHeader, ResponseHoleDetailData holeDetailData, boolean isSelected) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
        this.isHeader = isHeader;
        this.holeDetailData = holeDetailData;
        this.isSelected = isSelected;
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
                    binding.holeIdVal.setVisibility(View.GONE);
                    binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
                } else {
                    binding.holeIdValTxt.setVisibility(View.GONE);
                    binding.holeIdVal.setVisibility(View.VISIBLE);
                    binding.holeIdVal.setText(StringUtill.getString(model.getCheckBox()));
                }
            } else {
                binding.holeIdValTxt.setVisibility(View.VISIBLE);
                binding.holeIdVal.setVisibility(View.GONE);
                binding.holeIdValTxt.setText(StringUtill.getString(model.getCheckBox()));
            }
        }

        void setDataBind(TableEditModel model) {

            if (((HoleDetailActivity) context).isTableHeaderFirstTimeLoad) {
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

            binding.holeIdVal.setLayoutParams(layoutParams);
            binding.holeIdValTxt.setLayoutParams(new LinearLayout.LayoutParams(300, LinearLayout.LayoutParams.MATCH_PARENT));

            if (setViewOfTitle(model.getTitleVal())) {
                if (!isHeader) {
                    binding.holeIdVal.setEnabled(true);
                    binding.holeIdVal.setCursorVisible(true);
                    binding.holeIdVal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    binding.holeIdVal.setBackgroundResource(R.drawable.table_cell_border_white_bg);
                }
            }

            binding.holeIdValTxt.setOnClickListener(view -> {
                if (!isHeader && !binding.holeIdValTxt.getText().toString().equals("Charging")) {
                    if (model.getTitleVal().equals("Hole Status")) {
                        FragmentManager fragmentManager = ((BaseActivity) context).getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        HoleStatusDialog holeStatusDialog = HoleStatusDialog.getInstance();
                        holeStatusDialog.setupListener(new HoleStatusDialog.HoleStatusListener() {
                            @Override
                            public void holeStatusCallBack(String status) {
                                binding.holeIdValTxt.setText(StringUtill.getString(status));
                                if (StringUtill.getString(model.getTitleVal()).equals("Hole Status")) {
                                    holeDetailData.setHoleStatus(StringUtill.getString(status));
                                }
                                ((HoleDetailActivity) context).updateEditedDataIntoDb(holeDetailData, true);
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
                            holeDetailData.setHoleDepth(binding.holeIdVal.getText().toString());
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Hole Status")) {
                        holeDetailData.setHoleStatus(binding.holeIdVal.getText().toString());
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Hole Angle")) {
                        if (!StringUtill.isEmpty(binding.holeIdVal.getText().toString()))
                            holeDetailData.setHoleAngle(binding.holeIdVal.getText().toString());
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Burden")) {
                        if (!StringUtill.isEmpty(binding.holeIdVal.getText().toString()))
                            holeDetailData.setBurden(binding.holeIdVal.getText().toString());
                    }
                    if (StringUtill.getString(model.getTitleVal()).equals("Spacing")) {
                        if (!StringUtill.isEmpty(binding.holeIdVal.getText().toString()))
                            holeDetailData.setSpacing(binding.holeIdVal.getText().toString());
                    }
                    ((HoleDetailActivity) context).updateEditedDataIntoDb(holeDetailData, true);
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
                    /*|| StringUtill.getString(title).equals("Burden")
                    || StringUtill.getString(title).equals("Spacing")*/;
        }

        private void setChargingData(ResponseBladesRetrieveData data) {
            try {
                List<ChargeTypeArrayItem> chargeTypeArrayItemList = new ArrayList<>();

                if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getExpCode()))) {

                }
            } catch (Exception e) {

            }
        }

    }

}
