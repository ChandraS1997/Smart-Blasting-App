package com.mineexcellence.sblastingapp.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseBenchTable;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseDrillMaterialData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseDrillMethodData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseEmployeeData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseExplosiveDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseMineTable;
import com.mineexcellence.sblastingapp.api.apis.response.ResponsePitTable;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseProjectDeatilDialogData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseRigData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseRockData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseShiftData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseSiteDetail;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseZoneTable;
import com.mineexcellence.sblastingapp.api.apis.response.hole_tables.Table1Item;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable16PilotDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table.Response3DTable18PreSpilitDataModel;
import com.mineexcellence.sblastingapp.app.AppDelegate;
import com.mineexcellence.sblastingapp.app.BaseApplication;
import com.mineexcellence.sblastingapp.databinding.ProjectInfoLayoutBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.room_database.entities.AllProjectBladesModelEntity;
import com.mineexcellence.sblastingapp.room_database.entities.UpdatedProjectDetailEntity;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.ui.activity.HomeActivity;
import com.mineexcellence.sblastingapp.utils.DateUtils;
import com.mineexcellence.sblastingapp.utils.StringUtill;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProjectDetail3DDataDialog extends BaseDialogFragment {

    ProjectInfoLayoutBinding binding;
    public static final String TAG = "ProjectDetail3DDataDialog";
    Dialog dialog;
    ProjectDetail3DDataDialog _self;
    private ProjectDetail3DDataDialog.ProjectInfoDialogListener mListener;
    private String from;
    public Response3DTable1DataModel bladesRetrieveData;
    public Response3DTable1DataModel updateBladesData;

    int siteId, rigId, empId, drillTypeId, drillerCode, drillMethodId, drillMaterialId, shiftCode, drillPatternId, mineCode, zoneCode, rockCode, benchCode, expCode, pitCode;
    String startDate, startTime, endTime, endDate;
    String rockDensity;

    List<Response3DTable4HoleChargingDataModel> allTablesData = new ArrayList<>();
    List<Response3DTable16PilotDataModel> pilotDataModelList = new ArrayList<>();
    List<Response3DTable18PreSpilitDataModel> preSpilitDataModelList = new ArrayList<>();

    public ProjectDetail3DDataDialog() {
        _self = this;
    }

    public static ProjectDetail3DDataDialog getInstance(Response3DTable1DataModel bladesRetrieveData) {
        ProjectDetail3DDataDialog frag = new ProjectDetail3DDataDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bladesRetrieveData", bladesRetrieveData);
        frag.setArguments(bundle);
        return frag;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setUpListener(ProjectDetail3DDataDialog.ProjectInfoDialogListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bladesRetrieveData = (Response3DTable1DataModel) getArguments().getSerializable("bladesRetrieveData");
            updateBladesData = bladesRetrieveData;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_info_layout, container, false);
        return binding.getRoot();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

    }

    Calendar calendar = Calendar.getInstance();

    @Override
    public void setupClickListener() {

        binding.saveAndProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("project_name", binding.projectName.getText().toString());
                    jsonObject.addProperty("project_id", bladesRetrieveData.getDesignId());
                    jsonObject.addProperty("site_name", binding.spinnerSiteName.getText().toString());
                    jsonObject.addProperty("site_id", siteId);
                    jsonObject.addProperty("team_name", binding.spinnerSelectTeam.getText().toString());
                    jsonObject.addProperty("team_id", empId);
                    jsonObject.addProperty("rig_name", binding.spinnerRig.getText().toString());
                    jsonObject.addProperty("rig_id", rigId);
                    jsonObject.addProperty("mine_name", binding.spinnerMine.getText().toString());
                    jsonObject.addProperty("mine_id", mineCode);
                    jsonObject.addProperty("zone_name", binding.spinnerZone.getText().toString());
                    jsonObject.addProperty("zone_id", zoneCode);
                    jsonObject.addProperty("pit_name", binding.spinnerPit.getText().toString());
                    jsonObject.addProperty("pit_id", pitCode);
                    if (!binding.spinnerExplosive.getText().toString().equals("Select Explosive")) {
                        jsonObject.addProperty("explosive_name", binding.spinnerExplosive.getText().toString());
                        jsonObject.addProperty("explosive_id", expCode);
                    } else {
                        jsonObject.addProperty("explosive_name", "");
                        jsonObject.addProperty("explosive_id", "");
                    }
                    jsonObject.addProperty("bench_name", binding.spinnerBench.getText().toString());
                    jsonObject.addProperty("bench_id", benchCode);
                    jsonObject.addProperty("rock_name", binding.spinnerRock.getText().toString());
                    jsonObject.addProperty("rock_id", rockCode);
                    jsonObject.addProperty("rock_density", rockDensity);
                    jsonObject.addProperty("drill_pattern", binding.spinnerDrillingPattern.getText().toString());
                    jsonObject.addProperty("drill_pattern_id", drillPatternId);
                    jsonObject.addProperty("drill_type", binding.spinnerDrillingType.getText().toString());
                    jsonObject.addProperty("drill_type_id", drillTypeId);
                    jsonObject.addProperty("drill_material", binding.spinnerMaterialDrilled.getText().toString());
                    jsonObject.addProperty("drill_material_id", drillMaterialId);
                    jsonObject.addProperty("shift", binding.spinnerShift.getText().toString());
                    jsonObject.addProperty("shift_code", shiftCode);
                    jsonObject.addProperty("drill_method", binding.spinnerDrillMethod.getText().toString());
                    jsonObject.addProperty("drill_method_code", drillMethodId);
                    jsonObject.addProperty("driller_name", binding.spinnerDriller.getText().toString());
                    jsonObject.addProperty("driller_code", drillerCode);
                    jsonObject.addProperty("client_name", binding.clientName.getText().toString());
                    jsonObject.addProperty("project_number", binding.projectNumber.getText().toString());
                    jsonObject.addProperty("project_status", binding.spinnerProjectStatus.getText().toString());
                    jsonObject.addProperty("start_time", binding.startTime.getText().toString());
                    jsonObject.addProperty("start_date", binding.startDate.getText().toString());
                    jsonObject.addProperty("end_date", binding.endDate.getText().toString());
                    jsonObject.addProperty("end_time", binding.endTime.getText().toString());

                    if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId())) {
                        appDatabase.updatedProjectDataDao().updateItem(bladesRetrieveData.getDesignId(), new Gson().toJson(jsonObject));
                        showSnackBar(binding.getRoot(), "Project updated successfully");
                    } else {
                        appDatabase.updatedProjectDataDao().insertItem(new UpdatedProjectDetailEntity(bladesRetrieveData.getDesignId(), new Gson().toJson(jsonObject)));
                        showSnackBar(binding.getRoot(), "Project added successfully");
                    }

                    if (BaseApplication.getInstance().isInternetConnected(mContext)) {
                        if (!appDatabase.allProjectBladesModelDao().isExistItem(bladesRetrieveData.getDesignId())) {
                            List<Response3DTable4HoleChargingDataModel> finalAllTablesData = allTablesData;
                            ((BaseActivity) mContext).setJsonForSyncProject3DData(bladesRetrieveData, allTablesData, pilotDataModelList, preSpilitDataModelList).observe((LifecycleOwner) mContext, new Observer<JsonElement>() {
                                @Override
                                public void onChanged(JsonElement jsonPrimitive) {
                                    setNavigationOnHole(finalAllTablesData);
                                }
                            });
                        } else {
                            AllProjectBladesModelEntity entity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
                            if (StringUtill.isEmpty(entity.getProjectCode())) {
                                List<Response3DTable4HoleChargingDataModel> finalAllTablesData1 = allTablesData;
                                ((BaseActivity) mContext).setJsonForSyncProject3DData(bladesRetrieveData, allTablesData, pilotDataModelList, preSpilitDataModelList).observe((LifecycleOwner) mContext, new Observer<JsonElement>() {
                                    @Override
                                    public void onChanged(JsonElement jsonPrimitive) {
                                        setNavigationOnHole(finalAllTablesData1);
                                    }
                                });
                            } else {
                                setNavigationOnHole(allTablesData);
                            }
                        }
                    } else {
                        setNavigationOnHole(allTablesData);
                    }
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });

    }

    private void setNavigationOnHole(List<Response3DTable4HoleChargingDataModel> allTablesData) {
        dismiss();
        if (!StringUtill.isEmpty(from)) {
            if (from.equals("Home")) {
                Intent i = new Intent(mContext, HoleDetail3DModelActivity.class);
                AppDelegate.getInstance().addResponse3DTable1DataModel(bladesRetrieveData);
                AppDelegate.getInstance().setHoleChargingDataModel(allTablesData);
                mContext.startActivity(i);
            }
        }
    }

    private DatePickerDialog.OnDateSetListener setDataWithDatePicker(TextView textView, boolean isDate) {
        return (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel(textView, isDate);
        };
    }

    private TimePickerDialog.OnTimeSetListener setDataWithTimePicker(TextView textView, boolean isDate) {
        return (timePicker, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            updateLabel(textView, isDate);
        };
    }

    private void updateLabel(TextView textView, boolean isDate){
        String myFormat = isDate ? "yyyy/MM/dd" : "HH:mm:ss";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void loadData() {

        try {

            String explosiveName = "";

            if (!StringUtill.isEmpty(from)) {
                if (from.equals("Home")) {
                    allTablesData = ((HomeActivity) mContext).response3DTable4HoleChargingDataModels;
                    pilotDataModelList = ((HomeActivity) mContext).response3DTable16PilotDataModelList;
                    preSpilitDataModelList = ((HomeActivity) mContext).response3DTable18PreSpilitDataModelList;
                } else {
                    allTablesData = ((HoleDetail3DModelActivity) mContext).allTablesData;
                    pilotDataModelList = ((HoleDetail3DModelActivity) mContext).pilotTableData;
                    preSpilitDataModelList = ((HoleDetail3DModelActivity) mContext).preSplitTableData;
                }
            } else {
                allTablesData = ((HoleDetail3DModelActivity) mContext).allTablesData;
                pilotDataModelList = ((HoleDetail3DModelActivity) mContext).pilotTableData;
                preSpilitDataModelList = ((HoleDetail3DModelActivity) mContext).preSplitTableData;
            }

            if (!Constants.isListEmpty(allTablesData)) {
                for (int i = 0; i < allTablesData.size(); i++) {
                    if (i == 0) {
                        if (StringUtill.isEmpty(explosiveName) && !Constants.isListEmpty(allTablesData.get(0).getChargeTypeArray())) {
                            for (int x = 0; x < allTablesData.get(0).getChargeTypeArray().size(); x++) {
                                if (allTablesData.get(0).getChargeTypeArray().get(x).getType().equals("Bulk")) {
                                    explosiveName = allTablesData.get(0).getChargeTypeArray().get(x).getName();
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            endDate = DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd");
            startDate = DateUtils.getLastMonthDateFromCurDate("yyyy-MM-dd");

            binding.endDate.setText(DateUtils.getDate(System.currentTimeMillis(), "yyyy/MM/dd"));
            binding.startTime.setText(DateUtils.getDate(System.currentTimeMillis(), "HH:mm:ss"));
            binding.startDate.setText(DateUtils.getLastMonthDateFromCurDate("yyyy/MM/dd"));
            binding.endTime.setText(DateUtils.getLastMonthDateFromCurDate("HH:mm:ss"));

            binding.startDate.setOnClickListener(view -> {
                calendar = Calendar.getInstance();
                new DatePickerDialog(mContext, setDataWithDatePicker(binding.startDate, true), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            });
            binding.startTime.setOnClickListener(view -> {
                calendar = Calendar.getInstance();
                new TimePickerDialog(mContext, setDataWithTimePicker(binding.startTime, false), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), true).show();
            });
            binding.endDate.setOnClickListener(view -> {
                calendar = Calendar.getInstance();
                new DatePickerDialog(mContext, setDataWithDatePicker(binding.endDate, true), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            });
            binding.endTime.setOnClickListener(view -> {
                calendar = Calendar.getInstance();
                new TimePickerDialog(mContext, setDataWithTimePicker(binding.endTime, false), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), true).show();
            });

            if (!Constants.isListEmpty(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject())) {
                if (appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0) != null) {

                    Type siteList = new TypeToken<List<ResponseSiteDetail>>(){}.getType();
                    List<ResponseSiteDetail> siteDetailList = new ArrayList<>();
                    JsonObject jsonObject = new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class);
                    if (jsonObject.has("data")) {
                        siteDetailList = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data"), String.class), JsonObject.class).get("Table"), siteList);
                    } else {
                        siteDetailList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("Table"), siteList);
                    }
                    if (!Constants.isListEmpty(siteDetailList)) {
                        String[] siteNameList = new String[siteDetailList.size()];
                        for (int i = 0; i < siteDetailList.size(); i++) {
                            siteNameList[i] = siteDetailList.get(i).getSiteName();
                        }
                        binding.spinnerSiteName.setText(StringUtill.getString(siteNameList[0]));
                        binding.clientName.setText(StringUtill.getString(siteDetailList.get(0).getClientName()));
                        binding.spinnerSiteName.setAdapter(Constants.getAdapter(mContext, siteNameList));
                        siteId = siteDetailList.get(0).getSiteCode();

                        List<ResponseSiteDetail> finalSiteDetailList = siteDetailList;
                        binding.spinnerSiteName.setOnItemClickListener((adapterView, view, i, l) -> {
                            siteId = finalSiteDetailList.get(i).getSiteCode();
                            binding.clientName.setText(StringUtill.getString(finalSiteDetailList.get(i).getClientName()));
                        });
                    }

                    Type rigList = new TypeToken<List<ResponseRigData>>(){}.getType();
                    List<ResponseRigData> rigDetailList = new ArrayList<>();
                    jsonObject = new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class);
                    if (jsonObject.has("data")) {
                        rigDetailList = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data"), String.class), JsonObject.class).get("Table1"), rigList);
                    } else {
                        rigDetailList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("Table1"), rigList);
                    }
                    if (!Constants.isListEmpty(rigDetailList)) {
                        String[] rigNameList = new String[rigDetailList.size()];
                        for (int i = 0; i < rigDetailList.size(); i++) {
                            rigNameList[i] = rigDetailList.get(i).getName();
                        }
                        rigId = rigDetailList.get(0).getRigCode();
                        binding.spinnerRig.setAdapter(Constants.getAdapter(mContext, rigNameList));
                        binding.spinnerRig.setText(StringUtill.getString(rigNameList[0]));
                        List<ResponseRigData> finalRigDetailList = rigDetailList;
                        binding.spinnerRig.setOnItemClickListener((adapterView, view, i, l) -> {
                            rigId = finalRigDetailList.get(i).getRigCode();
                        });
                    }

                    Type empList = new TypeToken<List<ResponseEmployeeData>>(){}.getType();
                    List<ResponseEmployeeData> employeeDataList = new ArrayList<>();
                    jsonObject = new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class);
                    if (jsonObject.has("data")) {
                        employeeDataList = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data"), String.class), JsonObject.class).get("Table6"), empList);
                    } else {
                        employeeDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("Table6"), empList);
                    }
                    if (!Constants.isListEmpty(employeeDataList)) {
                        String[] employeeNameList = new String[employeeDataList.size()];
                        for (int i = 0; i < employeeDataList.size(); i++) {
                            employeeNameList[i] = employeeDataList.get(i).getName();
                        }
                        empId = employeeDataList.get(0).getEmployeeCode();
                        binding.spinnerSelectTeam.setAdapter(Constants.getAdapter(mContext, employeeNameList));
                        binding.spinnerSelectTeam.setText(StringUtill.getString(employeeNameList[0]));
                        List<ResponseEmployeeData> finalEmployeeDataList = employeeDataList;
                        binding.spinnerSelectTeam.setOnItemClickListener((adapterView, view, i, l) -> {
                            empId = finalEmployeeDataList.get(i).getEmployeeCode();
                        });

                        binding.spinnerDriller.setAdapter(Constants.getAdapter(mContext, employeeNameList));
                        binding.spinnerDriller.setText(StringUtill.getString(employeeNameList[0]));
                        binding.spinnerDriller.setOnItemClickListener((adapterView, view, i, l) -> {
                            drillerCode = finalEmployeeDataList.get(i).getEmployeeCode();
                        });
                        drillerCode = employeeDataList.get(0).getEmployeeCode();
                    }

                }
            }

            if (!Constants.isListEmpty(appDatabase.drillMethodDao().getAllEntityDataList())) {
                if (appDatabase.drillMethodDao().getAllEntityDataList().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseDrillMethodData>>(){}.getType();
                    List<ResponseDrillMethodData> drillMethodDataList = new ArrayList<>();
                    if (!new Gson().fromJson(appDatabase.drillMethodDao().getAllEntityDataList().get(0).getData(), JsonElement.class).isJsonArray()) {
                        drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillMethodDao().getAllEntityDataList().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                    } else {
                        drillMethodDataList = new Gson().fromJson(appDatabase.drillMethodDao().getAllEntityDataList().get(0).getData(), teamList);
                    }
                    if (!Constants.isListEmpty(drillMethodDataList)) {
                        String[] drillMethodDataItem = new String[drillMethodDataList.size()];
                        for (int i = 0; i < drillMethodDataList.size(); i++) {
                            drillMethodDataItem[i] = drillMethodDataList.get(i).getDrillMethod();
                        }
                        binding.spinnerDrillingType.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                        binding.spinnerDrillingType.setText(StringUtill.getString(drillMethodDataItem[0]));
                        List<ResponseDrillMethodData> finalDrillMethodDataList = drillMethodDataList;
                        binding.spinnerDrillingType.setOnItemClickListener((adapterView, view, i, l) -> {
                            drillTypeId = finalDrillMethodDataList.get(i).getDrillMethodId();
                        });
                        drillTypeId = drillMethodDataList.get(0).getDrillMethodId();

                        binding.spinnerDrillMethod.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                        binding.spinnerDrillMethod.setText(StringUtill.getString(drillMethodDataItem[0]));
                        binding.spinnerDrillMethod.setOnItemClickListener((adapterView, view, i, l) -> {
                            drillMethodId = finalDrillMethodDataList.get(i).getDrillMethodId();
                        });
                        drillMethodId = drillMethodDataList.get(0).getDrillMethodId();
                    }
                }
            }

            if (!Constants.isListEmpty(appDatabase.projectHoleDetailRowColDao().getAllBladesProjectList())) {
                if (appDatabase.projectHoleDetailRowColDao().getAllBladesProjectList().get(0) != null) {
                    Type teamList = new TypeToken<List<Table1Item>>(){}.getType();
                    JsonArray array = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(appDatabase.projectHoleDetailRowColDao().getAllBladesProject(bladesRetrieveData.getDesignId()).getProjectHole(), JsonObject.class).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                    List<Response3DTable2DataModel> holeDetailDataList = new ArrayList<>();
                    for (JsonElement e : new Gson().fromJson(new Gson().fromJson(array.get(1), String.class), JsonArray.class)) {
                        holeDetailDataList.add(new Gson().fromJson(e, Response3DTable2DataModel.class));
                    }
                    /*AllTablesData tablesData = new Gson().fromJson(new Gson().fromJson(appDatabase.projectHoleDetailRowColDao().getAllBladesProject(bladesRetrieveData.getDesignId()).getProjectHole(), JsonObject.class).get("GetAll3DDesignInfoResult"), AllTablesData.class);
                    List<Table1Item> drillPatternDataItemList = tablesData.getTable1();*/
                    if (!Constants.isListEmpty(holeDetailDataList)) {
                        String[] drillPatternDataItem = new String[holeDetailDataList.size()];
                        for (int i = 0; i < holeDetailDataList.size(); i++) {
                            drillPatternDataItem[i] = holeDetailDataList.get(i).getPatternType();
                        }
                        binding.spinnerDrillingPattern.setAdapter(Constants.getAdapter(mContext, drillPatternDataItem));
                        binding.spinnerDrillingPattern.setText(StringUtill.getString(drillPatternDataItem[0]));
                        binding.spinnerDrillingPattern.setOnItemClickListener((adapterView, view, i, l) -> {
                            drillPatternId = 2;
                        });
                        drillPatternId = 2;
                    }
                }
            }

            if (!Constants.isListEmpty(appDatabase.drillShiftInfoDao().getAllEntityDataList())) {
                if (appDatabase.drillShiftInfoDao().getAllEntityDataList().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseShiftData>>(){}.getType();
                    List<ResponseShiftData> drillMaterialDataList = new ArrayList<>();
                    if (!new Gson().fromJson(appDatabase.drillShiftInfoDao().getAllEntityDataList().get(0).getData(), JsonElement.class).isJsonArray()) {
                        drillMaterialDataList = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(appDatabase.drillShiftInfoDao().getAllEntityDataList().get(0).getData(), JsonObject.class).get("data"), String.class), teamList);
                    } else {
                        drillMaterialDataList = new Gson().fromJson(appDatabase.drillShiftInfoDao().getAllEntityDataList().get(0).getData(), teamList);
                    }
                    if (!Constants.isListEmpty(drillMaterialDataList)) {
                        String[] drillMaterialDataItem = new String[drillMaterialDataList.size()];
                        for (int i = 0; i < drillMaterialDataList.size(); i++) {
                            drillMaterialDataItem[i] = drillMaterialDataList.get(i).getShiftName();
                        }
                        binding.spinnerShift.setAdapter(Constants.getAdapter(mContext, drillMaterialDataItem));
                        binding.spinnerShift.setText(StringUtill.getString(drillMaterialDataItem[0]));
                        List<ResponseShiftData> finalDrillMaterialDataList = drillMaterialDataList;
                        binding.spinnerShift.setOnItemClickListener((adapterView, view, i, l) -> {
                            shiftCode = finalDrillMaterialDataList.get(i).getShiftCode();
                        });
                        shiftCode = drillMaterialDataList.get(0).getShiftCode();
                    }
                }
            }

            if (!Constants.isListEmpty(appDatabase.drillMaterialDao().getAllEntityDataList())) {
                if (appDatabase.drillMaterialDao().getAllEntityDataList().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseDrillMaterialData>>(){}.getType();
                    List<ResponseDrillMaterialData> drillMaterialDataList = new ArrayList<>();
                    if (!new Gson().fromJson(appDatabase.drillMaterialDao().getAllEntityDataList().get(0).getData(), JsonElement.class).isJsonArray())
                        drillMaterialDataList = new Gson().fromJson(new Gson().fromJson(new Gson().fromJson(appDatabase.drillMaterialDao().getAllEntityDataList().get(0).getData(), JsonObject.class).get("data"), String.class), teamList);
                    else
                        drillMaterialDataList = new Gson().fromJson(appDatabase.drillMaterialDao().getAllEntityDataList().get(0).getData(), teamList);
                    if (!Constants.isListEmpty(drillMaterialDataList)) {
                        String[] drillMaterialDataItem = new String[drillMaterialDataList.size()];
                        for (int i = 0; i < drillMaterialDataList.size(); i++) {
                            drillMaterialDataItem[i] = drillMaterialDataList.get(i).getMatTypeName();
                        }
                        binding.spinnerMaterialDrilled.setAdapter(Constants.getAdapter(mContext, drillMaterialDataItem));
                        binding.spinnerMaterialDrilled.setText(StringUtill.getString(drillMaterialDataItem[0]));
                        List<ResponseDrillMaterialData> finalDrillMaterialDataList = drillMaterialDataList;
                        binding.spinnerMaterialDrilled.setOnItemClickListener((adapterView, view, i, l) -> {
                            drillMaterialId = finalDrillMaterialDataList.get(i).getMatTypeId();
                        });
                        drillMaterialId = drillMaterialDataList.get(0).getMatTypeId();
                    }
                }
            }

            if (!Constants.isListEmpty(appDatabase.mineTableDao().getAllBladesProject())) {
                if (appDatabase.mineTableDao().getAllBladesProject().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseMineTable>>(){}.getType();
                    List<ResponseMineTable> drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.mineTableDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                    if (!Constants.isListEmpty(drillMethodDataList)) {
                        String[] drillMethodDataItem = new String[drillMethodDataList.size()];
                        for (int i = 0; i < drillMethodDataList.size(); i++) {
                            drillMethodDataItem[i] = drillMethodDataList.get(i).getName();
                        }

                        boolean isFound = false;
                        for (int i = 0; i < drillMethodDataItem.length; i++) {
                            if (StringUtill.getString(updateBladesData.getMineName()).equals(drillMethodDataItem[i])) {
                                isFound = true;
                                binding.spinnerMine.setText(StringUtill.getString(drillMethodDataItem[i]));
                                mineCode = drillMethodDataList.get(i).getMineCode();
                                break;
                            }
                        }
                        if (!isFound) {
                            binding.spinnerMine.setText(StringUtill.getString(drillMethodDataItem[0]));
                            mineCode = drillMethodDataList.get(0).getMineCode();
                        }

                        binding.spinnerMine.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                        binding.spinnerMine.setOnItemClickListener((adapterView, view, i, l) -> {
                            mineCode = drillMethodDataList.get(i).getMineCode();
                        });
                    }
                }
            }

            List<ResponseRockData> responseRockDataList = new ArrayList<>();

            if (!Constants.isListEmpty(appDatabase.zoneTableDao().getAllBladesProject())) {
                if (appDatabase.zoneTableDao().getAllBladesProject().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseZoneTable>>(){}.getType();
                    List<ResponseZoneTable> drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.zoneTableDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                    if (!Constants.isListEmpty(drillMethodDataList)) {
                        String[] drillMethodDataItem = new String[drillMethodDataList.size()];
                        for (int i = 0; i < drillMethodDataList.size(); i++) {
                            drillMethodDataItem[i] = drillMethodDataList.get(i).getName();
                        }

                        boolean isFound = false;
                        for (int i = 0; i < drillMethodDataItem.length; i++) {
                            if (StringUtill.getString(updateBladesData.getZoneName()).equals(drillMethodDataItem[i])) {
                                isFound = true;
                                binding.spinnerZone.setText(StringUtill.getString(drillMethodDataItem[i]));
                                zoneCode = drillMethodDataList.get(i).getZoneCode();
                                break;
                            }
                        }
                        if (!isFound) {
                            binding.spinnerZone.setText(StringUtill.getString(drillMethodDataItem[0]));
                            zoneCode = drillMethodDataList.get(0).getZoneCode();
                        }

                        responseRockDataList.clear();
                        responseRockDataList.addAll(getRockListByZoneCode());

                        binding.spinnerZone.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                        binding.spinnerZone.setOnItemClickListener((adapterView, view, i, l) -> {
                            zoneCode = drillMethodDataList.get(i).getZoneCode();
                            responseRockDataList.clear();
                            responseRockDataList.addAll(getRockListByZoneCode());
                            if (!Constants.isListEmpty(responseRockDataList)) {
                                binding.spinnerRock.setText(StringUtill.getString(responseRockDataList.get(0).getRockName()));
                            }
                        });
                    }
                }
            }

            if (!Constants.isListEmpty(responseRockDataList)) {
                for (int i = 0; i < responseRockDataList.size(); i++) {
                    drillMethodDataItem[i] = responseRockDataList.get(i).getRockName();
                    rockCodeItem[i] = String.valueOf(responseRockDataList.get(i).getRockCode());
                }

                binding.spinnerRock.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                binding.spinnerRock.setOnItemClickListener((adapterView, view, i, l) -> {
                    rockCode = responseRockDataList.get(i).getRockCode();
                    rockDensity = responseRockDataList.get(i).getDensity();
                    JsonObject jsonObject = AppDelegate.getInstance().getCodeIdObject();
                    jsonObject.addProperty("rockDensity", rockDensity);
                    AppDelegate.getInstance().setCodeIdObject(jsonObject);
                });

                boolean isFound = false;
                for (int i = 0; i < drillMethodDataItem.length; i++) {
                    if (StringUtill.getString(updateBladesData.getRockCode()).equals(rockCodeItem[i])) {
                        isFound = true;
                        binding.spinnerRock.setText(StringUtill.getString(drillMethodDataItem[i]));
                        rockCode = responseRockDataList.get(i).getRockCode();
                        rockDensity = responseRockDataList.get(i).getDensity();
                        JsonObject jsonObject = AppDelegate.getInstance().getCodeIdObject();
                        jsonObject.addProperty("rockDensity", rockDensity);
                        AppDelegate.getInstance().setCodeIdObject(jsonObject);
                        break;
                    }
                }
                if (!isFound) {
                    binding.spinnerRock.setText(StringUtill.getString(drillMethodDataItem[0]));
                    rockCode = responseRockDataList.get(0).getRockCode();
                    rockDensity = responseRockDataList.get(0).getDensity();
                    JsonObject jsonObject = AppDelegate.getInstance().getCodeIdObject();
                    jsonObject.addProperty("rockDensity", rockDensity);
                    AppDelegate.getInstance().setCodeIdObject(jsonObject);
                }

                /*if (appDatabase.rockDataDao().getAllBladesProject().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseRockData>>(){}.getType();
                    List<ResponseRockData> drillMethodDataList = new ArrayList<>();
                    if (!new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), JsonElement.class).isJsonArray()) {
                        drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                    } else {
                        drillMethodDataList = new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), teamList);
                    }
                }*/
            }

            if (!Constants.isListEmpty(appDatabase.benchTableDao().getAllBladesProject())) {
                if (appDatabase.benchTableDao().getAllBladesProject().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseBenchTable>>(){}.getType();
                    List<ResponseBenchTable> drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.benchTableDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                    if (!Constants.isListEmpty(drillMethodDataList)) {
                        String[] drillMethodDataItem = new String[drillMethodDataList.size()];
                        for (int i = 0; i < drillMethodDataList.size(); i++) {
                            drillMethodDataItem[i] = drillMethodDataList.get(i).getName();
                        }

                        boolean isFound = false;
                        for (int i = 0; i < drillMethodDataItem.length; i++) {
                            if (StringUtill.getString(updateBladesData.getBenchName()).equals(drillMethodDataItem[i])) {
                                isFound = true;
                                binding.spinnerBench.setText(StringUtill.getString(drillMethodDataItem[i]));
                                benchCode = drillMethodDataList.get(i).getBenchCode();
                                break;
                            }
                        }
                        if (!isFound) {
                            binding.spinnerBench.setText(StringUtill.getString(drillMethodDataItem[0]));
                            benchCode = drillMethodDataList.get(0).getBenchCode();
                        }

                        binding.spinnerBench.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                        binding.spinnerBench.setOnItemClickListener((adapterView, view, i, l) -> {
                            benchCode = drillMethodDataList.get(i).getBenchCode();
                        });
                    }
                }
            }

            if (!Constants.isListEmpty(appDatabase.explosiveDataDao().getAllBladesProject())) {
                if (appDatabase.explosiveDataDao().getAllBladesProject().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponseExplosiveDataModel>>(){}.getType();
                    List<ResponseExplosiveDataModel> drillMethodDataList = new ArrayList<>();
                    if (!new Gson().fromJson(appDatabase.explosiveDataDao().getAllBladesProject().get(0).getData(), JsonElement.class).isJsonArray()) {
                        drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.explosiveDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                    } else {
                        drillMethodDataList = new Gson().fromJson(appDatabase.explosiveDataDao().getAllBladesProject().get(0).getData(), teamList);
                    }
                    if (!Constants.isListEmpty(drillMethodDataList)) {
                        List<String> expList = new ArrayList<>();
                        for (int i = 0; i < drillMethodDataList.size(); i++) {
                            expList.add(drillMethodDataList.get(i).getName());
                            if (explosiveName.equals(drillMethodDataList.get(i).getName())) {
                                expCode = drillMethodDataList.get(i).getExpCode();
                            }
                        }
                        expList.add(0, "Select Explosive");

                        String[] drillMethodDataItem = new String[expList.size()];
                        drillMethodDataItem = expList.toArray(drillMethodDataItem);

                        binding.spinnerExplosive.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                        if (StringUtill.isEmpty(explosiveName)) {
                            binding.spinnerExplosive.setText("Select Explosive");
                        } else {
                            binding.spinnerExplosive.setText(StringUtill.getString(explosiveName));
                        }
                        drillMethodDataList.add(0, new ResponseExplosiveDataModel());
                        List<ResponseExplosiveDataModel> finalDrillMethodDataList = drillMethodDataList;
                        binding.spinnerExplosive.setOnItemClickListener((adapterView, view, i, l) -> {
                            expCode = finalDrillMethodDataList.get(i).getExpCode();
                        });
                        /*if (!StringUtill.isEmpty(explosiveName)) {
                            expCode = drillMethodDataList.get(0).getExpCode();
                        }*/
                    }
                }
            }

            if (!Constants.isListEmpty(appDatabase.pitTableDao().getAllBladesProject())) {
                if (appDatabase.pitTableDao().getAllBladesProject().get(0) != null) {
                    Type teamList = new TypeToken<List<ResponsePitTable>>(){}.getType();
                    List<ResponsePitTable> drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.pitTableDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), teamList);
                    if (!Constants.isListEmpty(drillMethodDataList)) {
                        String[] drillMethodDataItem = new String[drillMethodDataList.size()];
                        for (int i = 0; i < drillMethodDataList.size(); i++) {
                            drillMethodDataItem[i] = drillMethodDataList.get(i).getName();
                        }

                        boolean isFound = false;
                        for (int i = 0; i < drillMethodDataItem.length; i++) {
                            if (StringUtill.getString(updateBladesData.getPitName()).equals(drillMethodDataItem[i])) {
                                isFound = true;
                                binding.spinnerPit.setText(StringUtill.getString(drillMethodDataItem[i]));
                                pitCode = drillMethodDataList.get(i).getPitCode();
                                break;
                            }
                        }
                        if (!isFound) {
                            binding.spinnerPit.setText(StringUtill.getString(drillMethodDataItem[0]));
                            pitCode = drillMethodDataList.get(0).getPitCode();
                        }

                        binding.spinnerPit.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                        binding.spinnerPit.setOnItemClickListener((adapterView, view, i, l) -> {
                            pitCode = drillMethodDataList.get(i).getPitCode();
                        });
                    }
                }
            }

            String[] projectStatusItem = new String[]{"Open", "Close"};
            binding.spinnerProjectStatus.setAdapter(Constants.getAdapter(mContext, projectStatusItem));

            if (bladesRetrieveData != null) {
                binding.projectName.setText(StringUtill.getString(bladesRetrieveData.getDesignName()));
                binding.projectNumber.setText(StringUtill.getString(bladesRetrieveData.getDesignCode()));
            }


            if (!Constants.isListEmpty(appDatabase.updatedProjectDataDao().getAllEntityDataList())) {
                if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId()))
                    setUpdatedProjectData();
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

    }

    String[] drillMethodDataItem;
    String[] rockCodeItem;

    @NonNull
    private List<ResponseRockData> getRockListByZoneCode() {
        List<ResponseRockData> rockDataEntityList = new ArrayList<>();
        if (!Constants.isListEmpty(appDatabase.rockDataDao().getAllBladesProject())) {
            if (appDatabase.rockDataDao().getAllBladesProject().get(0) != null) {
                Type tempTeamList = new TypeToken<List<ResponseRockData>>(){}.getType();
                List<ResponseRockData> tempDrillMethodDataList = new ArrayList<>();
                if (!new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), JsonElement.class).isJsonArray()) {
                    tempDrillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("data").getAsJsonPrimitive().getAsString(), tempTeamList);
                } else {
                    tempDrillMethodDataList = new Gson().fromJson(appDatabase.rockDataDao().getAllBladesProject().get(0).getData(), tempTeamList);
                }
                for (ResponseRockData entity : tempDrillMethodDataList) {
                    if (entity.getRockZone() == zoneCode) {
                        rockDataEntityList.add(entity);
                    }
                }
            }
        }

        drillMethodDataItem = new String[rockDataEntityList.size()];
        rockCodeItem = new String[rockDataEntityList.size()];
        for (int i = 0; i < rockDataEntityList.size(); i++) {
            drillMethodDataItem[i] = rockDataEntityList.get(i).getRockName();
            rockCodeItem[i] = String.valueOf(rockDataEntityList.get(i).getRockCode());
        }

        binding.spinnerRock.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));

        return rockDataEntityList;
    }

    private ProjectDetail3DDataDialog.ProjectInfoDialogListener getListener() {
        ProjectDetail3DDataDialog.ProjectInfoDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof ProjectDetail3DDataDialog.ProjectInfoDialogListener)
            listener = (ProjectDetail3DDataDialog.ProjectInfoDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof ProjectDetail3DDataDialog.ProjectInfoDialogListener)
            listener = (ProjectDetail3DDataDialog.ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(ProjectDetail3DDataDialog dialogFragment);

        default void onCancel(ProjectDetail3DDataDialog dialogFragment) {
        }
    }

    private void setUpdatedProjectData() {
        try {
            UpdatedProjectDetailEntity updatedProjectDetailEntity = appDatabase.updatedProjectDataDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            ResponseProjectDeatilDialogData data = new Gson().fromJson(updatedProjectDetailEntity.getData(), ResponseProjectDeatilDialogData.class);
            binding.projectName.setText(data.getProjectName());
            binding.spinnerSiteName.setText(data.getSiteName());
            binding.spinnerSelectTeam.setText(data.getTeamName());
            binding.spinnerRig.setText(data.getRigName());
            binding.spinnerMine.setText(data.getMineName());
            binding.spinnerZone.setText(data.getZoneName());
            binding.spinnerRock.setText(data.getRockName());
            binding.spinnerBench.setText(data.getBenchName());
            binding.spinnerExplosive.setText(data.getExplosiveName());
            binding.spinnerPit.setText(data.getPitName());
            binding.spinnerDrillingPattern.setText(data.getDrillPattern());
            binding.spinnerDrillingType.setText(data.getDrillType());
            binding.spinnerMaterialDrilled.setText(data.getDrillMaterial());
            binding.spinnerShift.setText(data.getShift());
            binding.spinnerDriller.setText(data.getDrillerName());
            binding.spinnerDrillMethod.setText(data.getDrillMethod());
            binding.clientName.setText(data.getClientName());
            binding.projectNumber.setText(data.getProjectNumber());
            binding.spinnerProjectStatus.setText(data.getProjectStatus());
            binding.startDate.setText(data.getStartDate());
            binding.startTime.setText(data.getStartTime());
            binding.endDate.setText(data.getEndDate());
            binding.endTime.setText(data.getEndTime());

            startDate = data.getStartDate();
            endDate = data.getEndDate();
            siteId = data.getSiteId();
            rigId = data.getRigId();
            drillTypeId = data.getDrillTypeId();
            drillerCode = data.getDrillerCode();
            drillMethodId = data.getDrillMethodCode();
            drillMaterialId = data.getDrillMaterialId();
            shiftCode = data.getShiftCode();
            drillPatternId = data.getDrillPatternId();
            mineCode = data.getMineId();
            zoneCode = data.getZoneId();
            rockCode = data.getRockId();
            benchCode = data.getBenchId();
            expCode = data.getExplosiveId();
            pitCode = data.getPitId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
