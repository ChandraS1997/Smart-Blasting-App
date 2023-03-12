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
import com.google.gson.JsonPrimitive;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.databinding.HoleDetailActivityBinding;
import com.smart_blasting_drilling.android.dialogs.HoleDetailDialog;
import com.smart_blasting_drilling.android.dialogs.HoleEditTableFieldSelectionDialog;
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

public class HoleDetailActivity extends BaseActivity implements View.OnClickListener, OnHoleClickListener {
    public NavController navController;
    public AllTablesData allTablesData;
    public ResponseBladesRetrieveData bladesRetrieveData;
    public boolean isTableHeaderFirstTimeLoad = true;
    public List<ResponseHoleDetailData> holeDetailDataList = new ArrayList<>();
    public int rowPageVal = 1;
    public RowItemDetail rowItemDetail;
    public boolean isFound;
    public MutableLiveData<Boolean> mapViewDataUpdateLiveData = new MutableLiveData<>();
    public HoleDetailActivityBinding binding;
    List<String> rowList = new ArrayList<>();
    public HoleDetailCallBackListener holeDetailCallBackListener;

    public interface HoleDetailCallBackListener {
        void setHoleDetailCallBack(ResponseBladesRetrieveData data, ResponseHoleDetailData detailData);
        void saveAndCloseHoleDetailCallBack(ResponseHoleDetailData detailData);
    }

