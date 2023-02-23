package com.smart_blasting_drilling.android.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.ResultsetItem;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.NoInternetBinding;
import com.smart_blasting_drilling.android.dialogs.AppAlertDialogFragment;
import com.smart_blasting_drilling.android.dialogs.AppProgressBar;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.helper.PreferenceManger;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.entities.ResponseBenchTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdatedProjectDetailEntity;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BaseActivity extends AppCompatActivity {
    Toast toast;
    public Dialog noInternetdialog;

    ConnectivityReceiver receiver;

    public PreferenceManger manger;
    public static final String SOMETHING_WENT_WRONG = "Something went wrong!";
    public static final String ERROR = "Error!";
    public static final String API_RESPONSE = "api response";
    public static final String APILOADINGTEXT = "Please wait...";
    public static final String NODATAFOUND = "Nothing to show here yet!";
    public static final String SESSION_EXPIRED_TEXT = "Session expired,Please Login Again.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noInternetdialog = new Dialog(this);

        receiver = ConnectivityReceiver.getInstance();
        manger = BaseApplication.getPreferenceManger();

    }
    public void showToast(String msg) {
        try {
            toast.getView().isShown();
            toast.setText(msg);
        } catch (Exception e) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        toast.show();
    }


    public void showLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    public void showDebug(String tag, String msg) {
        Log.d(tag, msg);
    }

    public void showSnackBar(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showActionSnackBar(Context context, View view, String msg, String action, View.OnClickListener clickListener) {
        Snackbar snackbar =
                Snackbar.make(
                        view,
                        msg,
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, clickListener);
        snackbar.setActionTextColor(
                context.getResources().getColor(R.color.F1E60E));
        snackbar.show();
    }




    public void hideLoader() {
        AppProgressBar.hideLoaderDialog();
    }

    public void showLoader() {
        AppProgressBar.showLoaderDialog(this);
    }

    public void showAlertDialog(String title, String msg, String positiveBtn, String negativeBtn) {
        showAlertDialog(title, msg, positiveBtn, negativeBtn, null);
    }

    public void showAlertDialog(String title, String msg, String positiveBtn, String negativeBtn, AppAlertDialogFragment.AppAlertDialogListener listener) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AppAlertDialogFragment infoDialogFragment = AppAlertDialogFragment.getInstance(title, msg, positiveBtn, negativeBtn, listener);
        infoDialogFragment.setupListener(listener);
        ft.add(infoDialogFragment, AppAlertDialogFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    public void logoutFromApp() {
        logOutNotification();
    }

/*    public void showMediaFile(Context context, String url, int type) {
        Intent intent = new Intent(context, MediaPreview.class);
        intent.putExtra("fileUrl", url);
        intent.putExtra("fileType", type);
        startActivity(intent);
    }*/





    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void logOutNotification() {
        showLoader();
    }

    public void noInternetDialog() {
        if(!noInternetdialog.isShowing())
        {
            NoInternetBinding sDialog = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.no_internet, null, false);
            noInternetdialog = new Dialog(this);
            noInternetdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            noInternetdialog.setContentView(sDialog.getRoot());
            noInternetdialog.setCancelable(false);
            noInternetdialog.setCanceledOnTouchOutside(false);
            noInternetdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            noInternetdialog.getWindow().setGravity(Gravity.CENTER);
            noInternetdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sDialog.retry.setOnClickListener(v ->
            {
                if (BaseApplication.getInstance().isInternet(this)) {
                    noInternetdialog.dismiss();
                    hideLoader();
                } else {
                    showToast("Please enable Data or Wifi connection");
                }
            });
            noInternetdialog.show();
        }
    }

    public boolean isBundleIntentNotEmpty() {
        return getIntent() != null && getIntent().getExtras() != null;
    }

    public boolean isCurrentLangArabic() {
//        return manger.getStringValue(PreferenceManger.LANGUAGE_TYPE, Constants.LANGUAGE_TYPE.ENGLISH.toString()).equalsIgnoreCase(Constants.LANGUAGE_TYPE.ARABIC.toString());
        return true;
    }

    public RequestBody toRequestBody(String value) {
        return !TextUtils.isEmpty(value) ? RequestBody.create(MediaType.parse("text/plain"), value) : null;
    }

    public File createImageFile() throws IOException {
        String imageFileName = "SBD_Image-" + System.currentTimeMillis() + "_";
        File storageDir = getFilesDir();
        return File.createTempFile(imageFileName, ".png", storageDir);
    }

    public File createVideoFile() throws IOException {
        String imageFileName = "Drill_Video-" + System.currentTimeMillis() + "_";
        File storageDir = getFilesDir();
        return File.createTempFile(imageFileName, ".mp4", storageDir);
    }

    public void setJsonForSyncProjectData(ResponseBladesRetrieveData bladesRetrieveData, ResponseHoleDetailData holeDetailData) {
        showLoader();

        AppDatabase appDatabase = BaseApplication.getAppDatabase(this, Constants.DATABASE_NAME);
        Map<String, Object> map = new HashMap<>();

        UpdatedProjectDetailEntity projectDetailEntity = new UpdatedProjectDetailEntity();
        ResponsePitTableEntity pitTableEntity = new ResponsePitTableEntity();
        ResponseZoneTableEntity zoneTableEntity = new ResponseZoneTableEntity();
        ResponseBenchTableEntity benchTableEntity = new ResponseBenchTableEntity();

        if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId())) {
            projectDetailEntity = appDatabase.updatedProjectDataDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
        }
        JsonObject projectDetailJson = new Gson().fromJson(projectDetailEntity.getData(), JsonObject.class);

        if (!Constants.isListEmpty(appDatabase.pitTableDao().getAllBladesProject())) {
            pitTableEntity = appDatabase.pitTableDao().getAllBladesProject(1);
        }
        List<ResultsetItem> pitResultItemList = new Gson().fromJson(pitTableEntity.getData(), new TypeToken<List<ResultsetItem>>() {}.getType());
        for (ResultsetItem resultsetItem : pitResultItemList) {
            if (resultsetItem.getName().equals(StringUtill.getString(bladesRetrieveData.getPitName()))) {
                map.put("pitCode", resultsetItem.getPitCode());
            }
        }

        if (!Constants.isListEmpty(appDatabase.zoneTableDao().getAllBladesProject())) {
            zoneTableEntity = appDatabase.zoneTableDao().getAllBladesProject(1);
        }
        List<ResultsetItem> zoneResultItem = new Gson().fromJson(zoneTableEntity.getData(), new TypeToken<List<ResultsetItem>>() {}.getType());
        for (ResultsetItem resultsetItem : zoneResultItem) {
            if (resultsetItem.getName().equals(StringUtill.getString(bladesRetrieveData.getZoneName()))) {
                map.put("zoneCode", resultsetItem.getZoneCode());
            }
        }

        if (!Constants.isListEmpty(appDatabase.benchTableDao().getAllBladesProject())) {
            benchTableEntity = appDatabase.benchTableDao().getAllBladesProject(1);
        }
        List<ResultsetItem> benchResultList = new Gson().fromJson(benchTableEntity.getData(), new TypeToken<List<ResultsetItem>>() {}.getType());
        for (ResultsetItem resultsetItem : benchResultList) {
            if (resultsetItem.getName().equals(StringUtill.getString(bladesRetrieveData.getPitName()))) {
                map.put("benchCode", resultsetItem.getPitCode());
            }
        }

        map.put("projectNumber", bladesRetrieveData.getDesignCode());
        map.put("projectName", bladesRetrieveData.getDesignName());

        JsonArray projectTeamArray = new JsonArray();
        JsonObject teamObject = new JsonObject();
        teamObject.addProperty("srNo", "1");

        if (projectDetailJson != null) {
            map.put("siteCode", projectDetailJson.get("site_id").getAsString());
            map.put("rigCode", projectDetailJson.get("rig_id").getAsString());
            map.put("drillingTypeId", projectDetailJson.get("drill_type_id").getAsString());
            map.put("materialTypeId", projectDetailJson.get("drill_material_id").getAsString());
            map.put("startTime", projectDetailJson.get("start_time").getAsString());
            map.put("endTime", projectDetailJson.get("end_time").getAsString());
            map.put("drillPattern", projectDetailJson.get("drill_pattern").getAsString());
            map.put("clientName", projectDetailJson.get("client_name").getAsString());
            map.put("projectStatus", projectDetailJson.get("project_status").getAsString());
            map.put("modificationDate", String.format("%s %s", projectDetailJson.get("start_date").getAsString(), projectDetailJson.get("start_time").getAsString()));
            teamObject.addProperty("employeeCode", projectDetailJson.get("team_id").getAsString());
        } else {
            map.put("siteCode", "0");
            map.put("rigCode", "0");
            map.put("drillingTypeId", "0");
            map.put("materialTypeId", "0");
            map.put("startTime", "");
            map.put("endTime", "");
            map.put("drillPattern", "");
            map.put("clientName", "");
            map.put("projectStatus", "");
            map.put("modificationDate", "");
            teamObject.addProperty("employeeCode", "0");
        }
        projectTeamArray.add(teamObject);

        map.put("creationDate", bladesRetrieveData.getDesignDateTime());
        map.put("companyId", manger.getUserDetails().getCompanyid());
        map.put("userId", manger.getUserDetails().getUserid());
        map.put("deviceType", "android");
        map.put("projectSource", "");



        map.put("projectTeamDetails", projectTeamArray);

        JsonArray rowDetailArray = new JsonArray();
        JsonObject rowObject = new JsonObject();
        rowObject.addProperty("rowNo", holeDetailData.getRowNo());
        rowObject.addProperty("holeNo", holeDetailData.getHoleNo());
        rowObject.addProperty("HoleType", "Production");
        rowDetailArray.add(rowObject);

        map.put("rowDetails", rowDetailArray);

        /*Type empList = new TypeToken<List<ResponseEmployeeData>>(){}.getType();
        List<ResponseEmployeeData> employeeDataList = new Gson().fromJson(new Gson().fromJson(appDatabase.drillAccessoriesInfoAllDataDao().getAllBladesProject().get(0).getData(), JsonObject.class).get("Table6"), empList);
        for (ResponseEmployeeData employeeData : employeeDataList) {
            if (String.valueOf(employeeData.getEmployeeCode()).equals(projectDetailJson.get("team_id").getAsString())) {

            }
        }*/

        JsonArray rowHoleDetailArray = new JsonArray();
        rowHoleDetailArray.add(new Gson().toJson(holeDetailData));

        JsonArray drillDetailArray = new JsonArray();
        JsonObject drillDetailObject = new JsonObject();
        drillDetailObject.addProperty("rowNo", holeDetailData.getRowNo());
        drillDetailObject.addProperty("holeNo", holeDetailData.getHoleNo());
        drillDetailObject.addProperty("holeName", holeDetailData.getName());
        drillDetailObject.addProperty("holeDiameter", holeDetailData.getHoleDiameter());
        drillDetailObject.addProperty("burden", String.valueOf(holeDetailData.getBurden()));
        drillDetailObject.addProperty("spacing", String.valueOf(holeDetailData.getSpacing()));
        drillDetailObject.addProperty("holeAngle", holeDetailData.getHoleAngle());
        drillDetailObject.addProperty("holeDeviation", String.valueOf(holeDetailData.getStdDev()));
        drillDetailObject.addProperty("drillDepth", holeDetailData.getHoleDepth());
        drillDetailObject.addProperty("depthStart", 0);
        drillDetailObject.addProperty("depthEnd", holeDetailData.getHoleDepth());
        drillDetailObject.addProperty("waterLevel", holeDetailData.getWaterDepth());
        drillDetailObject.addProperty("drillerName", "1");
        drillDetailObject.addProperty("drillMethod", "1");
        drillDetailObject.addProperty("northing", 20);
        drillDetailObject.addProperty("easting", 26);
        drillDetailObject.addProperty("rlTop", 0);
        drillDetailObject.addProperty("rlBottom", 0);
        drillDetailObject.addProperty("rockType", 11);
        drillDetailObject.addProperty("holeStatus", 1);
        drillDetailObject.addProperty("operationalSummary", "");
        drillDetailObject.addProperty("rigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsString() : "0");
        drillDetailObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
        drillDetailObject.addProperty("userId", manger.getUserDetails().getUserid());
        drillDetailObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        drillDetailObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        drillDetailObject.addProperty("syncDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        drillDetailObject.addProperty("deviceType", "android");
        drillDetailObject.addProperty("shiftCode", 18);
        drillDetailArray.add(drillDetailObject);


        JsonArray holeDetailArray = new JsonArray();
        JsonObject holeDetailObject = new JsonObject();
        holeDetailObject.addProperty("ProjectCode", 0);
        holeDetailObject.addProperty("RowNo", holeDetailData.getRowNo());
        holeDetailObject.addProperty("HoleNo", holeDetailData.getHoleNo());
        holeDetailObject.addProperty("HoleName", holeDetailData.getName());
        holeDetailObject.addProperty("UserDefineHoleName", holeDetailData.getName());
        holeDetailObject.addProperty("HoleDiameter", holeDetailData.getHoleDiameter());
        holeDetailObject.addProperty("Burden", String.valueOf(holeDetailData.getBurden()));
        holeDetailObject.addProperty("Spacing", String.valueOf(holeDetailData.getSpacing()));
        holeDetailObject.addProperty("HoleAngle", holeDetailData.getHoleAngle());
        holeDetailObject.addProperty("HoleDeviation", String.valueOf(holeDetailData.getStdDev()));
        holeDetailObject.addProperty("DrillDepth", holeDetailData.getHoleDepth());
        holeDetailObject.addProperty("Northing", 20);
        holeDetailObject.addProperty("Easting", 26);
        holeDetailObject.addProperty("RlTop", 0);
        holeDetailObject.addProperty("RlBottom", 0);
        holeDetailObject.addProperty("rockType", 11);
        holeDetailObject.addProperty("HoleStatus", 1);
        holeDetailObject.addProperty("OperationalSummary", "");
        holeDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
        holeDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
        holeDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        holeDetailObject.addProperty("DeviceType", 0);
        holeDetailObject.addProperty("shiftCode", 18);
        holeDetailObject.addProperty("UserRole", 0);
        holeDetailObject.addProperty("HoleType", String.valueOf(holeDetailData.getHoleType()));
        holeDetailObject.addProperty("CalculateDrillPenetration", 0);
        holeDetailObject.addProperty("DrillPenetrationRate", 0);
        holeDetailObject.addProperty("TotalDrillTime", 0);

        JsonObject logHoleSectionDetailObject = new JsonObject();
        logHoleSectionDetailObject.addProperty("ProjectCode", 0);
        logHoleSectionDetailObject.addProperty("HoleName", holeDetailData.getName());
        logHoleSectionDetailObject.addProperty("DepthStart", 0);
        logHoleSectionDetailObject.addProperty("DepthEnd", holeDetailData.getHoleDepth());
        logHoleSectionDetailObject.addProperty("RockType", 105);
        logHoleSectionDetailObject.addProperty("StartTime", "");
        logHoleSectionDetailObject.addProperty("EndTime", "");
        logHoleSectionDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
        logHoleSectionDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
        logHoleSectionDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        logHoleSectionDetailObject.addProperty("deviceType", "android");
        logHoleSectionDetailObject.addProperty("RigCode", 3);
        logHoleSectionDetailObject.addProperty("DrillerCode", 4);
        logHoleSectionDetailObject.addProperty("DrillMethod", 1);
        logHoleSectionDetailObject.addProperty("ShiftCode", 18);
        logHoleSectionDetailObject.addProperty("DrillLogActivityDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

        holeDetailObject.add("LogHoleSectionDetails", logHoleSectionDetailObject);
        holeDetailArray.add(holeDetailObject);



        map.put("basicInformation", new JsonArray());
        map.put("visitorDetails", new JsonArray());
        map.put("preStartCheckListDetails", new JsonArray());
        map.put("drillDetails", drillDetailArray);
        map.put("activityDetails", new JsonArray());
        map.put("drillAccessoriesDetails", new JsonArray());
        map.put("hazardDetails", new JsonArray());
        map.put("rigInspectionDetails", new JsonArray());
        map.put("confirmationFormDetails", new JsonArray());
        map.put("holeDetails", holeDetailArray);

        Log.e("Response Data : ", new Gson().toJson(map));

        MainService.insertUpdateAppSyncDetailsApiCaller(this, map).observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject response) {
                if (response == null) {
                    Log.e(ERROR, SOMETHING_WENT_WRONG);
                } else {
                    if (!(response.isJsonNull())) {
                        try {
                            JsonObject jsonObject = response.getAsJsonObject();
                            if (jsonObject != null) {
                                try {
                                   setInsertUpdateHoleDetailSync(bladesRetrieveData, holeDetailData, jsonObject.get("projectCode").getAsString());
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

    public void setInsertUpdateHoleDetailSync(ResponseBladesRetrieveData bladesRetrieveData, ResponseHoleDetailData holeDetailData, String projectCode) {
        JsonArray holeDetailArray = new JsonArray();
        JsonObject holeDetailObject = new JsonObject();
        holeDetailObject.addProperty("ProjectCode", projectCode);
        holeDetailObject.addProperty("RowNo", holeDetailData.getRowNo());
        holeDetailObject.addProperty("HoleNo", holeDetailData.getHoleNo());
        holeDetailObject.addProperty("HoleName", holeDetailData.getName());
        holeDetailObject.addProperty("UserDefineHoleName", holeDetailData.getName());
        holeDetailObject.addProperty("HoleDiameter", holeDetailData.getHoleDiameter());
        holeDetailObject.addProperty("Burden", String.valueOf(holeDetailData.getBurden()));
        holeDetailObject.addProperty("Spacing", String.valueOf(holeDetailData.getSpacing()));
        holeDetailObject.addProperty("HoleAngle", holeDetailData.getHoleAngle());
        holeDetailObject.addProperty("HoleDeviation", String.valueOf(holeDetailData.getStdDev()));
        holeDetailObject.addProperty("DrillDepth", holeDetailData.getHoleDepth());
        holeDetailObject.addProperty("Northing", 20);
        holeDetailObject.addProperty("Easting", 26);
        holeDetailObject.addProperty("RlTop", 0);
        holeDetailObject.addProperty("RlBottom", 0);
        holeDetailObject.addProperty("rockType", 11);
        holeDetailObject.addProperty("HoleStatus", 1);
        holeDetailObject.addProperty("OperationalSummary", "");
        holeDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
        holeDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
        holeDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        holeDetailObject.addProperty("DeviceType", 0);
        holeDetailObject.addProperty("shiftCode", 18);
        holeDetailObject.addProperty("UserRole", 0);
        holeDetailObject.addProperty("HoleType", String.valueOf(holeDetailData.getHoleType()));
        holeDetailObject.addProperty("CalculateDrillPenetration", 0);
        holeDetailObject.addProperty("DrillPenetrationRate", 0);
        holeDetailObject.addProperty("TotalDrillTime", 0);

        JsonObject logHoleSectionDetailObject = new JsonObject();
        logHoleSectionDetailObject.addProperty("ProjectCode", projectCode);
        logHoleSectionDetailObject.addProperty("HoleName", holeDetailData.getName());
        logHoleSectionDetailObject.addProperty("DepthStart", 0);
        logHoleSectionDetailObject.addProperty("DepthEnd", holeDetailData.getHoleDepth());
        logHoleSectionDetailObject.addProperty("RockType", 105);
        logHoleSectionDetailObject.addProperty("StartTime", "");
        logHoleSectionDetailObject.addProperty("EndTime", "");
        logHoleSectionDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
        logHoleSectionDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
        logHoleSectionDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
        logHoleSectionDetailObject.addProperty("deviceType", "android");
        logHoleSectionDetailObject.addProperty("RigCode", 3);
        logHoleSectionDetailObject.addProperty("DrillerCode", 4);
        logHoleSectionDetailObject.addProperty("DrillMethod", 1);
        logHoleSectionDetailObject.addProperty("ShiftCode", 18);
        logHoleSectionDetailObject.addProperty("DrillLogActivityDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

        holeDetailObject.add("LogHoleSectionDetails", logHoleSectionDetailObject);
        holeDetailArray.add(holeDetailObject);
        Map<String, Object> map = new HashMap<>();
        map.put("", holeDetailArray);

        MainService.insertUpdateAppSyncDetailsApiCaller(this, map).observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject response) {
                if (response == null) {
                    Log.e(ERROR, SOMETHING_WENT_WRONG);
                } else {
                    if (!(response.isJsonNull())) {
                        try {
                            JsonObject jsonObject = response.getAsJsonObject();
                            if (jsonObject != null) {
                                /*try {
                                    setInsertUpdateHoleDetailSync(bladesRetrieveData, holeDetailData, jsonObject.get("projectCode").getAsString());
                                } catch (Exception e) {
                                    Log.e(NODATAFOUND, e.getMessage());
                                }*/

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

}
