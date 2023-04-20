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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseAllRecordData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseAllRecordDataItem;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseLoginData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseProjectModelFromAllInfoApi;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.GetAllMinePitZoneBenchResult;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable3DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable7DesignElementDataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.app.BaseApis;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.dialogs.AppAlertDialogFragment;
import com.smart_blasting_drilling.android.dialogs.ProjectDetail3DDataDialog;
import com.smart_blasting_drilling.android.dialogs.ProjectDetailDialog;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.interfaces.OnChangeConnectivityListener;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.entities.AllMineInfoSurfaceInitiatorEntity;
import com.smart_blasting_drilling.android.room_database.entities.DrillShiftInfoEntity;
import com.smart_blasting_drilling.android.room_database.entities.ExplosiveDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseBenchTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillAccessoriesInfoAllDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillMaterialEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseDrillMethodEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseFileDetailsTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseMineTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseTypeTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.RockDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.TldDataEntity;
import com.smart_blasting_drilling.android.ui.adapter.ProjectLIstAdapter;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.databinding.ActivityHomeBinding;
import com.smart_blasting_drilling.android.dialogs.DownloadListDialog;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;
import com.smart_blasting_drilling.android.utils.TextUtil;
import com.smart_blasting_drilling.android.utils.ViewUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;

public class HomeActivity extends BaseActivity {
    public NavController navController;
    public ActivityHomeBinding binding;
    int projectFragType = 0;
    List<ResponseBladesRetrieveData> projectList = new ArrayList<>();
    ProjectLIstAdapter projectLIstAdapter;
    BaseApis baseApis;
    public AllTablesData allTablesData;

    public JsonElement element;

    public List<Response3DTable1DataModel> response3DTable1DataModels = new ArrayList<>();
    public List<Response3DTable2DataModel> response3DTable2DataModels = new ArrayList<>();
    public List<Response3DTable3DataModel> response3DTable3DataModels = new ArrayList<>();
    public List<Response3DTable4HoleChargingDataModel> response3DTable4HoleChargingDataModels = new ArrayList<>();
    public List<Response3DTable7DesignElementDataModel> response3DTable7DesignElementDataModels = new ArrayList<>();

    public static void openHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        ((Activity) context).finishAffinity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        baseApis = new BaseApis(this);

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

