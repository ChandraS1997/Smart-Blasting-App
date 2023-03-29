package com.smart_blasting_drilling.android.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseInitiatingData;
import com.smart_blasting_drilling.android.api.apis.response.ResultsetItem;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.ActivityInitiatingDeviceViewBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.InitiatingDataDao;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDeviceDataEntity;
import com.smart_blasting_drilling.android.ui.adapter.DownTheHoleAdapter;
import com.smart_blasting_drilling.android.ui.adapter.ElectronicDetonatorAdapter;
import com.smart_blasting_drilling.android.ui.adapter.TldHoleToHoleAdapter;
import com.smart_blasting_drilling.android.ui.adapter.TldRowToRowAdapter;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceAllTypeModel;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceModel;
import com.smart_blasting_drilling.android.utils.KeyboardUtils;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InitiatingDeviceViewActivity extends BaseActivity {

    ActivityInitiatingDeviceViewBinding binding;
    ElectronicDetonatorAdapter electronicDetonatorAdapter;
    DownTheHoleAdapter downTheHoleAdapter;
    TldRowToRowAdapter tldRowToRowAdapter;
    TldHoleToHoleAdapter tldHoleToHoleAdapter;

    String designId = "";

    List<ResultsetItem> responseInitiatingDataList = new ArrayList<>();

    List<InitiatingDeviceModel> electronicDetonatorModelList = new ArrayList<>();
    List<InitiatingDeviceModel> downTheHoleModelList = new ArrayList<>();
    List<InitiatingDeviceModel> tldRowToRowModelList = new ArrayList<>();
    List<InitiatingDeviceModel> tldHoleToHoleModelList = new ArrayList<>();

    List<InitiatingDeviceAllTypeModel> initiatingDeviceAllTypeModelList = new ArrayList<>();
    List<InitiatingDeviceAllTypeModel> dbSavedDataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_initiating_device_view);

        if (isBundleIntentNotEmpty()) {
            designId = getIntent().getExtras().getString("blades_data");
        }

        StatusBarUtils.statusBarColor(this, R.color._FFA722);
        binding.headerMedia.mediaTitle.setText(getString(R.string.initiating_device));

        binding.headerMedia.backImg.setOnClickListener(view -> {
            removeDuplicateItem();
            finish();
        });

        setDataList();

        binding.electronicDetonatorTxt.setOnClickListener(view -> {
            if (binding.eleListGroup.getVisibility() == View.VISIBLE) {
                binding.eleArrowImg.setRotation(270);
                binding.eleListGroup.setVisibility(View.GONE);
            } else {
                binding.eleArrowImg.setRotation(0);
                binding.eleListGroup.setVisibility(View.VISIBLE);
            }
            if (Constants.isListEmpty(electronicDetonatorModelList))
                setEleDetListNotify();
        });

        binding.downTheHoleTxt.setOnClickListener(view -> {
            if (binding.downToHoleListGroup.getVisibility() == View.VISIBLE) {
                binding.downTheHoleArrowImg.setRotation(270);
                binding.downToHoleListGroup.setVisibility(View.GONE);
            } else {
                binding.downTheHoleArrowImg.setRotation(0);
                binding.downToHoleListGroup.setVisibility(View.VISIBLE);
            }
            if (Constants.isListEmpty(downTheHoleModelList))
                setDownTheHoleNotify();
        });

        binding.tldRowToRowTxt.setOnClickListener(view -> {
            if (binding.tldRowToRowListGroup.getVisibility() == View.VISIBLE) {
                binding.tldRowToRowArrowImg.setRotation(270);
                binding.tldRowToRowListGroup.setVisibility(View.GONE);
            } else {
                binding.tldRowToRowArrowImg.setRotation(0);
                binding.tldRowToRowListGroup.setVisibility(View.VISIBLE);
            }
            if (Constants.isListEmpty(tldRowToRowModelList))
                setTldRowToRowListNotify();
        });

        binding.tlsHoleToHoleTxt.setOnClickListener(view -> {
            if (binding.tlsHoleToHoleListGroup.getVisibility() == View.VISIBLE) {
                binding.tlsHoleToHoleArrowImg.setRotation(270);
                binding.tlsHoleToHoleListGroup.setVisibility(View.GONE);
            } else {
                binding.tlsHoleToHoleArrowImg.setRotation(0);
                binding.tlsHoleToHoleListGroup.setVisibility(View.VISIBLE);
            }
            if (Constants.isListEmpty(tldHoleToHoleModelList))
                setTlsHoleToHoleListNotify();
        });

        binding.downTheHoleAddBtn.setOnClickListener(view -> {
            setDownTheHoleNotify();
        });

        binding.eleDetonatorAddBtn.setOnClickListener(view -> {
            setEleDetListNotify();
        });

        binding.tlsHoleToHoleAddBtn.setOnClickListener(view -> {
            setTlsHoleToHoleListNotify();
        });

        binding.tldRowToRowAddBtn.setOnClickListener(view -> {
            setTldRowToRowListNotify();
        });

        binding.eleSaveBtn.setOnClickListener(view -> {
            List<InitiatingDeviceModel> modelList = electronicDetonatorAdapter.getInitiatingDeviceModelList();
            initiatingDeviceAllTypeModelList.add(new InitiatingDeviceAllTypeModel("Electronic/Electric Detonator", modelList));
            showToast("Data added successfully");
            KeyboardUtils.hideSoftKeyboard(this);
        });

        binding.downTheHoleSaveBtn.setOnClickListener(view -> {
            List<InitiatingDeviceModel> modelList = downTheHoleAdapter.getInitiatingDeviceModelList();
            initiatingDeviceAllTypeModelList.add(new InitiatingDeviceAllTypeModel("Down The Hole", modelList));
            showToast("Data added successfully");
            KeyboardUtils.hideSoftKeyboard(this);
        });

        binding.tldRowToRowSaveBtn.setOnClickListener(view -> {
            List<InitiatingDeviceModel> modelList = tldRowToRowAdapter.getInitiatingDeviceModelList();
            initiatingDeviceAllTypeModelList.add(new InitiatingDeviceAllTypeModel("TLD(Row To Row)", modelList));
            showToast("Data added successfully");
            KeyboardUtils.hideSoftKeyboard(this);
        });

        binding.tlsHoleToHoleSaveBtn.setOnClickListener(view -> {
            List<InitiatingDeviceModel> modelList = tldHoleToHoleAdapter.getInitiatingDeviceModelList();
            initiatingDeviceAllTypeModelList.add(new InitiatingDeviceAllTypeModel("TLD(Hole To Hole)", modelList));
            showToast("Data added successfully");
            KeyboardUtils.hideSoftKeyboard(this);
        });

    }

    private void clearList() {
        responseInitiatingDataList.clear();
    }

    private void getSavedInitiatingDataFromDb() {
        try {
            if (appDatabase.initiatingDeviceDao().isExistItem(designId)) {
                Type itemList = new TypeToken<List<InitiatingDeviceAllTypeModel>>() {}.getType();
                List<InitiatingDeviceAllTypeModel> allTypeModelList = new Gson().fromJson(appDatabase.initiatingDeviceDao().getSingleItemEntity(designId).getData(), itemList);

                if (!Constants.isListEmpty(allTypeModelList)) {
                    dbSavedDataList.clear();
                    dbSavedDataList = allTypeModelList;
                    for (int i = 0; i < allTypeModelList.size(); i++) {
                        if (allTypeModelList.get(i).getDeviceName().equals("Electronic/Electric Detonator")) {
                            electronicDetonatorModelList = allTypeModelList.get(i).getDeviceModelList();
                        }
                        if (allTypeModelList.get(i).getDeviceName().equals("Down The Hole")) {
                            downTheHoleModelList = allTypeModelList.get(i).getDeviceModelList();
                        }
                        if (allTypeModelList.get(i).getDeviceName().equals("TLD(Row To Row)")) {
                            tldRowToRowModelList = allTypeModelList.get(i).getDeviceModelList();
                        }
                        if (allTypeModelList.get(i).getDeviceName().equals("TLD(Hole To Hole)")) {
                            tldHoleToHoleModelList = allTypeModelList.get(i).getDeviceModelList();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void setDataList() {
        getSavedInitiatingDataFromDb();

        InitiatingDataDao initiatingDataDao = appDatabase.initiatingDataDao();
        List<InitiatingDataEntity> initiatingDataEntities = initiatingDataDao.getAllBladesProject();
        if (!Constants.isListEmpty(initiatingDataEntities)) {
            clearList();
            JsonArray array = new JsonArray();
            if (!new Gson().fromJson(initiatingDataEntities.get(0).getData(), JsonElement.class).isJsonArray()) {
                array = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(initiatingDataEntities.get(0).getData(), JsonObject.class).get("data"), String.class), JsonArray.class);
            } else {
                array = new Gson().fromJson(initiatingDataEntities.get(0).getData(), JsonArray.class);
            }
            for (JsonElement jsonElement : array) {
                responseInitiatingDataList.add(new Gson().fromJson(new Gson().fromJson(jsonElement, JsonObject.class), ResultsetItem.class));
            }
//            responseInitiatingDataList.addAll(new Gson().fromJson(initiatingDataEntities.get(0).getData(), new TypeToken<List<ResultsetItem>>(){}.getType()));
        }

        electronicDetonatorAdapter = new ElectronicDetonatorAdapter(this, electronicDetonatorModelList, responseInitiatingDataList);
        binding.electronicDetonatorList.setLayoutManager(new LinearLayoutManager(this));
        binding.electronicDetonatorList.setAdapter(electronicDetonatorAdapter);

        downTheHoleAdapter = new DownTheHoleAdapter(this, downTheHoleModelList, responseInitiatingDataList);
        binding.downTheHoleList.setLayoutManager(new LinearLayoutManager(this));
        binding.downTheHoleList.setAdapter(downTheHoleAdapter);

        tldRowToRowAdapter = new TldRowToRowAdapter(this, tldRowToRowModelList, responseInitiatingDataList);
        binding.tldRowToRowList.setLayoutManager(new LinearLayoutManager(this));
        binding.tldRowToRowList.setAdapter(tldRowToRowAdapter);

        tldHoleToHoleAdapter = new TldHoleToHoleAdapter(this, tldHoleToHoleModelList, responseInitiatingDataList);
        binding.tlsHoleToHoleList.setLayoutManager(new LinearLayoutManager(this));
        binding.tlsHoleToHoleList.setAdapter(tldHoleToHoleAdapter);

    }

    private void setEleDetListNotify() {
        electronicDetonatorModelList.add(new InitiatingDeviceModel());
        electronicDetonatorAdapter.notifyItemChanged(electronicDetonatorModelList.size() - 1);
    }

    private void setDownTheHoleNotify() {
        downTheHoleModelList.add(new InitiatingDeviceModel());
        downTheHoleAdapter.notifyItemChanged(downTheHoleModelList.size() - 1);
    }

    private void setTldRowToRowListNotify() {
        tldRowToRowModelList.add(new InitiatingDeviceModel());
        tldRowToRowAdapter.notifyItemChanged(tldRowToRowModelList.size() - 1);
    }

    private void setTlsHoleToHoleListNotify() {
        tldHoleToHoleModelList.add(new InitiatingDeviceModel());
        tldHoleToHoleAdapter.notifyItemChanged(tldHoleToHoleModelList.size() - 1);
    }

    @Override
    protected void onDestroy() {
        removeDuplicateItem();
        super.onDestroy();
    }

    private void removeDuplicateItem() {
        List<InitiatingDeviceAllTypeModel> modelList = new ArrayList<>();
        for (InitiatingDeviceAllTypeModel model : initiatingDeviceAllTypeModelList) {
            boolean isFound = false;
            for (InitiatingDeviceAllTypeModel abc : modelList) {
                if (abc.getDeviceName().equals(model.getDeviceName())) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                InitiatingDeviceAllTypeModel typeModel = new InitiatingDeviceAllTypeModel();
                typeModel.setDeviceName(model.getDeviceName());
                modelList.add(typeModel);
            }
        }

        List<InitiatingDeviceAllTypeModel> selectionModelList = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            List<InitiatingDeviceModel> deviceModelList = new ArrayList<>();
            for (int j = 0; j < initiatingDeviceAllTypeModelList.size(); j++) {
                if (modelList.get(i).getDeviceName().equals(initiatingDeviceAllTypeModelList.get(j).getDeviceName())) {
                    deviceModelList.addAll(initiatingDeviceAllTypeModelList.get(j).getDeviceModelList());
                    break;
                }
            }
            if (deviceModelList.size() > 0) {
                InitiatingDeviceAllTypeModel typeModel = new InitiatingDeviceAllTypeModel();
                typeModel.setDeviceName(modelList.get(i).getDeviceName());
                typeModel.addDeviceModelList(deviceModelList);
                selectionModelList.add(typeModel);
            }
        }

        if (Constants.isListEmpty(selectionModelList)) {
            selectionModelList = dbSavedDataList;
        }

        if (appDatabase.initiatingDeviceDao().isExistItem(designId)) {
            appDatabase.initiatingDeviceDao().updateItem(designId, new Gson().toJson(selectionModelList));
        } else {
            InitiatingDeviceDataEntity entity = new InitiatingDeviceDataEntity();
            entity.setData(new Gson().toJson(selectionModelList));
            entity.setDesignId(designId);
            appDatabase.initiatingDeviceDao().insertItem(entity);
        }

        Log.e("Initiating Device : ", "Data added successfully");

    }

}
