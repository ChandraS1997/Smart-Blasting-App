package com.smart_blasting_drilling.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.databinding.HoleDetail3dActivityBinding;
import com.smart_blasting_drilling.android.databinding.HoleDetailActivityBinding;
import com.smart_blasting_drilling.android.dialogs.Hole3dEditTableFieldSelectionDialog;
import com.smart_blasting_drilling.android.dialogs.HoleDetail3dDialog;
import com.smart_blasting_drilling.android.dialogs.HoleDetailDialog;
import com.smart_blasting_drilling.android.dialogs.HoleEditTableFieldSelectionDialog;
import com.smart_blasting_drilling.android.dialogs.ProjectDetail3DDataDialog;
import com.smart_blasting_drilling.android.dialogs.ProjectDetailDialog;
import com.smart_blasting_drilling.android.dialogs.SyncProjectOptionDialog;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.OnHoleClickListener;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleDetail3DModelActivity extends BaseActivity implements View.OnClickListener, OnHoleClickListener {
    public NavController navController;
    public HoleDetail3dActivityBinding binding;
    public List<Response3DTable4HoleChargingDataModel> allTablesData = new ArrayList<>();
    public List<Response3DTable1DataModel> bladesRetrieveData = new ArrayList<>();
    public boolean isTableHeaderFirstTimeLoad = true;
    public JsonElement element;

    public List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
    public int rowPageVal = 1;
    public RowItemDetail rowItemDetail;
    List<String> rowList = new ArrayList<>();

    public MutableLiveData<Boolean> mapViewDataUpdateLiveData = new MutableLiveData<>();

    public HoleDetail3DModelActivity.HoleDetailCallBackListener holeDetailCallBackListener;

    public interface HoleDetailCallBackListener {
        void setHoleDetailCallBack(Response3DTable4HoleChargingDataModel detailData);
        void saveAndCloseHoleDetailCallBack(Response3DTable4HoleChargingDataModel detailData);
    }

    public void setDataFromBundle() {
        bladesRetrieveData = AppDelegate.getInstance().getResponse3DTable1DataModel();
        allTablesData = AppDelegate.getInstance().getHoleChargingDataModel();
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

            /*if (!appDatabase.allProjectBladesModelDao().isExistItem(bladesRetrieveData.get(0).getDesignId())) {
                setJsonForSyncProjectData(bladesRetrieveData, allTablesData);
            } else {
                AllProjectBladesModelEntity entity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
                if (StringUtill.isEmpty(entity.getProjectCode())) {
                    setJsonForSyncProjectData(bladesRetrieveData, allTablesData.getTable2());
                }
            }*/
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataFromBundle();
        binding = DataBindingUtil.setContentView(this, R.layout.hole_detail_3d_activity);

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
                    rowItemDetail.setRowOfTable(rowPageVal, allTablesData);
            });
        } else {
            binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
        }

        navController = Navigation.findNavController(this, R.id.nav_host_hole);
        Constants.onHoleClickListener = this;

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
                    syncDataAPi();
                }

                @Override
                public void syncWithBims() {
                    String blastCode = "";
                    if (!Constants.isListEmpty(appDatabase.blastCodeDao().getAllEntityDataList())) {
                        BlastCodeEntity blastCodeEntity = appDatabase.blastCodeDao().getSingleItemEntity(1);
                        if (blastCodeEntity != null)
                            blastCode = blastCodeEntity.getBlastCode();
                    }
                    blastInsertSyncRecord3DApiCaller(bladesRetrieveData.get(0), allTablesData, getRowWiseHoleIn3dList(allTablesData).size(), 0, blastCode);
                }

                @Override
                public void syncWithBlades() {
                    insertUpdate3DActualDesignHoleDetailApiCaller(allTablesData, bladesRetrieveData);
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

    public void syncDataAPi() {
        AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(bladesRetrieveData.get(0).getDesignId()));
        setInsertUpdateHoleDetailMultipleSync3D(bladesRetrieveData.get(0), allTablesData, (modelEntity != null  && !StringUtill.isEmpty(modelEntity.getProjectCode())) ? modelEntity.getProjectCode() : "").observe(this, new Observer<JsonPrimitive>() {
            @Override
            public void onChanged(JsonPrimitive response) {
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

    public interface RowItemDetail {
        void setRowOfTable(int rowNo, List<Response3DTable4HoleChargingDataModel> allTablesData);
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
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.galleryBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                bundle.putString("blades_data", bladesRetrieveData.get(0).getDesignId());
                intent = new Intent(this, MediaActivity.class);
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
            case R.id.homeBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.mapBtn:
                binding.headerLayHole.projectInfo.setVisibility(View.GONE);
                binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
                binding.holeParaLay.setVisibility(View.GONE);
                navController.navigate(R.id.mapViewFrament);
                break;
            case R.id.camIcon:
                startActivity(new Intent(this, MediaActivity.class));
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
                binding.headerLayHole.spinnerRow.setVisibility(View.VISIBLE);
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
        manger.logoutUser();
        startActivity(new Intent(this, AuthActivity.class));
        finishAffinity();
    }

    public void editTable() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Hole3dEditTableFieldSelectionDialog infoDialogFragment = Hole3dEditTableFieldSelectionDialog.getInstance();
        ft.add(infoDialogFragment, Hole3dEditTableFieldSelectionDialog.TAG);
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

    public void openHoleDetailDialog(Response3DTable4HoleChargingDataModel holeDetailData) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HoleDetail3dDialog infoDialogFragment = HoleDetail3dDialog.getInstance(holeDetailData, bladesRetrieveData.get(0));

        infoDialogFragment.setUpListener((dialogFragment, designId) -> {
            try {
                ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
                ProjectHoleDetailRowColEntity entity = dao.getAllBladesProject(designId);

                JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) new Gson().fromJson(entity.getProjectHole(), JsonElement.class)).get("GetAll3DDesignInfoResult").getAsJsonPrimitive(), String.class), JsonArray.class);
                List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
                for (JsonElement e : new Gson().fromJson(array.get(3), JsonArray.class)) {
                    holeDetailDataList.add(new Gson().fromJson(e, Response3DTable4HoleChargingDataModel.class));
                }

                allTablesData = holeDetailDataList;

                if (rowItemDetail != null)
                    rowItemDetail.setRowOfTable(rowPageVal, holeDetailDataList);

                if (mapViewDataUpdateLiveData != null)
                    mapViewDataUpdateLiveData.setValue(true);

                dialogFragment.dismiss();
            } catch (Exception e) {
                e.getLocalizedMessage();
            }

        });

        ft.add(infoDialogFragment, HoleDetailDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    public void set3dHoleDetail(Response3DTable1DataModel bladesRetrieveData, Response3DTable4HoleChargingDataModel holeDetailData) {

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
            binding.holeDetailLayout.inHoleDelayEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getInHoleDelay())));
            binding.holeDetailLayout.waterDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getWaterDepth())));
            binding.holeDetailLayout.subgradeEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSubgrade())));
        }

        binding.holeDetailLayout.closeBtn.setOnClickListener(view -> {
            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        });

        binding.holeDetailLayout.saveProceedBtn.setOnClickListener(view -> {

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

            updateHoleDetailData.setSubgrade(StringUtill.isEmpty(binding.holeDetailLayout.subgradeEt.getText().toString()) ? "0" : binding.holeDetailLayout.bottomYEt.getText().toString());
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

            updateEditedDataIntoDb(updateHoleDetailData);
        });
    }

    public void updateEditedDataIntoDb(Response3DTable4HoleChargingDataModel updateHoleDetailData) {
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

        String dataStr = "";

        JsonElement element = AppDelegate.getInstance().getHole3DDataElement();
        List<Response3DTable4HoleChargingDataModel> allTablesData = new ArrayList<>();
        if (element != null) {
            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) element).get("GetAll3DDesignInfoResult").getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
            for (JsonElement e : new Gson().fromJson(new Gson().fromJson(array.get(3), String.class), JsonArray.class)) {
                holeDetailDataList.add(new Gson().fromJson(e, Response3DTable4HoleChargingDataModel.class));
            }
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int i = 0; i < holeDetailDataList.size(); i++) {
                    if (String.valueOf(updateHoleDetailData.getRowNo()).equals(String.valueOf(holeDetailDataList.get(i).getRowNo())) && holeDetailDataList.get(i).getHoleID().equals(updateHoleDetailData.getHoleID())) {
                        holeDetailDataList.set(i, updateHoleDetailData);
                    } else {
                        holeDetailDataList.set(i, holeDetailDataList.get(i));
                    }
                }

                array.set(3, new Gson().fromJson(new Gson().toJson(holeDetailDataList), JsonElement.class));
                JsonObject jsonObject = new JsonObject();
                JsonPrimitive primitive = new JsonPrimitive(new Gson().toJson(array));
                jsonObject.add("GetAll3DDesignInfoResult", primitive);
                allTablesData = holeDetailDataList;
                dataStr = new Gson().toJson(allTablesData);

                if (!entity.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
                    entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), true, new Gson().toJson(jsonObject)));
                } else {
                    entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), new Gson().toJson(jsonObject));
                }

            }
        }

        AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(updateHoleDetailData.getDesignId()));

