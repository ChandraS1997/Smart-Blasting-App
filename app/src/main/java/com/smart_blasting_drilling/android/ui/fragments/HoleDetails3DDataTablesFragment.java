package com.smart_blasting_drilling.android.ui.fragments;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseLoginData;
import com.smart_blasting_drilling.android.api.apis.response.TableFieldItemModel;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable3DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable7DesignElementDataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetails3dTableBinding;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetailsTableBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.OnDataEditTable;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.TableView3dAdapter;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleDetails3DDataTablesFragment extends BaseFragment implements OnDataEditTable, HoleDetail3DModelActivity.RowItemDetail {

    FragmentHoleDetails3dTableBinding binding;
    TableView3dAdapter tableViewAdapter;
    List<TableFieldItemModel> tableFieldItemModelList = new ArrayList<>();
    List<TableEditModel> tableEditModelArrayList = new ArrayList<>();

    List<Response3DTable1DataModel> bladesRetrieveData;
    List<Response3DTable4HoleChargingDataModel> allTablesData;
    public List<Response3DTable1DataModel> response3DTable1DataModels = new ArrayList<>();
    public List<Response3DTable2DataModel> response3DTable2DataModels = new ArrayList<>();
    public List<Response3DTable3DataModel> response3DTable3DataModels = new ArrayList<>();
    public List<Response3DTable4HoleChargingDataModel> response3DTable4HoleChargingDataModels = new ArrayList<>();
    public List<Response3DTable7DesignElementDataModel> response3DTable7DesignElementDataModels = new ArrayList<>();

    List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();

    ProjectHoleDetailRowColDao entity;

    @Override
    public void onResume() {
        super.onResume();
        ((HoleDetail3DModelActivity) mContext).rowItemDetail = this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    ((HoleDetail3DModelActivity) mContext).finish();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hole_details_3d_table, container, false);
            clearTable();
            bladesRetrieveData = ((HoleDetail3DModelActivity) mContext).bladesRetrieveData;
            allTablesData = ((HoleDetail3DModelActivity) mContext).allTablesData;

            entity = appDatabase.projectHoleDetailRowColDao();

            Constants.onDataEditTable = this;

            tableEditModelArrayList.clear();
            tableEditModelArrayList.addAll(((HoleDetail3DModelActivity) mContext).getTableModel());
            tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));

            holeDetailDataList.add(null);

            tableViewAdapter = new TableView3dAdapter(mContext, tableFieldItemModelList, holeDetailDataList);
            binding.tableRv.setAdapter(tableViewAdapter);

            if (Constants.isListEmpty(allTablesData)) {
                getAllDesignInfoApiCaller(bladesRetrieveData.get(0).isIs3dBlade());
            } else {
                /*ProjectHoleDetailRowColEntity rowColEntity = entity.getAllBladesProject(bladesRetrieveData.getDesignId());
                AllTablesData tablesData = new AllTablesData();
                Type typeList = new TypeToken<List<ResponseHoleDetailData>>(){}.getType();
                tablesData.setTable2(new Gson().fromJson(rowColEntity.projectHole, typeList));*/
                holeDetailDataList.clear();
                holeDetailDataList.add(null);
                setTableData(allTablesData, false);
            }

            int count = 0;
            for (int i = 0; i < ((HoleDetail3DModelActivity) mContext).getTableModel().size(); i++) {
                if (((HoleDetail3DModelActivity) mContext).getTableModel().get(i).isSelected()) {
                    count++;
                }
            }
            if (((HoleDetail3DModelActivity) mContext).isTableHeaderFirstTimeLoad) {
                count = ((HoleDetail3DModelActivity) mContext).getTableModel().size();
            }
            setWidthOfRv(count);
        }
        return binding.getRoot();
    }

    private void setWidthOfRv(int size) {
        int dp2px = dp2px(mContext.getResources(),((HoleDetail3DModelActivity) mContext).getTableModel().size() * 300);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(size * 300, RecyclerView.LayoutParams.WRAP_CONTENT);
        binding.tableRv.setLayoutParams(layoutParams);
    }

    public void setTableData(List<Response3DTable4HoleChargingDataModel> tablesData, boolean isFromUpdateAdapter) {
        if (tablesData != null) {
            if (!Constants.isListEmpty(tablesData)) {
                binding.noHoleDataAvailableMsg.setVisibility(View.GONE);
                binding.horizontalScrollView.setVisibility(View.VISIBLE);
                List<Response3DTable4HoleChargingDataModel> holeDetailData = new ArrayList<>();
                for (int i = 0; i < tablesData.size(); i++) {
                    if (String.valueOf(tablesData.get(i).getRowNo()).equals(String.valueOf(((HoleDetail3DModelActivity) mContext).rowPageVal))) {
                        holeDetailData.add(tablesData.get(i));
                    }
                }
                holeDetailDataList.clear();
                holeDetailDataList.add(null);
                holeDetailDataList.addAll(holeDetailData);
                ((HoleDetail3DModelActivity) mContext).holeDetailDataList.clear();
                ((HoleDetail3DModelActivity) mContext).holeDetailDataList.addAll(tablesData);
                if (!isFromUpdateAdapter)
                    setDataNotifyList(true);
            } else {
                binding.noHoleDataAvailableMsg.setVisibility(View.VISIBLE);
                binding.horizontalScrollView.setVisibility(View.GONE);
            }
        } else {
            binding.noHoleDataAvailableMsg.setVisibility(View.VISIBLE);
            binding.horizontalScrollView.setVisibility(View.GONE);
        }
    }

    @Override
    public void editDataTable(List<TableEditModel> arrayList, boolean fromPref) {
        List<TableEditModel> models = manger.get3dTableField();
        if (!Constants.isListEmpty(models)) {
            int selectedCount = 0;
            for (int i = 0; i < models.size(); i++) {
                TableEditModel tableEditModel = models.get(i);
                tableEditModel.setSelected(models.get(i).isSelected());
                if (fromPref) {
                    if (models.get(i).isSelected())
                        selectedCount++;
                } else {
                    selectedCount++;
                }
//                tableEditModel.setFirstTime(false);
                tableEditModelArrayList.set(i, tableEditModel);
            }
            setDataNotifyList(false);
            setWidthOfRv(selectedCount);
        }
    }

    public void getAllDesignInfoApiCaller(boolean is3d) {
        showLoader();
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getAll2D_3DDesignInfoApiCaller(mContext, loginData.getUserid(), loginData.getCompanyid(), bladesRetrieveData.get(0).getDesignId(), "dev_centralmineinfo", 0, is3d).observe((LifecycleOwner) mContext, new Observer<JsonElement>() {
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
                                    clearTable();
                                    JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) response).get("GetAll3DDesignInfoResult").getAsJsonPrimitive(), String.class), JsonArray.class);
                                    for (JsonElement element : new Gson().fromJson(new Gson().fromJson(array.get(0), String.class), JsonArray.class)) {
                                        response3DTable1DataModels.add(new Gson().fromJson(element, Response3DTable1DataModel.class));
                                    }
                                    for (JsonElement element : new Gson().fromJson(new Gson().fromJson(array.get(1), String.class), JsonArray.class)) {
                                        response3DTable2DataModels.add(new Gson().fromJson(element, Response3DTable2DataModel.class));
                                    }
                                    for (JsonElement element : new Gson().fromJson(new Gson().fromJson(array.get(2), String.class), JsonArray.class)) {
                                        response3DTable3DataModels.add(new Gson().fromJson(element, Response3DTable3DataModel.class));
                                    }
                                    JsonArray jsonArray = new JsonArray();
                                    if (array.get(3) instanceof JsonArray) {
                                        jsonArray = new Gson().fromJson(array.get(3), JsonArray.class);
                                    } else {
                                        jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(3), String.class), JsonArray.class);
                                    }
                                    for (JsonElement element : jsonArray) {
                                        response3DTable4HoleChargingDataModels.add(new Gson().fromJson(element, Response3DTable4HoleChargingDataModel.class));
                                    }
                                    for (JsonElement element : new Gson().fromJson(new Gson().fromJson(array.get(6), String.class), JsonArray.class)) {
                                        response3DTable7DesignElementDataModels.add(new Gson().fromJson(element, Response3DTable7DesignElementDataModel.class));
                                    }
                                    AppDelegate.getInstance().setHoleChargingDataModel(response3DTable4HoleChargingDataModels);
                                    AppDelegate.getInstance().setResponse3DTable1DataModel(response3DTable1DataModels);
                                    AppDelegate.getInstance().setResponse3DTable2DataModel(response3DTable2DataModels);
                                    AppDelegate.getInstance().setResponse3DTable3DataModel(response3DTable3DataModels);
                                    AppDelegate.getInstance().setDesignElementDataModel(response3DTable7DesignElementDataModels);

                                    setDataIntoDb(response);
                                    setTableData(response3DTable4HoleChargingDataModels, false);
                                } catch (Exception e) {
                                    e.getLocalizedMessage();
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

    private void clearTable() {
        response3DTable1DataModels.clear();
        response3DTable2DataModels.clear();
        response3DTable3DataModels.clear();
        response3DTable4HoleChargingDataModels.clear();
        response3DTable7DesignElementDataModels.clear();
    }

    private void setDataIntoDb(JsonElement element) {
        String str = new Gson().toJson(element);
        if (!entity.isExistProject(bladesRetrieveData.get(0).getDesignId())) {
            entity.insertProject(new ProjectHoleDetailRowColEntity(bladesRetrieveData.get(0).getDesignId(), bladesRetrieveData.get(0).isIs3dBlade(), str));
        } else {
            entity.updateProject(bladesRetrieveData.get(0).getDesignId(), str);
        }
    }

    private void setDataNotifyList(boolean update) {
        try {
            tableFieldItemModelList.clear();
            tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int i = 1; i < holeDetailDataList.size(); i++) {
                    List<TableEditModel> editModelArrayList = new ArrayList<>();
                    Response3DTable4HoleChargingDataModel holeDetailData = holeDetailDataList.get(i);
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getRowNo()), tableEditModelArrayList.get(0).getTitleVal(), tableEditModelArrayList.get(0).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleNo()), tableEditModelArrayList.get(1).getTitleVal(), tableEditModelArrayList.get(1).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleID()), tableEditModelArrayList.get(2).getTitleVal(), tableEditModelArrayList.get(2).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(StringUtill.isEmpty(holeDetailData.getHoleStatus()) ? "1" : holeDetailData.getHoleDepth()), tableEditModelArrayList.get(3).getTitleVal(), tableEditModelArrayList.get(3).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(StringUtill.isEmpty(holeDetailData.getHoleDepth()) ? "" : holeDetailData.getHoleDepth()), tableEditModelArrayList.get(4).getTitleVal(), tableEditModelArrayList.get(4).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleStatus()), tableEditModelArrayList.get(5).getTitleVal(), tableEditModelArrayList.get(5).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getVerticalDip()), tableEditModelArrayList.get(6).getTitleVal(), tableEditModelArrayList.get(6).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleDiameter()), tableEditModelArrayList.get(7).getTitleVal(), tableEditModelArrayList.get(7).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBurden()), tableEditModelArrayList.get(8).getTitleVal(), tableEditModelArrayList.get(8).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getSpacing()), tableEditModelArrayList.get(9).getTitleVal(), tableEditModelArrayList.get(9).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getSubgrade()), tableEditModelArrayList.get(10).getTitleVal(), tableEditModelArrayList.get(10).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopX().equals("0.0") ? "" : holeDetailData.getTopX()), tableEditModelArrayList.get(11).getTitleVal(), tableEditModelArrayList.get(11).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopY().equals("0.0") ? "" : holeDetailData.getTopY()), tableEditModelArrayList.get(12).getTitleVal(), tableEditModelArrayList.get(12).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopZ().equals("0.0") ? "" : holeDetailData.getTopZ()), tableEditModelArrayList.get(13).getTitleVal(), tableEditModelArrayList.get(13).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomX().equals("0.0") ? "" : holeDetailData.getBottomX()), tableEditModelArrayList.get(14).getTitleVal(), tableEditModelArrayList.get(14).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomY().equals("0.0") ? "" : holeDetailData.getBottomY()), tableEditModelArrayList.get(15).getTitleVal(), tableEditModelArrayList.get(15).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomZ().equals("0.0") ? "" : holeDetailData.getBottomZ()), tableEditModelArrayList.get(16).getTitleVal(), tableEditModelArrayList.get(16).isSelected(), update));

                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTotalCharge()), tableEditModelArrayList.get(17).getTitleVal(), tableEditModelArrayList.get(17).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getChargeLength()), tableEditModelArrayList.get(18).getTitleVal(), tableEditModelArrayList.get(18).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getStemmingLength()), tableEditModelArrayList.get(19).getTitleVal(), tableEditModelArrayList.get(19).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getDeckLength()), tableEditModelArrayList.get(20).getTitleVal(), tableEditModelArrayList.get(20).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBlock()), tableEditModelArrayList.get(21).getTitleVal(), tableEditModelArrayList.get(21).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBlockLength()), tableEditModelArrayList.get(22).getTitleVal(), tableEditModelArrayList.get(22).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getInHoleDelay()), tableEditModelArrayList.get(23).getTitleVal(), tableEditModelArrayList.get(23).isSelected(), update));
                    if (!Constants.isListEmpty(holeDetailData.getChargeTypeArray()))
                        editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getChargeTypeArray().size()), tableEditModelArrayList.get(24).getTitleVal(), tableEditModelArrayList.get(24).isSelected(), update));
                    else
                        editModelArrayList.add(new TableEditModel(String.valueOf(0), tableEditModelArrayList.get(24).getTitleVal(), tableEditModelArrayList.get(24).isSelected(), update));

                    if (!update) {
                        if (tableFieldItemModelList.size() > i) {
                            tableFieldItemModelList.set(i, new TableFieldItemModel(editModelArrayList));
                        } else {
                            tableFieldItemModelList.add(new TableFieldItemModel(editModelArrayList));
                        }
                    } else {
                        tableFieldItemModelList.add(new TableFieldItemModel(editModelArrayList));
                    }
                }
            }
            tableViewAdapter = new TableView3dAdapter(mContext, tableFieldItemModelList, holeDetailDataList);
            binding.tableRv.setAdapter(tableViewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRowOfTable(int rowNo, List<Response3DTable4HoleChargingDataModel> allTablesData, boolean isFromUpdateAdapter) {
        setTableData(allTablesData, isFromUpdateAdapter);
    }
}