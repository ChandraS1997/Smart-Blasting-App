package com.smart_blasting_drilling.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

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
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.HoleDetailActivityBinding;
import com.smart_blasting_drilling.android.dialogs.DownloadListDialog;
import com.smart_blasting_drilling.android.dialogs.HoleDetailDialog;
import com.smart_blasting_drilling.android.dialogs.HoleEditTableFieldSelectionDialog;
import com.smart_blasting_drilling.android.dialogs.ProjectDetailDialog;
import com.smart_blasting_drilling.android.dialogs.SyncProjectOptionDialog;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.OnHoleClickListener;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.ui.models.MapHoleDataModel;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;

public class HoleDetailActivity extends BaseActivity implements View.OnClickListener, OnHoleClickListener {
    public NavController navController;
    HoleDetailActivityBinding binding;
    public AllTablesData allTablesData;
    public ResponseBladesRetrieveData bladesRetrieveData;
    public boolean isTableHeaderFirstTimeLoad = true;

    public List<ResponseHoleDetailData> holeDetailDataList = new ArrayList<>();
    public int rowPageVal = 1;
    public RowItemDetail rowItemDetail;
    List<String> rowList = new ArrayList<>();

    public MutableLiveData<Boolean> mapViewDataUpdateLiveData = new MutableLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isBundleIntentNotEmpty()) {
            bladesRetrieveData = (ResponseBladesRetrieveData) getIntent().getExtras().getSerializable("blades_data");
            allTablesData = (AllTablesData) getIntent().getExtras().getSerializable("all_table_Data");
            for (int i = 0; i < allTablesData.getTable2().size(); i++) {
                boolean isFound = false;
                for (int j = 0; j < rowList.size(); j++) {
                    if (rowList.get(j).replace("Row ", "").equals(String.valueOf(allTablesData.getTable2().get(i).getRowNo()))) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound)
                    rowList.add("Row " + allTablesData.getTable2().get(i).getRowNo());
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
        }
        binding = DataBindingUtil.setContentView(this, R.layout.hole_detail_activity);

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
                    blastInsertSyncRecordApiCaller(bladesRetrieveData, allTablesData, getRowWiseHoleList(allTablesData.getTable2()).size(), 0, blastCode);
                }

                @Override
                public void syncWithBlades() {

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
        setInsertUpdateHoleDetailMultipleSync(bladesRetrieveData, allTablesData.getTable2(), (modelEntity != null  && !StringUtill.isEmpty(modelEntity.getProjectCode())) ? modelEntity.getProjectCode() : "0").observe(this, new Observer<JsonPrimitive>() {
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
        editModelArrayList.add(new TableEditModel("Row No"));
        editModelArrayList.add(new TableEditModel("Hole No"));
        editModelArrayList.add(new TableEditModel("Hole Id"));
        editModelArrayList.add(new TableEditModel("Hole Depth"));
        editModelArrayList.add(new TableEditModel("Hole Status"));
        editModelArrayList.add(new TableEditModel("Hole Angle"));
        editModelArrayList.add(new TableEditModel("Diameter"));
        editModelArrayList.add(new TableEditModel("Burden"));
        editModelArrayList.add(new TableEditModel("Spacing"));
        editModelArrayList.add(new TableEditModel("X"));
        editModelArrayList.add(new TableEditModel("Y"));
        editModelArrayList.add(new TableEditModel("Z"));
        editModelArrayList.add(new TableEditModel("Charging"));
        return editModelArrayList;
    }

    public interface RowItemDetail {
        void setRowOfTable(int rowNo, AllTablesData allTablesData);
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
                bundle.putSerializable("blades_data", bladesRetrieveData);
                bundle.putSerializable("all_table_Data", allTablesData);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.switchBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.galleryBtn:
                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
                bundle.putSerializable("blades_data", bladesRetrieveData);
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
                bundle.putSerializable("blades_data", bladesRetrieveData);
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
        appDatabase.clearAllTables();
        manger.logoutUser();
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
        HoleDetailDialog infoDialogFragment = HoleDetailDialog.getInstance(holeDetailData);

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

}
