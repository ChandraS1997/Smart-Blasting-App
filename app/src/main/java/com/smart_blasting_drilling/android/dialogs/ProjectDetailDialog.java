package com.smart_blasting_drilling.android.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseDrillMaterialData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseDrillMethodData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseEmployeeData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseRigData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseSiteDetail;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.Table1Item;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.ProjectInfoDialogBinding;
import com.smart_blasting_drilling.android.databinding.ProjectInfoLayoutBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdatedProjectDetailEntity;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProjectDetailDialog extends BaseDialogFragment {

    ProjectInfoLayoutBinding binding;
    public static final String TAG = "ProjectDetailDialog";
    Dialog dialog;
    ProjectDetailDialog _self;
    private ProjectInfoDialogListener mListener;
    public ResponseBladesRetrieveData bladesRetrieveData;
    public ResponseBladesRetrieveData updateBladesData;
    AppDatabase appDatabase;

    int siteId, rigId, empId, drillTypeId, drillMaterialId, drillPatternId;
    String startDate, startTime, endTime, endDate;

    public ProjectDetailDialog() {
        _self = this;
        appDatabase = BaseApplication.getAppDatabase(mContext, Constants.DATABASE_NAME);
    }

    public static ProjectDetailDialog getInstance(ResponseBladesRetrieveData bladesRetrieveData) {
        ProjectDetailDialog frag = new ProjectDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bladesRetrieveData", bladesRetrieveData);
        frag.setArguments(bundle);
        return frag;
    }

    public void setUpListener(ProjectInfoDialogListener listener) {
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
            bladesRetrieveData = (ResponseBladesRetrieveData) getArguments().getSerializable("bladesRetrieveData");
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
                    jsonObject.addProperty("drill_pattern", binding.spinnerDrillingPattern.getText().toString());
                    jsonObject.addProperty("drill_pattern_id", drillPatternId);
                    jsonObject.addProperty("drill_type", binding.spinnerDrillingType.getText().toString());
                    jsonObject.addProperty("drill_type_id", drillTypeId);
                    jsonObject.addProperty("drill_material", binding.spinnerMaterialDrilled.getText().toString());
                    jsonObject.addProperty("drill_material_id", drillMaterialId);
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

                    ((BaseActivity) mContext).setJsonForSyncProjectData(((HoleDetailActivity) mContext).bladesRetrieveData, ((HoleDetailActivity) mContext).allTablesData.getTable2());
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });

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
        endDate = DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd");
        startDate = DateUtils.getLastMonthDateFromCurDate("yyyy-MM-dd");

        binding.startDate.setText(DateUtils.getDate(System.currentTimeMillis(), "yyyy/MM/dd"));
        binding.startTime.setText(DateUtils.getDate(System.currentTimeMillis(), "HH:mm:ss"));
        binding.endDate.setText(DateUtils.getLastMonthDateFromCurDate("yyyy/MM/dd"));
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
                List<ResponseSiteDetail> siteDetailList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("Table"), siteList);
                String[] siteNameList = new String[siteDetailList.size()];
                for (int i = 0; i < siteDetailList.size(); i++) {
                    siteNameList[i] = siteDetailList.get(i).getSiteName();
                }
                binding.spinnerSiteName.setText(StringUtill.getString(siteNameList[0]));
                binding.clientName.setText(StringUtill.getString(siteDetailList.get(0).getClientName()));
                binding.spinnerSiteName.setAdapter(Constants.getAdapter(mContext, siteNameList));

                binding.spinnerSiteName.setOnItemClickListener((adapterView, view, i, l) -> {
                    siteId = siteDetailList.get(i).getSiteCode();
                    binding.clientName.setText(StringUtill.getString(siteDetailList.get(i).getClientName()));
                });

                Type rigList = new TypeToken<List<ResponseRigData>>(){}.getType();
                List<ResponseRigData> rigDetailList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("Table1"), rigList);
                String[] rigNameList = new String[rigDetailList.size()];
                for (int i = 0; i < rigDetailList.size(); i++) {
                    rigNameList[i] = rigDetailList.get(i).getName();
                }
                binding.spinnerRig.setAdapter(Constants.getAdapter(mContext, rigNameList));
                binding.spinnerRig.setText(StringUtill.getString(rigNameList[0]));
                binding.spinnerRig.setOnItemClickListener((adapterView, view, i, l) -> {
                    rigId = rigDetailList.get(i).getRigCode();
                });

                Type empList = new TypeToken<List<ResponseEmployeeData>>(){}.getType();
                List<ResponseEmployeeData> employeeDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("Table6"), empList);
                String[] employeeNameList = new String[employeeDataList.size()];
                for (int i = 0; i < employeeDataList.size(); i++) {
                    employeeNameList[i] = employeeDataList.get(i).getName();
                }
                binding.spinnerSelectTeam.setAdapter(Constants.getAdapter(mContext, employeeNameList));
                binding.spinnerSelectTeam.setText(StringUtill.getString(employeeNameList[0]));
                binding.spinnerSelectTeam.setOnItemClickListener((adapterView, view, i, l) -> {
                    empId = employeeDataList.get(i).getEmployeeCode();
                });

            }
        }

        if (!Constants.isListEmpty(appDatabase.drillMethodDao().getAllEntityDataList())) {
            if (appDatabase.drillMethodDao().getAllEntityDataList().get(0) != null) {
                Type teamList = new TypeToken<List<ResponseDrillMaterialData>>(){}.getType();
                List<ResponseDrillMaterialData> drillMethodDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillMethodDao().getAllEntityDataList().get(0).getData(), JsonArray.class), teamList);
                String[] drillMethodDataItem = new String[drillMethodDataList.size()];
                for (int i = 0; i < drillMethodDataList.size(); i++) {
                    drillMethodDataItem[i] = drillMethodDataList.get(i).getMatTypeName();
                }
                binding.spinnerDrillingType.setAdapter(Constants.getAdapter(mContext, drillMethodDataItem));
                binding.spinnerDrillingType.setText(StringUtill.getString(drillMethodDataItem[0]));
                binding.spinnerDrillingType.setOnItemClickListener((adapterView, view, i, l) -> {
                    drillTypeId = drillMethodDataList.get(i).getMatTypeId();
                });
            }
        }

        if (!Constants.isListEmpty(appDatabase.projectHoleDetailRowColDao().getAllBladesProjectList())) {
            if (appDatabase.projectHoleDetailRowColDao().getAllBladesProjectList().get(0) != null) {
                Type teamList = new TypeToken<List<Table1Item>>(){}.getType();
                AllTablesData tablesData = new Gson().fromJson(appDatabase.projectHoleDetailRowColDao().getAllBladesProjectList().get(0).getProjectHole(), AllTablesData.class);
                List<Table1Item> drillPatternDataItemList = tablesData.getTable1();
                String[] drillPatternDataItem = new String[drillPatternDataItemList.size()];
                for (int i = 0; i < drillPatternDataItemList.size(); i++) {
                    drillPatternDataItem[i] = drillPatternDataItemList.get(i).getPatternType();
                }
                binding.spinnerDrillingPattern.setAdapter(Constants.getAdapter(mContext, drillPatternDataItem));
                binding.spinnerDrillingPattern.setText(StringUtill.getString(drillPatternDataItem[0]));
                binding.spinnerDrillingPattern.setOnItemClickListener((adapterView, view, i, l) -> {
                    drillPatternId = drillPatternDataItemList.get(i).getPatternTypeId();
                });
            }
        }

        if (!Constants.isListEmpty(appDatabase.drillMaterialDao().getAllEntityDataList())) {
            if (appDatabase.drillMaterialDao().getAllEntityDataList().get(0) != null) {
                Type teamList = new TypeToken<List<ResponseDrillMaterialData>>(){}.getType();
                List<ResponseDrillMaterialData> drillMaterialDataList = new Gson().fromJson(appDatabase.drillMaterialDao().getAllEntityDataList().get(0).getData(), teamList);
                String[] drillMaterialDataItem = new String[drillMaterialDataList.size()];
                for (int i = 0; i < drillMaterialDataList.size(); i++) {
                    drillMaterialDataItem[i] = drillMaterialDataList.get(i).getMatTypeName();
                }
                binding.spinnerMaterialDrilled.setAdapter(Constants.getAdapter(mContext, drillMaterialDataItem));
                binding.spinnerMaterialDrilled.setText(StringUtill.getString(drillMaterialDataItem[0]));
                binding.spinnerMaterialDrilled.setOnItemClickListener((adapterView, view, i, l) -> {
                    drillMaterialId = drillMaterialDataList.get(i).getMatTypeId();
                });
            }
        }

        String[] projectStatusItem = new String[]{"Open", "Close"};
        binding.spinnerProjectStatus.setAdapter(Constants.getAdapter(mContext, projectStatusItem));

        if (bladesRetrieveData != null) {
            binding.projectName.setText(StringUtill.getString(bladesRetrieveData.getDesignName()));
            binding.projectNumber.setText(StringUtill.getString(bladesRetrieveData.getDesignCode()));
        }

        binding.closeBtn.setOnClickListener(view -> {
            dismiss();
        });

    }

    private ProjectInfoDialogListener getListener() {
        ProjectInfoDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof ProjectInfoDialogListener)
            listener = (ProjectInfoDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof ProjectInfoDialogListener)
            listener = (ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(ProjectDetailDialog dialogFragment);

        default void onCancel(ProjectDetailDialog dialogFragment) {
        }
    }

}
