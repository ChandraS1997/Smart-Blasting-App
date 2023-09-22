package com.mineexcellence.sblastingapp.ui.fragments;

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
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.Service.MainService;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseLoginData;
import com.mineexcellence.sblastingapp.api.apis.response.TableFieldItemModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable17DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable3DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable7DesignElementDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.HoleDetailItem;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.Response3DTable18PreSpilitDataModel;
import com.mineexcellence.sblastingapp.app.AppDelegate;
import com.mineexcellence.sblastingapp.app.BaseFragment;
import com.mineexcellence.sblastingapp.databinding.FragmentHoleDetails3dTableBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.interfaces.OnDataEditTable;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.mineexcellence.sblastingapp.room_database.entities.ProjectHoleDetailRowColEntity;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.ui.adapter.TableView3dAdapter;
import com.mineexcellence.sblastingapp.ui.adapter.pilot_adapter.TableViewPilotAdapter;
import com.mineexcellence.sblastingapp.ui.adapter.pre_split_adapter.TableViewPreSplitAdapter;
import com.mineexcellence.sblastingapp.ui.models.TableEditModel;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleDetails3DDataTablesFragment extends BaseFragment implements OnDataEditTable, HoleDetail3DModelActivity.RowItemDetail, HoleDetail3DModelActivity.TableHoleDataListCallback {

    FragmentHoleDetails3dTableBinding binding;
    TableView3dAdapter tableViewAdapter;
    TableViewPilotAdapter tableViewPilotAdapter;
    TableViewPreSplitAdapter tableViewPreSplitAdapter;
    List<TableFieldItemModel> tableFieldItemModelList = new ArrayList<>();
    List<TableEditModel> tableEditModelArrayList = new ArrayList<>();

    List<Response3DTable1DataModel> bladesRetrieveData;
    List<Response3DTable2DataModel> response3DTable2Data;
    List<Response3DTable4HoleChargingDataModel> allTablesData;
    List<Response3DTable16PilotDataModel> pilotDataModelListData;
    List<Response3DTable18PreSpilitDataModel> preSpilitDataModelListData;
    public List<Response3DTable1DataModel> response3DTable1DataModels = new ArrayList<>();
    public List<Response3DTable2DataModel> response3DTable2DataModels = new ArrayList<>();
    public List<Response3DTable3DataModel> response3DTable3DataModels = new ArrayList<>();
    public List<Response3DTable4HoleChargingDataModel> response3DTable4HoleChargingDataModels = new ArrayList<>();
    public List<Response3DTable7DesignElementDataModel> response3DTable7DesignElementDataModels = new ArrayList<>();
    public List<Response3DTable17DataModel> response3DTable17DataModelList = new ArrayList<>();

    List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
    List<Response3DTable16PilotDataModel> holePilotDetailDataList = new ArrayList<>();
    List<Response3DTable18PreSpilitDataModel> holePreSplitDetailDataList = new ArrayList<>();

    ProjectHoleDetailRowColDao entity;
    int tableOfList = 0;

    @Override
    public void onResume() {
        super.onResume();
        ((HoleDetail3DModelActivity) mContext).rowItemDetail = this;
        ((HoleDetail3DModelActivity) mContext).tableHoleDataListCallback = this;
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
            pilotDataModelListData = ((HoleDetail3DModelActivity) mContext).pilotTableData;
            if (preSpilitDataModelListData != null)
                preSpilitDataModelListData.clear();
            preSpilitDataModelListData = ((HoleDetail3DModelActivity) mContext).preSplitTableData;
            response3DTable2Data = ((HoleDetail3DModelActivity) mContext).response3DTable2DataModelList;
            response3DTable17DataModelList = AppDelegate.getInstance().getResponse3DTable17DataModelList();

            entity = appDatabase.projectHoleDetailRowColDao();

            holeDataTableType(((HoleDetail3DModelActivity) mContext).tableTypeVal);
        }
        return binding.getRoot();
    }

    public void setNormalTableData() {
        Constants.onDataEditTable = this;

        tableEditModelArrayList.clear();
        tableFieldItemModelList.clear();
        tableEditModelArrayList.addAll(((HoleDetail3DModelActivity) mContext).getTableModel());
        tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));

        holeDetailDataList.add(null);

        tableViewAdapter = new TableView3dAdapter(mContext, tableFieldItemModelList, holeDetailDataList);
        binding.tableRv.setAdapter(tableViewAdapter);

        if (Constants.isListEmpty(allTablesData)) {
            getAllDesignInfoApiCaller(bladesRetrieveData.get(0).isIs3dBlade());
        } else {
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

    public void setPreSplitTableData() {
        Constants.onDataEditTable = this;

        tableEditModelArrayList.clear();
        tableFieldItemModelList.clear();
        tableEditModelArrayList.addAll(((HoleDetail3DModelActivity) mContext).getPreSplitTableModel());
        tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));

        /*tableViewPreSplitAdapter = new TableViewPreSplitAdapter(mContext, tableFieldItemModelList, preSpilitDataModelListData.get(0).getHoleDetail());
        binding.tableRv.setAdapter(tableViewPreSplitAdapter);*/

        if (Constants.isListEmpty(preSpilitDataModelListData)) {
            getAllDesignInfoApiCaller(bladesRetrieveData.get(0).isIs3dBlade());
        } else {
            holePreSplitDetailDataList.clear();
            setPreSplitTableData(preSpilitDataModelListData, false);
        }

        int count = 0;
        for (int i = 0; i < ((HoleDetail3DModelActivity) mContext).getPreSplitTableModel().size(); i++) {
            if (((HoleDetail3DModelActivity) mContext).getPreSplitTableModel().get(i).isSelected()) {
                count++;
            }
        }
        if (((HoleDetail3DModelActivity) mContext).isTableHeaderFirstTimeLoad) {
            count = ((HoleDetail3DModelActivity) mContext).getPreSplitTableModel().size();
        }
        setWidthOfRv(count);
    }

    public void setPilotTableData() {
        Constants.onDataEditTable = this;

        tableEditModelArrayList.clear();
        tableFieldItemModelList.clear();
        tableEditModelArrayList.addAll(((HoleDetail3DModelActivity) mContext).getPilotTableModel());
        tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));

        /*tableViewPilotAdapter = new TableViewPilotAdapter(mContext, tableFieldItemModelList, pilotDataModelListData);
        binding.tableRv.setAdapter(tableViewPilotAdapter);*/

        if (Constants.isListEmpty(pilotDataModelListData)) {
            getAllDesignInfoApiCaller(bladesRetrieveData.get(0).isIs3dBlade());
        } else {
            holePilotDetailDataList.clear();
            setPilotTableData(pilotDataModelListData, false);
        }

        int count = 0;
        for (int i = 0; i < ((HoleDetail3DModelActivity) mContext).getPilotTableModel().size(); i++) {
            if (((HoleDetail3DModelActivity) mContext).getPilotTableModel().get(i).isSelected()) {
                count++;
            }
        }
        if (((HoleDetail3DModelActivity) mContext).isTableHeaderFirstTimeLoad) {
            count = ((HoleDetail3DModelActivity) mContext).getPilotTableModel().size();
        }
        setWidthOfRv(count);
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
                    notifyHoleDataTableType(Constants.NORMAL, true);
            } else {
                binding.noHoleDataAvailableMsg.setVisibility(View.VISIBLE);
                binding.horizontalScrollView.setVisibility(View.GONE);
            }
        } else {
            binding.noHoleDataAvailableMsg.setVisibility(View.VISIBLE);
            binding.horizontalScrollView.setVisibility(View.GONE);
        }
    }

    public void setPilotTableData(List<Response3DTable16PilotDataModel> tablesData, boolean isFromUpdateAdapter) {
        if (tablesData != null) {
            if (!Constants.isListEmpty(tablesData)) {
                binding.noHoleDataAvailableMsg.setVisibility(View.GONE);
                binding.horizontalScrollView.setVisibility(View.VISIBLE);

                holePilotDetailDataList.clear();
                holePilotDetailDataList.add(null);
                holePilotDetailDataList.addAll(tablesData);

                if (!isFromUpdateAdapter)
                    notifyHoleDataTableType(Constants.PILOT, true);
            } else {
                binding.noHoleDataAvailableMsg.setVisibility(View.VISIBLE);
                binding.horizontalScrollView.setVisibility(View.GONE);
            }
        } else {
            binding.noHoleDataAvailableMsg.setVisibility(View.VISIBLE);
            binding.horizontalScrollView.setVisibility(View.GONE);
        }
    }

    public void setPreSplitTableData(List<Response3DTable18PreSpilitDataModel> tablesData, boolean isFromUpdateAdapter) {
        if (tablesData != null) {
            if (!Constants.isListEmpty(tablesData)) {
                binding.noHoleDataAvailableMsg.setVisibility(View.GONE);
                binding.horizontalScrollView.setVisibility(View.VISIBLE);

                holePreSplitDetailDataList.clear();
                holePreSplitDetailDataList.add(null);
                holePreSplitDetailDataList.addAll(tablesData);

                if (!isFromUpdateAdapter)
                    notifyHoleDataTableType(Constants.PRE_SPLIT, true);
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
        List<TableEditModel> models = arrayList;
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
                tableEditModelArrayList.set(i, tableEditModel);
            }
            setWidthOfRv(selectedCount);
            notifyHoleDataTableType(tableOfList, false);
        }
    }

    @Override
    public void pilotEditDataTable(List<TableEditModel> arrayList, boolean fromPref) {
        List<TableEditModel> models = arrayList;
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
                tableEditModelArrayList.set(i, tableEditModel);
            }
            setWidthOfRv(selectedCount);
            notifyHoleDataTableType(tableOfList, false);
        }
    }

    @Override
    public void preSplitEditDataTable(List<TableEditModel> arrayList, boolean fromPref) {
        List<TableEditModel> models = arrayList;
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
                tableEditModelArrayList.set(i, tableEditModel);
            }
            setWidthOfRv(selectedCount);
            notifyHoleDataTableType(tableOfList, false);
        }
    }

    public void getAllDesignInfoApiCaller(boolean is3d) {
        showLoader();
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getAll2D_3DDesignInfoApiCaller(mContext, loginData.getUserid(), loginData.getCompanyid(), bladesRetrieveData.get(0).getDesignId(), Constants.DB_NAME, 0, is3d).observe((LifecycleOwner) mContext, new Observer<JsonElement>() {
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
                                    // "GetAll3DDesignInfoResult" for Test
                                    JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) response).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
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
                                    if (array.get(15) instanceof JsonArray) {
                                        jsonArray = new Gson().fromJson(array.get(15), JsonArray.class);
                                    } else {
                                        jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(15), String.class), JsonArray.class);
                                    }

                                    for (JsonElement element : jsonArray) {
                                        response3DTable4HoleChargingDataModels.add(new Gson().fromJson(element, Response3DTable4HoleChargingDataModel.class));
                                    }
                                    for (JsonElement element : new Gson().fromJson(new Gson().fromJson(array.get(6), String.class), JsonArray.class)) {
                                        response3DTable7DesignElementDataModels.add(new Gson().fromJson(element, Response3DTable7DesignElementDataModel.class));
                                    }


                                    JsonArray pilotDataJsonArray = new JsonArray();
                                    if (array.get(16) instanceof JsonArray) {
                                        pilotDataJsonArray = new Gson().fromJson(array.get(16), JsonArray.class);
                                    } else {
                                        pilotDataJsonArray = new Gson().fromJson(new Gson().fromJson(array.get(16), String.class), JsonArray.class);
                                    }
                                    JsonArray preSplitDataJsonArray = new JsonArray();
                                    if (array.get(18) instanceof JsonArray) {
                                        preSplitDataJsonArray = new Gson().fromJson(array.get(18), JsonArray.class);
                                    } else {
                                        preSplitDataJsonArray = new Gson().fromJson(new Gson().fromJson(array.get(18), String.class), JsonArray.class);
                                    }

                                    for (JsonElement element : preSplitDataJsonArray) {
                                        Response3DTable18PreSpilitDataModel model = new Gson().fromJson(element, Response3DTable18PreSpilitDataModel.class);
                                        model.setHoleDetailStr(model.getHoleDetailStr());
                                        model.setHolePointsStr(model.getHolePointsStr());
                                        model.setLinePointsStr(model.getLinePointsStr());
                                        preSpilitDataModelListData.add(model);
                                    }
                                    for (JsonElement element : pilotDataJsonArray) {
                                        Response3DTable16PilotDataModel model = new Gson().fromJson(element, Response3DTable16PilotDataModel.class);
                                        model.setChargeTypeArrayStr(model.getChargeTypeArrayStr());
                                        pilotDataModelListData.add(model);
                                    }

                                    AppDelegate.getInstance().setHoleChargingDataModel(response3DTable4HoleChargingDataModels);
                                    AppDelegate.getInstance().setResponse3DTable1DataModel(response3DTable1DataModels);
                                    AppDelegate.getInstance().setResponse3DTable2DataModel(response3DTable2DataModels);
                                    AppDelegate.getInstance().setResponse3DTable3DataModel(response3DTable3DataModels);
                                    AppDelegate.getInstance().setDesignElementDataModel(response3DTable7DesignElementDataModels);
                                    AppDelegate.getInstance().setPilotDataModelList(pilotDataModelListData);
                                    AppDelegate.getInstance().setPreSpilitDataModelList(preSpilitDataModelListData);

                                    setDataIntoDb(response);

                                    holeDataTableType(((HoleDetail3DModelActivity) mContext).tableTypeVal);