//            ((BaseActivity) mContext).setInsertUpdateHoleDetailSync(((HoleDetailActivity) mContext).bladesRetrieveData, updateHoleDetailData, modelEntity != null ? modelEntity.getProjectCode() : "0");

        try {
            ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
            ProjectHoleDetailRowColEntity colEntity = dao.getAllBladesProject(updateHoleDetailData.getDesignId());

            JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) new Gson().fromJson(colEntity.getProjectHole(), JsonElement.class)).get("GetAll3DDesignInfoResult").getAsJsonPrimitive(), String.class), JsonArray.class);
            List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
            for (JsonElement e : new Gson().fromJson(array.get(3), JsonArray.class)) {
                holeDetailDataList.add(new Gson().fromJson(e, Response3DTable4HoleChargingDataModel.class));
            }

            allTablesData = holeDetailDataList;

            if (((HoleDetail3DModelActivity) this).rowItemDetail != null)
                ((HoleDetail3DModelActivity) this).rowItemDetail.setRowOfTable(((HoleDetail3DModelActivity) this).rowPageVal, holeDetailDataList);

            if (((HoleDetail3DModelActivity) this).mapViewDataUpdateLiveData != null)
                ((HoleDetail3DModelActivity) this).mapViewDataUpdateLiveData.setValue(true);

            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

}