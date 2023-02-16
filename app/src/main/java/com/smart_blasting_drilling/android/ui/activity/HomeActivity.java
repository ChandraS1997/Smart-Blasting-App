package com.smart_blasting_drilling.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.room.migration.Migration;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseAllRecordData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseLoginData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.app.BaseApis;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.entities.AllMineInfoSurfaceInitiatorEntity;
import com.smart_blasting_drilling.android.room_database.entities.ExplosiveDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseBenchTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseFileDetailsTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseTypeTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.RockDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.TldDataEntity;
import com.smart_blasting_drilling.android.ui.adapter.ProjectLIstAdapter;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.ActivityHomeBinding;
import com.smart_blasting_drilling.android.dialogs.DownloadListDialog;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.TextUtil;
import com.smart_blasting_drilling.android.utils.ViewUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends BaseActivity {
    public NavController navController;
    public ActivityHomeBinding binding;
    int projectFragType = 0;
    AppDatabase appDatabase;
    List<ResponseBladesRetrieveData> projectList = new ArrayList<>();
    ProjectLIstAdapter projectLIstAdapter;
    BaseApis baseApis;

    public static void openHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        ((Activity) context).finishAffinity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        baseApis = new BaseApis(this);

        appDatabase = BaseApplication.getAppDatabase(this, Constants.DATABASE_NAME);

        callBaseApis();

        hideKeyboard(this);

        projectList.clear();
        Type projectListType = new TypeToken<List<ResponseBladesRetrieveData>>(){}.getType();
        projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project2DBladesDao().getAllBladesProject()), projectListType));
        projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project3DBladesDao().getAllBladesProject()), projectListType));

        setDownloadDataView();

        StatusBarUtils.statusBarColor(this, R.color._FFA722);
        setPageTitle(getString(R.string.pro_title_list));

        binding.appLayout.headerLayout.homeBtn.setVisibility(View.GONE);

        binding.appLayout.headerLayout.downBtn.setOnClickListener(view -> {
            showDownloadListDialog((is3DBlades, dialogFragment) -> {
                showLoader();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                projectList.clear();
                                //if (is3DBlades) {
                                projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project3DBladesDao().getAllBladesProject()), projectListType));
                                //} else {
                                projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project2DBladesDao().getAllBladesProject()), projectListType));
                                //}
                                setDownloadDataView();
                                dialogFragment.dismiss();
                                hideLoader();
                            }
                        });
                    }
                }, 1500);
            });
        });

        binding.appLayout.bottomNavigation.designBtn.setOnClickListener(this::setBottomUiNavigation);
        binding.appLayout.bottomNavigation.drillingBtn.setOnClickListener(this::setBottomUiNavigation);
        binding.appLayout.bottomNavigation.blastingBtn.setOnClickListener(this::setBottomUiNavigation);

    }

    private void callBaseApis() {
        getMinePitZoneBenchApiCaller();
        getRecordApiCaller();
        getAllMineInfoSurfaceInitiatorApiCaller();
        getDrillAccessoriesInfoAllDataApiCaller();
    }

    private void setDownloadDataView() {
        if (!Constants.isListEmpty(projectList)) {
            binding.appLayout.noProjectTV.setVisibility(View.GONE);
            binding.appLayout.clickHereTv.setVisibility(View.GONE);
            projectLIstAdapter = new ProjectLIstAdapter(this, projectList);
            projectLIstAdapter.setItemClickCallBack(new ProjectLIstAdapter.OnItemClickCallBack() {
                @Override
                public void onClick(ResponseBladesRetrieveData data) {
                    setApiData(data);
                }
            });
            binding.appLayout.projectListRv.setAdapter(projectLIstAdapter);
        } else {
            binding.appLayout.noProjectTV.setVisibility(View.VISIBLE);
            binding.appLayout.clickHereTv.setVisibility(View.VISIBLE);
        }
    }

    private void setNavigationView() {
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.drillingFragment) {
                projectFragType = 0;
            } else if (navDestination.getId() == R.id.blastingFragment) {
                projectFragType = 1;
            }
        });
    }

    private void setBottomUiNavigation(View view) {
        switch (view.getId()) {
            case R.id.designBtn:
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.black));
                binding.appLayout.bottomNavigation.blastingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.black));
                break;
            case R.id.drillingBtn:
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.black));
                binding.appLayout.bottomNavigation.blastingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.black));
                navController.navigate(R.id.drillingFragment);
                break;
            case R.id.blastingBtn:
                binding.appLayout.bottomNavigation.blastingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.black));
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.black));
                navController.navigate(R.id.blastingFragment);
                break;
        }
    }

    private void showDownloadListDialog(DownloadListDialog.DownloadLIstDialogListener downlodeLIstDialogListener) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DownloadListDialog infoDialogFragment = DownloadListDialog.getInstance(downlodeLIstDialogListener);
        infoDialogFragment.setupListener(downlodeLIstDialogListener);
        ft.add(infoDialogFragment, DownloadListDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    public void setPageTitle(String title) {
        if (!TextUtil.isEmpty(title)) {
            binding.appLayout.headerLayout.pageTitle.setText(TextUtil.getString(title));
        }
    }

    // On Item Click
    ProjectHoleDetailRowColDao entity;
    ResponseBladesRetrieveData bladesRetrieveData;

    private void setApiData(ResponseBladesRetrieveData bladesRetrieveData) {
        this.bladesRetrieveData = bladesRetrieveData;
        appDatabase = BaseApplication.getAppDatabase(this, Constants.DATABASE_NAME);
        entity = appDatabase.projectHoleDetailRowColDao();
        if (!entity.isExistProject(bladesRetrieveData.getDesignId())) {
            getAllDesignInfoApiCaller(bladesRetrieveData.isIs3dBlade());
        } else {
            ProjectHoleDetailRowColEntity rowColEntity = entity.getAllBladesProject(bladesRetrieveData.getDesignId());
            AllTablesData tablesData = new AllTablesData();
            Type typeList = new TypeToken<List<ResponseHoleDetailData>>(){}.getType();
            tablesData.setTable2(new Gson().fromJson(rowColEntity.projectHole, typeList));
            setHoleTableData(tablesData);
        }
    }

    private void setHoleTableData(AllTablesData tablesData) {
        Intent i = new Intent(HomeActivity.this, HoleDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("all_table_Data", tablesData);
        bundle.putSerializable("blades_data", bladesRetrieveData);
        i.putExtras(bundle);
        startActivity(i);
    }

    public void getAllDesignInfoApiCaller(boolean is3D) {
        showLoader();
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getAll2D_3DDesignInfoApiCaller(this, loginData.getUserid(), loginData.getCompanyid(), bladesRetrieveData.getDesignId(), "dev_centralmineinfo", 0, is3D).observe((LifecycleOwner) this, new Observer<JsonElement>() {
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
                                        setHoleTableData(tablesData);
                                    }
                                } catch (Exception e) {
                                    Log.e(NODATAFOUND, e.getMessage());
                                }

                            } else {
                                showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
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

    public void getMinePitZoneBenchApiCaller() {
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getMinePitZoneBenchApiCaller(this, loginData.getUserid(), loginData.getCompanyid()).observe((LifecycleOwner) this, new Observer<JsonElement>() {
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
                                    if (jsonObject.has("GetAllMinePitZoneBenchResult")) {
                                        if (jsonObject.get("GetAllMinePitZoneBenchResult").getAsString().contains("Table")) {
                                            ResponseMineTableEntity mineTableEntity = new ResponseMineTableEntity();
                                            mineTableEntity.setData(new Gson().toJson(jsonObject.get("GetAllMinePitZoneBenchResult").getAsJsonObject().get("Table")));
                                            if (Constants.isListEmpty(appDatabase.mineTableDao().getAllBladesProject())) {
                                                appDatabase.mineTableDao().insertProject(mineTableEntity);
                                            } else {
                                                appDatabase.mineTableDao().updateProject(mineTableEntity);
                                            }
                                        }
                                        if (jsonObject.get("GetAllMinePitZoneBenchResult").getAsString().contains("Table1")) {
                                            ResponseBenchTableEntity benchTableEntity = new ResponseBenchTableEntity();
                                            benchTableEntity.setData(new Gson().toJson(jsonObject.get("GetAllMinePitZoneBenchResult").getAsJsonObject().get("Table1")));
                                            appDatabase.benchTableDao().insertProject(benchTableEntity);
                                            if (Constants.isListEmpty(appDatabase.benchTableDao().getAllBladesProject())) {
                                                appDatabase.benchTableDao().insertProject(benchTableEntity);
                                            } else {
                                                appDatabase.benchTableDao().updateProject(benchTableEntity);
                                            }
                                        }
                                        if (jsonObject.get("GetAllMinePitZoneBenchResult").getAsString().contains("Table2")) {
                                            ResponseZoneTableEntity zoneTableEntity = new ResponseZoneTableEntity();
                                            zoneTableEntity.setData(new Gson().toJson(jsonObject.get("GetAllMinePitZoneBenchResult").getAsJsonObject().get("Table2")));
                                            appDatabase.zoneTableDao().insertProject(zoneTableEntity);
                                            if (Constants.isListEmpty(appDatabase.zoneTableDao().getAllBladesProject())) {
                                                appDatabase.zoneTableDao().insertProject(zoneTableEntity);
                                            } else {
                                                appDatabase.zoneTableDao().updateProject(zoneTableEntity);
                                            }
                                        }
                                        if (jsonObject.get("GetAllMinePitZoneBenchResult").getAsString().contains("Table3")) {
                                            ResponsePitTableEntity pitTableEntity = new ResponsePitTableEntity();
                                            pitTableEntity.setData(new Gson().toJson(jsonObject.get("GetAllMinePitZoneBenchResult").getAsJsonObject().get("Table3")));
                                            if (Constants.isListEmpty(appDatabase.pitTableDao().getAllBladesProject())) {
                                                appDatabase.pitTableDao().insertProject(pitTableEntity);
                                            } else {
                                                appDatabase.pitTableDao().updateProject(pitTableEntity);
                                            }
                                        }
                                        if (jsonObject.get("GetAllMinePitZoneBenchResult").getAsString().contains("Table4")) {
                                            ResponseTypeTableEntity typeTableEntity = new ResponseTypeTableEntity();
                                            typeTableEntity.setData(new Gson().toJson(jsonObject.get("GetAllMinePitZoneBenchResult").getAsJsonObject().get("Table4")));
                                            appDatabase.typeTableDao().insertProject(typeTableEntity);
                                            if (Constants.isListEmpty(appDatabase.typeTableDao().getAllBladesProject())) {
                                                appDatabase.typeTableDao().insertProject(typeTableEntity);
                                            } else {
                                                appDatabase.typeTableDao().updateProject(typeTableEntity);
                                            }
                                        }
                                        if (jsonObject.get("GetAllMinePitZoneBenchResult").getAsString().contains("Table5")) {
                                            ResponseFileDetailsTableEntity fileDetailsTableEntity = new ResponseFileDetailsTableEntity();
                                            fileDetailsTableEntity.setData(new Gson().toJson(jsonObject.get("GetAllMinePitZoneBenchResult").getAsJsonObject().get("Table5")));
                                            appDatabase.fileDetailsTableDao().insertProject(fileDetailsTableEntity);
                                            if (Constants.isListEmpty(appDatabase.fileDetailsTableDao().getAllBladesProject())) {
                                                appDatabase.fileDetailsTableDao().insertProject(fileDetailsTableEntity);
                                            } else {
                                                appDatabase.fileDetailsTableDao().updateProject(fileDetailsTableEntity);
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    Log.e(NODATAFOUND, e.getMessage());
                                }

                            } else {
                                Log.e(ERROR, SOMETHING_WENT_WRONG);
//                                showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                            }
                        } catch (Exception e) {
                            Log.e(NODATAFOUND, e.getMessage());
                        }
                        hideLoader();
                    }
                }
            }
        });
    }

    public void getRecordApiCaller() {
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getRecordApiCaller(this, loginData.getUserid(), loginData.getCompanyid()).observe((LifecycleOwner) this, new Observer<JsonElement>() {
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
                                    Type listType = new TypeToken<List<ResponseAllRecordData>>(){}.getType();
                                    List<ResponseAllRecordData> allRecordDataList = new Gson().fromJson(jsonObject.get("ReturnObject").getAsJsonArray(), listType);
                                    for (ResponseAllRecordData allRecordData : allRecordDataList) {
                                        switch (allRecordData.getFieldInfo()) {
                                            case "Mine":
                                                ResponseMineTableEntity mineTableEntity = new ResponseMineTableEntity();
                                                mineTableEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                if (Constants.isListEmpty(appDatabase.mineTableDao().getAllBladesProject())) {
                                                    appDatabase.mineTableDao().insertProject(mineTableEntity);
                                                } else {
                                                    appDatabase.mineTableDao().updateProject(mineTableEntity);
                                                }
                                                break;
                                            case "Pit":
                                                ResponsePitTableEntity pitTableEntity = new ResponsePitTableEntity();
                                                pitTableEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                if (Constants.isListEmpty(appDatabase.pitTableDao().getAllBladesProject())) {
                                                    appDatabase.pitTableDao().insertProject(pitTableEntity);
                                                } else {
                                                    appDatabase.pitTableDao().updateProject(pitTableEntity);
                                                }
                                                break;
                                            case "Bench":
                                                ResponseBenchTableEntity benchTableEntity = new ResponseBenchTableEntity();
                                                benchTableEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                appDatabase.benchTableDao().insertProject(benchTableEntity);
                                                if (Constants.isListEmpty(appDatabase.benchTableDao().getAllBladesProject())) {
                                                    appDatabase.benchTableDao().insertProject(benchTableEntity);
                                                } else {
                                                    appDatabase.benchTableDao().updateProject(benchTableEntity);
                                                }
                                                break;
                                            case "Zone":
                                                ResponseZoneTableEntity zoneTableEntity = new ResponseZoneTableEntity();
                                                zoneTableEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                appDatabase.zoneTableDao().insertProject(zoneTableEntity);
                                                if (Constants.isListEmpty(appDatabase.zoneTableDao().getAllBladesProject())) {
                                                    appDatabase.zoneTableDao().insertProject(zoneTableEntity);
                                                } else {
                                                    appDatabase.zoneTableDao().updateProject(zoneTableEntity);
                                                }
                                                break;
                                            case "Explosive":
                                                ExplosiveDataEntity explosiveDataEntity = new ExplosiveDataEntity();
                                                explosiveDataEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                appDatabase.explosiveDataDao().insertProject(explosiveDataEntity);
                                                if (Constants.isListEmpty(appDatabase.explosiveDataDao().getAllBladesProject())) {
                                                    appDatabase.explosiveDataDao().insertProject(explosiveDataEntity);
                                                } else {
                                                    appDatabase.explosiveDataDao().updateProject(explosiveDataEntity);
                                                }
                                                break;
                                            case "tld":
                                                TldDataEntity tldDataEntity = new TldDataEntity();
                                                tldDataEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                appDatabase.tldDataEntity().insertProject(tldDataEntity);
                                                if (Constants.isListEmpty(appDatabase.tldDataEntity().getAllBladesProject())) {
                                                    appDatabase.tldDataEntity().insertProject(tldDataEntity);
                                                } else {
                                                    appDatabase.tldDataEntity().updateProject(tldDataEntity);
                                                }
                                                break;
                                            case "initiating":
                                                InitiatingDataEntity initiatingDataEntity = new InitiatingDataEntity();
                                                initiatingDataEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                appDatabase.initiatingDataDao().insertProject(initiatingDataEntity);
                                                if (Constants.isListEmpty(appDatabase.initiatingDataDao().getAllBladesProject())) {
                                                    appDatabase.initiatingDataDao().insertProject(initiatingDataEntity);
                                                } else {
                                                    appDatabase.initiatingDataDao().updateProject(initiatingDataEntity);
                                                }
                                                break;
                                            case "rock":
                                                RockDataEntity rockDataEntity = new RockDataEntity();
                                                rockDataEntity.setData(new Gson().toJson(allRecordData.getResultSetList()));
                                                appDatabase.rockDataDao().insertProject(rockDataEntity);
                                                if (Constants.isListEmpty(appDatabase.rockDataDao().getAllBladesProject())) {
                                                    appDatabase.rockDataDao().insertProject(rockDataEntity);
                                                } else {
                                                    appDatabase.rockDataDao().updateProject(rockDataEntity);
                                                }
                                                break;
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(NODATAFOUND, e.getMessage());
                                }

                            } else {
                                Log.e(ERROR, SOMETHING_WENT_WRONG);
//                                showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                            }
                        } catch (Exception e) {
                            Log.e(NODATAFOUND, e.getMessage());
                        }
                        hideLoader();
                    }
                }
            }
        });
    }

    public void getAllMineInfoSurfaceInitiatorApiCaller() {
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getAllMineInfoSurfaceInitiatorApiCaller(this, loginData.getUserid(), loginData.getCompanyid()).observe((LifecycleOwner) this, new Observer<JsonElement>() {
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
                                    AllMineInfoSurfaceInitiatorEntity entity = new AllMineInfoSurfaceInitiatorEntity();
                                    entity.setData(jsonObject.get("ReturnObject").getAsString());
                                    appDatabase.allMineInfoSurfaceInitiatorDao().insertProject(entity);
                                } catch (Exception e) {
                                    Log.e(NODATAFOUND, e.getMessage());
                                }

                            } else {
                                Log.e(ERROR, SOMETHING_WENT_WRONG);
//                                showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                            }
                        } catch (Exception e) {
                            Log.e(NODATAFOUND, e.getMessage());
                        }
                        hideLoader();
                    }
                }
            }
        });
    }

    public void getDrillAccessoriesInfoAllDataApiCaller() {
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getDrillAccessoriesInfoAllDataApiCaller(this, loginData.getUserid(), loginData.getCompanyid()).observe(this, response -> {
            if (response == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (!(response.isJsonNull())) {
                    try {
                        JsonObject jsonObject = response.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                AllMineInfoSurfaceInitiatorEntity entity = new AllMineInfoSurfaceInitiatorEntity();
                                entity.setData(jsonObject.get("ReturnObject").getAsString());
                                appDatabase.allMineInfoSurfaceInitiatorDao().insertProject(entity);
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        } else {
                            Log.e(ERROR, SOMETHING_WENT_WRONG);
//                                showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                    hideLoader();
                }
            }
        });
    }

}
