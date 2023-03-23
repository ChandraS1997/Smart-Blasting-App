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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.TableViewAdapter;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetailsTableBinding;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HoleDetailsTableFragment extends BaseFragment implements OnDataEditTable, HoleDetailActivity.RowItemDetail {

    FragmentHoleDetailsTableBinding binding;
    TableViewAdapter tableViewAdapter;
    List<TableFieldItemModel> tableFieldItemModelList = new ArrayList<>();
    List<TableEditModel> tableEditModelArrayList = new ArrayList<>();
    ResponseBladesRetrieveData bladesRetrieveData;
    AllTablesData allTablesData;
    List<ResponseHoleDetailData> holeDetailDataList = new ArrayList<>();

    ProjectHoleDetailRowColDao entity;

    @Override
    public void onResume() {
        super.onResume();
        ((HoleDetailActivity) mContext).rowItemDetail = this;
    }

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
            allTablesData = ((HoleDetailActivity) mContext).allTablesData;

            entity = appDatabase.projectHoleDetailRowColDao();

            Constants.onDataEditTable = this;

            tableEditModelArrayList.clear();
            tableEditModelArrayList.addAll(((HoleDetailActivity) mContext).getTableModel());
            tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));

            holeDetailDataList.add(null);

            /*tableViewAdapter = new TableViewAdapter(mContext, tableFieldItemModelList, holeDetailDataList);
            binding.tableRv.setAdapter(tableViewAdapter);*/

            if (allTablesData == null) {
                getAllDesignInfoApiCaller(bladesRetrieveData.isIs3dBlade());
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
            for (int i = 0; i < ((HoleDetailActivity) mContext).getTableModel().size(); i++) {
                if (((HoleDetailActivity) mContext).getTableModel().get(i).isSelected()) {
                    count++;
                }
            }
            if (((HoleDetailActivity) mContext).isTableHeaderFirstTimeLoad) {
                count = ((HoleDetailActivity) mContext).getTableModel().size();
            }
            setWidthOfRv(count);
        }
        return binding.getRoot();
    }

    private void setTableData(AllTablesData tablesData, boolean isFromUpdateAdapter) {
        if (tablesData != null) {
            if (!Constants.isListEmpty(tablesData.getTable2())) {
                binding.noHoleDataAvailableMsg.setVisibility(View.GONE);
                binding.horizontalScrollView.setVisibility(View.VISIBLE);
                List<ResponseHoleDetailData> holeDetailData = new ArrayList<>();
                for (int i = 0; i < tablesData.getTable2().size(); i++) {
                    if (tablesData.getTable2().get(i).getRowNo() == ((HoleDetailActivity) mContext).rowPageVal) {
                        holeDetailData.add(tablesData.getTable2().get(i));
                    }
                }
                holeDetailDataList.clear();
                holeDetailDataList.add(null);
                holeDetailDataList.addAll(holeDetailData);
                ((HoleDetailActivity) mContext).holeDetailDataList.clear();
                ((HoleDetailActivity) mContext).holeDetailDataList.addAll(tablesData.getTable2());
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
//        List<TableEditModel> models = manger.getTableField();
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
//                tableEditModel.setFirstTime(false);
                tableEditModelArrayList.set(i, tableEditModel);
            }
            setWidthOfRv(selectedCount);
            setDataNotifyList(false);
        }
    }

    private void setWidthOfRv(int size) {
        int dp2px = dp2px(mContext.getResources(),((HoleDetailActivity) mContext).getTableModel().size() * 300);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(size * 300, RecyclerView.LayoutParams.WRAP_CONTENT);
        binding.tableRv.setLayoutParams(layoutParams);
    }

    public void getAllDesignInfoApiCaller(boolean is3d) {
        showLoader();
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getAll2D_3DDesignInfoApiCaller(mContext, loginData.getUserid(), loginData.getCompanyid(), bladesRetrieveData.getDesignId(), "dev_centralmineinfo", 0, is3d).observe((LifecycleOwner) mContext, new Observer<JsonElement>() {
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
                                        holeDetailDataList.clear();
                                        holeDetailDataList.add(null);
                                        allTablesData = tablesData;
                                        setTableData(tablesData, false);
                                        setDataIntoDb(tablesData);
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

    private void setDataIntoDb(AllTablesData tablesData) {
        String str = new Gson().toJson(tablesData);
        if (!entity.isExistProject(bladesRetrieveData.getDesignId())) {
            entity.insertProject(new ProjectHoleDetailRowColEntity(bladesRetrieveData.getDesignId(), bladesRetrieveData.isIs3dBlade(), str));
        } else {
            entity.updateProject(bladesRetrieveData.getDesignId(), str);
        }
    }

    private void setDataNotifyList(boolean update) {
        try {
            tableFieldItemModelList.clear();
            tableFieldItemModelList.add(0, new TableFieldItemModel(tableEditModelArrayList));
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int i = 1; i < holeDetailDataList.size(); i++) {
                    List<TableEditModel> editModelArrayList = new ArrayList<>();
                    ResponseHoleDetailData holeDetailData = holeDetailDataList.get(i);
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getRowNo()), tableEditModelArrayList.get(0).getTitleVal(), tableEditModelArrayList.get(0).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleNo()), tableEditModelArrayList.get(1).getTitleVal(), tableEditModelArrayList.get(1).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleID()), tableEditModelArrayList.get(2).getTitleVal(), tableEditModelArrayList.get(2).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleDepthDouble()), tableEditModelArrayList.get(3).getTitleVal(), tableEditModelArrayList.get(3).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleStatus()), tableEditModelArrayList.get(4).getTitleVal(), tableEditModelArrayList.get(4).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleAngleDouble()), tableEditModelArrayList.get(5).getTitleVal(), tableEditModelArrayList.get(5).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getHoleDiameter()), tableEditModelArrayList.get(6).getTitleVal(), tableEditModelArrayList.get(6).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getBurdenDouble()), tableEditModelArrayList.get(7).getTitleVal(), tableEditModelArrayList.get(7).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getSpacingDouble()), tableEditModelArrayList.get(8).getTitleVal(), tableEditModelArrayList.get(8).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getX() == 0.0 ? "" : holeDetailData.getX()), tableEditModelArrayList.get(9).getTitleVal(), tableEditModelArrayList.get(9).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getY() == 0.0 ? "" : holeDetailData.getY()), tableEditModelArrayList.get(10).getTitleVal(), tableEditModelArrayList.get(10).isSelected(), update));
                    editModelArrayList.add(new TableEditModel(String.valueOf(holeDetailData.getZ()), tableEditModelArrayList.get(11).getTitleVal(), tableEditModelArrayList.get(11).isSelected(), update));
                    int chargingCount = 0;
                    if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getExpCode()))) {
                        chargingCount = chargingCount + 1;
                    }
                    if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getExpCode1()))) {
                        chargingCount = chargingCount + 1;
                    }
                    if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getExpCode2()))) {
                        chargingCount = chargingCount + 1;
                    }
                    if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getStemLngth()))) {
                        chargingCount = chargingCount + 1;
                    }
                    if (!StringUtill.isEmpty(String.valueOf(holeDetailData.getDeckLength1()))) {
                        chargingCount = chargingCount + 1;
                    }
                    editModelArrayList.add(new TableEditModel(String.valueOf(chargingCount), tableEditModelArrayList.get(12).getTitleVal(), tableEditModelArrayList.get(12).isSelected(), update));

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
            tableViewAdapter = new TableViewAdapter(mContext, tableFieldItemModelList, holeDetailDataList);
            binding.tableRv.setAdapter(tableViewAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRowOfTable(int rowNo, AllTablesData allTablesData, boolean isFromUpdateAdapter) {
        setTableData(allTablesData, isFromUpdateAdapter);
    }
}