        binding.appLayout.headerLayout.logoutBtn.setOnClickListener(view -> {
            showAlertDialog("Logout", "Are you sure you want to logout?", "Yes", "No", new AppAlertDialogFragment.AppAlertDialogListener() {
                @Override
                public void onOk(AppAlertDialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                    manger.logoutUser();
                    startActivity(new Intent(HomeActivity.this, AuthActivity.class));
                    finishAffinity();
                }

                @Override
                public void onCancel(AppAlertDialogFragment dialogFragment) {
                    dialogFragment.dismiss();
                }
            });
        });

    }

    private boolean isDbTableCheck() {
        return Constants.isListEmpty(appDatabase.mineTableDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.benchTableDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.zoneTableDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.pitTableDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.typeTableDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.fileDetailsTableDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.typeTableDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.explosiveDataDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.tldDataEntity().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.initiatingDataDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.rockDataDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.allMineInfoSurfaceInitiatorDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject()) ||
                Constants.isListEmpty(appDatabase.drillMethodDao().getAllEntityDataList()) ||
                Constants.isListEmpty(appDatabase.drillMaterialDao().getAllEntityDataList()) ||
                Constants.isListEmpty(appDatabase.drillShiftInfoDao().getAllEntityDataList());

    }

    private void callBaseApis() {
        if (BaseApplication.getInstance().isInternetConnected(this)) {
            if (isDbTableCheck()) {
                getMinePitZoneBenchApiCaller();
            }
        }
        ConnectivityReceiver.getInstance().setConnectivityListener(b -> {
            if (isDbTableCheck()) {
                getMinePitZoneBenchApiCaller();
            }
        });
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

    public void setJsonCodeIdData(ResponseProjectModelFromAllInfoApi infoApi, int zoneId, int benchId, int pitId, int mineId) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("zoneId", zoneId);
            jsonObject.addProperty("benchId", benchId);
            jsonObject.addProperty("pitId", pitId);
            jsonObject.addProperty("MineId", mineId);

            AppDelegate.getInstance().setCodeIdObject(jsonObject);
            AppDelegate.getInstance().setProjectModelFromAllInfoApi(infoApi);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void set3dJsonCodeIdData(Response3DTable1DataModel infoApi, String zoneId, String benchId, String pitId, String mineId) {
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("zoneId", Integer.parseInt(StringUtill.isEmpty(zoneId) ? "0" : zoneId));
            jsonObject.addProperty("benchId", Integer.parseInt(StringUtill.isEmpty(benchId) ? "0" : benchId));
            jsonObject.addProperty("pitId", Integer.parseInt(StringUtill.isEmpty(pitId) ? "0" : pitId));
            jsonObject.addProperty("MineId", Integer.parseInt(StringUtill.isEmpty(mineId) ? "0" : mineId));

            AppDelegate.getInstance().setCodeIdObject(jsonObject);
            AppDelegate.getInstance().setProject3DModelFromAllInfoApi(infoApi);

        } catch (Exception e) {
            e.getLocalizedMessage();
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
        try {
            this.bladesRetrieveData = bladesRetrieveData;
            entity = appDatabase.projectHoleDetailRowColDao();
            if (!entity.isExistProject(bladesRetrieveData.getDesignId())) {
                getAllDesignInfoApiCaller(bladesRetrieveData.isIs3dBlade());
            } else {
                ProjectHoleDetailRowColEntity rowColEntity = entity.getAllBladesProject(bladesRetrieveData.getDesignId());
                HomeActivity.this.element = new Gson().fromJson(rowColEntity.projectHole, JsonElement.class);
                AppDelegate.getInstance().setHole3DDataElement(element);
                if (rowColEntity.getIs3DBlades()) {
                    List<Response3DTable1DataModel> response3DTable1DataModels = new ArrayList<>();
                    // "GetAll3DDesignInfoResult" for Test
                    JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) element).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                    for (JsonElement element : new Gson().fromJson(new Gson().fromJson(array.get(0), String.class), JsonArray.class)) {
                        response3DTable1DataModels.add(new Gson().fromJson(element, Response3DTable1DataModel.class));
                    }

                    Response3DTable1DataModel infoApi = response3DTable1DataModels.get(0);
                    set3dJsonCodeIdData(infoApi, infoApi.getZoneId(), infoApi.getBenchID(), infoApi.getPitId(), infoApi.getMineId());

                    set3dHoleTableData(element, rowColEntity.getIs3DBlades());
                } else {
                    AllTablesData tablesData = new AllTablesData();
                    Type typeList = new TypeToken<List<ResponseHoleDetailData>>(){}.getType();
                    tablesData = new Gson().fromJson(rowColEntity.projectHole, AllTablesData.class);

                    if (!Constants.isListEmpty(tablesData.getTable2())) {
                        ResponseProjectModelFromAllInfoApi infoApi = tablesData.getTable().get(0);
                        setJsonCodeIdData(infoApi, infoApi.getZoneId(), infoApi.getBenchID(), infoApi.getPitId(), infoApi.getMineId());
                    }
                    setHoleTableData(tablesData, rowColEntity.getIs3DBlades());
                }
            }
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void setHoleTableData(AllTablesData tablesData, boolean is3D) {
        Executors.newSingleThreadExecutor().execute(new TimerTask() {
            @Override
            public void run() {
                try {
                    allTablesData = tablesData;
                    String str = new Gson().toJson(tablesData);
                    if (!Constants.isListEmpty(allTablesData.getTable1())) {
                        bladesRetrieveData.setRockName(allTablesData.getTable1().get(0).getRockName());
                        bladesRetrieveData.setRockCode(String.valueOf(allTablesData.getTable1().get(0).getRockCode()));
                    }
                    if (!entity.isExistProject(bladesRetrieveData.getDesignId())) {
                        entity.insertProject(new ProjectHoleDetailRowColEntity(bladesRetrieveData.getDesignId(), is3D, str));
                    } else {
                        entity.updateProject(bladesRetrieveData.getDesignId(), str);
                    }

                    if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId())) {
                        Intent i = new Intent(HomeActivity.this, HoleDetailActivity.class);
                        AppDelegate.getInstance().setAllTablesData(tablesData);
                        AppDelegate.getInstance().setBladesRetrieveData(bladesRetrieveData);
                        startActivity(i);
                    } else {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ProjectDetailDialog infoDialogFragment = ProjectDetailDialog.getInstance(bladesRetrieveData);
                        infoDialogFragment.setFrom("Home");
                        ft.add(infoDialogFragment, ProjectDetailDialog.TAG);
                        ft.commitAllowingStateLoss();
                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
    }

    private void set3dHoleTableData(JsonElement response, boolean is3D) {
        Executors.newSingleThreadExecutor().execute(new TimerTask() {
            @Override
            public void run() {
                try {
                    String str = new Gson().toJson(response);
                    if (!entity.isExistProject(bladesRetrieveData.getDesignId())) {
                        entity.insertProject(new ProjectHoleDetailRowColEntity(bladesRetrieveData.getDesignId(), is3D, str));
                    } else {
                        entity.updateProject(bladesRetrieveData.getDesignId(), str);
                    }

                    convertList3dDataList(response);
                    if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId())) {
                        Intent i = new Intent(HomeActivity.this, HoleDetail3DModelActivity.class);
                        startActivity(i);
                    } else {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ProjectDetail3DDataDialog infoDialogFragment = ProjectDetail3DDataDialog.getInstance(response3DTable1DataModels.get(0));
                        infoDialogFragment.setFrom("Home");
                        ft.add(infoDialogFragment, ProjectDetailDialog.TAG);
                        ft.commitAllowingStateLoss();
                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
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

    private void convertList3dDataList(JsonElement response) {
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
            AppDelegate.getInstance().setHoleChargingDataModel(response3DTable4HoleChargingDataModels);
            AppDelegate.getInstance().setResponse3DTable1DataModel(response3DTable1DataModels);
            AppDelegate.getInstance().setResponse3DTable2DataModel(response3DTable2DataModels);
            AppDelegate.getInstance().setResponse3DTable3DataModel(response3DTable3DataModels);
            AppDelegate.getInstance().setDesignElementDataModel(response3DTable7DesignElementDataModels);
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    public void getAllDesignInfoApiCaller(boolean is3D) {
        showLoader();
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getAll2D_3DDesignInfoApiCaller(this, loginData.getUserid(), loginData.getCompanyid(), bladesRetrieveData.getDesignId(), Constants.DB_NAME, 0, is3D).observe((LifecycleOwner) this, new Observer<JsonElement>() {
            @Override
            public void onChanged(JsonElement response) {
                if (response == null) {
                    showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
                } else {
                    if (!(response.isJsonNull())) {
                        try {
                            if (is3D) {
                                try {
                                    List<Response3DTable1DataModel> response3DTable1DataModels = new ArrayList<>();
                                    //GetAll3DDesignInfoResult for live
                                    JsonArray array = new Gson().fromJson(new Gson().fromJson(((JsonObject) response).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                                    for (JsonElement element : new Gson().fromJson(new Gson().fromJson(array.get(0), String.class), JsonArray.class)) {
                                        response3DTable1DataModels.add(new Gson().fromJson(element, Response3DTable1DataModel.class));
                                    }
                                    HomeActivity.this.element = response;
                                    AppDelegate.getInstance().setHole3DDataElement(element);
                                    Response3DTable1DataModel infoApi = response3DTable1DataModels.get(0);
                                    set3dJsonCodeIdData(infoApi, infoApi.getZoneId(), infoApi.getBenchID(), infoApi.getPitId(), infoApi.getMineId());

                                    set3dHoleTableData(response, is3D);
                                } catch (Exception e) {
                                    e.getLocalizedMessage();
                                }
                            } else {
                                JsonObject jsonObject = response.getAsJsonObject();
                                if (jsonObject != null) {
                                    try {
                                        if (jsonObject.get("GetAllDesignInfoResult").getAsString().contains("Table2")) {
                                            AllTablesData tablesData = new Gson().fromJson(jsonObject.get("GetAllDesignInfoResult").getAsString(), AllTablesData.class);
                                            ResponseProjectModelFromAllInfoApi infoApi = new Gson().fromJson(new Gson().fromJson(jsonObject.get("GetAllDesignInfoResult").getAsString(), JsonObject.class).get("Table").getAsJsonArray().get(0), ResponseProjectModelFromAllInfoApi.class);
                                            setJsonCodeIdData(infoApi, infoApi.getZoneId(), infoApi.getBenchID(), infoApi.getPitId(), infoApi.getMineId());
                                            setHoleTableData(tablesData, is3D);
                                        }
                                    } catch (Exception e) {
                                        Log.e(NODATAFOUND, e.getMessage());
                                    }
                                } else {
                                    showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                                }
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
        showLoader();
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getMinePitZoneBenchApiCaller(this, loginData.getUserid(), loginData.getCompanyid()).observe((LifecycleOwner) this, new Observer<GetAllMinePitZoneBenchResult>() {
            @Override
            public void onChanged(GetAllMinePitZoneBenchResult response) {
                if (response == null) {
                    showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
                } else {
                    try {
                        AllTablesData allTablesData = new Gson().fromJson(response.getGetAllMinePitZoneBenchResult(), AllTablesData.class);
                        if (allTablesData != null) {
                            try {
                                if (allTablesData.getTable() != null) {
                                    ResponseMineTableEntity mineTableEntity = new ResponseMineTableEntity();
                                    mineTableEntity.setData(new Gson().toJson(allTablesData.getTable()));
                                    if (Constants.isListEmpty(appDatabase.mineTableDao().getAllBladesProject())) {
                                        appDatabase.mineTableDao().insertProject(mineTableEntity);
                                    } else {
                                        appDatabase.mineTableDao().updateProject(1, new Gson().toJson(mineTableEntity));
                                    }
                                }
                                if (allTablesData.getTable1() != null) {
                                    ResponseBenchTableEntity benchTableEntity = new ResponseBenchTableEntity();
                                    benchTableEntity.setData(new Gson().toJson(allTablesData.getTable1()));
                                    if (Constants.isListEmpty(appDatabase.benchTableDao().getAllBladesProject())) {
                                        appDatabase.benchTableDao().insertProject(benchTableEntity);
                                    } else {
                                        appDatabase.benchTableDao().updateProject(1, new Gson().toJson(benchTableEntity));
                                    }
                                }
                                if (allTablesData.getTable2() != null) {
                                    ResponseZoneTableEntity zoneTableEntity = new ResponseZoneTableEntity();
                                    zoneTableEntity.setData(new Gson().toJson(allTablesData.getTable2()));
                                    if (Constants.isListEmpty(appDatabase.zoneTableDao().getAllBladesProject())) {
                                        appDatabase.zoneTableDao().insertProject(zoneTableEntity);
                                    } else {
                                        appDatabase.zoneTableDao().updateProject(1, new Gson().toJson(zoneTableEntity));
                                    }
                                }
                                if (allTablesData.getTable3() != null) {
                                    ResponsePitTableEntity pitTableEntity = new ResponsePitTableEntity();
                                    pitTableEntity.setData(new Gson().toJson(allTablesData.getTable3()));
                                    if (Constants.isListEmpty(appDatabase.pitTableDao().getAllBladesProject())) {
                                        appDatabase.pitTableDao().insertProject(pitTableEntity);
                                    } else {
                                        appDatabase.pitTableDao().updateProject(1, new Gson().toJson(pitTableEntity));
                                    }
                                }
                                if (allTablesData.getTable4() != null) {
                                    ResponseTypeTableEntity typeTableEntity = new ResponseTypeTableEntity();
                                    typeTableEntity.setData(new Gson().toJson(allTablesData.getTable4()));
                                    if (Constants.isListEmpty(appDatabase.typeTableDao().getAllBladesProject())) {
                                        appDatabase.typeTableDao().insertProject(typeTableEntity);
                                    } else {
                                        appDatabase.typeTableDao().updateProject(1, new Gson().toJson(typeTableEntity));
                                    }
                                }
                                if (allTablesData.getTable5() != null) {
                                    ResponseFileDetailsTableEntity fileDetailsTableEntity = new ResponseFileDetailsTableEntity();
                                    fileDetailsTableEntity.setData(new Gson().toJson(allTablesData.getTable5()));
                                    if (Constants.isListEmpty(appDatabase.fileDetailsTableDao().getAllBladesProject())) {
                                        appDatabase.fileDetailsTableDao().insertProject(fileDetailsTableEntity);
                                    } else {
                                        appDatabase.fileDetailsTableDao().updateProject(1, new Gson().toJson(fileDetailsTableEntity));
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
                }
                getRecordApiCaller();
            }
        });
    }

    public void getRecordApiCaller() {
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getRecordApiCaller(this, loginData.getUserid(), loginData.getCompanyid()).observe((LifecycleOwner) this, new Observer<ResponseAllRecordData>() {
            @Override
            public void onChanged(ResponseAllRecordData responseAllRecordData) {
                if (responseAllRecordData == null) {
                    showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
                } else {
                    try {
                        try {
                            Type itemList = new TypeToken<List<ResponseAllRecordDataItem>>(){}.getType();
                            List<ResponseAllRecordDataItem> allRecordDataItemList = new Gson().fromJson(responseAllRecordData.getResponseAllRecordData(), itemList);
                            for (ResponseAllRecordDataItem allRecordData : allRecordDataItemList) {
                                if ("Mine".equals(allRecordData.getFieldInfo())) {
                                    ResponseMineTableEntity mineTableEntity = new ResponseMineTableEntity();
                                    mineTableEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.mineTableDao().getAllBladesProject())) {
                                        appDatabase.mineTableDao().insertProject(mineTableEntity);
                                    } else {
                                        appDatabase.mineTableDao().updateProject(1, new Gson().toJson(mineTableEntity));
                                    }
                                } else if ("Pit".equals(allRecordData.getFieldInfo())) {
                                    ResponsePitTableEntity pitTableEntity = new ResponsePitTableEntity();
                                    pitTableEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.pitTableDao().getAllBladesProject())) {
                                        appDatabase.pitTableDao().insertProject(pitTableEntity);
                                    } else {
                                        appDatabase.pitTableDao().updateProject(1, new Gson().toJson(pitTableEntity));
                                    }
                                } else if ("Bench".equals(allRecordData.getFieldInfo())) {
                                    ResponseBenchTableEntity benchTableEntity = new ResponseBenchTableEntity();
                                    benchTableEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.benchTableDao().getAllBladesProject())) {
                                        appDatabase.benchTableDao().insertProject(benchTableEntity);
                                    } else {
                                        appDatabase.benchTableDao().updateProject(1, new Gson().toJson(benchTableEntity));
                                    }
                                } else if ("Zone".equals(allRecordData.getFieldInfo())) {
                                    ResponseZoneTableEntity zoneTableEntity = new ResponseZoneTableEntity();
                                    zoneTableEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.zoneTableDao().getAllBladesProject())) {
                                        appDatabase.zoneTableDao().insertProject(zoneTableEntity);
                                    } else {
                                        appDatabase.zoneTableDao().updateProject(1, new Gson().toJson(zoneTableEntity));
                                    }
                                } else if ("Explosive".equals(allRecordData.getFieldInfo())) {
                                    ExplosiveDataEntity explosiveDataEntity = new ExplosiveDataEntity();
                                    explosiveDataEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.explosiveDataDao().getAllBladesProject())) {
                                        appDatabase.explosiveDataDao().insertProject(explosiveDataEntity);
                                    } else {
                                        appDatabase.explosiveDataDao().updateProject(1, new Gson().toJson(explosiveDataEntity));
                                    }
                                } else if ("tld".equals(allRecordData.getFieldInfo())) {
                                    TldDataEntity tldDataEntity = new TldDataEntity();
                                    tldDataEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.tldDataEntity().getAllBladesProject())) {
                                        appDatabase.tldDataEntity().insertProject(tldDataEntity);
                                    } else {
                                        appDatabase.tldDataEntity().updateProject(1, new Gson().toJson(tldDataEntity));
                                    }
                                } else if ("initiating".equals(allRecordData.getFieldInfo())) {
                                    InitiatingDataEntity initiatingDataEntity = new InitiatingDataEntity();
                                    initiatingDataEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.initiatingDataDao().getAllBladesProject())) {
                                        appDatabase.initiatingDataDao().insertProject(initiatingDataEntity);
                                    } else {
                                        appDatabase.initiatingDataDao().updateProject(1, new Gson().toJson(initiatingDataEntity));
                                    }
                                } else if ("rock".equals(allRecordData.getFieldInfo())) {
                                    RockDataEntity rockDataEntity = new RockDataEntity();
                                    rockDataEntity.setData(new Gson().toJson(allRecordData.getResultset()));
                                    if (Constants.isListEmpty(appDatabase.rockDataDao().getAllBladesProject())) {
                                        appDatabase.rockDataDao().insertProject(rockDataEntity);
                                    } else {
                                        appDatabase.rockDataDao().updateProject(1, new Gson().toJson(rockDataEntity));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e(NODATAFOUND, e.getMessage());
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                }
                getAllMineInfoSurfaceInitiatorApiCaller();
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
                                    entity.setData(jsonObject.get("GetAllmineinfoResult").getAsString());
                                    if (Constants.isListEmpty(appDatabase.allMineInfoSurfaceInitiatorDao().getAllBladesProject())) {
                                        appDatabase.allMineInfoSurfaceInitiatorDao().insertProject(entity);
                                    } else {
                                        appDatabase.allMineInfoSurfaceInitiatorDao().updateProject(1, new Gson().toJson(entity));
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
                    }
                }
                getDrillAccessoriesInfoAllDataApiCaller();
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
                                ResponseDrillAccessoriesInfoAllDataEntity entity = new ResponseDrillAccessoriesInfoAllDataEntity();
                                entity.setData(jsonObject.get("GetdrillaccessoriesinfoalldataResult").getAsString());
                                if (Constants.isListEmpty(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject())) {
                                    appDatabase.drillAccessoriesInfoAllDataDao().insertProject(entity);
                                } else {
                                    appDatabase.drillAccessoriesInfoAllDataDao().updateProject(1, new Gson().toJson(entity));
                                }
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        } else {
                            Log.e(ERROR, SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                }
            }
            getDrillMethodApiCaller();
        });
    }

    public void getDrillMethodApiCaller() {
        MainService.getDrillMethodApiCaller(this).observe(this, response -> {
            if (response == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (!(response.isJsonNull())) {
                    try {
                        JsonObject jsonObject = response.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                ResponseDrillMethodEntity entity = new ResponseDrillMethodEntity();
                                entity.setData(jsonObject.get("GetDrillMethodResult").getAsString());
                                if (Constants.isListEmpty(appDatabase.drillMethodDao().getAllEntityDataList())) {
                                    appDatabase.drillMethodDao().insertItem(entity);
                                } else {
                                    appDatabase.drillMethodDao().updateItem(1, new Gson().toJson(entity));
                                }
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        } else {
                            Log.e(ERROR, SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                }
            }
            getDrillMaterialApiCaller();
        });
    }

    public void getDrillMaterialApiCaller() {
        MainService.getDrillMaterialApiCaller(this).observe(this, response -> {
            if (response == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (!(response.isJsonNull())) {
                    try {
                        JsonObject jsonObject = response.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                ResponseDrillMaterialEntity entity = new ResponseDrillMaterialEntity();
                                entity.setData(jsonObject.get("GetDrillMaterialResult").getAsString());
                                if (Constants.isListEmpty(appDatabase.drillMaterialDao().getAllEntityDataList())) {
                                    appDatabase.drillMaterialDao().insertItem(entity);
                                } else {
                                    appDatabase.drillMaterialDao().updateItem(1, new Gson().toJson(entity));
                                }
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        } else {
                            Log.e(ERROR, SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                }
            }
            getDrillShiftInfoApiCaller();
        });
    }

    public void getDrillShiftInfoApiCaller() {
        ResponseLoginData loginData = manger.getUserDetails();
        MainService.getDrillShiftInfoApiCaller(this, loginData.getUserid(), loginData.getCompanyid()).observe(this, response -> {
            if (response == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (!(response.isJsonNull())) {
                    try {
                        JsonObject jsonObject = response.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                DrillShiftInfoEntity entity = new DrillShiftInfoEntity();
                                entity.setData(jsonObject.get("GetdrillaccessoriesinfoResult").getAsString());
                                if (Constants.isListEmpty(appDatabase.drillShiftInfoDao().getAllEntityDataList())) {
                                    appDatabase.drillShiftInfoDao().insertItem(entity);
                                } else {
                                    appDatabase.drillShiftInfoDao().updateItem(1, new Gson().toJson(entity));
                                }
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }

                        } else {
                            Log.e(ERROR, SOMETHING_WENT_WRONG);
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                }
            }
            hideLoader();
        });
    }


}
