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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.ResultsetItem;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.NoInternetBinding;
import com.smart_blasting_drilling.android.dialogs.AppAlertDialogFragment;
import com.smart_blasting_drilling.android.dialogs.AppProgressBar;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.helper.PreferenceManger;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastPerformanceEntity;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDeviceDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseBenchTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdatedProjectDetailEntity;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceAllTypeModel;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceModel;
import com.smart_blasting_drilling.android.ui.models.MapHoleDataModel;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    public AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noInternetdialog = new Dialog(this);
        appDatabase = BaseApplication.getAppDatabase(this, Constants.DATABASE_NAME);

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

    public List<MapHoleDataModel> getRowWiseHoleList(List<ResponseHoleDetailData> holeDetailDataList) {
        List<MapHoleDataModel> rowMapHoleDataModelList = new ArrayList<>();
        List<MapHoleDataModel> colHoleDetailDataList = new ArrayList<>();

        for (ResponseHoleDetailData categoryList : holeDetailDataList) {
            boolean isFound = false;
            List<ResponseHoleDetailData> dataList = new ArrayList<>();
            for (MapHoleDataModel e : rowMapHoleDataModelList) {
                if (e.getRowId() == categoryList.getRowNo()) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                rowMapHoleDataModelList.add(new MapHoleDataModel((int) categoryList.getRowNo(), dataList));
            }
        }

        for (MapHoleDataModel e : rowMapHoleDataModelList) {
            List<ResponseHoleDetailData> dataList = new ArrayList<>();
            for (ResponseHoleDetailData categoryList : holeDetailDataList) {
                if (e.getRowId() == categoryList.getRowNo()) {
                    dataList.add(categoryList);
                }
            }
            colHoleDetailDataList.add(new MapHoleDataModel(e.getRowId(), dataList));
        }
        return colHoleDetailDataList;
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

    public MutableLiveData<JsonPrimitive> setJsonForSyncProjectData(ResponseBladesRetrieveData bladesRetrieveData, List<ResponseHoleDetailData> holeDetailData) {
        MutableLiveData<JsonPrimitive> jsonPrimitiveMutableLiveData = new MutableLiveData<>();
        try {
            showLoader();

            JsonObject map = new JsonObject();

            UpdatedProjectDetailEntity projectDetailEntity = new UpdatedProjectDetailEntity();
            ResponsePitTableEntity pitTableEntity = new ResponsePitTableEntity();
            ResponseZoneTableEntity zoneTableEntity = new ResponseZoneTableEntity();
            ResponseBenchTableEntity benchTableEntity = new ResponseBenchTableEntity();

            if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId())) {
                projectDetailEntity = appDatabase.updatedProjectDataDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            }
            JsonObject projectDetailJson = new Gson().fromJson(projectDetailEntity.getData(), JsonObject.class);

            JsonArray projectTeamArray = new JsonArray();
            JsonObject teamObject = new JsonObject();
            teamObject.addProperty("srNo", 1);

            if (projectDetailJson != null) {
                teamObject.addProperty("employeeCode", projectDetailJson.get("team_id").getAsInt());
            } else {
                teamObject.addProperty("employeeCode", 1);
            }
            projectTeamArray.add(teamObject);

            JsonArray rowDetailArray = new JsonArray();
            if (!Constants.isListEmpty(holeDetailData)) {
                JsonObject rowObject = new JsonObject();
                rowObject.addProperty("rowNo", String.valueOf(holeDetailData.get(0).getRowNo()));
                rowObject.addProperty("holeNo", String.valueOf(holeDetailData.get(0).getHoleNo()));
                rowObject.addProperty("HoleType", "Production");
                rowDetailArray.add(rowObject);
            }

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
            if (!Constants.isListEmpty(holeDetailData)) {
                drillDetailObject.addProperty("rowNo", String.valueOf(holeDetailData.get(0).getRowNo()));
                drillDetailObject.addProperty("holeNo", String.valueOf(holeDetailData.get(0).getHoleNo()));
                drillDetailObject.addProperty("holeName", String.format("R%s/H%s", holeDetailData.get(0).getRowNo(), holeDetailData.get(0).getHoleNo()));
                drillDetailObject.addProperty("holeDiameter", String.valueOf(holeDetailData.get(0).getHoleDiameter()));
                drillDetailObject.addProperty("burden", String.valueOf(holeDetailData.get(0).getBurden()));
                drillDetailObject.addProperty("spacing", String.valueOf(holeDetailData.get(0).getSpacing()));
                drillDetailObject.addProperty("holeAngle", String.valueOf(holeDetailData.get(0).getHoleAngle()));
                drillDetailObject.addProperty("holeDeviation", String.valueOf(0));
                drillDetailObject.addProperty("drillDepth", String.valueOf(holeDetailData.get(0).getHoleDepth()));
                drillDetailObject.addProperty("depthStart", "0");
                drillDetailObject.addProperty("depthEnd", String.valueOf(holeDetailData.get(0).getHoleDepth()));
                drillDetailObject.addProperty("waterLevel", String.valueOf(holeDetailData.get(0).getWaterDepth()));
            }
            drillDetailObject.addProperty("drillerName", "1");
            drillDetailObject.addProperty("drillMethod", "1");
            drillDetailObject.addProperty("northing", String.valueOf(20));
            drillDetailObject.addProperty("easting", String.valueOf(26));
            drillDetailObject.addProperty("rlTop", String.valueOf(0));
            drillDetailObject.addProperty("rlBottom", String.valueOf(0));
            drillDetailObject.addProperty("rockType", String.valueOf(11));
            drillDetailObject.addProperty("holeStatus", String.valueOf(1));
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
            if (!Constants.isListEmpty(holeDetailData)) {
                for (ResponseHoleDetailData holeDetail : holeDetailData) {
                    JsonObject holeDetailObject = new JsonObject();
                    holeDetailObject.addProperty("ProjectCode", 0);
                    holeDetailObject.addProperty("HoleName", String.format("R%s/H%s", holeDetail.getRowNo(), holeDetail.getHoleNo()));
                    holeDetailObject.addProperty("UserDefineHoleName", String.format("R%sH%s", holeDetail.getRowNo(), holeDetail.getHoleNo()));
                    holeDetailObject.addProperty("RowNo", holeDetail.getRowNo());
                    holeDetailObject.addProperty("HoleNo", holeDetail.getHoleNo());
                    holeDetailObject.addProperty("HoleAngle", holeDetail.getHoleAngle());
                    holeDetailObject.addProperty("HoleDeviation", 0);
                    holeDetailObject.addProperty("DrillDepth", holeDetail.getHoleDepth());
                    holeDetailObject.addProperty("Northing", "20");
                    holeDetailObject.addProperty("Easting", "26");
                    holeDetailObject.addProperty("RlTop", 0);
                    holeDetailObject.addProperty("RlBottom", 0);
                    holeDetailObject.addProperty("HoleStatus", 1);
                    holeDetailObject.addProperty("OperationalSummary", "");
                    holeDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    holeDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                    holeDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                    holeDetailObject.addProperty("DeviceType", "android");
                    holeDetailObject.addProperty("UserRole", 0);
                    holeDetailObject.addProperty("HoleDiameter", holeDetail.getHoleDiameter());
                    holeDetailObject.addProperty("Burden", holeDetail.getBurden());
                    holeDetailObject.addProperty("Spacing", holeDetail.getSpacing());
                    holeDetailObject.addProperty("HoleType", (int) Double.parseDouble(holeDetail.getHoleType().toString()));
                    holeDetailObject.addProperty("CalculateDrillPenetration", 0);
                    holeDetailObject.addProperty("DrillPenetrationRate", 0);
                    holeDetailObject.addProperty("TotalDrillTime", 0);

                    JsonObject logHoleSectionDetailObject = new JsonObject();
                    logHoleSectionDetailObject.addProperty("ProjectCode", 0);
                    logHoleSectionDetailObject.addProperty("HoleName", String.format("R%s/H%s", holeDetail.getRowNo(), holeDetail.getHoleNo()));
                    logHoleSectionDetailObject.addProperty("DepthStart", 0);
                    logHoleSectionDetailObject.addProperty("DepthEnd", holeDetail.getHoleDepth());
                    logHoleSectionDetailObject.addProperty("RockType", 105);
                    logHoleSectionDetailObject.addProperty("StartTime", "");
                    logHoleSectionDetailObject.addProperty("EndTime", "");
                    logHoleSectionDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                    logHoleSectionDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                    logHoleSectionDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    logHoleSectionDetailObject.addProperty("DeviceType", "android");
                    logHoleSectionDetailObject.addProperty("RigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsInt() : 0);
                    logHoleSectionDetailObject.addProperty("DrillerCode", 4);
                    logHoleSectionDetailObject.addProperty("DrillMethod", 1);
                    logHoleSectionDetailObject.addProperty("ShiftCode", 18);
                    logHoleSectionDetailObject.addProperty("DrillLogActivityDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

                    holeDetailObject.add("LogHoleSectionDetails", logHoleSectionDetailObject);
                    holeDetailArray.add(holeDetailObject);
                }
            }

            JsonArray basicInformationArray = new JsonArray();
            JsonObject basicInformationObject = new JsonObject();
            basicInformationObject.addProperty("employeeCode", projectDetailJson != null ? projectDetailJson.get("team_id").getAsString() : "0");
            basicInformationObject.addProperty("designation", "4");
            basicInformationObject.addProperty("costperHour", "0");
            basicInformationObject.addProperty("workingHours", "0");
            basicInformationObject.addProperty("totalCost", "40");
            basicInformationObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            basicInformationObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            basicInformationObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
            basicInformationObject.addProperty("userId", manger.getUserDetails().getUserid());
            basicInformationObject.addProperty("shiftCode", 18);
            basicInformationArray.add(basicInformationObject);

            JsonArray visitorDetailsArray = new JsonArray();
            JsonObject visitorDetailsObject = new JsonObject();
            visitorDetailsObject.addProperty("name", "");
            visitorDetailsObject.addProperty("designation", "");
            visitorDetailsObject.addProperty("companyName", "");
            visitorDetailsObject.addProperty("timeIn", "");
            visitorDetailsObject.addProperty("timeOut", "");
            visitorDetailsObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            visitorDetailsObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            visitorDetailsObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
            visitorDetailsObject.addProperty("userId", manger.getUserDetails().getUserid());
            visitorDetailsObject.addProperty("shiftCode", 18);
            visitorDetailsArray.add(visitorDetailsObject);

            JsonArray preStartCheckListArray = new JsonArray();
            JsonObject preStartCheckListObject = new JsonObject();
            preStartCheckListObject.addProperty("preStartCode",0);
            preStartCheckListObject.addProperty("result",0);
            preStartCheckListObject.addProperty("issuesDiscussed",0);
            preStartCheckListObject.addProperty("dailyActivityDate",DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            preStartCheckListObject.addProperty("modificationDate",DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            preStartCheckListObject.addProperty("companyId",manger.getUserDetails().getCompanyid());
            preStartCheckListObject.addProperty("userId",manger.getUserDetails().getUserid());
            preStartCheckListObject.addProperty("shiftCode",18);
            preStartCheckListArray.add(preStartCheckListObject);

            JsonArray activityDetailArray = new JsonArray();
            JsonObject activityDetailsObject = new JsonObject();
            activityDetailsObject.addProperty("activityCode", 0);
            activityDetailsObject.addProperty("startTime", 0);
            activityDetailsObject.addProperty("endTime", 0);
            activityDetailsObject.addProperty("comments", 0);
            activityDetailsObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            activityDetailsObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            activityDetailsObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
            activityDetailsObject.addProperty("userId", manger.getUserDetails().getUserid());
            activityDetailsObject.addProperty("shiftCode", 18);
            activityDetailArray.add(activityDetailsObject);

            JsonArray drillAccessoriesDetailArray = new JsonArray();
            JsonObject drillAccessoriesDetailObject = new JsonObject();
            drillAccessoriesDetailObject.addProperty("categoryCode", 0);
            drillAccessoriesDetailObject.addProperty("categoryItemCode", 0);
            drillAccessoriesDetailObject.addProperty("cost", 0);
            drillAccessoriesDetailObject.addProperty("size", 0);
            drillAccessoriesDetailObject.addProperty("qty", 0);
            drillAccessoriesDetailObject.addProperty("meters", 0);
            drillAccessoriesDetailObject.addProperty("assestNo", 0);
            drillAccessoriesDetailObject.addProperty("SerialNo", 0);
            drillAccessoriesDetailObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            drillAccessoriesDetailObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            drillAccessoriesDetailObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
            drillAccessoriesDetailObject.addProperty("userId", manger.getUserDetails().getUserid());
            drillAccessoriesDetailObject.addProperty("shiftCode", 18);
            drillAccessoriesDetailObject.addProperty("KG", 0);
            drillAccessoriesDetailObject.addProperty("Leter", 0);
            drillAccessoriesDetailObject.addProperty("TotalCost", 0);
            drillAccessoriesDetailArray.add(drillAccessoriesDetailObject);

            JsonArray hazardDetailsArray = new JsonArray();
            JsonObject hazardDetailsObject = new JsonObject();
            hazardDetailsObject.addProperty("hazardAssessment", "");
            hazardDetailsObject.addProperty("risk", "");
            hazardDetailsObject.addProperty("significant", "");
            hazardDetailsObject.addProperty("eliminateIsolateMinimise", "");
            hazardDetailsObject.addProperty("whatControlstoReduceRisk", "");
            hazardDetailsObject.addProperty("actionedBy", "");
            hazardDetailsObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            hazardDetailsObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            hazardDetailsObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
            hazardDetailsObject.addProperty("userId", manger.getUserDetails().getUserid());
            hazardDetailsObject.addProperty("shiftCode", 18);

            hazardDetailsArray.add(hazardDetailsObject);

            JsonArray rigInspectionDetailArray = new JsonArray();
            JsonObject rigInspectionObject = new JsonObject();
            rigInspectionObject.addProperty("rigInspectionCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsString() : "0");
            rigInspectionObject.addProperty("rigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsString() : "0");
            rigInspectionObject.addProperty("result", "10");
            rigInspectionObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            rigInspectionObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            rigInspectionObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
            rigInspectionObject.addProperty("userId", manger.getUserDetails().getUserid());
            rigInspectionObject.addProperty("shiftCode", 18);
            rigInspectionDetailArray.add(rigInspectionObject);

            JsonArray confirmationFormDetailArray = new JsonArray();
            JsonObject confirmationFormDetailObject = new JsonObject();
            confirmationFormDetailObject.addProperty("drillerName", projectDetailJson != null ? projectDetailJson.get("team_name").getAsString() : "");
            confirmationFormDetailObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            confirmationFormDetailObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            confirmationFormDetailObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
            confirmationFormDetailObject.addProperty("userId", manger.getUserDetails().getUserid());
            confirmationFormDetailObject.addProperty("shiftCode", 18);
            confirmationFormDetailArray.add(confirmationFormDetailObject);


            // Setup Map Data

            map.addProperty("projectNumber", bladesRetrieveData.getDesignCode());
            map.addProperty("projectName", bladesRetrieveData.getDesignName());
            map.addProperty("siteCode", projectDetailJson != null ? projectDetailJson.get("site_id").getAsInt() : 0);
            map.addProperty("pitCode", AppDelegate.getInstance().getCodeIdObject().get("pitId").getAsInt());
            map.addProperty("zoneCode", AppDelegate.getInstance().getCodeIdObject().get("zoneId").getAsInt());
            map.addProperty("benchCode", AppDelegate.getInstance().getCodeIdObject().get("benchId").getAsString());

            map.addProperty("rigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsString() : "0");
            map.addProperty("drillingTypeId", projectDetailJson != null ? projectDetailJson.get("drill_type_id").getAsInt() : 0);
            map.addProperty("materialTypeId", projectDetailJson != null ? projectDetailJson.get("drill_material_id").getAsInt() : 0);
            map.addProperty("startTime", DateUtils.getDate(System.currentTimeMillis(),"yyyy-MM-dd hh:mm a"));
            map.addProperty("endTime", "");
            map.addProperty("drillPattern", projectDetailJson != null ? projectDetailJson.get("drill_pattern").getAsString() : "Staggered");
            map.addProperty("clientName", projectDetailJson != null ? projectDetailJson.get("client_name").getAsString() : "");
            map.addProperty("creationDate", DateUtils.getDate(System.currentTimeMillis(),"yyyy-MM-dd hh:mm a"));
            map.addProperty("companyId", manger.getUserDetails().getCompanyid());
            map.addProperty("userId", manger.getUserDetails().getUserid());
            map.addProperty("projectStatus", projectDetailJson != null ? projectDetailJson.get("project_status").getAsString() : "Open");
            map.addProperty("modificationDate", projectDetailJson != null ? String.format("%s %s", projectDetailJson.get("start_date").getAsString().replace("/", "-"), projectDetailJson.get("start_time").getAsString()) : DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            map.addProperty("deviceType", "android");
            map.addProperty("projectSource", "");
            map.add("projectTeamDetails", projectTeamArray);
            map.add("rowDetails", rowDetailArray);
            map.add("basicInformation", /*basicInformationArray*/ new JsonArray());
            map.add("hazardDetails", /*hazardDetailsArray*/ new JsonArray());
            map.add("visitorDetails", /*visitorDetailsArray*/ new JsonArray());
            map.add("preStartCheckListDetails", /*preStartCheckListArray*/new JsonArray());
            map.add("drillDetails", drillDetailArray);
            map.add("activityDetails", /*activityDetailArray*/ new JsonArray());
            map.add("drillAccessoriesDetails", /*drillAccessoriesDetailArray*/new JsonArray());
            map.add("rigInspectionDetails", /*rigInspectionDetailArray*/new JsonArray());
            map.add("confirmationFormDetails", confirmationFormDetailArray);
            map.add("holeDetails", holeDetailArray);

            Log.e("Response Data : ", new Gson().toJson(map));

            MainService.insertUpdateAppSyncDetailsApiCaller(this, map).observe(this, new Observer<JsonPrimitive>() {
                @Override
                public void onChanged(JsonPrimitive response) {
                    if (response == null) {
                        Log.e(ERROR, SOMETHING_WENT_WRONG);
                    } else {
                        if (!(response.isJsonNull())) {
                            try {
                                JsonObject jsonObject = new Gson().fromJson(response.getAsString(), JsonElement.class).getAsJsonObject();
                                if (jsonObject != null) {
                                    try {
                                        if (jsonObject.get("response").getAsString().equalsIgnoreCase("fail")) {
                                            showToast(StringUtill.getString(jsonObject.get("message").getAsString()));
                                        } else {
                                            AllProjectBladesModelEntity modelEntity = appDatabase.allProjectBladesModelDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
                                            if (modelEntity == null) {
                                                modelEntity = new AllProjectBladesModelEntity();
                                                modelEntity.setDesignId(bladesRetrieveData.getDesignId());
                                                modelEntity.setData(new Gson().toJson(bladesRetrieveData));
                                                modelEntity.setIs2dBlade(bladesRetrieveData.isIs3dBlade());
                                            }
                                            modelEntity.setProjectCode(jsonObject.get("projectCode").getAsString());

                                            if (!appDatabase.allProjectBladesModelDao().isExistItem(bladesRetrieveData.getDesignId())) {
                                                appDatabase.allProjectBladesModelDao().insertItem(modelEntity);
                                            } else {
                                                appDatabase.allProjectBladesModelDao().updateItem(bladesRetrieveData.getDesignId(), modelEntity.getProjectCode(), new Gson().toJson(bladesRetrieveData));
                                            }
                                        }
//                                   setInsertUpdateHoleDetailSync(bladesRetrieveData, holeDetailData, jsonObject.get("projectCode").getAsString());
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
                    jsonPrimitiveMutableLiveData.setValue(response);
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
        return jsonPrimitiveMutableLiveData;
    }

    public void setInsertUpdateHoleDetailSync(ResponseBladesRetrieveData bladesRetrieveData, ResponseHoleDetailData holeDetailData, String projectCode) {
        try {
            showLoader();
            JsonArray holeDetailArray = new JsonArray();
            JsonObject holeDetailObject = new JsonObject();
            holeDetailObject.addProperty("ProjectCode", Integer.parseInt(projectCode));
            holeDetailObject.addProperty("RowNo", holeDetailData.getRowNo());
            holeDetailObject.addProperty("HoleNo", holeDetailData.getHoleNo());
            holeDetailObject.addProperty("HoleName", String.format("R%s/H%s", holeDetailData.getRowNo(), holeDetailData.getHoleNo()));
            holeDetailObject.addProperty("UserDefineHoleName", holeDetailData.getName());
            holeDetailObject.addProperty("HoleDiameter", holeDetailData.getHoleDiameter());
            holeDetailObject.addProperty("Burden", String.valueOf(holeDetailData.getBurden()));
            holeDetailObject.addProperty("Spacing", String.valueOf(holeDetailData.getSpacing()));
            holeDetailObject.addProperty("HoleAngle", holeDetailData.getHoleAngle());
            holeDetailObject.addProperty("HoleDeviation", String.valueOf(0));
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
            logHoleSectionDetailObject.addProperty("ProjectCode", Integer.parseInt(projectCode));
            logHoleSectionDetailObject.addProperty("HoleName", String.format("R%s/H%s", holeDetailData.getRowNo(), holeDetailData.getHoleNo()));
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

            MainService.insertUpdateAppHoleDetailsSyncApiCaller(this, holeDetailObject).observe(this, new Observer<JsonPrimitive>() {
                @Override
                public void onChanged(JsonPrimitive response) {
                    if (response == null) {
                        Log.e(ERROR, SOMETHING_WENT_WRONG);
                    } else {
                        if (!(response.isJsonNull())) {
                            showToast("Project Sync Successfully");
                        } else {
                            showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                        }
                    }
                    hideLoader();
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
    }

    public MutableLiveData<JsonPrimitive> setInsertUpdateHoleDetailMultipleSync(ResponseBladesRetrieveData bladesRetrieveData, List<ResponseHoleDetailData> holeDetailData, String projectCode) {
        MutableLiveData<JsonPrimitive> jsonObjectMutableLiveData = new MutableLiveData<>();
        try {
            showLoader();
            JsonObject mapObject = new JsonObject();
            mapObject.addProperty("ProjectCode", Integer.parseInt(projectCode));
            JsonArray holeDetailArray = new JsonArray();
            if (!Constants.isListEmpty(holeDetailData)) {
                for (ResponseHoleDetailData detailData : holeDetailData) {
                    JsonObject holeDetailObject = new JsonObject();
                    holeDetailObject.addProperty("ProjectCode", projectCode);
                    holeDetailObject.addProperty("RowNo", detailData.getRowNo());
                    holeDetailObject.addProperty("HoleNo", detailData.getHoleNo());
                    holeDetailObject.addProperty("HoleName", String.format("R%s/H%s", detailData.getRowNo(), detailData.getHoleNo()));
                    holeDetailObject.addProperty("UserDefineHoleName", detailData.getName());
                    holeDetailObject.addProperty("HoleDiameter", detailData.getHoleDiameter());
                    holeDetailObject.addProperty("Burden", String.valueOf(detailData.getBurden()));
                    holeDetailObject.addProperty("Spacing", String.valueOf(detailData.getSpacing()));
                    holeDetailObject.addProperty("HoleAngle", detailData.getHoleAngle());
                    holeDetailObject.addProperty("HoleDeviation", String.valueOf(0));
                    holeDetailObject.addProperty("DrillDepth", detailData.getHoleDepth());
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
                    holeDetailObject.addProperty("HoleType", String.valueOf(detailData.getHoleType()));
                    holeDetailObject.addProperty("CalculateDrillPenetration", 0);
                    holeDetailObject.addProperty("DrillPenetrationRate", 0);
                    holeDetailObject.addProperty("TotalDrillTime", 0);

                    JsonObject logHoleSectionDetailObject = new JsonObject();
                    logHoleSectionDetailObject.addProperty("ProjectCode", projectCode);
                    logHoleSectionDetailObject.addProperty("HoleName", String.format("R%s/H%s", detailData.getRowNo(), detailData.getHoleNo()));
                    logHoleSectionDetailObject.addProperty("DepthStart", 0);
                    logHoleSectionDetailObject.addProperty("DepthEnd", detailData.getHoleDepth());
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
                }
            }

            mapObject.add("HoleDetails", holeDetailArray);

            MainService.insertUpdateAppHoleDetailsmultipleSyncApiCaller(this, mapObject).observe(this, new Observer<JsonPrimitive>() {
                @Override
                public void onChanged(JsonPrimitive response) {
                    jsonObjectMutableLiveData.setValue(response);
                    hideLoader();
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
        return jsonObjectMutableLiveData;
    }


    // Bims Insert Sync
    public void blastInsertSyncRecordApiCaller(ResponseBladesRetrieveData bladesRetrieveData, AllTablesData tablesData, int rowCount, int holeCount, String blastCode) {
        try {
            showLoader();
            // Blast Performance Data
            BlastPerformanceEntity blastPerformanceEntity = appDatabase.blastPerformanceDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            String blastPerformanceStr = "";
            JsonObject blastPerformanceObject = new JsonObject();
            if (blastPerformanceEntity != null) {
                blastPerformanceStr = blastPerformanceEntity.getData();
                blastPerformanceObject = new Gson().fromJson(blastPerformanceStr, JsonObject.class);
            }

            // Blast Performance Data
            InitiatingDeviceDataEntity initiatingDeviceDataEntity = appDatabase.initiatingDeviceDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            String initiatingDeviceDataStr = "";
            JsonArray initiatingDeviceDataObject = new JsonArray();
            if (initiatingDeviceDataEntity != null) {
                initiatingDeviceDataStr = initiatingDeviceDataEntity.getData();
                initiatingDeviceDataObject = new Gson().fromJson(initiatingDeviceDataStr, JsonArray.class);
            }


            // ExpUsedDetails Array
            JsonArray expUsedDetailArray = new JsonArray();

            if (!Constants.isListEmpty(tablesData.getTable2())) {
                if (!Constants.isListEmpty(tablesData.getTable9())) {
                    if (tablesData.getTable9().size() >= 3) {
                        JsonObject expUsedDetailObject = new JsonObject();
                        expUsedDetailObject.addProperty("expcode", String.valueOf(tablesData.getTable9().get(2).getProdId()));
                        expUsedDetailObject.addProperty("qty", String.valueOf(tablesData.getTable9().get(2).getProdQty()));
                        expUsedDetailArray.add(expUsedDetailObject);
                    }

                    if (tablesData.getTable9().size() >= 4) {
                        JsonObject expUsedDetailObject = new JsonObject();
                        expUsedDetailObject.addProperty("expcode1", String.valueOf(tablesData.getTable9().get(3).getProdId()));
                        expUsedDetailObject.addProperty("qty", String.valueOf(tablesData.getTable9().get(3).getProdQty()));
                        expUsedDetailArray.add(expUsedDetailObject);
                    }

                    if (tablesData.getTable9().size() >= 5) {
                        JsonObject expUsedDetailObject = new JsonObject();
                        expUsedDetailObject.addProperty("expcode2", String.valueOf(tablesData.getTable9().get(4).getProdId()));
                        expUsedDetailObject.addProperty("qty", String.valueOf(tablesData.getTable9().get(4).getProdQty()));
                        expUsedDetailArray.add(expUsedDetailObject);
                    }
                }
            }

            // RowDetails Array
            JsonArray rowDetailsArray = new JsonArray();
            JsonObject rowDetailsObject = new JsonObject();
            if (!Constants.isListEmpty(tablesData.getTable2())) {
                List<MapHoleDataModel> mapHoleDataModels = getRowWiseHoleList(tablesData.getTable2());
                for (int i = 0; i < mapHoleDataModels.size(); i++) {
                    rowDetailsObject.addProperty("rowno", String.valueOf(i + 1));
                    if (!Constants.isListEmpty(mapHoleDataModels.get(i).getHoleDetailDataList()))
                        rowDetailsObject.addProperty("holeno", String.valueOf(mapHoleDataModels.get(i).getHoleDetailDataList().size()));
                    else rowDetailsObject.addProperty("holeno", String.valueOf(0));
                    rowDetailsObject.addProperty("rowtype", String.valueOf(1));
                    rowDetailsArray.add(rowDetailsObject);
                }
            }

            // Charge Details Array
            JsonArray chargeDetailsArray = new JsonArray();
            if (!Constants.isListEmpty(tablesData.getTable2())) {
                for (int i = 0; i < tablesData.getTable2().size(); i++) {
                    JsonObject chargeDetailsObject = new JsonObject();
                    chargeDetailsObject.addProperty("BlastCode", blastCode);
                    chargeDetailsObject.addProperty("RowNo",String.valueOf(tablesData.getTable2().get(i).getRowNo()));
                    chargeDetailsObject.addProperty("HoleNo", String.valueOf(tablesData.getTable2().get(i).getHoleNo()));
                    chargeDetailsObject.addProperty("HoleName",String.format("R%sH%s",  tablesData.getTable2().get(i).getRowNo(), tablesData.getTable2().get(i).getHoleNo()));
                    chargeDetailsObject.addProperty("RowType","Production");
                    chargeDetailsObject.addProperty("HoleDia", String.valueOf(tablesData.getTable2().get(i).getHoleDiameter()));
                    chargeDetailsObject.addProperty("ExpCode", String.valueOf(tablesData.getTable2().get(i).getExpCode()));
                    chargeDetailsObject.addProperty("Weight", "30");
                    chargeDetailsObject.addProperty("ExpLength", "4");
                    chargeDetailsObject.addProperty("CostPerUnit", String.valueOf(tablesData.getTable2().get(i).getCostUnit()));
                    chargeDetailsObject.addProperty("ExpCode1",String.valueOf(tablesData.getTable2().get(i).getExpCode1()));
                    chargeDetailsObject.addProperty("Weight1","20");
                    chargeDetailsObject.addProperty("ExpLength1","4");
                    chargeDetailsObject.addProperty("CostPerUnit1",String.valueOf(tablesData.getTable2().get(i).getCostUnit1()));
                    chargeDetailsObject.addProperty("ExpCode2",String.valueOf(tablesData.getTable2().get(i).getExpCode2()));
                    chargeDetailsObject.addProperty("Weight2","0.4");
                    chargeDetailsObject.addProperty("ExpLength2","0");
                    chargeDetailsObject.addProperty("CostPerUnit2",String.valueOf(tablesData.getTable2().get(i).getCostUnit2()));
                    chargeDetailsObject.addProperty("Burden",String.valueOf(tablesData.getTable2().get(i).getBurden()));
                    chargeDetailsObject.addProperty("Spacing",String.valueOf(tablesData.getTable2().get(i).getSpacing()));
                    chargeDetailsObject.addProperty("Delay1", "0");
                    chargeDetailsObject.addProperty("Delay2", String.valueOf(tablesData.getTable2().get(i).getDelay()));
                    chargeDetailsObject.addProperty("TopBaseChargePercent", "0");
                    chargeDetailsObject.addProperty("BottomBaseChargePercent", "100");
                    chargeDetailsObject.addProperty("DeckDepth","");
                    chargeDetailsObject.addProperty("DeckStart","");
                    chargeDetailsObject.addProperty("SteamLen", String.valueOf(tablesData.getTable2().get(i).getStemLngth()));
                    chargeDetailsObject.addProperty("WaterDepth", String.valueOf(tablesData.getTable2().get(i).getWaterDepth()));
                    chargeDetailsObject.addProperty("HoleDepth", String.valueOf(tablesData.getTable2().get(i).getHoleDepth()));
                    chargeDetailsObject.addProperty("Subgrade", String.valueOf(tablesData.getTable2().get(i).getSubgrade()));
                    chargeDetailsObject.addProperty("IsHoleBlock","");
                    chargeDetailsObject.addProperty("HoleBlockLength","");
                    chargeDetailsObject.addProperty("HoleAngle", String.valueOf(tablesData.getTable2().get(i).getHoleAngle()));
                    chargeDetailsObject.addProperty("InHoleDelay", String.valueOf(tablesData.getTable2().get(i).getInHoleDelay()));
                    chargeDetailsArray.add(chargeDetailsObject);
                }
            }
            JsonArray edDetailsArray = new JsonArray();
            JsonArray dthDetailsArray = new JsonArray();
            JsonArray tldDetailsHoleToHoleArray = new JsonArray();
            JsonArray tldDetailsRowToRowArray = new JsonArray();

            if (initiatingDeviceDataObject != null) {
                if (initiatingDeviceDataObject.size() > 0) {
                    for (int i = 0; i < initiatingDeviceDataObject.size(); i++) {
                        InitiatingDeviceAllTypeModel model = new Gson().fromJson(initiatingDeviceDataObject.get(i).getAsJsonObject(), InitiatingDeviceAllTypeModel.class);
                        if (model.getDeviceName().equals("Electronic/Electric Detonator")) {
                            // EDDetails Array
                            for (InitiatingDeviceModel deviceModel : model.getDeviceModelList()) {
                                JsonObject edDetailsObject = new JsonObject();
                                edDetailsObject.addProperty("BlastCode", blastCode);
                                edDetailsObject.addProperty("EDCode", String.valueOf(deviceModel.getPageCount()));
                                edDetailsObject.addProperty("EDName", deviceModel.getType());
                                edDetailsObject.addProperty("EDCost", deviceModel.getCost());
                                edDetailsObject.addProperty("EDQty", deviceModel.getQty());
                                edDetailsArray.add(edDetailsObject);
                            }
                        }

                        if (model.getDeviceName().equals("Down The Hole")) {
                            for (InitiatingDeviceModel deviceModel : model.getDeviceModelList()) {
                                // DTHDetails Array
                                JsonObject dthDetailsObject = new JsonObject();
                                dthDetailsObject.addProperty("BlastCode", blastCode);
                                dthDetailsObject.addProperty("DTHCode", String.valueOf(deviceModel.getPageCount()));
                                dthDetailsObject.addProperty("DTHName", deviceModel.getType());
                                dthDetailsObject.addProperty("DTHCost",  deviceModel.getCost());
                                dthDetailsObject.addProperty("DTHQty", deviceModel.getQty());
                                dthDetailsArray.add(dthDetailsObject);
                            }
                        }

                        if (model.getDeviceName().equals("TLD(Row To Row)")) {
                            for (InitiatingDeviceModel deviceModel : model.getDeviceModelList()) {
                                // TLDDetailsRowToRow Array
                                JsonObject tldDetailsRowToRowObject = new JsonObject();
                                tldDetailsRowToRowObject.addProperty("BlastCode", blastCode);
                                tldDetailsRowToRowObject.addProperty("TldCode", String.valueOf(deviceModel.getPageCount()));
                                tldDetailsRowToRowObject.addProperty("TldName", deviceModel.getType());
                                tldDetailsRowToRowObject.addProperty("TldCost", deviceModel.getCost());
                                tldDetailsRowToRowObject.addProperty("TldQty", deviceModel.getQty());
                                tldDetailsRowToRowArray.add(tldDetailsRowToRowObject);
                            }
                        }

                        if (model.getDeviceName().equals("TLD(Hole To Hole)")) {
                            for (InitiatingDeviceModel deviceModel : model.getDeviceModelList()) {
                                // TLDDetailsHoletoHole Array
                                JsonObject tldDetailsHoletoHoleObject = new JsonObject();
                                tldDetailsHoletoHoleObject.addProperty("BlastCode", blastCode);
                                tldDetailsHoletoHoleObject.addProperty("TldCode", String.valueOf(deviceModel.getPageCount()));
                                tldDetailsHoletoHoleObject.addProperty("TldName", deviceModel.getType());
                                tldDetailsHoletoHoleObject.addProperty("TldCost", deviceModel.getCost());
                                tldDetailsHoletoHoleObject.addProperty("TldQty", deviceModel.getQty());
                                tldDetailsHoleToHoleArray.add(tldDetailsHoletoHoleObject);
                            }
                        }
                    }



                }
            }

            // HolePoints Array
            JsonArray holepointsArray = new JsonArray();
            JsonObject holepointsObject = new JsonObject();
            holepointsObject.addProperty("BlastCode", blastCode);
            holepointsObject.addProperty("HoleNumber", "");
            holepointsObject.addProperty("HoleDelay", "");
            holepointsObject.addProperty("x", "");
            holepointsObject.addProperty("y", "");
            holepointsObject.addProperty("lat", "");
            holepointsObject.addProperty("lng", "");
            holepointsObject.addProperty("rltop", "");
            holepointsObject.addProperty("rlbottom", "");
            holepointsObject.addProperty("HoleName", "");
            holepointsObject.addProperty("InHoleDelay", "");
            holepointsArray.add(holepointsObject);

            // All Map Data
            JsonObject mapObject = new JsonObject();
            mapObject.addProperty("BlastCode", blastCode);
            mapObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
            mapObject.addProperty("UserId", manger.getUserDetails().getUserid());
            mapObject.addProperty("DeviceType", "Android");
            mapObject.addProperty("DeviceId", Constants.getDeviceId(this));
            mapObject.addProperty("blastno", "NAR275201913155677");

            mapObject.addProperty("mineCode", AppDelegate.getInstance().getCodeIdObject().get("MineId").getAsString());
            mapObject.addProperty("pitCode", AppDelegate.getInstance().getCodeIdObject().get("pitId").getAsString());
            mapObject.addProperty("zoneCode", AppDelegate.getInstance().getCodeIdObject().get("zoneId").getAsString());
            mapObject.addProperty("benchCode", AppDelegate.getInstance().getCodeIdObject().get("benchId").getAsString());
            mapObject.addProperty("rockCode", "8");
            mapObject.addProperty("rockDensity", "2.5");
            mapObject.addProperty("blastDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("blastTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("BenchHeight", "10");
            mapObject.addProperty("FaceLength", "40");
            if (!Constants.isListEmpty(tablesData.getTable2())) {
                mapObject.addProperty("Burden", String.valueOf(tablesData.getTable2().get(0).getBurden()));
                mapObject.addProperty("Spacing", String.valueOf(tablesData.getTable2().get(0).getSpacing()));
                mapObject.addProperty("DrillPattern", "1");
            }
            mapObject.addProperty("HoleDelay", "17");
            mapObject.addProperty("RowDelay", "25");
            mapObject.addProperty("Rows", String.valueOf(rowCount));
            mapObject.addProperty("TotalHoles", String.valueOf(tablesData.getTable2().size()));
            mapObject.addProperty("CreationDate", DateUtils.getFormattedTime(bladesRetrieveData.getDesignDateTime(), "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("ModificationDate", DateUtils.getFormattedTime(bladesRetrieveData.getDesignDateTime(), "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("SyncDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("UpdatedBy", manger.getUserDetails().getUserid());
            mapObject.addProperty("ProdVol", "140000");
            mapObject.addProperty("Prodton", "400");
            mapObject.addProperty("TotExp", "15000");
            mapObject.addProperty("TotColChrge", "400");
            mapObject.addProperty("TotBasechrge", "200");
            mapObject.addProperty("PowderFactor", "15");
            mapObject.addProperty("DrillFactor", "10");
            mapObject.addProperty("TonRecover", "10");
            mapObject.addProperty("TotCharge", "10");
            mapObject.addProperty("TotSteam", "10");
            mapObject.addProperty("DrillMtr", "10");
            mapObject.addProperty("FlyRock", (blastPerformanceObject != null && blastPerformanceObject.has("FlyRock")) ? blastPerformanceObject.get("FlyRock").getAsString() : "");
            mapObject.addProperty("Heavy",  1);
            mapObject.addProperty("BoulderCount",  (blastPerformanceObject != null && blastPerformanceObject.has("BounderCount")) ? blastPerformanceObject.get("BounderCount").getAsString() : "");
            mapObject.addProperty("Displacement",  (blastPerformanceObject != null && blastPerformanceObject.has("DisPlace")) ? blastPerformanceObject.get("DisPlace").getAsString() : "");
            mapObject.addProperty("StemEject", 1);
            mapObject.addProperty("Muck",  1);
            mapObject.addProperty("BlastFumes",  1);
            mapObject.addProperty("OverBlock", (blastPerformanceObject != null && blastPerformanceObject.has("BackBreak")) ? blastPerformanceObject.get("BackBreak").getAsString() : "");
            mapObject.add("ExpUsedDetails", expUsedDetailArray);
            mapObject.add("RowDetails", rowDetailsArray);
            mapObject.add("chargedetails", chargeDetailsArray);
            mapObject.add("EDDetails", edDetailsArray);
            mapObject.add("DthDetails", dthDetailsArray);
            mapObject.add("TLDDetailsHoletoHole", tldDetailsHoleToHoleArray);
            mapObject.add("TLDDetailsRowToRow", tldDetailsRowToRowArray);
            mapObject.addProperty("Drillcostpermeter", "0.0");
            mapObject.addProperty("Latcord", "20");
            mapObject.addProperty("longcord", "30");
            mapObject.add("Holepoints", holepointsArray);
            mapObject.addProperty("Facepoints", "");

            Log.e("Record : ", new Gson().toJson(mapObject));
            MainService.bimsInsertSyncRecordApiCaller(this, mapObject).observe(this, new Observer<JsonObject>() {
                @Override
                public void onChanged(JsonObject response) {
                    if (response == null) {
                        Log.e(ERROR, SOMETHING_WENT_WRONG);
                    } else {
                        if (!(response.isJsonNull())) {
                            JsonObject jsonObject;
                            if (response.isJsonObject()) {
                                jsonObject = response.getAsJsonObject();
                            } else {
                                jsonObject = new Gson().fromJson(response, JsonObject.class);
                            }
                            String blastCode = jsonObject.get("ReturnObject").getAsString();
                            if (!Constants.isListEmpty(appDatabase.blastCodeDao().getAllEntityDataList())) {
                                appDatabase.blastCodeDao().updateItem(0, blastCode);
                            } else {
                                BlastCodeEntity blastCodeEntity = new BlastCodeEntity();
                                blastCodeEntity.setBlastCode(blastCode);
                                appDatabase.blastCodeDao().insertItem(blastCodeEntity);
                            }
                        }
                    }
                    hideLoader();
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
    }

    public void insertActualDesignChartSheetApiCaller() {
        JsonArray mapObjectArray = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("BsterId", "");
        object.addProperty("BsterLength", "");
        object.addProperty("BsterWt", "");
        object.addProperty("BtmBasePrcnt", "");
        object.addProperty("BtmId", "");
        object.addProperty("BtmLength", "");
        object.addProperty("BtmWt", "");
        object.addProperty("Burden", "");
        object.addProperty("ColId", "");
        object.addProperty("ColLength", "");
        object.addProperty("ColWt", "");
        object.addProperty("DeckLength", "");
        object.addProperty("DeckLength1", "");
        object.addProperty("DeckLength2", "");
        object.addProperty("DeckLength3", "");
        object.addProperty("DeckLength4", "");
        object.addProperty("DeckLength5", "");
        object.addProperty("DeckStartAt", "");
        object.addProperty("DeckStartAt1", "");
        object.addProperty("DeckStartAt2", "");
        object.addProperty("DeckStartAt3", "");
        object.addProperty("DeckStartAt4", "");
        object.addProperty("DeckStartAt5", "");
        object.addProperty("Delay", "");
        object.addProperty("DesignId", "");
        object.addProperty("DrillX", "");
        object.addProperty("DrillY", "");
        object.addProperty("HHDelay", "");
        object.addProperty("HoleDepth", "");
        object.addProperty("HoleID", "");
        object.addProperty("HoleNo", "");
        object.addProperty("HoleType", "");
        object.addProperty("InHoleDelay", "");
        object.addProperty("NoOfDecks", "");
        object.addProperty("RRDelay", "");
        object.addProperty("RowNo", "");
        object.addProperty("Spacing", "");
        object.addProperty("StemLngth", "");
        object.addProperty("Subgrade", "");
        object.addProperty("TopBasePrcnt", "");
        object.addProperty("WaterDepth", "");
        object.addProperty("X", "");
        object.addProperty("Y", "");
        object.addProperty("holeAngle", "");
        object.addProperty("inHoleDelayQty", "");
        mapObjectArray.add(object);

        MainService.insertActualDesignChartSheetApiCaller(this, mapObjectArray).observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

            }
        });

    }

    public void insertUpdate3DActualDesignHoleDetailApiCaller() {
        JsonArray mapObjectArray = new JsonArray();
        JsonObject object = new JsonObject();
        object.addProperty("Block", "");
        object.addProperty("BlockLength", "");
        object.addProperty("BottomX", "");
        object.addProperty("BottomY", "");
        object.addProperty("BottomZ", "");
        object.addProperty("Burden", "");
        object.addProperty("ChargeLength", "");

        JsonArray chargeTypeArray = new JsonArray();
        JsonObject chargeTypeObject = new JsonObject();
        chargeTypeObject.addProperty("type","");
        chargeTypeObject.addProperty("name","");
        chargeTypeObject.addProperty("cost","");
        chargeTypeObject.addProperty("weight","");
        chargeTypeObject.addProperty("length","");
        chargeTypeObject.addProperty("prodType","");
        chargeTypeObject.addProperty("prodId","");
        chargeTypeObject.addProperty("color","");
        chargeTypeObject.addProperty("percentage","");
        chargeTypeArray.add(chargeTypeObject);

        object.add("ChargeTypeArray", chargeTypeArray);
        object.addProperty("DeckLength", "");
        object.addProperty("DesignId", "");
        object.addProperty("HoleDelay", "");
        object.addProperty("HoleDepth", "");
        object.addProperty("HoleDiameter", "");
        object.addProperty("HoleID", "");
        object.addProperty("HoleNo", "");
        object.addProperty("HoleType", "");
        object.addProperty("InHoleDelay", "");
        object.addProperty("RowNo", "");
        object.addProperty("Spacing", "");
        object.addProperty("StemmingLength", "");
        object.addProperty("Subgrade", "");
        object.addProperty("TielineId", "");
        object.addProperty("TopX", "");
        object.addProperty("TopY", "");
        object.addProperty("TopZ", "");
        object.addProperty("TotalCharge", "");
        object.addProperty("VerticalDip", "");
        object.addProperty("WaterDepth", "");

        mapObjectArray.add(object);

        MainService.insertUpdate3DActualDesignHoleDetailApiCaller(this, mapObjectArray).observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {

            }
        });

    }

}
