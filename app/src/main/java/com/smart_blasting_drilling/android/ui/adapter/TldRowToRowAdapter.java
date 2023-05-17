package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResultsetItem;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.AdapterInitiatingDeviceBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.List;

public class TldRowToRowAdapter extends BaseRecyclerAdapter {

    Context context;
    List<InitiatingDeviceModel> initiatingDeviceModelList;
    List<ResultsetItem> responseInitiatingDataList;
    String[] typeList;
    int[] iniTypeId;
    JsonArray jsonObject = new JsonArray();

    public TldRowToRowAdapter(Context context, List<InitiatingDeviceModel> initiatingDeviceModelList, List<ResultsetItem> responseInitiatingDataList) {
        this.context = context;
        this.initiatingDeviceModelList = initiatingDeviceModelList;
        this.responseInitiatingDataList = responseInitiatingDataList;
        typeList = new String[responseInitiatingDataList.size()];
        iniTypeId = new int[responseInitiatingDataList.size()];
        for (int i = 0; i < responseInitiatingDataList.size(); i++) {
            typeList[i] = responseInitiatingDataList.get(i).getName();
            iniTypeId[i] = responseInitiatingDataList.get(i).getIniType();
        }
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        AdapterInitiatingDeviceBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_initiating_device, group, false);
        return new TldRowToRowViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TldRowToRowViewHolder viewHolder = (TldRowToRowViewHolder) holder;
        viewHolder.setDataBInd(initiatingDeviceModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return initiatingDeviceModelList.size();
    }

    public List<InitiatingDeviceModel> getInitiatingDeviceModelList() {
        return initiatingDeviceModelList;
    }

    class TldRowToRowViewHolder extends RecyclerView.ViewHolder {

        AdapterInitiatingDeviceBinding binding;
        ResultsetItem responseInitiatingData;

        public TldRowToRowViewHolder(@NonNull AdapterInitiatingDeviceBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBInd(InitiatingDeviceModel initiatingDeviceModel) {
            binding.typeSpinner.setAdapter(Constants.getAdapter(context, typeList));
            binding.typeSpinner.setText(StringUtill.isEmpty(initiatingDeviceModel.getType()) ? typeList[0] : initiatingDeviceModel.getType());
            if (StringUtill.isEmpty(initiatingDeviceModel.getType())) {
                binding.unitCostEt.setText(String.valueOf(responseInitiatingDataList.get(0).getUnitCost()));
            } else {
                binding.unitCostEt.setText(StringUtill.getString(initiatingDeviceModel.getCost()));
            }
            binding.qtyEt.setText(StringUtill.getString(initiatingDeviceModel.getQty()));
            binding.unitCostEt.setEnabled(false);
//            binding.unitCostEt.setText(String.valueOf(responseInitiatingDataList.get(0).getUnitCost()));

            binding.itemPageCountTxt.setText(String.format("Item No. :- %s", (getBindingAdapterPosition() + 1)));
            binding.deleteIcn.setOnClickListener(view -> {
                initiatingDeviceModelList.remove(getBindingAdapterPosition());
                notifyItemRemoved(getBindingAdapterPosition());
                notifyDataSetChanged();
            });

            binding.typeSpinner.setOnItemClickListener((adapterView, view, i, l) -> {
                responseInitiatingData = responseInitiatingDataList.get(i);
                binding.unitCostEt.setText(String.valueOf(responseInitiatingDataList.get(i).getUnitCost()));
                /*JsonObject jObject = new JsonObject();
                jObject.addProperty("iniType", responseInitiatingDataList.get(i).getIniType());
                jObject.addProperty("type", responseInitiatingDataList.get(i).getName());
                jObject.addProperty("cost", String.valueOf(responseInitiatingDataList.get(i).getUnitCost()));
                jObject.addProperty("quantity", binding.qtyEt.getText().toString());
                jsonObject.add(jObject);*/
                addData(responseInitiatingDataList.get(i));
            });
            binding.qtyEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    getInitiatingData(binding.typeSpinner.getText().toString());
                    if (responseInitiatingData != null)
                        addData(responseInitiatingData);
                }
            });
        }

        private void getInitiatingData(String val) {
            for (int i = 0; i < typeList.length; i++) {
                if (typeList[i].equals(val)) {
                    responseInitiatingData = responseInitiatingDataList.get(i);
                    break;
                }
            }
        }

        public void addData(ResultsetItem data) {
            InitiatingDeviceModel deviceModel = new InitiatingDeviceModel();
            deviceModel.setPageCount(data.getTldCode());
            deviceModel.setCost(String.valueOf(data.getUnitCost()));
            deviceModel.setQty(binding.qtyEt.getText().toString());
            deviceModel.setType(data.getName());
            initiatingDeviceModelList.set(getBindingAdapterPosition(), deviceModel);
        }

    }
}
