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

import java.util.List;

public class TldHoleToHoleAdapter extends BaseRecyclerAdapter {

    Context context;
    List<InitiatingDeviceModel> initiatingDeviceModelList;
    List<ResultsetItem> responseInitiatingDataList;
    String[] typeList;
    int[] iniTypeId;
    JsonArray jsonObject = new JsonArray();

    public TldHoleToHoleAdapter(Context context, List<InitiatingDeviceModel> initiatingDeviceModelList, List<ResultsetItem> responseInitiatingDataList) {
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
        return new TldHoleToHoleViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TldHoleToHoleViewHolder viewHolder = (TldHoleToHoleViewHolder) holder;
        viewHolder.setDataBInd(initiatingDeviceModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return initiatingDeviceModelList.size();
    }

    public List<InitiatingDeviceModel> getInitiatingDeviceModelList() {
        return initiatingDeviceModelList;
    }

    class TldHoleToHoleViewHolder extends RecyclerView.ViewHolder {

        AdapterInitiatingDeviceBinding binding;
        ResultsetItem responseInitiatingData;

        public TldHoleToHoleViewHolder(@NonNull AdapterInitiatingDeviceBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        void setDataBInd(InitiatingDeviceModel initiatingDeviceModel) {
            binding.typeSpinner.setAdapter(Constants.getAdapter(context, typeList));
            binding.unitCostEt.setEnabled(false);
            binding.unitCostEt.setText(String.valueOf(responseInitiatingDataList.get(0).getUnitCost()));

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
                    if (responseInitiatingData != null)
                        addData(responseInitiatingData);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        public void addData(ResultsetItem data) {
            InitiatingDeviceModel deviceModel = new InitiatingDeviceModel();
            deviceModel.setPageCount(data.getIniType());
            deviceModel.setCost(String.valueOf(data.getUnitCost()));
            deviceModel.setQty(binding.qtyEt.getText().toString());
            deviceModel.setType(data.getName());
            initiatingDeviceModelList.set(getBindingAdapterPosition(), deviceModel);
        }

    }
}