    public void setDataFromBundle() {
        // if (isBundleIntentNotEmpty()) {
        bladesRetrieveData = AppDelegate.getInstance().getBladesRetrieveData();
        allTablesData = AppDelegate.getInstance().getAllTablesData();
        try {
            if (!allTablesData.getTable2().isEmpty()) {
                for (int i = 0; i < allTablesData.getTable2().size(); i++) {
                    isFound = false;
                    if (!rowList.isEmpty()) {
                        for (int j = 0; j < rowList.size(); j++) {
                            if (rowList.get(j).replace("Row ", "").equals(String.valueOf(allTablesData.getTable2().get(i).getRowNo()))) {
                                isFound = true;
                                break;
                            }
                        }
                    }
                    if (!isFound)
                        rowList.add("Row " + allTablesData.getTable2().get(i).getRowNo());
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        if (!appDatabase.allProjectBladesModelDao().isExistItem(bladesRetrieveData.getDesignId())) {
            setJsonForSyncProjectData(bladesRetrieveData, allTablesData.getTable2());
        } else {
            AllProjectBladesModelEntity entity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            if (StringUtill.isEmpty(entity.getProjectCode())) {
                setJsonForSyncProjectData(bladesRetrieveData, allTablesData.getTable2());
            }
        }

//            setJsonForSyncProjectData(bladesRetrieveData, allTablesData.getTable2());
//        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataFromBundle();
        binding = DataBindingUtil.setContentView(this, R.layout.hole_detail_activity);

        String[] rowSpinnerList = new String[rowList.size()];
        for (int i = 0; i < rowList.size(); i++) {
            rowSpinnerList[i] = rowList.get(i);
        }

        if (rowSpinnerList.length > 0) {
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
                    blastInsertSyncRecordApiCaller(bladesRetrieveData, allTablesData, getRowWiseHoleList(allTablesData.getTable2()).size(), 0, blastCode);
                }

                @Override
                public void syncWithBlades() {
                    if (!bladesRetrieveData.isIs3dBlade())
                        insertActualDesignChartSheetApiCaller(allTablesData, bladesRetrieveData);
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
        binding.headerLayHole.refreshIcn.setOnClickListener(view -> syncDataAPi());
    }

    public void syncDataAPi() {
        AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(bladesRetrieveData.getDesignId()));
        setInsertUpdateHoleDetailMultipleSync(bladesRetrieveData, allTablesData.getTable2(), (modelEntity != null && !StringUtill.isEmpty(modelEntity.getProjectCode())) ? modelEntity.getProjectCode() : "").observe(this, new Observer<JsonPrimitive>() {
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
        if (!Constants.isListEmpty(manger.getTableField())) {
            isTableHeaderFirstTimeLoad = false;
            return manger.getTableField();
        }

        List<TableEditModel> editModelArrayList = new ArrayList<>();
        editModelArrayList.add(new TableEditModel("Row No", "Row No"));
        editModelArrayList.add(new TableEditModel("Hole No", "Hole No"));
        editModelArrayList.add(new TableEditModel("Hole Id", "Hole Id"));
        editModelArrayList.add(new TableEditModel("Hole Depth", "Hole Depth"));
        editModelArrayList.add(new TableEditModel("Hole Status", "Hole Status"));
        editModelArrayList.add(new TableEditModel("Hole Angle", "Hole Angle"));
        editModelArrayList.add(new TableEditModel("Diameter", "Diameter"));
        editModelArrayList.add(new TableEditModel("Burden", "Burden"));
        editModelArrayList.add(new TableEditModel("Spacing", "Spacing"));
        editModelArrayList.add(new TableEditModel("X", "X"));
        editModelArrayList.add(new TableEditModel("Y", "Y"));
        editModelArrayList.add(new TableEditModel("Z","Z"));
        editModelArrayList.add(new TableEditModel("Charging", "Charging"));
        return editModelArrayList;
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
                bundle.putString("blades_data", bladesRetrieveData.getDesignId());
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
                bundle.putString("blades_data", bladesRetrieveData.getDesignId());
                intent = new Intent(this, MediaActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.projectBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ProjectDetailDialog infoDialogFragment = ProjectDetailDialog.getInstance(bladesRetrieveData);
                ft.add(infoDialogFragment, ProjectDetailDialog.TAG);
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
                startActivity(new Intent(this, MediaActivity.class));
                break;
            case R.id.initiatingDeviceContainer:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                bundle.putString("blades_data", bladesRetrieveData.getDesignId());
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
        HoleEditTableFieldSelectionDialog infoDialogFragment = HoleEditTableFieldSelectionDialog.getInstance();
        ft.add(infoDialogFragment, HoleEditTableFieldSelectionDialog.TAG);
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

    public void openHoleDetailDialog(ResponseHoleDetailData holeDetailData) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        HoleDetailDialog infoDialogFragment = HoleDetailDialog.getInstance(holeDetailData, bladesRetrieveData);

        infoDialogFragment.setUpListener((dialogFragment, designId) -> {
            ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
            ProjectHoleDetailRowColEntity entity = dao.getAllBladesProject(designId);
            allTablesData = new Gson().fromJson(entity.getProjectHole(), AllTablesData.class);

            if (rowItemDetail != null)
                rowItemDetail.setRowOfTable(rowPageVal, allTablesData);

            if (mapViewDataUpdateLiveData != null)
                mapViewDataUpdateLiveData.setValue(true);

            dialogFragment.dismiss();
        });

        ft.add(infoDialogFragment, HoleDetailDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    public interface RowItemDetail {
        void setRowOfTable(int rowNo, AllTablesData allTablesData);
    }

    public void setHoleDetailDialog(ResponseBladesRetrieveData bladesRetrieveData, ResponseHoleDetailData holeDetailData) {
        ResponseHoleDetailData updateHoleDetailData = holeDetailData;
        if (holeDetailData != null) {
            binding.holeDetailLayout.holeDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDepth())));
            String[] status = new String[]{"Pending Hole", "Work in Progress", "Completed", "Deleted/ Blocked holes/ Do not blast"};
            binding.holeDetailLayout.holeStatusSpinner.setAdapter(Constants.getAdapter(this, status));

            if (StringUtill.isEmpty(holeDetailData.getHoleStatus())) {
                binding.holeDetailLayout.holeStatusSpinner.setText("Pending Hole");
            } else {
                binding.holeDetailLayout.holeStatusSpinner.setText(StringUtill.getString(holeDetailData.getHoleStatus()));
            }

            binding.holeDetailLayout.holeAngleEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleAngle())));
            binding.holeDetailLayout.diameterEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDiameter())));
            binding.holeDetailLayout.burdenEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBurden())));
            binding.holeDetailLayout.spacingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSpacing())));
            binding.holeDetailLayout.xEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getX())));
            binding.holeDetailLayout.yTxtEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getY())));
            binding.holeDetailLayout.zEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getZ())));
        }

        binding.holeDetailLayout.closeBtn.setOnClickListener(view -> {
            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        });

        binding.holeDetailLayout.saveProceedBtn.setOnClickListener(view -> {

            updateHoleDetailData.setHoleDepth(StringUtill.isEmpty(binding.holeDetailLayout.holeDepthEt.getText().toString()) ? 0 : Integer.parseInt(binding.holeDetailLayout.holeDepthEt.getText().toString()));
            updateHoleDetailData.setHoleAngle(StringUtill.isEmpty(binding.holeDetailLayout.holeAngleEt.getText().toString()) ? 0 : Integer.parseInt(binding.holeDetailLayout.holeAngleEt.getText().toString()));
            updateHoleDetailData.setHoleDiameter(StringUtill.isEmpty(binding.holeDetailLayout.diameterEt.getText().toString()) ? 0 : Integer.parseInt(binding.holeDetailLayout.diameterEt.getText().toString()));
            updateHoleDetailData.setBurden(StringUtill.isEmpty(binding.holeDetailLayout.burdenEt.getText().toString()) ? 0 : binding.holeDetailLayout.burdenEt.getText().toString());
            updateHoleDetailData.setSpacing(StringUtill.isEmpty(binding.holeDetailLayout.spacingEt.getText().toString()) ? 0 : binding.holeDetailLayout.spacingEt.getText().toString());
            updateHoleDetailData.setX(StringUtill.isEmpty(binding.holeDetailLayout.xEt.getText().toString()) ? 0 : binding.holeDetailLayout.xEt.getText().toString());
            updateHoleDetailData.setY(StringUtill.isEmpty(binding.holeDetailLayout.yTxtEt.getText().toString()) ? 0 : binding.holeDetailLayout.yTxtEt.getText().toString());
            updateHoleDetailData.setZ(StringUtill.isEmpty(binding.holeDetailLayout.zEt.getText().toString()) ? 0 : binding.holeDetailLayout.zEt.getText().toString());
            updateHoleDetailData.setHoleStatus(StringUtill.getString(binding.holeDetailLayout.holeStatusSpinner.getText().toString()));

            updateEditedDataIntoDb(updateHoleDetailData);

            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        });
    }

    public void updateEditedDataIntoDb(ResponseHoleDetailData updateHoleDetailData) {
        ProjectHoleDetailRowColDao entity = appDatabase.projectHoleDetailRowColDao();
        UpdateProjectBladesEntity bladesEntity = new UpdateProjectBladesEntity();
        UpdateProjectBladesDao updateProjectBladesDao = appDatabase.updateProjectBladesDao();

        bladesEntity.setData(new Gson().toJson(updateHoleDetailData));
        bladesEntity.setHoleId(updateHoleDetailData.getHoleNo());
        bladesEntity.setRowId((int) updateHoleDetailData.getRowNo());
        bladesEntity.setDesignId(String.valueOf(updateHoleDetailData.getDesignId()));

        if (!updateProjectBladesDao.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()), (int) updateHoleDetailData.getRowNo(), updateHoleDetailData.getHoleNo())) {
            updateProjectBladesDao.insertProject(bladesEntity);
        } else {
            updateProjectBladesDao.updateProject(bladesEntity);
        }

        String dataStr = "";

        AllTablesData allTablesData = this.allTablesData;
        if (allTablesData != null) {
            List<ResponseHoleDetailData> holeDetailDataList = allTablesData.getTable2();
            if (!Constants.isListEmpty(holeDetailDataList)) {
                for (int i = 0; i < holeDetailDataList.size(); i++) {
                    if (holeDetailDataList.get(i).getRowNo() == updateHoleDetailData.getRowNo() && holeDetailDataList.get(i).getHoleID().equals(updateHoleDetailData.getHoleID())) {
                        holeDetailDataList.set(i, updateHoleDetailData);
                    } else {
                        holeDetailDataList.set(i, holeDetailDataList.get(i));
                    }
                }

                allTablesData.setTable2(holeDetailDataList);
                dataStr = new Gson().toJson(allTablesData);

                if (!entity.isExistProject(String.valueOf(updateHoleDetailData.getDesignId()))) {
                    entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(updateHoleDetailData.getDesignId()), bladesRetrieveData.isIs3dBlade(), dataStr));
                } else {
                    entity.updateProject(String.valueOf(updateHoleDetailData.getDesignId()), dataStr);
                }

            }
        }

        AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(updateHoleDetailData.getDesignId()));

//            ((BaseActivity) mContext).setInsertUpdateHoleDetailSync(((HoleDetailActivity) mContext).bladesRetrieveData, updateHoleDetailData, modelEntity != null ? modelEntity.getProjectCode() : "0");

        ProjectHoleDetailRowColDao dao = appDatabase.projectHoleDetailRowColDao();
        ProjectHoleDetailRowColEntity colEntity = dao.getAllBladesProject(String.valueOf(updateHoleDetailData.getDesignId()));
        allTablesData = new Gson().fromJson(colEntity.getProjectHole(), AllTablesData.class);

        if (((HoleDetailActivity) this).rowItemDetail != null)
            ((HoleDetailActivity) this).rowItemDetail.setRowOfTable(((HoleDetailActivity) this).rowPageVal, allTablesData);

        if (((HoleDetailActivity) this).mapViewDataUpdateLiveData != null)
            ((HoleDetailActivity) this).mapViewDataUpdateLiveData.setValue(true);
    }

}
