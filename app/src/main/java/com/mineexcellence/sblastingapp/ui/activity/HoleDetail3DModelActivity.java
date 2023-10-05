package com.mineexcellence.sblastingapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseRockData;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable17DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.HoleDetailItem;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.Response3DTable18PreSpilitDataModel;
import com.mineexcellence.sblastingapp.app.AppDelegate;
import com.mineexcellence.sblastingapp.app.CoordinationHoleHelperKt;
import com.mineexcellence.sblastingapp.databinding.HoleDetail3dActivityBinding;
import com.mineexcellence.sblastingapp.dialogs.AppAlertDialogFragment;
import com.mineexcellence.sblastingapp.dialogs.Hole3dEditFieldTableForPilotSelectionDialog;
import com.mineexcellence.sblastingapp.dialogs.Hole3dEditFieldTableForPreSplitSelectionDialog;
import com.mineexcellence.sblastingapp.dialogs.Hole3dEditTableFieldSelectionDialog;
import com.mineexcellence.sblastingapp.dialogs.ProjectDetail3DDataDialog;
import com.mineexcellence.sblastingapp.dialogs.ProjectDetailDialog;
import com.mineexcellence.sblastingapp.dialogs.SyncProjectOptionDialog;
import com.mineexcellence.sblastingapp.helper.ConnectivityReceiver;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.interfaces.OnHoleClickListener;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.mineexcellence.sblastingapp.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.mineexcellence.sblastingapp.room_database.entities.AllProjectBladesModelEntity;
import com.mineexcellence.sblastingapp.room_database.entities.BlastCodeEntity;
import com.mineexcellence.sblastingapp.room_database.entities.ProjectHoleDetailRowColEntity;
import com.mineexcellence.sblastingapp.room_database.entities.UpdateProjectBladesEntity;
import com.mineexcellence.sblastingapp.ui.models.TableEditModel;
import com.mineexcellence.sblastingapp.utils.KeyboardUtils;
import com.mineexcellence.sblastingapp.utils.StatusBarUtils;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HoleDetail3DModelActivity extends BaseActivity implements View.OnClickListener, OnHoleClickListener {
    public NavController navController;
    public HoleDetail3dActivityBinding binding;
    public List<Response3DTable4HoleChargingDataModel> allTablesData = new ArrayList<>();
    public List<Response3DTable18PreSpilitDataModel> preSplitTableData = new ArrayList<>();
    public List<Response3DTable16PilotDataModel> pilotTableData = new ArrayList<>();
    public List<Response3DTable1DataModel> bladesRetrieveData = new ArrayList<>();
    public List<Response3DTable2DataModel> response3DTable2DataModelList = new ArrayList<>();
    public List<Response3DTable17DataModel> response3DTable17DataModelList = new ArrayList<>();
    public boolean isTableHeaderFirstTimeLoad = true;
    public JsonElement element;
    public int tableTypeVal = 0, lastTableTypeVal = 0;

    public List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
    public int rowPageVal = 1;
    public RowItemDetail rowItemDetail;
    public TableHoleDataListCallback tableHoleDataListCallback;
    List<String> rowList = new ArrayList<>();

    public int updateValPos = -1, updateRowNo = -1, updateHoleNo = -1, rowPos = -1, pilotRowPos = -1, preSplitRowPos = -1;
    public String pilotUpdateHoleId = "";
    public String preSplitUpdateHoleId = "";

    public MutableLiveData<Boolean> mapViewDataUpdateLiveData = new MutableLiveData<>();

    public HoleDetail3DModelActivity.HoleDetailCallBackListener holeDetailCallBackListener;
    public HoleDetail3DModelActivity.HoleDetailForPilotCallBackListener mapPilotCallBackListener;
    public HoleDetail3DModelActivity.HoleDetailForPreSplitCallBackListener mapPreSplitCallBackListener;

    public interface HoleDetailCallBackListener {
        void setHoleDetailCallBack(Response3DTable4HoleChargingDataModel detailData);
        void setBgOfHoleOnNewRowChange(int row, int hole, int pos);
        void saveAndCloseHoleDetailCallBack();
    }

    public interface HoleDetailForPilotCallBackListener {
        void setHoleDetailForPilotCallBack(Response3DTable16PilotDataModel detailData);
        void setBgOfHoleOnPilotOnNewRowChange(String holeId, int pos);
        void saveAndCloseHoleDetailForPilotCallBack();
    }

    public interface HoleDetailForPreSplitCallBackListener {
        void setHoleDetailForPreSplitCallBack(HoleDetailItem detailData);
        void setBgOfHoleOnPreSplitOnNewRowChange(String holeId, int pos);
        void saveAndCloseHoleDetailForPreSplitCallBack();
    }

    public void setDataFromBundle() {
        bladesRetrieveData = AppDelegate.getInstance().getResponse3DTable1DataModel();
        response3DTable2DataModelList = AppDelegate.getInstance().getResponse3DTable2DataModel();
        response3DTable17DataModelList = AppDelegate.getInstance().getResponse3DTable17DataModelList();
        getRockDensity();
        allTablesData = AppDelegate.getInstance().getHoleChargingDataModel();
        preSplitTableData = AppDelegate.getInstance().getPreSpilitDataModelList();
        pilotTableData = AppDelegate.getInstance().getPilotDataModelList();

        if (!Constants.isListEmpty(bladesRetrieveData) && !Constants.isListEmpty(allTablesData)) {
            for (int i = 0; i < allTablesData.size(); i++) {
                boolean isFound = false;
                for (int j = 0; j < rowList.size(); j++) {
                    if (rowList.get(j).replace("Row ", "").equals(String.valueOf(allTablesData.get(i).getRowNo()))) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound)
                    rowList.add("Row " + allTablesData.get(i).getRowNo());
            }

        }

    }

    public interface TableHoleDataListCallback {
        void holeDataTableType(int tableType);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataFromBundle();
        binding = DataBindingUtil.setContentView(this, R.layout.hole_detail_3d_activity);

        String[] tableType = new String[]{"Normal", "Pilot", "Pre-Split"};
        binding.headerLayHole.spinnerHoleType.setAdapter(Constants.getAdapter(this, tableType));
        binding.headerLayHole.spinnerHoleType.selectItem(0);

        binding.headerLayHole.spinnerHoleType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastTableTypeVal = tableTypeVal;
                binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
                isTableHeaderFirstTimeLoad = true;
                switch (tableType[i]) {
                    case "Pilot":
                        tableTypeVal = Constants.TABLE_TYPE.PILOT.getType();
                        break;
                    case "Pre-Split":
                        tableTypeVal = Constants.TABLE_TYPE.PRE_SPLIT.getType();
                        break;
                    default:
                        tableTypeVal = Constants.TABLE_TYPE.NORMAL.getType();
                        setRowVisibility();
                        break;
                }
                if (lastTableTypeVal != tableTypeVal)
                    tableHoleDataListCallback.holeDataTableType(tableTypeVal);
            }
        });

        String[] rowSpinnerList = new String[rowList.size()];
        for (int i = 0; i < rowList.size(); i++) {
            rowSpinnerList[i] = rowList.get(i);
        }

        if (rowSpinnerList.length > 0){
            binding.headerLayHole.spinnerRow.setVisibility(View.VISIBLE);
            binding.headerLayHole.spinnerRow.setAdapter(Constants.getAdapter(this, rowSpinnerList));
            binding.headerLayHole.spinnerRow.setText(rowSpinnerList[0]);
            binding.headerLayHole.spinnerRow.setOnItemClickListener((adapterView, view, i, l) -> {
                rowPageVal = Integer.parseInt(rowSpinnerList[i].replace("Row ", "0"));
                if (rowItemDetail != null)
                    rowItemDetail.setRowOfTable(rowPageVal, allTablesData, false);
            });
        } else {
            binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
        }

        navController = Navigation.findNavController(this, R.id.nav_host_hole);
        Constants.onHoleClickListener = this;

        setNavController();

        StatusBarUtils.statusBarColor(this, R.color._FFA722);

        binding.headerLayHole.pageTitle.setText(getString(R.string.hole_detail));
        binding.headerLayHole.projectInfo.setText(getString(R.string.edit_field));
        binding.headerLayHole.camIcon.setVisibility(View.GONE);
        binding.headerLayHole.homeBtn.setVisibility(View.GONE);
        binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
        binding.headerLayHole.editTable.setVisibility(View.GONE);
        binding.headerLayHole.menuBtn.setVisibility(View.VISIBLE);

        binding.headerLayHole.camIcon.setOnClickListener(this);
        binding.bottomHoleNavigation.mapBtn.setOnClickListener(this);
        binding.bottomHoleNavigation.listBtn.setOnClickListener(this);
        binding.headerLayHole.homeBtn.setOnClickListener(this);
        binding.headerLayHole.menuBtn.setOnClickListener(this);
        binding.drawerLayout.BlastPerformanceBtn.setOnClickListener(this);
        binding.drawerLayout.switchBtn.setOnClickListener(this);
        binding.drawerLayout.galleryBtn.setOnClickListener(this);
        binding.drawerLayout.logoutBtn.setOnClickListener(this);
        binding.drawerLayout.projectBtn.setOnClickListener(this);
        binding.drawerLayout.initiatingDeviceContainer.setOnClickListener(this);
        binding.drawerLayout.closeBtn.setOnClickListener(view -> binding.mainDrawerLayout.closeDrawer(GravityCompat.START));

        binding.drawerLayout.syncProjectContainer.setOnClickListener(view -> {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SyncProjectOptionDialog infoDialogFragment = SyncProjectOptionDialog.getInstance(new SyncProjectOptionDialog.SyncProjectListener() {
                @Override
                public void syncWithDrims() {
                    if (ConnectivityReceiver.getInstance().isInternetAvailable()) {
                        syncDataAPi();
                    } else {
                        showToast("No internet connection. Make sure Wi-Fi or Cellular data is turn on, then try again");
                    }
                }

                @Override
                public void syncWithBims() {
                    if (ConnectivityReceiver.getInstance().isInternetAvailable()) {
                        String blastCode = "";
                        if (!StringUtill.isEmpty(String.valueOf(bladesRetrieveData.get(0).getBimsId()))) {
                            blastCode = String.valueOf(bladesRetrieveData.get(0).getBimsId());
                        } else {
                            if (!Constants.isListEmpty(appDatabase.blastCodeDao().getAllEntityDataList())) {
                                BlastCodeEntity blastCodeEntity = appDatabase.blastCodeDao().getSingleItemEntityByDesignId(bladesRetrieveData.get(0).getDesignId());
                                if (blastCodeEntity != null)
                                    blastCode = blastCodeEntity.getBlastCode();
                            }
                        }
                        blastInsertSyncRecord3DApiCaller(response3DTable17DataModelList, bladesRetrieveData.get(0), allTablesData, response3DTable2DataModelList, getRowWiseHoleIn3dList(allTablesData).size(), blastCode).observe(HoleDetail3DModelActivity.this, new Observer<JsonElement>() {
                            @Override
                            public void onChanged(JsonElement element) {
                                bladesRetrieveData = AppDelegate.getInstance().getResponse3DTable1DataModel();
                            }
                        });
                    } else {
                        showToast("No internet connection. Make sure Wi-Fi or Cellular data is turn on, then try again");
                    }
                }

                @Override
                public void syncWithBlades() {
                    if (ConnectivityReceiver.getInstance().isInternetAvailable()) {
                        insertUpdate3DActualDesignHoleDetailApiCaller(allTablesData, pilotTableData, preSplitTableData);
                    } else {
                        showToast("No internet connection. Make sure Wi-Fi or Cellular data is turn on, then try again");
                    }
                }
            });
            ft.add(infoDialogFragment, ProjectDetailDialog.TAG);
            ft.commitAllowingStateLoss();
        });

        binding.headerLayHole.projectInfo.setOnClickListener(view -> {
            editTable();
        });
        binding.headerLayHole.editTable.setOnClickListener(this);
        binding.headerLayHole.refreshIcn.setVisibility(View.GONE);
    }

    void setRowVisibility() {
        if (navController.getCurrentDestination().getId() == R.id.holeDetailsTableViewFragment) {
            if (tableTypeVal == Constants.TABLE_TYPE.NORMAL.getType())
                binding.headerLayHole.spinnerRow.setVisibility(View.VISIBLE);
            else binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
        } else if (navController.getCurrentDestination().getId() == R.id.mapViewFrament) {
            binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
        }
    }

    public void syncDataAPi() {

        CoordinationHoleHelperKt._3dMapCoordinatesAndroid(Double.parseDouble(holeDetailDataList.get(0).getTopX()), Double.parseDouble(holeDetailDataList.get(0).getTopY()), holeDetailDataList);

        AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(bladesRetrieveData.get(0).getDesignId()));
        String code = "";
        code = String.valueOf(bladesRetrieveData.get(0).getDrimsId());
        if (StringUtill.isEmpty(code)) {
            code = (modelEntity != null  && !StringUtill.isEmpty(modelEntity.getProjectCode())) ? modelEntity.getProjectCode() : "";
        }

        drimzsSyncApi(code);

    }

    private void drimzsSyncApi(String code) {
        List<Response3DTable4HoleChargingDataModel> modelList = new ArrayList<>();
        for (int i = 0; i < allTablesData.size(); i++) {
            Response3DTable4HoleChargingDataModel model = allTablesData.get(i);
            String[] coordinate = StringUtill.getString(CoordinationHoleHelperKt.getCoOrdinateOfHole(model.getTopX(), model.getTopY())).split(",");
            if (coordinate.length > 0) {
                model.setNorthing(coordinate[0]);
                if (coordinate.length > 1) {
                    model.setEasting(coordinate[1]);
                }
            }
            modelList.add(model);
        }

        List<Response3DTable16PilotDataModel> pilotDataModelList = new ArrayList<>();
        for (int i = 0; i < pilotTableData.size(); i++) {
            Response3DTable16PilotDataModel model = pilotTableData.get(i);
            String[] coordinate = StringUtill.getString(CoordinationHoleHelperKt.getCoOrdinateOfHole(model.getTopX(), model.getTopY())).split(",");
            if (coordinate.length > 0) {
                model.setNorthing(coordinate[0]);
                if (coordinate.length > 1) {
                    model.setEasting(coordinate[1]);
                }
            }
            pilotDataModelList.add(model);
        }

        List<HoleDetailItem> preSplitDataModelList = new ArrayList<>();
        if (!Constants.isListEmpty(preSplitTableData)) {
            for (int i = 0; i < preSplitTableData.get(0).getHoleDetail().size(); i++) {
                HoleDetailItem model = preSplitTableData.get(0).getHoleDetail().get(i);
                String[] coordinate = StringUtill.getString(CoordinationHoleHelperKt.getCoOrdinateOfHole(String.valueOf(model.getTopNorthing()), String.valueOf(model.getTopEasting()))).split(",");
                if (coordinate.length > 0) {
                    model.setNorthing(coordinate[0]);
                    if (coordinate.length > 1) {
                        model.setEasting(coordinate[1]);
                    }
                }
                preSplitDataModelList.add(model);
            }
        }

        Log.e("modelList : ", new Gson().toJson(modelList));
        setInsertUpdateHoleDetailMultipleSync3D(bladesRetrieveData.get(0), modelList, pilotDataModelList, preSplitDataModelList, code).observe(this, new Observer<JsonElement>() {
            @Override
            public void onChanged(JsonElement response) {
                if (response == null) {
                    Log.e(ERROR, SOMETHING_WENT_WRONG);
                } else {
                    if (!(response.isJsonNull())) {
                        showToast("Project Holes Sync Successfully");
                    } else {
                        showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                    }
                }
                hideLoader();
            }
        });
    }

    public List<TableEditModel> getTableModel() {
        if (!Constants.isListEmpty(manger.get3dTableField())) {
            isTableHeaderFirstTimeLoad = false;
            return manger.get3dTableField();
        }

        List<TableEditModel> editModelArrayList = new ArrayList<>();
        editModelArrayList.add(new TableEditModel("Row No", "Row No"));
        editModelArrayList.add(new TableEditModel("Hole No", "Hole No"));
        editModelArrayList.add(new TableEditModel("Hole Id", "Hole Id"));
        editModelArrayList.add(new TableEditModel("Hole Type", "Hole Type"));
        editModelArrayList.add(new TableEditModel("Hole Depth", "Hole Depth"));
        editModelArrayList.add(new TableEditModel("Hole Status", "Hole Status"));
        editModelArrayList.add(new TableEditModel("VerticalDip", "VerticalDip"));
        editModelArrayList.add(new TableEditModel("Diameter", "Diameter"));
        editModelArrayList.add(new TableEditModel("Burden", "Burden"));
        editModelArrayList.add(new TableEditModel("Spacing", "Spacing"));
        editModelArrayList.add(new TableEditModel("Subgrade", "Subgrade"));
        editModelArrayList.add(new TableEditModel("TopX", "TopX"));
        editModelArrayList.add(new TableEditModel("TopY", "TopY"));
        editModelArrayList.add(new TableEditModel("TopZ", "TopZ"));
        editModelArrayList.add(new TableEditModel("BottomX" ,"BottomX"));
        editModelArrayList.add(new TableEditModel("BottomY", "BottomY"));
        editModelArrayList.add(new TableEditModel("BottomZ", "BottomZ"));
        editModelArrayList.add(new TableEditModel("TotalCharge", "TotalCharge"));
        editModelArrayList.add(new TableEditModel("ChargeLength", "ChargeLength"));
        editModelArrayList.add(new TableEditModel("StemmingLength", "StemmingLength"));
        editModelArrayList.add(new TableEditModel("DeckLength", "DeckLength"));
        editModelArrayList.add(new TableEditModel("Block", "Block"));
        editModelArrayList.add(new TableEditModel("BlockLength", "BlockLength"));
        editModelArrayList.add(new TableEditModel("InHoleDelay", "InHoleDelay"));
        editModelArrayList.add(new TableEditModel("Charging", "Charging"));
        return editModelArrayList;
    }

    public List<TableEditModel> getPreSplitTableModel() {
        if (!Constants.isListEmpty(manger.get3dPreSplitTableField())) {
            isTableHeaderFirstTimeLoad = false;
            return manger.get3dPreSplitTableField();
        }

        List<TableEditModel> editModelArrayList = new ArrayList<>();
        editModelArrayList.add(new TableEditModel("Hole Id", "Hole Id"));
        editModelArrayList.add(new TableEditModel("Hole Type", "Hole Type"));
        editModelArrayList.add(new TableEditModel("Diameter", "Diameter"));
        editModelArrayList.add(new TableEditModel("Hole Depth", "Hole Depth"));
        editModelArrayList.add(new TableEditModel("Top Easting", "Top Easting"));
        editModelArrayList.add(new TableEditModel("Top Northing", "Top Northing"));
        editModelArrayList.add(new TableEditModel("Top RL", "Top RL"));
        editModelArrayList.add(new TableEditModel("Bottom Easting", "Bottom Easting"));
        editModelArrayList.add(new TableEditModel("Bottom Northing", "Bottom Northing"));
        editModelArrayList.add(new TableEditModel("Bottom RL", "Bottom RL"));
        editModelArrayList.add(new TableEditModel("Total Charge", "Total Charge"));
        editModelArrayList.add(new TableEditModel("Charge Length", "Charge Length"));
        editModelArrayList.add(new TableEditModel("Decking", "Decking"));
        editModelArrayList.add(new TableEditModel("Charging", "Charging"));
        return editModelArrayList;
    }

    public List<TableEditModel> getPilotTableModel() {
        if (!Constants.isListEmpty(manger.get3dPilotTableField())) {
            isTableHeaderFirstTimeLoad = false;
            return manger.get3dPilotTableField();
        }

        List<TableEditModel> editModelArrayList = new ArrayList<>();
        editModelArrayList.add(new TableEditModel("Hole Id", "Hole Id"));
        editModelArrayList.add(new TableEditModel("Hole Depth", "Hole Depth"));
        editModelArrayList.add(new TableEditModel("VerticalDip", "VerticalDip"));
        editModelArrayList.add(new TableEditModel("Diameter", "Diameter"));
        editModelArrayList.add(new TableEditModel("Burden", "Burden"));
        editModelArrayList.add(new TableEditModel("Spacing", "Spacing"));
        editModelArrayList.add(new TableEditModel("Subgrade", "Subgrade"));
        editModelArrayList.add(new TableEditModel("TopX", "TopX"));
        editModelArrayList.add(new TableEditModel("TopY", "TopY"));
        editModelArrayList.add(new TableEditModel("TopZ", "TopZ"));
        editModelArrayList.add(new TableEditModel("BottomX" ,"BottomX"));
        editModelArrayList.add(new TableEditModel("BottomY", "BottomY"));
        editModelArrayList.add(new TableEditModel("BottomZ", "BottomZ"));
        editModelArrayList.add(new TableEditModel("TotalCharge", "TotalCharge"));
        editModelArrayList.add(new TableEditModel("ChargeLength", "ChargeLength"));
        editModelArrayList.add(new TableEditModel("StemmingLength", "StemmingLength"));
        editModelArrayList.add(new TableEditModel("DeckLength", "DeckLength"));
        editModelArrayList.add(new TableEditModel("Block", "Block"));
        editModelArrayList.add(new TableEditModel("BlockLength", "BlockLength"));
        editModelArrayList.add(new TableEditModel("InHoleDelay", "InHoleDelay"));
        editModelArrayList.add(new TableEditModel("Charging", "Charging"));
        return editModelArrayList;
    }

    private void setNavController() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.holeDetailsTableViewFragment) {
                    binding.holeDetailLayoutContainer.setVisibility(View.GONE);
                    binding.holeDetailLayoutContainerForPilot.setVisibility(View.GONE);
                    binding.holeDetailLayoutContainerPreSplit.setVisibility(View.GONE);
                    binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
                    if (tableTypeVal == Constants.TABLE_TYPE.NORMAL.getType())
                        binding.headerLayHole.spinnerRow.setVisibility(View.VISIBLE);
                    else binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
                    binding.holeParaLay.setVisibility(View.GONE);
                } else if (navDestination.getId() == R.id.mapViewFrament) {
                    binding.headerLayHole.projectInfo.setVisibility(View.GONE);
                    binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
                    binding.holeParaLay.setVisibility(View.GONE);
                }
            }
        });
    }

    public interface RowItemDetail {
        void setRowOfTable(int rowNo, List<Response3DTable4HoleChargingDataModel> allTablesData, boolean isFromUpdateAdapter);
        default void setRowOfTableForPilot(int rowNo, List<Response3DTable16PilotDataModel> allTablesData, boolean isFromUpdateAdapter) {}
        default void setRowOfTableForPreSplit(int rowNo, List<Response3DTable18PreSpilitDataModel> allTablesData, boolean isFromUpdateAdapter) {}
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        Intent intent;
        switch (view.getId()) {
            case R.id.menuBtn:
                binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                binding.mainDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.BlastPerformanceBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                intent = new Intent(this, PerformanceActivity.class);
                bundle.putString("blades_data", bladesRetrieveData.get(0).getDesignId());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.switchBtn:
            case R.id.homeBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.galleryBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                bundle.putString("blades_data", bladesRetrieveData.get(0).getDesignId());
                intent = new Intent(this, Media3dActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.projectBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ProjectDetail3DDataDialog infoDialogFragment = ProjectDetail3DDataDialog.getInstance(bladesRetrieveData.get(0));
                ft.add(infoDialogFragment, ProjectDetail3DDataDialog.TAG);
                ft.commitAllowingStateLoss();
                break;
            case R.id.logoutBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                setLogOut();
                break;
            case R.id.mapBtn:
                binding.headerLayHole.projectInfo.setVisibility(View.GONE);
                binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
                binding.holeParaLay.setVisibility(View.GONE);
                navController.navigate(R.id.mapViewFrament);
                break;
            case R.id.camIcon:
                startActivity(new Intent(this, Media3dActivity.class));
                break;
            case R.id.initiatingDeviceContainer:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                bundle.putString("blades_data", bladesRetrieveData.get(0).getDesignId());
                intent = new Intent(this, InitiatingDeviceViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.listBtn:
                binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
                if (tableTypeVal == Constants.TABLE_TYPE.NORMAL.getType())
                    binding.headerLayHole.spinnerRow.setVisibility(View.VISIBLE);
                else binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
                binding.holeParaLay.setVisibility(View.GONE);
                navController.navigate(R.id.holeDetailsTableViewFragment);
                break;
            case R.id.editTable:
                editTable();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }

    private void setLogOut() {
//        appDatabase.clearAllTables();
        showAlertDialog("Logout", "Are you sure you want to logout?", "Yes", "No", new AppAlertDialogFragment.AppAlertDialogListener() {
            @Override
            public void onOk(AppAlertDialogFragment dialogFragment) {
                dialogFragment.dismiss();
                manger.logoutUser();
                startActivity(new Intent(HoleDetail3DModelActivity.this, AuthActivity.class));
                finishAffinity();
            }

            @Override
            public void onCancel(AppAlertDialogFragment dialogFragment) {
                dialogFragment.dismiss();
            }
        });
    }

    public void editTable() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (tableTypeVal) {
            case Constants.PILOT:
                Hole3dEditFieldTableForPilotSelectionDialog pilotSelectionDialog = Hole3dEditFieldTableForPilotSelectionDialog.getInstance();
                ft.add(pilotSelectionDialog, Hole3dEditFieldTableForPilotSelectionDialog.TAG);
                break;
            case Constants.PRE_SPLIT:
                Hole3dEditFieldTableForPreSplitSelectionDialog preSplitSelectionDialog = Hole3dEditFieldTableForPreSplitSelectionDialog.getInstance();
                ft.add(preSplitSelectionDialog, Hole3dEditFieldTableForPreSplitSelectionDialog.TAG);
                break;
            default:
                Hole3dEditTableFieldSelectionDialog infoDialogFragment = Hole3dEditTableFieldSelectionDialog.getInstance();
                ft.add(infoDialogFragment, Hole3dEditTableFieldSelectionDialog.TAG);
                break;
        }
        ft.commitAllowingStateLoss();
    }


    @Override
    public void onHoleClick(String frommapview) {
        if (frommapview.equals("mapviewFragment")) {
            binding.holeParaLay.setVisibility(View.VISIBLE);
        } else {
            binding.holeParaLay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (binding.holeParaLay.getVisibility() == View.VISIBLE)
            binding.holeParaLay.setVisibility(View.GONE);
        binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
    }

    String explosiveName = "";

    public void set3dHoleDetail(Response3DTable4HoleChargingDataModel holeDetailData) {

        Response3DTable4HoleChargingDataModel updateHoleDetailData = holeDetailData;
        if (holeDetailData != null) {
            binding.holeDetailLayout.holeDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDepth())));
            String[] status = new String[]{"Pending Hole", "Work in Progress", "Completed", "Deleted/ Blocked holes/ Do not blast"};
            binding.holeDetailLayout.holeStatusSpinner.setAdapter(Constants.getAdapter(this, status));

            if (StringUtill.isEmpty(holeDetailData.getHoleStatus())) {
                binding.holeDetailLayout.holeStatusSpinner.setText("Pending Hole");
            } else {
                binding.holeDetailLayout.holeStatusSpinner.setText(StringUtill.getString(holeDetailData.getHoleStatus()));
            }

            binding.holeDetailLayout.holeNameEt.setText(String.format("R%sH%s", StringUtill.getString(String.valueOf(holeDetailData.getRowNo())), StringUtill.getString(String.valueOf(holeDetailData.getHoleNo()))));
            binding.holeDetailLayout.holeAngleEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getVerticalDip())));
            binding.holeDetailLayout.diameterEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDiameter())));
            binding.holeDetailLayout.burdenEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBurden())));
            binding.holeDetailLayout.spacingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSpacing())));
            binding.holeDetailLayout.xEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopX())));
            binding.holeDetailLayout.yTxtEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopY())));
            binding.holeDetailLayout.zEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopZ())));
            binding.holeDetailLayout.bottomXEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomX())));
            binding.holeDetailLayout.bottomYEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomY())));
            binding.holeDetailLayout.bottomZEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomZ())));

            binding.holeDetailLayout.totalChargeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTotalCharge())));
            binding.holeDetailLayout.chargeLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getChargeLength())));
            binding.holeDetailLayout.stemmingLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getStemmingLength())));
            binding.holeDetailLayout.deckLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getDeckLength())));
            binding.holeDetailLayout.blockEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBlock())));
            binding.holeDetailLayout.blockLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBlockLength())));
            binding.holeDetailLayout.holeDelayEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDelay())));

            if (!Constants.isListEmpty(response3DTable17DataModelList)) {
                JsonArray inHoleDelayArr = new Gson().fromJson(new Gson().fromJson(response3DTable17DataModelList.get(0).getInHoleDelayArr(), String.class), JsonArray.class);
                if (inHoleDelayArr != null) {
                    if (inHoleDelayArr.size() > 0) {
                        binding.holeDetailLayout.inHoleDelayEt.setText(String.valueOf(inHoleDelayArr.get(0).getAsInt()));
                    } else {
                        binding.holeDetailLayout.inHoleDelayEt.setText("0");
                    }
                } else {
                    binding.holeDetailLayout.inHoleDelayEt.setText("0");
                }
            } else {
                binding.holeDetailLayout.inHoleDelayEt.setText("0");
            }

            binding.holeDetailLayout.waterDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getWaterDepth())));
            binding.holeDetailLayout.subgradeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSubgrade())));
        }

        binding.holeDetailLayout.closeBtn.setOnClickListener(view -> {
            KeyboardUtils.hideSoftKeyboard(this);
            if (Constants.holeBgListener != null)
                Constants.holeBgListener.setBackgroundRefresh();
            if (Constants.hole3DBgListener != null)
                Constants.hole3DBgListener.setBackgroundRefresh();
            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        });

        binding.holeDetailLayout.saveProceedBtn.setOnClickListener(view -> {
            assert updateHoleDetailData != null;
            updateHoleDetailData.setHoleDepth(StringUtill.isEmpty(binding.holeDetailLayout.holeDepthEt.getText().toString()) ? "0" : binding.holeDetailLayout.holeDepthEt.getText().toString());
            updateHoleDetailData.setVerticalDip(StringUtill.isEmpty(binding.holeDetailLayout.holeAngleEt.getText().toString()) ? "0" : binding.holeDetailLayout.holeAngleEt.getText().toString());
            updateHoleDetailData.setHoleDiameter(StringUtill.isEmpty(binding.holeDetailLayout.diameterEt.getText().toString()) ? "0" : binding.holeDetailLayout.diameterEt.getText().toString());
            updateHoleDetailData.setBurden(StringUtill.isEmpty(binding.holeDetailLayout.burdenEt.getText().toString()) ? "0" : binding.holeDetailLayout.burdenEt.getText().toString());
            updateHoleDetailData.setSpacing(StringUtill.isEmpty(binding.holeDetailLayout.spacingEt.getText().toString()) ? "0" : binding.holeDetailLayout.spacingEt.getText().toString());
            updateHoleDetailData.setTopX(StringUtill.isEmpty(binding.holeDetailLayout.xEt.getText().toString()) ? "0" : binding.holeDetailLayout.xEt.getText().toString());
            updateHoleDetailData.setTopY(StringUtill.isEmpty(binding.holeDetailLayout.yTxtEt.getText().toString()) ? "0" : binding.holeDetailLayout.yTxtEt.getText().toString());
            updateHoleDetailData.setTopZ(StringUtill.isEmpty(binding.holeDetailLayout.zEt.getText().toString()) ? "0" : binding.holeDetailLayout.zEt.getText().toString());
            updateHoleDetailData.setBottomX(StringUtill.isEmpty(binding.holeDetailLayout.bottomXEt.getText().toString()) ? "0" : binding.holeDetailLayout.bottomXEt.getText().toString());
            updateHoleDetailData.setBottomY(StringUtill.isEmpty(binding.holeDetailLayout.bottomYEt.getText().toString()) ? "0" : binding.holeDetailLayout.bottomYEt.getText().toString());
            updateHoleDetailData.setBottomZ(StringUtill.isEmpty(binding.holeDetailLayout.bottomZEt.getText().toString()) ? "0" : binding.holeDetailLayout.bottomZEt.getText().toString());

            updateHoleDetailData.setSubgrade(StringUtill.isEmpty(binding.holeDetailLayout.subgradeEt.getText().toString()) ? "0" : binding.holeDetailLayout.subgradeEt.getText().toString());
            updateHoleDetailData.setWaterDepth(StringUtill.isEmpty(binding.holeDetailLayout.waterDepthEt.getText().toString()) ? "0" : binding.holeDetailLayout.waterDepthEt.getText().toString());
            updateHoleDetailData.setTotalCharge(StringUtill.isEmpty(binding.holeDetailLayout.totalChargeEt.getText().toString()) ? "0" : binding.holeDetailLayout.totalChargeEt.getText().toString());
            updateHoleDetailData.setChargeLength(StringUtill.isEmpty(binding.holeDetailLayout.chargeLengthEt.getText().toString()) ? "0" : binding.holeDetailLayout.chargeLengthEt.getText().toString());
            updateHoleDetailData.setStemmingLength(StringUtill.isEmpty(binding.holeDetailLayout.stemmingLengthEt.getText().toString()) ? "0" : binding.holeDetailLayout.stemmingLengthEt.getText().toString());
            updateHoleDetailData.setDeckLength(StringUtill.isEmpty(binding.holeDetailLayout.deckLengthEt.getText().toString()) ? "0" : binding.holeDetailLayout.deckLengthEt.getText().toString());
            updateHoleDetailData.setBlock(StringUtill.isEmpty(binding.holeDetailLayout.blockEt.getText().toString()) ? "0" : binding.holeDetailLayout.blockEt.getText().toString());
            updateHoleDetailData.setBlockLength(StringUtill.isEmpty(binding.holeDetailLayout.blockLengthEt.getText().toString()) ? "0" : binding.holeDetailLayout.blockLengthEt.getText().toString());
            updateHoleDetailData.setHoleDelay(StringUtill.isEmpty(binding.holeDetailLayout.holeDelayEt.getText().toString()) ? "0" : binding.holeDetailLayout.holeDelayEt.getText().toString());
            updateHoleDetailData.setInHoleDelay(StringUtill.isEmpty(binding.holeDetailLayout.inHoleDelayEt.getText().toString()) ? "0" : binding.holeDetailLayout.inHoleDelayEt.getText().toString());

            updateHoleDetailData.setHoleStatus(StringUtill.getString(binding.holeDetailLayout.holeStatusSpinner.getText().toString()));

            updateEditedDataIntoDb(updateHoleDetailData, false);
        });
    }

    public void set3dPilotHoleDetail(Response3DTable16PilotDataModel holeDetailData) {

        if (holeDetailData != null) {
            binding.pilotHoleDetailLayout.holeIdEt.setText(String.format("%s", StringUtill.getString(String.valueOf(holeDetailData.getHoleID()))));
            binding.pilotHoleDetailLayout.holeDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDepth())));
            binding.pilotHoleDetailLayout.verticalDipEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getVerticalDip())));

            binding.pilotHoleDetailLayout.diameterVal.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDiameter())));
            binding.pilotHoleDetailLayout.burdenEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBurden())));
            binding.pilotHoleDetailLayout.spacingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSpacing())));
            binding.pilotHoleDetailLayout.subgradeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSubgrade())));
            binding.pilotHoleDetailLayout.topXEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopX())));
            binding.pilotHoleDetailLayout.topYEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopY())));
            binding.pilotHoleDetailLayout.topZEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopZ())));
            binding.pilotHoleDetailLayout.bottomXEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomX())));
            binding.pilotHoleDetailLayout.bottomYEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomY())));
            binding.pilotHoleDetailLayout.bottomZEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomZ())));

            binding.pilotHoleDetailLayout.totalChargeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTotalCharge())));
            binding.pilotHoleDetailLayout.chargeLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getChargeLength())));
            binding.pilotHoleDetailLayout.stemmingLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getStemmingLength())));
            binding.pilotHoleDetailLayout.deckLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getDeckLength())));
            binding.pilotHoleDetailLayout.blockEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBlock())));
            binding.pilotHoleDetailLayout.blockLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBlockLength())));

            if (!Constants.isListEmpty(response3DTable17DataModelList)) {
                JsonArray inHoleDelayArr = new Gson().fromJson(new Gson().fromJson(response3DTable17DataModelList.get(0).getInHoleDelayArr(), String.class), JsonArray.class);
                if (inHoleDelayArr != null) {
                    if (inHoleDelayArr.size() > 0) {
                        binding.pilotHoleDetailLayout.inHoleDelayEt.setText(String.valueOf(inHoleDelayArr.get(0).getAsInt()));
                    } else {
                        binding.pilotHoleDetailLayout.inHoleDelayEt.setText("0");
                    }
                } else {
                    binding.pilotHoleDetailLayout.inHoleDelayEt.setText("0");
                }
            } else {
                binding.pilotHoleDetailLayout.inHoleDelayEt.setText("0");
            }
        }

        binding.pilotHoleDetailLayout.closeBtn.setOnClickListener(view -> {
            KeyboardUtils.hideSoftKeyboard(this);
            if (Constants.holeBgListener != null)
                Constants.holeBgListener.setBackgroundRefresh();
            if (Constants.hole3DBgListener != null)
                Constants.hole3DBgListener.setBackgroundRefresh();
            binding.holeDetailLayoutContainerForPilot.setVisibility(View.GONE);
        });

        binding.pilotHoleDetailLayout.saveProceedBtn.setOnClickListener(view -> {
            assert holeDetailData != null;
            holeDetailData.setHoleDepth(StringUtill.isEmpty(binding.pilotHoleDetailLayout.holeDepthEt.getText().toString()) ? "0" : binding.pilotHoleDetailLayout.holeDepthEt.getText().toString());
            holeDetailData.setVerticalDip(StringUtill.isEmpty(binding.pilotHoleDetailLayout.verticalDipEt.getText().toString()) ? "0" : binding.pilotHoleDetailLayout.verticalDipEt.getText().toString());

            updateEditedDataForPilotIntoDb(holeDetailData, false);
        });
    }

    public void set3dPreSplitHoleDetail(HoleDetailItem holeDetailData) {

        if (holeDetailData != null) {

            binding.preSplitHoleDetailLayout.holeIdEt.setText(String.format("%s", StringUtill.getString(String.valueOf(holeDetailData.getHoleId().toUpperCase()))));
            binding.preSplitHoleDetailLayout.holeTypeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleType())));
            binding.preSplitHoleDetailLayout.diameterVal.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDiameter())));
            binding.preSplitHoleDetailLayout.holeDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDepth())));
            binding.preSplitHoleDetailLayout.topEastingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopEasting())));
            binding.preSplitHoleDetailLayout.topNorthingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopNorthing())));
            binding.preSplitHoleDetailLayout.topRlEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTopRL())));
            binding.preSplitHoleDetailLayout.bottomEastingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomEasting())));
            binding.preSplitHoleDetailLayout.bottomNorthingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomNorthing())));
            binding.preSplitHoleDetailLayout.bottomRLEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBottomRL())));

            binding.preSplitHoleDetailLayout.totalChargeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getTotalCharge())));
            binding.preSplitHoleDetailLayout.chargeLengthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getChargeLength())));
            binding.preSplitHoleDetailLayout.deckingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getDecking())));

        }

        binding.preSplitHoleDetailLayout.closeBtn.setOnClickListener(view -> {
            KeyboardUtils.hideSoftKeyboard(this);
            if (Constants.holeBgListener != null)
                Constants.holeBgListener.setBackgroundRefresh();
            if (Constants.hole3DBgListener != null)
                Constants.hole3DBgListener.setBackgroundRefresh();
            binding.holeDetailLayoutContainerPreSplit.setVisibility(View.GONE);
        });

        binding.preSplitHoleDetailLayout.saveProceedBtn.setOnClickListener(view -> {
            assert holeDetailData != null;
            holeDetailData.setHoleDepth(StringUtill.isEmpty(binding.preSplitHoleDetailLayout.holeDepthEt.getText().toString()) ? "0" : binding.preSplitHoleDetailLayout.holeDepthEt.getText().toString());

            updateEditedDataForPreSplitIntoDb(holeDetailData, false);
        });
    }

    public void updateEditedDataIntoDb(Response3DTable4HoleChargingDataModel updateHoleDetailData, boolean isFromUpdateAdapter) {
        UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
        UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();
        ProjectHoleDetailRowColDao entity = appDatabase.projectHoleDetailRowColDao();

        bladesEntity.setData(new Gson().toJson(updateHoleDetailData));
        bladesEntity.setHoleId(Integer.parseInt(String.valueOf(updateHoleDetailData.getHoleNo())));
        bladesEntity.setRowId(Integer.parseInt(String.valueOf(updateHoleDetailData.getRowNo())));
        bladesEntity.setDesignId(String.valueOf(updateHoleDetailData.getDesignId()));

        if (!updateProjectBladesDao.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()), Integer.parseInt(String.valueOf(updateHoleDetailData.getRowNo())), Integer.parseInt(String.valueOf(updateHoleDetailData.getHoleNo())))) {
            updateProjectBladesDao.insertProject(bladesEntity);
        } else {
            updateProjectBladesDao.updateProject(bladesEntity);
        }

        JsonElement element = new Gson().fromJson(entity.getAllBladesProject(updateHoleDetailData.getDesignId()).getProjectHole(), JsonElement.class);
        List<Response3DTable4HoleChargingDataModel> allTablesData = new ArrayList<>();
        if (element != null) {
            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) element).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
            JsonArray jsonArray = new JsonArray();
            if (array.get(15) instanceof JsonArray) {
                jsonArray = new Gson().fromJson(array.get(15), JsonArray.class);
            } else {
                jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(15), String.class), JsonArray.class);
            }
            for (JsonElement e : jsonArray) {
                holeDetailDataList.add(new Gson().fromJson(e, Response3DTable4HoleChargingDataModel.class));
            }
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int i = 0; i < holeDetailDataList.size(); i++) {
                    if (String.valueOf(updateHoleDetailData.getRowNo()).equals(String.valueOf(holeDetailDataList.get(i).getRowNo())) && holeDetailDataList.get(i).getHoleID().equals(updateHoleDetailData.getHoleID())) {
                        if (!Constants.isListEmpty(holeDetailDataList.get(i).getChargeTypeArray())) {
                            double length = 0, deckLen = 0;
                            for (int j = 0; j < holeDetailDataList.get(i).getChargeTypeArray().size(); j++) {
                                ChargeTypeArrayItem item = holeDetailDataList.get(i).getChargeTypeArray().get(j);
                                if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                    length = length + item.getLength();
                                }
                                if (StringUtill.getString(item.getType()).equals("Decking")) {
                                    deckLen = deckLen + item.getLength();
                                }
                            }
                        }
                        holeDetailDataList.set(i, updateHoleDetailData);
                    } else {
                        if (!Constants.isListEmpty(holeDetailDataList.get(i).getChargeTypeArray())) {
                            double length = 0, deckLen = 0;
                            for (int j = 0; j < holeDetailDataList.get(i).getChargeTypeArray().size(); j++) {
                                ChargeTypeArrayItem item = holeDetailDataList.get(i).getChargeTypeArray().get(j);
                                if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                    length = length + item.getLength();
                                }
                                if (StringUtill.getString(item.getType()).equals("Decking")) {
                                    deckLen = deckLen + item.getLength();
                                }
                            }
                            Response3DTable4HoleChargingDataModel model = holeDetailDataList.get(i);
                            model.setStemmingLength(String.valueOf(length));
                            model.setDeckLength(String.valueOf(deckLen));
                            holeDetailDataList.set(i, model);
                        }
                        holeDetailDataList.set(i, holeDetailDataList.get(i));
                    }
                }

                array.set(15, new Gson().fromJson(new Gson().toJson(holeDetailDataList), JsonElement.class));
                JsonObject jsonObject = new JsonObject();
                JsonPrimitive primitive = new JsonPrimitive(new Gson().toJson(array));
                jsonObject.add(Constants._3D_TBALE_NAME, primitive);
                allTablesData = holeDetailDataList;

                if (!entity.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
                    entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), true, new Gson().toJson(jsonObject)));
                } else {
                    entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), new Gson().toJson(jsonObject));
                }

            }
        }

        try {
            ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
            ProjectHoleDetailRowColEntity colEntity = dao.getAllBladesProject(updateHoleDetailData.getDesignId());

            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) new Gson().fromJson(colEntity.getProjectHole(), JsonElement.class)).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
            for (JsonElement e : new Gson().fromJson(array.get(15), JsonArray.class)) {
                holeDetailDataList.add(new Gson().fromJson(e, Response3DTable4HoleChargingDataModel.class));
            }

            allTablesData = holeDetailDataList;

            if (this.rowItemDetail != null)
                this.rowItemDetail.setRowOfTable(this.rowPageVal, holeDetailDataList, isFromUpdateAdapter);

            if (this.mapViewDataUpdateLiveData != null)
                this.mapViewDataUpdateLiveData.setValue(true);

            if (Constants.holeBgListener != null)
                Constants.holeBgListener.setBackgroundRefresh();
            if (Constants.hole3DBgListener != null) {
                Constants.hole3DBgListener.setBackgroundRefresh();
            }
            AppDelegate.getInstance().setHoleChargingDataModel(allTablesData);
            this.holeDetailDataList = allTablesData;
            if (holeDetailCallBackListener != null)
                holeDetailCallBackListener.saveAndCloseHoleDetailCallBack();
            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void updateEditedDataForPilotIntoDb(Response3DTable16PilotDataModel updateHoleDetailData, boolean isFromUpdateAdapter) {
        UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
        UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();
        ProjectHoleDetailRowColDao entity = appDatabase.projectHoleDetailRowColDao();

        bladesEntity.setData(new Gson().toJson(updateHoleDetailData));
//        bladesEntity.setHoleId(Integer.parseInt(String.valueOf(updateHoleDetailData.getHoleNo())));
//        bladesEntity.setRowId(Integer.parseInt(String.valueOf(updateHoleDetailData.getRowNo())));
        bladesEntity.setDesignId(String.valueOf(updateHoleDetailData.getHoleID()));

        if (!updateProjectBladesDao.isExistProject(String.valueOf(updateHoleDetailData.getHoleID()))) {
            updateProjectBladesDao.insertProject(bladesEntity);
        } else {
            updateProjectBladesDao.updateProject(bladesEntity);
        }

        JsonElement element = new Gson().fromJson(entity.getAllBladesProject(updateHoleDetailData.getDesignId()).getProjectHole(), JsonElement.class);
        List<Response3DTable16PilotDataModel> allTablesData = new ArrayList<>();
        if (element != null) {
            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) element).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable16PilotDataModel> holeDetailDataList = new ArrayList<>();
            JsonArray jsonArray = new JsonArray();
            if (array.get(16) instanceof JsonArray) {
                jsonArray = new Gson().fromJson(array.get(16), JsonArray.class);
            } else {
                jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(16), String.class), JsonArray.class);
            }
            for (JsonElement e : jsonArray) {
                holeDetailDataList.add(new Gson().fromJson(e, Response3DTable16PilotDataModel.class));
            }
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int i = 0; i < holeDetailDataList.size(); i++) {
                    if (holeDetailDataList.get(i).getHoleID().equals(updateHoleDetailData.getHoleID())) {
                        if (!Constants.isListEmpty(holeDetailDataList.get(i).getChargeTypeArray())) {
                            double length = 0, deckLen = 0;
                            for (int j = 0; j < holeDetailDataList.get(i).getChargeTypeArray().size(); j++) {
                                ChargeTypeArrayItem item = holeDetailDataList.get(i).getChargeTypeArray().get(j);
                                if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                    length = length + item.getLength();
                                }
                                if (StringUtill.getString(item.getType()).equals("Decking")) {
                                    deckLen = deckLen + item.getLength();
                                }
                            }
                        }
                        holeDetailDataList.set(i, updateHoleDetailData);
                    } else {
                        if (!Constants.isListEmpty(holeDetailDataList.get(i).getChargeTypeArray())) {
                            double length = 0, deckLen = 0;
                            for (int j = 0; j < holeDetailDataList.get(i).getChargeTypeArray().size(); j++) {
                                ChargeTypeArrayItem item = holeDetailDataList.get(i).getChargeTypeArray().get(j);
                                if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                    length = length + item.getLength();
                                }
                                if (StringUtill.getString(item.getType()).equals("Decking")) {
                                    deckLen = deckLen + item.getLength();
                                }
                            }
                            Response3DTable16PilotDataModel model = holeDetailDataList.get(i);
                            model.setStemmingLength(String.valueOf(length));
                            model.setDeckLength(String.valueOf(deckLen));
                            holeDetailDataList.set(i, model);
                        }
                        holeDetailDataList.set(i, holeDetailDataList.get(i));
                    }
                }

                array.set(16, new Gson().fromJson(new Gson().toJson(holeDetailDataList), JsonElement.class));
                JsonObject jsonObject = new JsonObject();
                JsonPrimitive primitive = new JsonPrimitive(new Gson().toJson(array));
                jsonObject.add(Constants._3D_TBALE_NAME, primitive);
                allTablesData = holeDetailDataList;

                if (!entity.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
                    entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), true, new Gson().toJson(jsonObject)));
                } else {
                    entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), new Gson().toJson(jsonObject));
                }

            }
        }

        try {
            ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
            ProjectHoleDetailRowColEntity colEntity = dao.getAllBladesProject(updateHoleDetailData.getDesignId());

            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) new Gson().fromJson(colEntity.getProjectHole(), JsonElement.class)).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable16PilotDataModel> holeDetailDataList = new ArrayList<>();
            for (JsonElement e : new Gson().fromJson(array.get(16), JsonArray.class)) {
                holeDetailDataList.add(new Gson().fromJson(e, Response3DTable16PilotDataModel.class));
            }

            allTablesData = holeDetailDataList;

            if (this.rowItemDetail != null)
                this.rowItemDetail.setRowOfTableForPilot(this.rowPageVal, holeDetailDataList, isFromUpdateAdapter);

            if (this.mapViewDataUpdateLiveData != null)
                this.mapViewDataUpdateLiveData.setValue(true);

            if (Constants.holeBgListener != null)
                Constants.holeBgListener.setBackgroundRefresh();
            if (Constants.hole3DBgListener != null) {
                Constants.hole3DBgListener.setBackgroundRefresh();
            }
            AppDelegate.getInstance().setPilotDataModelList(allTablesData);
            this.pilotTableData = allTablesData;
            if (mapPilotCallBackListener != null)
                mapPilotCallBackListener.saveAndCloseHoleDetailForPilotCallBack();
            binding.holeDetailLayoutContainerForPilot.setVisibility(View.GONE);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void updateEditedDataForPreSplitIntoDb(HoleDetailItem updateHoleDetailData, boolean isFromUpdateAdapter) {
        UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
        UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();
        ProjectHoleDetailRowColDao entity = appDatabase.projectHoleDetailRowColDao();

        bladesEntity.setData(new Gson().toJson(updateHoleDetailData));
        /*bladesEntity.setHoleId(Integer.parseInt(String.valueOf(updateHoleDetailData.getHoleNo())));
        bladesEntity.setRowId(Integer.parseInt(String.valueOf(updateHoleDetailData.getRowNo())));*/
        bladesEntity.setDesignId(String.valueOf(updateHoleDetailData.getHoleId()));

        if (!updateProjectBladesDao.isExistProject(String.valueOf(updateHoleDetailData.getHoleId()))) {
            updateProjectBladesDao.insertProject(bladesEntity);
        } else {
            updateProjectBladesDao.updateProject(bladesEntity);
        }

        JsonElement element = new Gson().fromJson(entity.getAllBladesProject(updateHoleDetailData.getDesignId()).getProjectHole(), JsonElement.class);
        List<Response3DTable18PreSpilitDataModel> allTablesData = new ArrayList<>();
        if (element != null) {
            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) element).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable18PreSpilitDataModel> holeDetailDataList = new ArrayList<>();
            JsonArray jsonArray = new JsonArray();
            if (array.get(18) instanceof JsonArray) {
                jsonArray = new Gson().fromJson(array.get(18), JsonArray.class);
            } else {
                jsonArray = new Gson().fromJson(new Gson().fromJson(array.get(18), String.class), JsonArray.class);
            }
            for (JsonElement e : jsonArray) {
                Response3DTable18PreSpilitDataModel model = new Gson().fromJson(e, Response3DTable18PreSpilitDataModel.class);
                model.setHoleDetailStr(model.getHoleDetailStr());
                model.setHolePointsStr(model.getHolePointsStr());
                model.setLinePointsStr(model.getLinePointsStr());
                holeDetailDataList.add(model);
            }
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int ind = 0; ind < holeDetailDataList.size(); ind++) {
                    Response3DTable18PreSpilitDataModel model = holeDetailDataList.get(ind);
                    List<HoleDetailItem> itemList = new Gson().fromJson(holeDetailDataList.get(ind).getHoleDetailStr(), new TypeToken<List<HoleDetailItem>>() {
                    }.getType());
                    for (int i = 0; i < itemList.size(); i++) {
                        HoleDetailItem item = itemList.get(i);
                        if (item.getHoleId().equals(updateHoleDetailData.getHoleId())) {
                            itemList.set(i, updateHoleDetailData);
                        }
                    }
                    model.setHoleDetailStr(new Gson().toJson(itemList));
                    holeDetailDataList.set(ind, model);
                }

                for (int i = 0; i < holeDetailDataList.size(); i++) {
                    for (int a = 0; a < holeDetailDataList.get(i).getHoleDetail().size(); a++) {
                        if (holeDetailDataList.get(i).getHoleDetail().get(a).getHoleId().equals(updateHoleDetailData.getHoleId())) {
                            if (!Constants.isListEmpty(holeDetailDataList.get(i).getHoleDetail().get(a).getChargingArray())) {
                                double length = 0, deckLen = 0;
                                for (int j = 0; j < holeDetailDataList.get(i).getHoleDetail().get(j).getChargingArray().size(); j++) {
                                    ChargeTypeArrayItem item = holeDetailDataList.get(i).getHoleDetail().get(j).getChargingArray().get(j);
                                    if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                        length = length + item.getLength();
                                    }
                                    if (StringUtill.getString(item.getType()).equals("Decking")) {
                                        deckLen = deckLen + item.getLength();
                                    }
                                }
                            }
//                            holeDetailDataList.set(i, updateHoleDetailData);
                        } else {
                            if (!Constants.isListEmpty(holeDetailDataList.get(i).getHoleDetail().get(a).getChargingArray())) {
                                double length = 0, deckLen = 0;
                                for (int j = 0; j < holeDetailDataList.get(i).getHoleDetail().get(a).getChargingArray().size(); j++) {
                                    ChargeTypeArrayItem item = holeDetailDataList.get(i).getHoleDetail().get(a).getChargingArray().get(j);
                                    if (StringUtill.getString(item.getType()).equals("Stemming")) {
                                        length = length + item.getLength();
                                    }
                                    if (StringUtill.getString(item.getType()).equals("Decking")) {
                                        deckLen = deckLen + item.getLength();
                                    }
                                }
                                HoleDetailItem model = holeDetailDataList.get(i).getHoleDetail().get(a);
//                                model.setStemmingLength(String.valueOf(length));
                                model.setDecking(String.valueOf(deckLen));

                                List<HoleDetailItem> itemList = holeDetailDataList.get(i).getHoleDetail();
                                itemList.set(a, model);
                                holeDetailDataList.get(i).setHoleDetailStr(new Gson().toJson(itemList));
                            }
//                            holeDetailDataList.set(i, holeDetailDataList.get(i));
                        }
                    }
                    Response3DTable18PreSpilitDataModel model = holeDetailDataList.get(i);
                    model.setHoleDetailStr(new Gson().toJson(model.getHoleDetail()));
                    model.setHolePointsStr(new Gson().toJson(model.getHolePoints()));
                    model.setLinePointsStr(new Gson().toJson(model.getLinePoints()));
                    holeDetailDataList.set(i, model);
                }

                array.set(18, new Gson().fromJson(new Gson().toJson(holeDetailDataList), JsonElement.class));
                JsonObject jsonObject = new JsonObject();
                JsonPrimitive primitive = new JsonPrimitive(new Gson().toJson(array));
                jsonObject.add(Constants._3D_TBALE_NAME, primitive);
                allTablesData = holeDetailDataList;

                if (!entity.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
                    entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), true, new Gson().toJson(jsonObject)));
                } else {
                    entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), new Gson().toJson(jsonObject));
                }

            }
        }

        try {
            ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
            ProjectHoleDetailRowColEntity colEntity = dao.getAllBladesProject(updateHoleDetailData.getDesignId());

            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) new Gson().fromJson(colEntity.getProjectHole(), JsonElement.class)).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable18PreSpilitDataModel> holeDetailDataList = new ArrayList<>();
            for (JsonElement e : new Gson().fromJson(array.get(18), JsonArray.class)) {
                holeDetailDataList.add(new Gson().fromJson(e, Response3DTable18PreSpilitDataModel.class));
            }

            allTablesData = holeDetailDataList;

            if (this.rowItemDetail != null)
                this.rowItemDetail.setRowOfTableForPreSplit(this.rowPageVal, holeDetailDataList, isFromUpdateAdapter);

            if (this.mapViewDataUpdateLiveData != null)
                this.mapViewDataUpdateLiveData.setValue(true);

            if (Constants.holeBgListener != null)
                Constants.holeBgListener.setBackgroundRefresh();
            if (Constants.hole3DBgListener != null) {
                Constants.hole3DBgListener.setBackgroundRefresh();
            }
            AppDelegate.getInstance().setPreSpilitDataModelList(allTablesData);
            this.preSplitTableData = allTablesData;
            if (mapPreSplitCallBackListener != null)
                mapPreSplitCallBackListener.saveAndCloseHoleDetailForPreSplitCallBack();
            binding.holeDetailLayoutContainerPreSplit.setVisibility(View.GONE);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void getRockDensity() {
        String rockDensity = "";
        if (!Constants.isListEmpty(appDatabase.rockDataDao().getAllBladesProject())) {
            if (appDatabase.rockDataDao().getAllBladesProject().get(0) != null) {
                Type teamList = new TypeToken<List<ResponseRockData>>(){}.getType();
                List<ResponseRockData> drillMethodDataList = new ArrayList<>();
                if (!new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), JsonElement.class).isJsonArray()) {
                    drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                } else {
                    drillMethodDataList = new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), teamList);
                }
                if (!Constants.isListEmpty(drillMethodDataList)) {
                    String[] rockCodeItem = new String[drillMethodDataList.size()];
                    for (int i = 0; i < drillMethodDataList.size(); i++) {
                        rockCodeItem[i] = String.valueOf(drillMethodDataList.get(i).getRockCode());
                    }

                    boolean isFound = false;
                    for (String s : rockCodeItem) {
                        if (StringUtill.getString(bladesRetrieveData.get(0).getRockCode()).equals(s)) {
                            isFound = true;
                            rockDensity = drillMethodDataList.get(0).getDensity();
                            JsonObject jsonObject = AppDelegate.getInstance().getCodeIdObject();
                            jsonObject.addProperty("rockDensity", rockDensity);
                            AppDelegate.getInstance().setCodeIdObject(jsonObject);
                            break;
                        }
                    }
                    if (!isFound) {
                        rockDensity = drillMethodDataList.get(0).getDensity();
                        JsonObject jsonObject = AppDelegate.getInstance().getCodeIdObject();
                        jsonObject.addProperty("rockDensity", rockDensity);
                        AppDelegate.getInstance().setCodeIdObject(jsonObject);
                    }
                }
            }
        }
    }

}