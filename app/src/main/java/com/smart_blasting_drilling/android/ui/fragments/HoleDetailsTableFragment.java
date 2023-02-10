package com.smart_blasting_drilling.android.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseLoginData;
import com.smart_blasting_drilling.android.api.apis.response.TableFieldItemModel;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.OnDataEditTable;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.ui.activity.AuthActivity;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.activity.HomeActivity;
import com.smart_blasting_drilling.android.ui.adapter.TableViewAdapter;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetailsTableBinding;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HoleDetailsTableFragment extends BaseFragment implements OnDataEditTable {

    FragmentHoleDetailsTableBinding binding;
    TableViewAdapter tableViewAdapter;
    List<TableFieldItemModel> tableFieldItemModelList = new ArrayList<>();
    List<TableEditModel> tableEditModelArrayList = new ArrayList<>();
    ResponseBladesRetrieveData bladesRetrieveData;
    List<ResponseHoleDetailData> holeDetailDataList = new ArrayList<>();

    AppDatabase appDatabase;
    ProjectHoleDetailRowColDao entity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    ((HoleDetailActivity) mContext).finish();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hole_details_table, container, false);

            bladesRetrieveData = ((HoleDetailActivity) mContext).bladesRetrieveData;

            appDatabase = BaseApplication.getAppDatabase(mContext, Constants.DATABASE_NAME);
            entity = appDatabase.projectHoleDetailRowColDao();

            Constants.onDataEditTable = this;

            tableEditModelArrayList.clear();
            tableEditModelArrayList.addAll(((HoleDetailActivity) mContext).getTableModel());
            tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));

            holeDetailDataList.add(null);

            tableViewAdapter = new TableViewAdapter(mContext, tableFieldItemModelList, holeDetailDataList);
            binding.tableRv.setAdapter(tableViewAdapter);

//            if (!entity.isExistProject(bladesRetrieveData.getDesignId())) {
                getAllDesignInfoApiCaller();
           /* } else {
                ProjectHoleDetailRowColEntity rowColEntity = entity.getAllBladesProject(bladesRetrieveData.getDesignId());
                AllTablesData tablesData = new AllTablesData();
                Type typeList = new TypeToken<List<ResponseHoleDetailData>>(){}.getType();
                tablesData.setTable2(new Gson().fromJson(rowColEntity.projectHole, typeList));
                setTableData(tablesData);
            }*/
        }
        return binding.getRoot();
    }

    private void setTableData(AllTablesData tablesData) {
        holeDetailDataList.clear();
        holeDetailDataList.add(null);
        holeDetailDataList.addAll(tablesData.getTable2());
        ((HoleDetailActivity) mContext).holeDetailDataList.clear();
        ((HoleDetailActivity) mContext).holeDetailDataList.addAll(tablesData.getTable2());
        setDataNotifyList();
    }

    @Override
    public void editDataTable(List<TableEditModel> arrayList) {
        if (!Constants.isListEmpty(arrayList)) {
            for (int i = 0; i < arrayList.size(); i++) {
                TableEditModel tableEditModel = arrayList.get(i);
                tableEditModel.setSelected(arrayList.get(i).isSelected());
                tableEditModel.setFirstTime(false);
                tableEditModelArrayList.set(i, tableEditModel);
            }
            if (!Constants.isListEmpty(arrayList)) {
                tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));
            }
        }
        setDataNotifyList();
    }

    public void getAllDesignInfoApiCaller() {
        showLoader();
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getAllDesignInfoApiCaller(mContext, loginData.getUserid(), loginData.getCompanyid(), bladesRetrieveData.getDesignId(), "dev_centralmineinfo", 0).observe((LifecycleOwner) mContext, new Observer<JsonElement>() {
            @Override
            public void onChanged(JsonElement response) {
                if (response == null) {
                    showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
                } else {
                    if (!(response.isJsonNull())) {
                        try {
                            JsonObject jsonObject = response.getAsJsonObject();
                            if (jsonObject != null) {
                                try {
                                    if (jsonObject.get("GetAllDesignInfoResult").getAsString().contains("Table2")) {
                                        AllTablesData tablesData = new Gson().fromJson(jsonObject.get("GetAllDesignInfoResult").getAsString(), AllTablesData.class);
                                        setTableData(tablesData);
                                        setDataIntoDb();
                                    }
                                } catch (Exception e) {
                                    Log.e(NODATAFOUND, e.getMessage());
                                }

                            } else {
                                ((BaseActivity) requireActivity()).showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                            }
                        } catch (Exception e) {
                            Log.e(NODATAFOUND, e.getMessage());
                        }
                        hideLoader();
                    }
                }
                hideLoader();
            }
        });
    }

    private void setDataIntoDb() {
        String str = new Gson().toJson(holeDetailDataList);
        if (!entity.isExistProject(bladesRetrieveData.getDesignId())) {
            entity.insertProject(new ProjectHoleDetailRowColEntity(bladesRetrieveData.getDesignId(), str));
        } else {
            entity.updateProject(new ProjectHoleDetailRowColEntity(bladesRetrieveData.getDesignId(), str));
        }
    }

    private void setDataNotifyList() {
        try {
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int i = 1; i < holeDetailDataList.size(); i++) {
                    List<TableEditModel> editModelArrayList = new ArrayList<>();
                    ResponseHoleDetailData holeDetailData = holeDetailDataList.get(i);
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getRowNo()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleNo()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleID()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleDepth()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel("Hole Status", tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleAngle()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleDiameter()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBurden()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getSpacing()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getX()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getY()), tableEditModelArrayList.get(0).isSelected()));
                    editModelArrayList.add(new TableEditModel("Z", tableEditModelArrayList.get(0).isSelected()));
                    int chargingCount = 0;
                    if (!StringUtill.isEmpty(holeDetailData.getColName())) {
                        chargingCount = chargingCount + 1;
                    }
                    if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getBtmName()))) {
                        chargingCount = chargingCount + 1;
                    }
                    if (!StringUtill.isEmpty(holeDetailData.getBsterName())) {
                        chargingCount = chargingCount + 1;
                    }
                    if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getStemLngth()))) {
                        chargingCount = chargingCount + 1;
                    }
                    editModelArrayList.add(new TableEditModel(String.valueOf(chargingCount), tableEditModelArrayList.get(0).isSelected()));

                    tableFieldItemModelList.add(new TableFieldItemModel(editModelArrayList));
                }
            }
            Log.e("Data : ", new Gson().toJson(tableFieldItemModelList));
            tableViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}