package com.smart_blasting_drilling.android.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.smart_blasting_drilling.android.app.BaseRecyclerAdapter;
import com.smart_blasting_drilling.android.databinding.ChargingItemListAdapterBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class Charging3dDataListAdapter extends BaseRecyclerAdapter {

    Context context;
    List<ChargeTypeArrayItem> chargingDataModelList;
    JsonArray stemmingArr = new JsonArray();
    JsonArray deckingArr = new JsonArray();
    JsonArray otherArr = new JsonArray();
    AppDatabase appDatabase;

    String[] stemArr, deckArr, otherDataArr;

    ChargeTypeArrayItem jsonObject = new ChargeTypeArrayItem();
    List<ChargeTypeArrayItem> jsonArray = new ArrayList<>();

    public Charging3dDataListAdapter(Context context, List<ChargeTypeArrayItem> chargingDataModelList) {
        this.context = context;
        this.chargingDataModelList = chargingDataModelList;
        appDatabase = ((BaseActivity) context).appDatabase;
        try {
            if (!Constants.isListEmpty(appDatabase.allMineInfoSurfaceInitiatorDao().getAllBladesProject())) {
                JsonObject jsonObject = new Gson().fromJson(appDatabase.allMineInfoSurfaceInitiatorDao().getAllBladesProject().get(0).getData(), JsonObject.class);
                if (jsonObject.getAsJsonObject().has("Table1")) {
                    deckingArr = jsonObject.getAsJsonObject().get("Table1").getAsJsonArray();
                    deckArr = new String[deckingArr.size()];
                    for (int i = 0; i < deckingArr.size(); i++) {
                        deckArr[i] = deckingArr.get(i).getAsJsonObject().get("Name").getAsString();
                    }
                }
                if (jsonObject.getAsJsonObject().has("Table2")) {
                    stemmingArr = jsonObject.getAsJsonObject().get("Table2").getAsJsonArray();
                    stemArr = new String[stemmingArr.size()];
                    for (int i = 0; i < stemmingArr.size(); i++) {
                        stemArr[i] = stemmingArr.get(i).getAsJsonObject().get("Name").getAsString();
                    }
                }
                if (jsonObject.getAsJsonObject().has("Table3")) {
                    otherArr = jsonObject.getAsJsonObject().get("Table3").getAsJsonArray();
                    otherDataArr = new String[otherArr.size()];
                    for (int i = 0; i < otherArr.size(); i++) {
                        otherDataArr[i] = otherArr.get(i).getAsJsonObject().get("Name").getAsString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group) {
        ChargingItemListAdapterBinding binding = DataBindingUtil.inflate(inflater, R.layout.charging_item_list_adapter, group, false);
        return new Charging3dDataListAdapter.ChargingDataViewHolder(binding);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return getViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChargingDataViewHolder viewHolder = (ChargingDataViewHolder) holder;
        viewHolder.setDataBind(chargingDataModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return chargingDataModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItemInArray() {
        if (jsonArray == null)
            jsonArray = new ArrayList<>();
        jsonArray.add(jsonObject);
        jsonObject = new ChargeTypeArrayItem();
    }

    public List<ChargeTypeArrayItem> getJsonArray() {
        return jsonArray;
    }

    class ChargingDataViewHolder extends RecyclerView.ViewHolder {

        ChargingItemListAdapterBinding binding;

        public ChargingDataViewHolder(@NonNull ChargingItemListAdapterBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        private void setExpArray(String type) {
            if (type.equals("Stemming")) {
                binding.explosiveEt.setAdapter(Constants.getAdapter(context, stemArr));
            } else if (type.equals("Decking")) {
                binding.explosiveEt.setAdapter(Constants.getAdapter(context, deckArr));
            } else {
                binding.explosiveEt.setAdapter(Constants.getAdapter(context, otherDataArr));
            }
        }

        void setDataBind(ChargeTypeArrayItem chargingDataModel) {

            String[] typeArr = new String[]{"Bulk", "Cartridge", "Booster", "Stemming", "Decking"};
            binding.typeSpinner.setAdapter(Constants.getAdapter(context, typeArr));

            binding.typeSpinner.setText(StringUtill.getString(chargingDataModel.getType()));
            setExpArray(chargingDataModel.getType());

            binding.typeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        setExpArray(typeArr[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            binding.explosiveEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        if (binding.typeSpinner.getText().toString().equals("Stemming")) {
                            binding.costEt.setText(stemmingArr.get(i).getAsJsonObject().get("UnitCost").getAsString());
                            jsonObject.setProdId(stemmingArr.get(i).getAsJsonObject().get("ExpCode").getAsInt());
                        } else if (binding.typeSpinner.getText().toString().equals("Decking")) {
                            binding.costEt.setText(deckingArr.get(i).getAsJsonObject().get("UnitCost").getAsString());
                            jsonObject.setProdId(deckingArr.get(i).getAsJsonObject().get("ExpCode").getAsInt());
                        } else {
                            binding.costEt.setText(otherArr.get(i).getAsJsonObject().get("UnitCost").getAsString());
                            jsonObject.setProdId(otherArr.get(i).getAsJsonObject().get("ExpCode").getAsInt());
                        }
                        jsonObject.setType(binding.typeSpinner.getText().toString());
                        jsonObject.setColor(chargingDataModel.getColor());
                        jsonObject.setPercentage(chargingDataModel.getPercentage());
                        jsonObject.setProdType("explosive", binding.explosiveEt.getText().toString());
                        jsonObject.setCost(binding.costEt.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            binding.columnLengthEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    jsonObject.setLength(binding.columnLengthEt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            binding.columnWeightEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    jsonObject.setWeight(binding.columnWeightEt.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

    }

}
