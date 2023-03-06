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
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleDetail3DModelActivity extends BaseActivity implements View.OnClickListener, OnHoleClickListener {
    public NavController navController;
    HoleDetail3dActivityBinding binding;
    public List<Response3DTable4HoleChargingDataModel> allTablesData = new ArrayList<>();
    public List<Response3DTable1DataModel> bladesRetrieveData = new ArrayList<>();
    public boolean isTableHeaderFirstTimeLoad = true;
    public JsonElement element;

    public List<Response3DTable4HoleChargingDataModel> holeDetailDataList = new ArrayList<>();
    public int rowPageVal = 1;
    public RowItemDetail rowItemDetail;
    List<String> rowList = new ArrayList<>();

    public MutableLiveData<Boolean> mapViewDataUpdateLiveData = new MutableLiveData<>();

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
        editModelArrayList.add(new TableEditModel("Row No"));
        editModelArrayList.add(new TableEditModel("Hole No"));
        editModelArrayList.add(new TableEditModel("Hole Id"));
        editModelArrayList.add(new TableEditModel("Hole Type"));
        editModelArrayList.add(new TableEditModel("Hole Depth"));
        editModelArrayList.add(new TableEditModel("Hole Status"));
        editModelArrayList.add(new TableEditModel("VerticalDip"));
        editModelArrayList.add(new TableEditModel("Diameter"));
        editModelArrayList.add(new TableEditModel("Burden"));
        editModelArrayList.add(new TableEditModel("Spacing"));
        editModelArrayList.add(new TableEditModel("Subgrade"));
        editModelArrayList.add(new TableEditModel("TopX"));
        editModelArrayList.add(new TableEditModel("TopY"));
        editModelArrayList.add(new TableEditModel("TopZ"));
        editModelArrayList.add(new TableEditModel("BottomX"));
        editModelArrayList.add(new TableEditModel("BottomY"));
        editModelArrayList.add(new TableEditModel("BottomZ"));
        editModelArrayList.add(new TableEditModel("TotalCharge"));
        editModelArrayList.add(new TableEditModel("ChargeLength"));
        editModelArrayList.add(new TableEditModel("StemmingLength"));
        editModelArrayList.add(new TableEditModel("DeckLength"));
        editModelArrayList.add(new TableEditModel("Block"));
        editModelArrayList.add(new TableEditModel("BlockLength"));
        editModelArrayList.add(new TableEditModel("InHoleDelay"));
        editModelArrayList.add(new TableEditModel("Charging"));
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

}