package com.smart_blasting_drilling.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.databinding.HoleDetailActivityBinding;
import com.smart_blasting_drilling.android.dialogs.AppAlertDialogFragment;
import com.smart_blasting_drilling.android.dialogs.HoleDetailDialog;
import com.smart_blasting_drilling.android.dialogs.HoleEditTableFieldSelectionDialog;
import com.smart_blasting_drilling.android.dialogs.ProjectDetailDialog;
import com.smart_blasting_drilling.android.dialogs.SyncProjectOptionDialog;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.OnHoleClickListener;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.KeyboardUtils;
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
    public int updateValPos = -1, updateRowNo = -1, updateHoleNo = -1, rowPos = -1;

    public interface HoleDetailCallBackListener {
        void setHoleDetailCallBack(ResponseBladesRetrieveData data, ResponseHoleDetailData detailData);
        void setBgOfHoleOnNewRowChange(int row, int hole, int pos);
        void saveAndCloseHoleDetailCallBack();
    }

    public void setDataFromBundle() {
        // if (isBundleIntentNotEmpty()) {
        bladesRetrieveData = AppDelegate.getInstance().getBladesRetrieveData();
        allTablesData = AppDelegate.getInstance().getAllTablesData();
        try {
            if (!Constants.isListEmpty(allTablesData.getTable1())) {
                bladesRetrieveData.setRockName(allTablesData.getTable1().get(0).getRockName());
                bladesRetrieveData.setRockCode(String.valueOf(allTablesData.getTable1().get(0).getRockCode()));
            }
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
            setJsonForSyncProjectData(bladesRetrieveData, allTablesData).observe((LifecycleOwner) this, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement jsonPrimitive) {
                    allTablesData = AppDelegate.getInstance().getAllTablesData();
                }
            });
        } else {
            AllProjectBladesModelEntity entity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            if (StringUtill.isEmpty(entity.getProjectCode())) {
                setJsonForSyncProjectData(bladesRetrieveData, allTablesData).observe((LifecycleOwner) this, new Observer<JsonElement>() {
                    @Override
                    public void onChanged(JsonElement jsonPrimitive) {
                        allTablesData = AppDelegate.getInstance().getAllTablesData();
                    }
                });
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
                        if (!Constants.isListEmpty(allTablesData.getTable())) {
                            if (!StringUtill.isEmpty(String.valueOf(allTablesData.getTable().get(0).getBimsId()))) {
                                blastCode = String.valueOf(allTablesData.getTable().get(0).getBimsId());
                            } else {
                                if (!Constants.isListEmpty(appDatabase.blastCodeDao().getAllEntityDataList())) {
                                    BlastCodeEntity blastCodeEntity = appDatabase.blastCodeDao().getSingleItemEntityByDesignId(bladesRetrieveData.getDesignId());
                                    if (blastCodeEntity != null)
                                        blastCode = blastCodeEntity.getBlastCode();
                                }
                            }
                        } else {
                            if (!Constants.isListEmpty(appDatabase.blastCodeDao().getAllEntityDataList())) {
                                BlastCodeEntity blastCodeEntity = appDatabase.blastCodeDao().getSingleItemEntityByDesignId(bladesRetrieveData.getDesignId());
                                if (blastCodeEntity != null)
                                    blastCode = blastCodeEntity.getBlastCode();
                            }
                        }
                        blastInsertSyncRecordApiCaller(bladesRetrieveData, allTablesData, getRowWiseHoleList(allTablesData.getTable2()).size(), 0, blastCode).observe(HoleDetailActivity.this, new Observer<JsonElement>() {
                            @Override
                            public void onChanged(JsonElement element) {
                                allTablesData = AppDelegate.getInstance().getAllTablesData();
                            }
                        });
                    } else {
                        showToast("No internet connection. Make sure Wi-Fi or Cellular data is turn on, then try again");
                    }
                }

                @Override
                public void syncWithBlades() {
                    if (ConnectivityReceiver.getInstance().isInternetAvailable()) {
                        insertActualDesignChartSheetApiCaller(allTablesData, bladesRetrieveData);
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
        binding.headerLayHole.refreshIcn.setOnClickListener(view -> syncDataAPi());
    }

    public void syncDataAPi() {
        AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(String.valueOf(bladesRetrieveData.getDesignId()));
        String code = "";
        code = String.valueOf(allTablesData.getTable().get(0).getDrimsId());
        if (StringUtill.isEmpty(code)) {
            code = (modelEntity != null  && !StringUtill.isEmpty(modelEntity.getProjectCode())) ? modelEntity.getProjectCode() : "";
        }
        setInsertUpdateHoleDetailMultipleSync(bladesRetrieveData, allTablesData.getTable2(), allTablesData, code).observe(this, new Observer<JsonElement>() {
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
        editModelArrayList.add(new TableEditModel("Z", "Z"));
        editModelArrayList.add(new TableEditModel("Charging", "Charging"));
        return editModelArrayList;
    }

    private void setNavController() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.holeDetailsTableViewFragment) {
                    binding.holeDetailLayoutContainer.setVisibility(View.GONE);
                    binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
                    binding.headerLayHole.spinnerRow.setVisibility(View.VISIBLE);
                    binding.holeParaLay.setVisibility(View.GONE);
                } else if (navDestination.getId() == R.id.mapViewFrament) {
                    binding.headerLayHole.projectInfo.setVisibility(View.GONE);
                    binding.headerLayHole.spinnerRow.setVisibility(View.GONE);
                    binding.holeParaLay.setVisibility(View.GONE);
                }
            }
        });
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
            case R.id.camIcon:
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
            case R.id.initiatingDeviceContainer:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                bundle.putString("blades_data", bladesRetrieveData.getDesignId());
                intent = new Intent(this, InitiatingDeviceViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.listBtn:
                binding.holeDetailLayoutContainer.setVisibility(View.GONE);
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
        showAlertDialog("Logout", "Are you sure you want to logout?", "Yes", "No", new AppAlertDialogFragment.AppAlertDialogListener() {
            @Override
            public void onOk(AppAlertDialogFragment dialogFragment) {
                dialogFragment.dismiss();
                manger.logoutUser();
                startActivity(new Intent(HoleDetailActivity.this, AuthActivity.class));
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
                rowItemDetail.setRowOfTable(rowPageVal, allTablesData, false);

            if (mapViewDataUpdateLiveData != null)
                mapViewDataUpdateLiveData.setValue(true);

            dialogFragment.dismiss();
        });

        ft.add(infoDialogFragment, HoleDetailDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    public interface RowItemDetail {
        void setRowOfTable(int rowNo, AllTablesData allTablesData, boolean isFromUpdateAdapter);
    }

    public void setHoleDetailDialog(ResponseBladesRetrieveData bladesRetrieveData, ResponseHoleDetailData holeDetailData) {
        ResponseHoleDetailData updateHoleDetailData = holeDetailData;
        if (holeDetailData != null) {
            binding.holeDetailLayout.holeDepthEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDepthDouble())));
            String[] status = new String[]{"Pending Hole", "Work in Progress", "Completed", "Deleted/ Blocked holes/ Do not blast"};
            binding.holeDetailLayout.holeStatusSpinner.setAdapter(Constants.getAdapter(this, status));

            if (StringUtill.isEmpty(holeDetailData.getHoleStatus())) {
                binding.holeDetailLayout.holeStatusSpinner.setText("Pending Hole");
            } else {
                binding.holeDetailLayout.holeStatusSpinner.setText(StringUtill.getString(holeDetailData.getHoleStatus()));
            }

            binding.holeDetailLayout.holeNameEt.setText(String.format("R%sH%s", StringUtill.getString(String.valueOf(holeDetailData.getRowNo())), StringUtill.getString(String.valueOf(holeDetailData.getHoleNo()))));
            binding.holeDetailLayout.holeAngleEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleAngle())));
            binding.holeDetailLayout.diameterEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getHoleDiameter())));
            binding.holeDetailLayout.burdenEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getBurdenDouble())));
            binding.holeDetailLayout.spacingEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getSpacingDouble())));
            binding.holeDetailLayout.xEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getX())));
            binding.holeDetailLayout.yTxtEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getY())));
            binding.holeDetailLayout.zEt.setText(StringUtill.getString(String.valueOf(holeDetailData.getZ())));
        }

        binding.holeDetailLayout.closeBtn.setOnClickListener(view -> {
            KeyboardUtils.hideSoftKeyboard(this);
            if (Constants.holeBgListener != null)
                Constants.holeBgListener.setBackgroundRefresh();
            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        });

        binding.holeDetailLayout.saveProceedBtn.setOnClickListener(view -> {

            updateHoleDetailData.setHoleDepth(StringUtill.isEmpty(binding.holeDetailLayout.holeDepthEt.getText().toString()) ? 0 : Double.parseDouble(binding.holeDetailLayout.holeDepthEt.getText().toString()));
            updateHoleDetailData.setHoleAngle(StringUtill.isEmpty(binding.holeDetailLayout.holeAngleEt.getText().toString()) ? 0 : Double.parseDouble(binding.holeDetailLayout.holeAngleEt.getText().toString()));
            updateHoleDetailData.setHoleDiameter(StringUtill.isEmpty(binding.holeDetailLayout.diameterEt.getText().toString()) ? 0 : Integer.parseInt(binding.holeDetailLayout.diameterEt.getText().toString()));
            updateHoleDetailData.setBurden(StringUtill.isEmpty(binding.holeDetailLayout.burdenEt.getText().toString()) ? 0 : binding.holeDetailLayout.burdenEt.getText().toString());
            updateHoleDetailData.setSpacing(StringUtill.isEmpty(binding.holeDetailLayout.spacingEt.getText().toString()) ? 0 : binding.holeDetailLayout.spacingEt.getText().toString());
            updateHoleDetailData.setX(StringUtill.isEmpty(binding.holeDetailLayout.xEt.getText().toString()) ? 0 : binding.holeDetailLayout.xEt.getText().toString());
            updateHoleDetailData.setY(StringUtill.isEmpty(binding.holeDetailLayout.yTxtEt.getText().toString()) ? 0 : binding.holeDetailLayout.yTxtEt.getText().toString());
            updateHoleDetailData.setZ(StringUtill.isEmpty(binding.holeDetailLayout.zEt.getText().toString()) ? 0 : binding.holeDetailLayout.zEt.getText().toString());
            updateHoleDetailData.setHoleStatus(StringUtill.getString(binding.holeDetailLayout.holeStatusSpinner.getText().toString()));

            updateEditedDataIntoDb(updateHoleDetailData, false);

            binding.holeDetailLayoutContainer.setVisibility(View.GONE);
        });
    }

    public void updateEditedDataIntoDb(ResponseHoleDetailData updateHoleDetailData, boolean isFromUpdateAdapter) {
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
            ((HoleDetailActivity) this).rowItemDetail.setRowOfTable(((HoleDetailActivity) this).rowPageVal, allTablesData, isFromUpdateAdapter);

        if (((HoleDetailActivity) this).mapViewDataUpdateLiveData != null)
            ((HoleDetailActivity) this).mapViewDataUpdateLiveData.setValue(true);
    }

}