//                                    setTableData(response3DTable4HoleChargingDataModels, false);
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
        response3DTable17DataModelList.clear();
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
                    editModelArrayList.add(new TableEditModel(String.valueOf(getHoleType(String.valueOf(holeDetailData.getHoleType()))), tableEditModelArrayList.get(3).getTitleVal(), tableEditModelArrayList.get(3).isSelected(), update));
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

                    double totalCharge = 0.0, chargeLen = 0.0;
                    if (!Constants.isListEmpty(holeDetailData.getChargeTypeArray())) {
                        for (int j = 0; j < holeDetailData.getChargeTypeArray().size(); j++) {
                            ChargeTypeArrayItem item = holeDetailData.getChargeTypeArray().get(j);
                            totalCharge = totalCharge + item.getWeight();

                            if (StringUtill.getString(item.getType()).equals("Bulk") || StringUtill.getString(item.getType()).equals("Cartridge")) {
                                chargeLen = chargeLen + item.getLength();
                            }
                        }
                    }

                    editModelArrayList.add(new TableEditModel(String.valueOf(totalCharge), tableEditModelArrayList.get(17).getTitleVal(), tableEditModelArrayList.get(17).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(chargeLen), tableEditModelArrayList.get(18).getTitleVal(), tableEditModelArrayList.get(18).isSelected(), update));

                    if (!Constants.isListEmpty(holeDetailData.getChargeTypeArray())) {
                        double length = 0, deckLen = 0;
                        for (int j = 0; j < holeDetailData.getChargeTypeArray().size(); j++) {
                            ChargeTypeArrayItem item = holeDetailData.getChargeTypeArray().get(j);
                            if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                length = length + item.getLength();
                            }
                            if (StringUtill.getString(item.getType()).equals("Decking")) {
                                deckLen = deckLen + item.getLength();
                            }
                        }
                        editModelArrayList.add(new TableEditModel(String.valueOf(length), tableEditModelArrayList.get(19).getTitleVal(), tableEditModelArrayList.get(19).isSelected(), update));
                        editModelArrayList.add(new TableEditModel(String.valueOf(deckLen), tableEditModelArrayList.get(20).getTitleVal(), tableEditModelArrayList.get(20).isSelected(), update));
                    } else {
                        editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getStemmingLength()), tableEditModelArrayList.get(19).getTitleVal(), tableEditModelArrayList.get(19).isSelected(), update));
                        editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getDeckLength()), tableEditModelArrayList.get(20).getTitleVal(), tableEditModelArrayList.get(20).isSelected(), update));
                    }

                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBlock()), tableEditModelArrayList.get(21).getTitleVal(), tableEditModelArrayList.get(21).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBlockLength()), tableEditModelArrayList.get(22).getTitleVal(), tableEditModelArrayList.get(22).isSelected(), update));

                    if (!Constants.isListEmpty(response3DTable17DataModelList)) {
                        JsonArray inHoleDelayArr = new Gson().fromJson(new Gson().fromJson(response3DTable17DataModelList.get(0).getInHoleDelayArr(), String.class), JsonArray.class);
                        if (inHoleDelayArr != null) {
                            if (inHoleDelayArr.size() > 0) {
                                editModelArrayList.add(new TableEditModel(String.valueOf(inHoleDelayArr.get(0).getAsInt()), tableEditModelArrayList.get(23).getTitleVal(), tableEditModelArrayList.get(23).isSelected(), update));
                            } else {
                                editModelArrayList.add(new TableEditModel(String.valueOf("0"), tableEditModelArrayList.get(23).getTitleVal(), tableEditModelArrayList.get(23).isSelected(), update));
                            }
                        } else {
                            editModelArrayList.add(new TableEditModel(String.valueOf("0"), tableEditModelArrayList.get(23).getTitleVal(), tableEditModelArrayList.get(23).isSelected(), update));
                        }
                    } else {
                        editModelArrayList.add(new TableEditModel(String.valueOf("0"), tableEditModelArrayList.get(23).getTitleVal(), tableEditModelArrayList.get(23).isSelected(), update));
                    }

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

    private void setDataNotifyListOfPilot(boolean update) {
        try {
            tableFieldItemModelList.clear();
            tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));
            if (!Constants.isListEmpty(holePilotDetailDataList)) {
                for (int i = 1; i < holePilotDetailDataList.size(); i++) {
                    List<TableEditModel> editModelArrayList = new ArrayList<>();
                    Response3DTable16PilotDataModel holeDetailData = holePilotDetailDataList.get(i);
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleID()), tableEditModelArrayList.get(0).getTitleVal(), tableEditModelArrayList.get(0).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(StringUtill.isEmpty(holeDetailData.getHoleDepth()) ? "" : holeDetailData.getHoleDepth()), tableEditModelArrayList.get(1).getTitleVal(), tableEditModelArrayList.get(1).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getVerticalDip()), tableEditModelArrayList.get(2).getTitleVal(), tableEditModelArrayList.get(2).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleDiameter()), tableEditModelArrayList.get(3).getTitleVal(), tableEditModelArrayList.get(3).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBurden()), tableEditModelArrayList.get(4).getTitleVal(), tableEditModelArrayList.get(4).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getSpacing()), tableEditModelArrayList.get(5).getTitleVal(), tableEditModelArrayList.get(5).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getSubgrade()), tableEditModelArrayList.get(6).getTitleVal(), tableEditModelArrayList.get(6).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopX().equals("0.0") ? "" : holeDetailData.getTopX()), tableEditModelArrayList.get(7).getTitleVal(), tableEditModelArrayList.get(7).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopY().equals("0.0") ? "" : holeDetailData.getTopY()), tableEditModelArrayList.get(8).getTitleVal(), tableEditModelArrayList.get(8).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopZ().equals("0.0") ? "" : holeDetailData.getTopZ()), tableEditModelArrayList.get(9).getTitleVal(), tableEditModelArrayList.get(9).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomX().equals("0.0") ? "" : holeDetailData.getBottomX()), tableEditModelArrayList.get(10).getTitleVal(), tableEditModelArrayList.get(10).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomY().equals("0.0") ? "" : holeDetailData.getBottomY()), tableEditModelArrayList.get(11).getTitleVal(), tableEditModelArrayList.get(11).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomZ().equals("0.0") ? "" : holeDetailData.getBottomZ()), tableEditModelArrayList.get(12).getTitleVal(), tableEditModelArrayList.get(12).isSelected(), update));

                    double totalCharge = 0.0, chargeLen = 0.0;
                    if (!Constants.isListEmpty(holeDetailData.getChargeTypeArray())) {
                        for (int j = 0; j < holeDetailData.getChargeTypeArray().size(); j++) {
                            ChargeTypeArrayItem item = holeDetailData.getChargeTypeArray().get(j);
                            totalCharge = totalCharge + item.getWeight();

                            if (StringUtill.getString(item.getType()).equals("Bulk") || StringUtill.getString(item.getType()).equals("Cartridge")) {
                                chargeLen = chargeLen + item.getLength();
                            }
                        }
                    }

                    editModelArrayList.add(new TableEditModel(String.valueOf(totalCharge), tableEditModelArrayList.get(13).getTitleVal(), tableEditModelArrayList.get(13).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(chargeLen), tableEditModelArrayList.get(14).getTitleVal(), tableEditModelArrayList.get(14).isSelected(), update));

                    if (!Constants.isListEmpty(holeDetailData.getChargeTypeArray())) {
                        double length = 0, deckLen = 0;
                        for (int j = 0; j < holeDetailData.getChargeTypeArray().size(); j++) {
                            ChargeTypeArrayItem item = holeDetailData.getChargeTypeArray().get(j);
                            if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                length = length + item.getLength();
                            }
                            if (StringUtill.getString(item.getType()).equals("Decking")) {
                                deckLen = deckLen + item.getLength();
                            }
                        }
                        editModelArrayList.add(new TableEditModel(String.valueOf(length), tableEditModelArrayList.get(15).getTitleVal(), tableEditModelArrayList.get(15).isSelected(), update));
                        editModelArrayList.add(new TableEditModel(String.valueOf(deckLen), tableEditModelArrayList.get(16).getTitleVal(), tableEditModelArrayList.get(16).isSelected(), update));
                    } else {
                        editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getStemmingLength()), tableEditModelArrayList.get(15).getTitleVal(), tableEditModelArrayList.get(15).isSelected(), update));
                        editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getDeckLength()), tableEditModelArrayList.get(16).getTitleVal(), tableEditModelArrayList.get(16).isSelected(), update));
                    }

                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBlock()), tableEditModelArrayList.get(17).getTitleVal(), tableEditModelArrayList.get(17).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBlockLength()), tableEditModelArrayList.get(18).getTitleVal(), tableEditModelArrayList.get(18).isSelected(), update));

                    if (!Constants.isListEmpty(response3DTable17DataModelList)) {
                        JsonArray inHoleDelayArr = new Gson().fromJson(new Gson().fromJson(response3DTable17DataModelList.get(0).getInHoleDelayArr(), String.class), JsonArray.class);
                        if (inHoleDelayArr != null) {
                            if (inHoleDelayArr.size() > 0) {
                                editModelArrayList.add(new TableEditModel(String.valueOf(inHoleDelayArr.get(0).getAsInt()), tableEditModelArrayList.get(19).getTitleVal(), tableEditModelArrayList.get(19).isSelected(), update));
                            } else {
                                editModelArrayList.add(new TableEditModel(String.valueOf("0"), tableEditModelArrayList.get(19).getTitleVal(), tableEditModelArrayList.get(19).isSelected(), update));
                            }
                        } else {
                            editModelArrayList.add(new TableEditModel(String.valueOf("0"), tableEditModelArrayList.get(19).getTitleVal(), tableEditModelArrayList.get(19).isSelected(), update));
                        }
                    } else {
                        editModelArrayList.add(new TableEditModel(String.valueOf("0"), tableEditModelArrayList.get(19).getTitleVal(), tableEditModelArrayList.get(19).isSelected(), update));
                    }

                    if (!Constants.isListEmpty(holeDetailData.getChargeTypeArray()))
                        editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getChargeTypeArray().size()), tableEditModelArrayList.get(20).getTitleVal(), tableEditModelArrayList.get(20).isSelected(), update));
                    else
                        editModelArrayList.add(new TableEditModel(String.valueOf(0), tableEditModelArrayList.get(20).getTitleVal(), tableEditModelArrayList.get(20).isSelected(), update));

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
            tableViewPilotAdapter = new TableViewPilotAdapter(mContext, tableFieldItemModelList, holePilotDetailDataList);
            binding.tableRv.setAdapter(tableViewPilotAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataNotifyListOfPreSplit(boolean update) {
        try {
            tableFieldItemModelList.clear();
            tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));
            if (!Constants.isListEmpty(holePreSplitDetailDataList)) {
                if (holePreSplitDetailDataList.size() > 1) {
                    if (!Constants.isListEmpty(holePreSplitDetailDataList.get(1).getHoleDetail())) {
                        if (!Constants.isListEmpty(holePreSplitDetailDataList.get(1).getHoleDetail())) {
                            List<HoleDetailItem> itemList = holePreSplitDetailDataList.get(1).getHoleDetail();
                            if (itemList.get(0) == null) {
                                itemList.remove(0);
                            }
                            holePreSplitDetailDataList.get(1).setHoleDetailStr(new Gson().toJson(itemList));
                        }
                        for (int i = 0; i < holePreSplitDetailDataList.get(1).getHoleDetail().size(); i++) {
                            List<TableEditModel> editModelArrayList = new ArrayList<>();
                            HoleDetailItem holeDetailData = holePreSplitDetailDataList.get(1).getHoleDetail().get(i);

                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleId()), tableEditModelArrayList.get(0).getTitleVal(), tableEditModelArrayList.get(0).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(getHoleType(String.valueOf(holeDetailData.getHoleType()))), tableEditModelArrayList.get(1).getTitleVal(), tableEditModelArrayList.get(1).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleDiameter()), tableEditModelArrayList.get(2).getTitleVal(), tableEditModelArrayList.get(2).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(StringUtill.isEmpty(holeDetailData.getHoleDepth()) ? "" : holeDetailData.getHoleDepth()), tableEditModelArrayList.get(3).getTitleVal(), tableEditModelArrayList.get(3).isSelected(), update));

                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopEasting()), tableEditModelArrayList.get(4).getTitleVal(), tableEditModelArrayList.get(4).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopNorthing()), tableEditModelArrayList.get(5).getTitleVal(), tableEditModelArrayList.get(5).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getTopRL()), tableEditModelArrayList.get(6).getTitleVal(), tableEditModelArrayList.get(6).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomEasting()), tableEditModelArrayList.get(7).getTitleVal(), tableEditModelArrayList.get(7).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomNorthing()), tableEditModelArrayList.get(8).getTitleVal(), tableEditModelArrayList.get(8).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBottomRL() == 0.0 ? "" : holeDetailData.getBottomRL()), tableEditModelArrayList.get(9).getTitleVal(), tableEditModelArrayList.get(9).isSelected(), update));

                            double totalCharge = 0.0, chargeLen = 0.0;
                            if (!Constants.isListEmpty(holeDetailData.getChargingArray())) {
                                for (int j = 0; j < holeDetailData.getChargingArray().size(); j++) {
                                    ChargeTypeArrayItem item = holeDetailData.getChargingArray().get(j);
                                    totalCharge = totalCharge + item.getWeight();

                                    if (StringUtill.getString(item.getType()).equals("Bulk") || StringUtill.getString(item.getType()).equals("Cartridge")) {
                                        chargeLen = chargeLen + item.getLength();
                                    }
                                }
                            }

                            editModelArrayList.add(new TableEditModel(String.valueOf(totalCharge), tableEditModelArrayList.get(10).getTitleVal(), tableEditModelArrayList.get(10).isSelected(), update));
                            editModelArrayList.add(new TableEditModel(String.valueOf(chargeLen), tableEditModelArrayList.get(11).getTitleVal(), tableEditModelArrayList.get(11).isSelected(), update));

                            if (!Constants.isListEmpty(holeDetailData.getChargingArray())) {
                                double length = 0, deckLen = 0;
                                for (int j = 0; j < holeDetailData.getChargingArray().size(); j++) {
                                    ChargeTypeArrayItem item = holeDetailData.getChargingArray().get(j);
                                    if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                        length = length + item.getLength();
                                    }
                                    if (StringUtill.getString(item.getType()).equals("Decking")) {
                                        deckLen = deckLen + item.getLength();
                                    }
                                }
                                editModelArrayList.add(new TableEditModel(String.valueOf(deckLen), tableEditModelArrayList.get(12).getTitleVal(), tableEditModelArrayList.get(12).isSelected(), update));
                            } else {
                                editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getDecking()), tableEditModelArrayList.get(12).getTitleVal(), tableEditModelArrayList.get(12).isSelected(), update));
                            }

                            if (!Constants.isListEmpty(holeDetailData.getChargingArray()))
                                editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getChargingArray().size()), tableEditModelArrayList.get(13).getTitleVal(), tableEditModelArrayList.get(13).isSelected(), update));
                            else
                                editModelArrayList.add(new TableEditModel(String.valueOf(0), tableEditModelArrayList.get(13).getTitleVal(), tableEditModelArrayList.get(13).isSelected(), update));

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
                }
            }
            List<HoleDetailItem> holeDetailItemList = holePreSplitDetailDataList.get(1).getHoleDetail();
            holeDetailItemList.add(0, null);
            tableViewPreSplitAdapter = new TableViewPreSplitAdapter(mContext, tableFieldItemModelList, holeDetailItemList);
            binding.tableRv.setAdapter(tableViewPreSplitAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getHoleType(String holeType) {
        if (!StringUtill.isEmpty(holeType)) {
            if (holeType.equals("1")) {
                return "Production";
            }
            if (holeType.equals("2")) {
                return "Buffer";
            }
            if (holeType.equals("3")) {
                return "PreSplit";
            }
        }
        return "Production";
    }

    @Override
    public void setRowOfTable(int rowNo, List<Response3DTable4HoleChargingDataModel> allTablesData, boolean isFromUpdateAdapter) {
        setTableData(allTablesData, isFromUpdateAdapter);
    }

    @Override
    public void setRowOfTableForPilot(int rowNo, List<Response3DTable16PilotDataModel> allTablesData, boolean isFromUpdateAdapter) {
        setPilotTableData(allTablesData, isFromUpdateAdapter);
    }

    @Override
    public void setRowOfTableForPreSplit(int rowNo, List<Response3DTable18PreSpilitDataModel> allTablesData, boolean isFromUpdateAdapter) {
        setPreSplitTableData(allTablesData, isFromUpdateAdapter);
    }

    @Override
    public void holeDataTableType(int tableType) {
        this.tableOfList = tableType;
        switch (tableType) {
            case Constants.PILOT:
                setPilotTableData();
                break;
            case Constants.PRE_SPLIT:
                setPreSplitTableData();
                break;
            case Constants.NORMAL:
            default:
                setNormalTableData();
                break;
        }
    }

    public void notifyHoleDataTableType(int tableType, boolean update) {
        switch (tableType) {
            case Constants.PILOT:
                setDataNotifyListOfPilot(update);
                break;
            case Constants.PRE_SPLIT:
                setDataNotifyListOfPreSplit(update);
                break;
            case Constants.NORMAL:
            default:
                setDataNotifyList(update);
                break;
        }
    }

}