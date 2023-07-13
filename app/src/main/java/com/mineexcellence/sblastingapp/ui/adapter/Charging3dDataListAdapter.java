package com.mineexcellence.sblastingapp.ui.adapter;

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
import com.google.gson.JsonPrimitive;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.mineexcellence.sblastingapp.app.BaseRecyclerAdapter;
import com.mineexcellence.sblastingapp.databinding.ChargingItemListAdapterBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.room_database.AppDatabase;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.utils.StringUtill;

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

    List<ChargeTypeArrayItem> jsonArray = new ArrayList<>();

    public Charging3dDataListAdapter(Context context, List<ChargeTypeArrayItem> chargingDataModelList) {
        this.context = context;
        this.chargingDataModelList = chargingDataModelList;
        appDatabase = ((BaseActivity) context).appDatabase;
        try {
            if (!Constants.isListEmpty(appDatabase.allMineInfoSurfaceInitiatorDao().getAllBladesProject())) {
                JsonObject jsonObject = new Gson().fromJson(appDatabase.allMineInfoSurfaceInitiatorDao().getAllBladesProject().get(0).getData(), JsonObject.class);
                JsonObject jsObj = jsonObject;
                if (jsonObject.has("data")) {
                    jsObj = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(jsonObject.getAsJsonObject().get("data"), JsonPrimitive.class), String.class), JsonObject.class);
                }
                if (jsObj.getAsJsonObject().has("Table1")) {
                    deckingArr = jsObj.getAsJsonObject().get("Table1").getAsJsonArray();
                    deckArr = new String[deckingArr.size()];
                    for (int i = 0; i < deckingArr.size(); i++) {
                        deckArr[i] = deckingArr.get(i).getAsJsonObject().get("Name").getAsString();
                    }
                }
                if (jsObj.getAsJsonObject().has("Table2")) {
                    stemmingArr = jsObj.getAsJsonObject().get("Table2").getAsJsonArray();
                    stemArr = new String[stemmingArr.size()];
                    for (int i = 0; i < stemmingArr.size(); i++) {
                        stemArr[i] = stemmingArr.get(i).getAsJsonObject().get("Name").getAsString();
                    }
                }
                if (jsObj.getAsJsonObject().has("Table3")) {
                    otherArr = jsObj.getAsJsonObject().get("Table3").getAsJsonArray();
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
//        jsonArray.add(jsonObject);
//        jsonObject = new ChargeTypeArrayItem();
    }

    public List<ChargeTypeArrayItem> getJsonArray() {
        if (jsonArray.size() != chargingDataModelList.size()) {
            addItemInArray();
        }
        return chargingDataModelList;
    }

    class ChargingDataViewHolder extends RecyclerView.ViewHolder {

        ChargingItemListAdapterBinding binding;

        public ChargingDataViewHolder(@NonNull ChargingItemListAdapterBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        private void setExpArray(String type) {
            if (!StringUtill.isEmpty(type)) {
                if (type.equals("Stemming")) {
                    binding.explosiveEt.setAdapter(Constants.getAdapter(context, stemArr));
                } else if (type.equals("Decking")) {
                    binding.explosiveEt.setAdapter(Constants.getAdapter(context, deckArr));
                } else {
                    binding.explosiveEt.setAdapter(Constants.getAdapter(context, otherDataArr));
                }
            }
        }

        void setDataBind(ChargeTypeArrayItem chargingDataModel) {

            String[] typeArr = new String[]{"Bulk", "Cartridge", "Booster", "Stemming", "Decking"};
            binding.typeSpinner.setAdapter(Constants.getAdapter(context, typeArr));

            binding.typeSpinner.setText(StringUtill.getString(chargingDataModel.getType()));
            if (!StringUtill.isEmpty(chargingDataModel.getType())) {
                binding.explosiveEt.setText(StringUtill.getString(chargingDataModel.getName()));
                binding.costEt.setText(StringUtill.getString(String.valueOf(chargingDataModel.getCost())));
                binding.columnLengthEt.setText(StringUtill.getString(String.valueOf(chargingDataModel.getLength())));
                binding.columnWeightEt.setText(StringUtill.getString(String.valueOf(chargingDataModel.getWeight())));
            }
            setExpArray(chargingDataModel.getType());

            binding.typeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        chargingDataModel.setType(typeArr[i]);
                        setExpArray(typeArr[i]);
                        addData(chargingDataModel);
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
                            chargingDataModel.setProdId(stemmingArr.get(i).getAsJsonObject().get("ExpCode").getAsInt());
                        } else if (binding.typeSpinner.getText().toString().equals("Decking")) {
                            binding.costEt.setText(deckingArr.get(i).getAsJsonObject().get("UnitCost").getAsString());
                            chargingDataModel.setProdId(deckingArr.get(i).getAsJsonObject().get("ExpCode").getAsInt());
                        } else {
                            binding.costEt.setText(otherArr.get(i).getAsJsonObject().get("UnitCost").getAsString());
                            chargingDataModel.setProdId(otherArr.get(i).getAsJsonObject().get("ExpCode").getAsInt());
                        }
                        chargingDataModel.setType(binding.typeSpinner.getText().toString());
                        chargingDataModel.setName(binding.explosiveEt.getText().toString());
                        chargingDataModel.setColor(chargingDataModel.getColor());
                        chargingDataModel.setPercentage(chargingDataModel.getPercentage());
                        for (int a = 0; a < typeArr.length; a++) {
                            if (typeArr[a].equals(binding.typeSpinner.getText().toString())) {
                                chargingDataModel.setProdType(a+1);
                                break;
                            }
                        }
                        chargingDataModel.setCost(binding.costEt.getText().toString());

                        addData(chargingDataModel);
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
                    chargingDataModel.setLength(binding.columnLengthEt.getText().toString());
                    addData(chargingDataModel);
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
                    chargingDataModel.setWeight(binding.columnWeightEt.getText().toString());
                    addData(chargingDataModel);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }

        public void addData(ChargeTypeArrayItem data) {
            chargingDataModelList.set(getBindingAdapterPosition(), data);
        }

    }

}
