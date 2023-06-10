package com.smart_blasting_drilling.android.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
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
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseProjectModelFromAllInfoApi;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.Table9Item;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.ChargeTypeArrayItem;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.NoInternetBinding;
import com.smart_blasting_drilling.android.dialogs.AppAlertDialogFragment;
import com.smart_blasting_drilling.android.dialogs.AppProgressBar;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.helper.PreferenceManger;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastCodeEntity;
import com.smart_blasting_drilling.android.room_database.entities.BlastPerformanceEntity;
import com.smart_blasting_drilling.android.room_database.entities.InitiatingDeviceDataEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseBenchTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponsePitTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.ResponseZoneTableEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdatedProjectDetailEntity;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceAllTypeModel;
import com.smart_blasting_drilling.android.ui.models.InitiatingDeviceModel;
import com.smart_blasting_drilling.android.ui.models.MapHole3DDataModel;
import com.smart_blasting_drilling.android.ui.models.MapHoleDataModel;
import com.smart_blasting_drilling.android.ui.models.NorthEastCoordinateModel;
import com.smart_blasting_drilling.android.ui.models.ReferenceCoordinateModel;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;


import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BaseActivity extends AppCompatActivity {
    Toast toast;
    public Dialog noInternetdialog;

    DecimalFormat decimalFormat = new DecimalFormat("#.##");
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

    public int dp2px(@NotNull Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
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

    public List<MapHole3DDataModel> getRowWiseHoleIn3dList(List<Response3DTable4HoleChargingDataModel> holeDetailDataList) {
        List<MapHole3DDataModel> rowMapHoleDataModelList = new ArrayList<>();
        List<MapHole3DDataModel> colHoleDetailDataList = new ArrayList<>();

        for (Response3DTable4HoleChargingDataModel categoryList : holeDetailDataList) {
            boolean isFound = false;
            List<Response3DTable4HoleChargingDataModel> dataList = new ArrayList<>();
            for (MapHole3DDataModel e : rowMapHoleDataModelList) {
                if (String.valueOf(categoryList.getRowNo()).equals(String.valueOf(e.getRowId()))) {
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                rowMapHoleDataModelList.add(new MapHole3DDataModel(String.valueOf(categoryList.getRowNo()), dataList));
            }
        }

        for (MapHole3DDataModel e : rowMapHoleDataModelList) {
            List<Response3DTable4HoleChargingDataModel> dataList = new ArrayList<>();
            for (Response3DTable4HoleChargingDataModel categoryList : holeDetailDataList) {
                if (e.getRowId().equals(String.valueOf(categoryList.getRowNo()))) {
                    dataList.add(categoryList);
                }
            }
            colHoleDetailDataList.add(new MapHole3DDataModel(e.getRowId(), dataList));
        }
        return colHoleDetailDataList;
    }

    public void noInternetDialog() {
        if (!noInternetdialog.isShowing()) {
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

    public MutableLiveData<JsonElement> setJsonForSyncProject3DData(Response3DTable1DataModel bladesRetrieveData, List<Response3DTable4HoleChargingDataModel> holeDetailData) {
        MutableLiveData<JsonElement> jsonPrimitiveMutableLiveData = new MutableLiveData<>();
        if (ConnectivityReceiver.getInstance().isInternetAvailable()) {
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
                    List<MapHole3DDataModel> mapHoleDataModels = getRowWiseHoleIn3dList(holeDetailData);
                    for (int i = 0; i < mapHoleDataModels.size(); i++) {
                        JsonObject rowObject = new JsonObject();
                        rowObject.addProperty("rowNo", String.valueOf(i + 1));
                        if (!Constants.isListEmpty(mapHoleDataModels.get(i).getHoleDetailDataList()))
                            rowObject.addProperty("holeNo", String.valueOf(mapHoleDataModels.get(i).getHoleDetailDataList().size()));
                        else rowObject.addProperty("holeNo", String.valueOf(0));
                        rowObject.addProperty("HoleType", "Production");
                        rowDetailArray.add(rowObject);
                    }
                }

                JsonArray rowHoleDetailArray = new JsonArray();
                rowHoleDetailArray.add(new Gson().toJson(holeDetailData));

                JsonArray drillDetailArray = new JsonArray();
                JsonObject drillDetailObject = new JsonObject();
                if (!Constants.isListEmpty(holeDetailData)) {
                    drillDetailObject.addProperty("rowNo", String.valueOf(holeDetailData.get(0).getRowNo()));
                    drillDetailObject.addProperty("holeNo", String.valueOf(holeDetailData.get(0).getHoleNo()));
                    drillDetailObject.addProperty("holeName", String.format("R%s/H%s", holeDetailData.get(0).getRowNo(), holeDetailData.get(0).getHoleNo()));
                    drillDetailObject.addProperty("holeDiameter", String.valueOf(holeDetailData.get(0).getHoleDiameter()));
                    drillDetailObject.addProperty("burden", String.valueOf(StringUtill.isEmpty(holeDetailData.get(0).getBurden()) ? "" : holeDetailData.get(0).getBurden()));
                    drillDetailObject.addProperty("spacing", String.valueOf(holeDetailData.get(0).getSpacing()));
                    drillDetailObject.addProperty("holeAngle", String.valueOf(holeDetailData.get(0).getVerticalDip()));
                    drillDetailObject.addProperty("holeDeviation", String.valueOf(0));
                    drillDetailObject.addProperty("drillDepth", String.valueOf(holeDetailData.get(0).getHoleDepth()));
                    drillDetailObject.addProperty("depthStart", "0");
                    drillDetailObject.addProperty("depthEnd", String.valueOf(holeDetailData.get(0).getHoleDepth()));
                    drillDetailObject.addProperty("waterLevel", String.valueOf(holeDetailData.get(0).getWaterDepth()));
                    drillDetailObject.addProperty("drillerName", projectDetailJson != null ? projectDetailJson.get("driller_code").getAsString() : "");
                    drillDetailObject.addProperty("drillMethod", projectDetailJson != null ? projectDetailJson.get("drill_method_code").getAsString() : "");
                    drillDetailObject.addProperty("northing", String.valueOf(holeDetailData.get(0).getTopX()));
                    drillDetailObject.addProperty("easting", String.valueOf(holeDetailData.get(0).getTopY()));
                }
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
                drillDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                drillDetailArray.add(drillDetailObject);


                JsonArray holeDetailArray = new JsonArray();
                if (!Constants.isListEmpty(holeDetailData)) {
                    for (Response3DTable4HoleChargingDataModel holeDetail : holeDetailData) {
                        JsonObject holeDetailObject = new JsonObject();
                        holeDetailObject.addProperty("ProjectCode", 0);
                        holeDetailObject.addProperty("HoleName", String.format("R%s/H%s", holeDetail.getRowNo(), holeDetail.getHoleNo()));
                        holeDetailObject.addProperty("UserDefineHoleName", String.format("R%sH%s", holeDetail.getRowNo(), holeDetail.getHoleNo()));
                        holeDetailObject.addProperty("RowNo", holeDetail.getRowNo());
                        holeDetailObject.addProperty("HoleNo", holeDetail.getHoleNo());
                        holeDetailObject.addProperty("HoleAngle", holeDetail.getVerticalDip());
                        holeDetailObject.addProperty("HoleDeviation", 0);
                        holeDetailObject.addProperty("DrillDepth", holeDetail.getHoleDepth());
                        holeDetailObject.addProperty("Northing", holeDetail.getTopX());
                        holeDetailObject.addProperty("Easting", holeDetail.getTopY());
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
                        holeDetailObject.addProperty("HoleType", holeDetail.getHoleType());
                        holeDetailObject.addProperty("CalculateDrillPenetration", 0);
                        holeDetailObject.addProperty("DrillPenetrationRate", 0);
                        holeDetailObject.addProperty("TotalDrillTime", 0);

                        JsonObject logHoleSectionDetailObject = new JsonObject();
                        logHoleSectionDetailObject.addProperty("ProjectCode", 0);
                        logHoleSectionDetailObject.addProperty("HoleName", String.format("R%s/H%s", holeDetail.getRowNo(), holeDetail.getHoleNo()));
                        logHoleSectionDetailObject.addProperty("DepthStart", 0);
                        logHoleSectionDetailObject.addProperty("DepthEnd", holeDetail.getHoleDepth());
                        logHoleSectionDetailObject.addProperty("RockType", projectDetailJson != null ? projectDetailJson.get("rock_id").getAsString() : "0");
                        logHoleSectionDetailObject.addProperty("StartTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                        logHoleSectionDetailObject.addProperty("EndTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                        logHoleSectionDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                        logHoleSectionDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                        logHoleSectionDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                        logHoleSectionDetailObject.addProperty("DeviceType", "android");
                        logHoleSectionDetailObject.addProperty("RigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsInt() : 0);
                        logHoleSectionDetailObject.addProperty("DrillerCode", projectDetailJson != null ? projectDetailJson.get("driller_code").getAsInt() : 4);
                        logHoleSectionDetailObject.addProperty("DrillMethod", projectDetailJson != null ? projectDetailJson.get("drill_method_code").getAsInt() : 1);
                        logHoleSectionDetailObject.addProperty("ShiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                basicInformationObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                visitorDetailsObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                visitorDetailsArray.add(visitorDetailsObject);

                JsonArray preStartCheckListArray = new JsonArray();
                JsonObject preStartCheckListObject = new JsonObject();
                preStartCheckListObject.addProperty("preStartCode", 0);
                preStartCheckListObject.addProperty("result", 0);
                preStartCheckListObject.addProperty("issuesDiscussed", 0);
                preStartCheckListObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                preStartCheckListObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                preStartCheckListObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
                preStartCheckListObject.addProperty("userId", manger.getUserDetails().getUserid());
                preStartCheckListObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                activityDetailsObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                drillAccessoriesDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                hazardDetailsObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);

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
                rigInspectionObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                rigInspectionDetailArray.add(rigInspectionObject);

                JsonArray confirmationFormDetailArray = new JsonArray();
                JsonObject confirmationFormDetailObject = new JsonObject();
                confirmationFormDetailObject.addProperty("drillerName", projectDetailJson != null ? projectDetailJson.get("team_name").getAsString() : "");
                confirmationFormDetailObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                confirmationFormDetailObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                confirmationFormDetailObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
                confirmationFormDetailObject.addProperty("userId", manger.getUserDetails().getUserid());
                confirmationFormDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                confirmationFormDetailArray.add(confirmationFormDetailObject);


                // Setup Map Data

                map.addProperty("projectNumber", bladesRetrieveData.getDesignCode());
                map.addProperty("projectName", bladesRetrieveData.getDesignName());
                map.addProperty("siteCode", projectDetailJson != null ? projectDetailJson.get("site_id").getAsInt() : 0);
                map.addProperty("pitCode", AppDelegate.getInstance().getCodeIdObject().get("pitId").getAsInt());
                map.addProperty("mineCode", AppDelegate.getInstance().getCodeIdObject().get("MineId").getAsInt());
                map.addProperty("zoneCode", AppDelegate.getInstance().getCodeIdObject().get("zoneId").getAsInt());
                map.addProperty("benchCode", AppDelegate.getInstance().getCodeIdObject().get("benchId").getAsString());

                map.addProperty("rigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsString() : "0");
                map.addProperty("drillingTypeId", projectDetailJson != null ? projectDetailJson.get("drill_type_id").getAsInt() : 0);
                map.addProperty("materialTypeId", projectDetailJson != null ? projectDetailJson.get("drill_material_id").getAsInt() : 0);
                map.addProperty("startTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm a"));
                map.addProperty("endTime", "");
                if (projectDetailJson != null) {
                    if (projectDetailJson.get("drill_pattern").getAsString().equals("Rectangular/Square")) {
                        map.addProperty("drillPattern", "Square");
                    } else {
                        map.addProperty("drillPattern", projectDetailJson.get("drill_pattern").getAsString());
                    }
                } else {
                    map.addProperty("drillPattern", "Square");
                }
                map.addProperty("clientName", projectDetailJson != null ? projectDetailJson.get("client_name").getAsString() : "");
                map.addProperty("creationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm a"));
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
                                                showToast(StringUtill.getString("Project is already created in DRIMS"));
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
                                            /*if (StringUtill.isEmpty(String.valueOf(bladesRetrieveData.getDrimsId()))) {
                                                updateDesignIdBimsDrimsApiCaller(bladesRetrieveData.getDesignId(), jsonObject.get("projectCode").getAsString(), false, false).observe(BaseActivity.this, new Observer<JsonElement>() {
                                                    @Override
                                                    public void onChanged(JsonElement element) {
                                                        bladesRetrieveData.setDrimsId(jsonObject.get("projectCode").getAsString());
                                                        List<Response3DTable1DataModel> modelList = AppDelegate.instance.getResponse3DTable1DataModel();
                                                        modelList.set(0, bladesRetrieveData);
                                                        AppDelegate.getInstance().setResponse3DTable1DataModel(modelList);
//                                                        jsonPrimitiveMutableLiveData.setValue(element);
                                                    }
                                                });
                                            }*/
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
        }
        return jsonPrimitiveMutableLiveData;
    }

    public MutableLiveData<JsonElement> setJsonForSyncProjectData(ResponseBladesRetrieveData bladesRetrieveData, AllTablesData tablesData) {
        List<ResponseHoleDetailData> holeDetailData = tablesData.getTable2();
        MutableLiveData<JsonElement> jsonPrimitiveMutableLiveData = new MutableLiveData<>();
        if (ConnectivityReceiver.getInstance().isInternetAvailable()) {
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
                    List<MapHoleDataModel> mapHoleDataModels = getRowWiseHoleList(holeDetailData);
                    for (int i = 0; i < mapHoleDataModels.size(); i++) {
                        JsonObject rowObject = new JsonObject();
                        rowObject.addProperty("rowNo", String.valueOf(i + 1));
                        if (!Constants.isListEmpty(mapHoleDataModels.get(i).getHoleDetailDataList()))
                            rowObject.addProperty("holeNo", String.valueOf(mapHoleDataModels.get(i).getHoleDetailDataList().size()));
                        else rowObject.addProperty("holeNo", String.valueOf(0));
                        rowObject.addProperty("HoleType", "Production");
                        rowDetailArray.add(rowObject);
                    }
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
                    drillDetailObject.addProperty("burden", String.valueOf(holeDetailData.get(0).getBurden() == 0 ? "" : holeDetailData.get(0).getBurden()));
                    drillDetailObject.addProperty("spacing", String.valueOf(holeDetailData.get(0).getSpacing()));
                    drillDetailObject.addProperty("holeAngle", String.valueOf(holeDetailData.get(0).getHoleAngle()));
                    drillDetailObject.addProperty("holeDeviation", String.valueOf(0));
                    drillDetailObject.addProperty("drillDepth", String.valueOf(holeDetailData.get(0).getHoleDepth()));
                    drillDetailObject.addProperty("depthStart", "0");
                    drillDetailObject.addProperty("depthEnd", String.valueOf(holeDetailData.get(0).getHoleDepth()));
                    drillDetailObject.addProperty("waterLevel", String.valueOf(holeDetailData.get(0).getWaterDepth()));
                    drillDetailObject.addProperty("drillerName", projectDetailJson != null ? projectDetailJson.get("driller_code").getAsString() : "");
                    drillDetailObject.addProperty("drillMethod", projectDetailJson != null ? projectDetailJson.get("drill_method_code").getAsString() : "");
                    drillDetailObject.addProperty("northing", String.valueOf(holeDetailData.get(0).getDrillX()));
                    drillDetailObject.addProperty("easting", String.valueOf(holeDetailData.get(0).getDrillY()));
                }
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
                drillDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                        holeDetailObject.addProperty("Northing", holeDetail.getDrillX());
                        holeDetailObject.addProperty("Easting", holeDetail.getDrillY());
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
                        holeDetailObject.addProperty("HoleType", holeDetail.getHoleType());
                        holeDetailObject.addProperty("CalculateDrillPenetration", 0);
                        holeDetailObject.addProperty("DrillPenetrationRate", 0);
                        holeDetailObject.addProperty("TotalDrillTime", 0);

                        JsonObject logHoleSectionDetailObject = new JsonObject();
                        logHoleSectionDetailObject.addProperty("ProjectCode", 0);
                        logHoleSectionDetailObject.addProperty("HoleName", String.format("R%s/H%s", holeDetail.getRowNo(), holeDetail.getHoleNo()));
                        logHoleSectionDetailObject.addProperty("DepthStart", 0);
                        logHoleSectionDetailObject.addProperty("DepthEnd", holeDetail.getHoleDepth());
                        logHoleSectionDetailObject.addProperty("RockType", projectDetailJson != null ? projectDetailJson.get("rock_id").getAsString() : "0");
                        logHoleSectionDetailObject.addProperty("StartTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                        logHoleSectionDetailObject.addProperty("EndTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                        logHoleSectionDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                        logHoleSectionDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                        logHoleSectionDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                        logHoleSectionDetailObject.addProperty("DeviceType", "android");
                        logHoleSectionDetailObject.addProperty("RigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsInt() : 0);
                        logHoleSectionDetailObject.addProperty("DrillerCode", projectDetailJson != null ? projectDetailJson.get("driller_code").getAsInt() : 4);
                        logHoleSectionDetailObject.addProperty("DrillMethod", projectDetailJson != null ? projectDetailJson.get("drill_method_code").getAsInt() : 1);
                        logHoleSectionDetailObject.addProperty("ShiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                basicInformationObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                visitorDetailsObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                visitorDetailsArray.add(visitorDetailsObject);

                JsonArray preStartCheckListArray = new JsonArray();
                JsonObject preStartCheckListObject = new JsonObject();
                preStartCheckListObject.addProperty("preStartCode", 0);
                preStartCheckListObject.addProperty("result", 0);
                preStartCheckListObject.addProperty("issuesDiscussed", 0);
                preStartCheckListObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                preStartCheckListObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                preStartCheckListObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
                preStartCheckListObject.addProperty("userId", manger.getUserDetails().getUserid());
                preStartCheckListObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                activityDetailsObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                drillAccessoriesDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                hazardDetailsObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);

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
                rigInspectionObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                rigInspectionDetailArray.add(rigInspectionObject);

                JsonArray confirmationFormDetailArray = new JsonArray();
                JsonObject confirmationFormDetailObject = new JsonObject();
                confirmationFormDetailObject.addProperty("drillerName", projectDetailJson != null ? projectDetailJson.get("team_name").getAsString() : "");
                confirmationFormDetailObject.addProperty("dailyActivityDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                confirmationFormDetailObject.addProperty("modificationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                confirmationFormDetailObject.addProperty("companyId", manger.getUserDetails().getCompanyid());
                confirmationFormDetailObject.addProperty("userId", manger.getUserDetails().getUserid());
                confirmationFormDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                confirmationFormDetailArray.add(confirmationFormDetailObject);


                // Setup Map Data

                map.addProperty("projectNumber", bladesRetrieveData.getDesignCode());
                map.addProperty("projectName", bladesRetrieveData.getDesignName());
                map.addProperty("siteCode", projectDetailJson != null ? projectDetailJson.get("site_id").getAsInt() : 0);
                map.addProperty("pitCode", AppDelegate.getInstance().getCodeIdObject().get("pitId").getAsInt());
                map.addProperty("mineCode", AppDelegate.getInstance().getCodeIdObject().get("MineId").getAsInt());
                map.addProperty("zoneCode", AppDelegate.getInstance().getCodeIdObject().get("zoneId").getAsInt());
                map.addProperty("benchCode", AppDelegate.getInstance().getCodeIdObject().get("benchId").getAsString());

                map.addProperty("rigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsString() : "0");
                map.addProperty("drillingTypeId", projectDetailJson != null ? projectDetailJson.get("drill_type_id").getAsInt() : 0);
                map.addProperty("materialTypeId", projectDetailJson != null ? projectDetailJson.get("drill_material_id").getAsInt() : 0);
                map.addProperty("startTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm a"));
                map.addProperty("endTime", "");
                if (projectDetailJson != null) {
                    if (projectDetailJson.get("drill_pattern").getAsString().equals("Rectangular/Square")) {
                        map.addProperty("drillPattern", "Square");
                    } else {
                        map.addProperty("drillPattern", projectDetailJson.get("drill_pattern").getAsString());
                    }
                } else {
                    map.addProperty("drillPattern", "Square");
                }
                map.addProperty("clientName", projectDetailJson != null ? projectDetailJson.get("client_name").getAsString() : "");
                map.addProperty("creationDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm a"));
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
                                                showToast(StringUtill.getString("Project is already created in DRIMS"));
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
                                            /*if (!Constants.isListEmpty(tablesData.getTable())) {
                                                if (StringUtill.isEmpty(String.valueOf(tablesData.getTable().get(0).getDrimsId())))
                                                    updateDesignIdBimsDrimsApiCaller(bladesRetrieveData.getDesignId(), jsonObject.get("projectCode").getAsString(), false, false).observe(BaseActivity.this, new Observer<JsonElement>() {
                                                        @Override
                                                        public void onChanged(JsonElement element) {
                                                            List<ResponseProjectModelFromAllInfoApi> apiList = tablesData.getTable();
                                                            ResponseProjectModelFromAllInfoApi api = apiList.get(0);
                                                            api.setDrimsId(jsonObject.get("projectCode").getAsString());
                                                            apiList.set(0, api);
                                                            tablesData.setTable(apiList);
                                                            AppDelegate.getInstance().setAllTablesData(tablesData);
//                                                            jsonPrimitiveMutableLiveData.setValue(response);
                                                        }
                                                    });
                                            }*/
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
            holeDetailObject.addProperty("UserDefineHoleName", String.format("R%sH%s", holeDetailData.getRowNo(), holeDetailData.getHoleNo()));
            holeDetailObject.addProperty("HoleDiameter", holeDetailData.getHoleDiameter());
            holeDetailObject.addProperty("Burden", String.valueOf(holeDetailData.getBurden() == 0 ? "" : holeDetailData.getBurden()));
            holeDetailObject.addProperty("Spacing", String.valueOf(holeDetailData.getSpacing()));
            holeDetailObject.addProperty("HoleAngle", holeDetailData.getHoleAngle());
            holeDetailObject.addProperty("HoleDeviation", String.valueOf(0));
            holeDetailObject.addProperty("DrillDepth", holeDetailData.getHoleDepth());
            holeDetailObject.addProperty("Northing", holeDetailData.getDrillX());
            holeDetailObject.addProperty("Easting", holeDetailData.getDrillY());
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
            logHoleSectionDetailObject.addProperty("StartTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            logHoleSectionDetailObject.addProperty("EndTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
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

    public MutableLiveData<JsonElement> setInsertUpdateHoleDetailMultipleSync(ResponseBladesRetrieveData bladesRetrieveData, List<ResponseHoleDetailData> holeDetailData, AllTablesData allTablesData, String projectCode) {
        MutableLiveData<JsonElement> jsonObjectMutableLiveData = new MutableLiveData<>();
        try {
            showLoader();
            JsonObject mapObject = new JsonObject();
            mapObject.addProperty("ProjectCode", projectCode);
            JsonArray holeDetailArray = new JsonArray();

            UpdatedProjectDetailEntity projectDetailEntity = new UpdatedProjectDetailEntity();
            if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId())) {
                projectDetailEntity = appDatabase.updatedProjectDataDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            }
            JsonObject projectDetailJson = new Gson().fromJson(projectDetailEntity.getData(), JsonObject.class);

            if (!Constants.isListEmpty(holeDetailData)) {
                for (ResponseHoleDetailData detailData : holeDetailData) {
                    JsonObject holeDetailObject = new JsonObject();
                    holeDetailObject.addProperty("ProjectCode", projectCode);
                    holeDetailObject.addProperty("RowNo", detailData.getRowNo());
                    holeDetailObject.addProperty("HoleNo", detailData.getHoleNo());
                    holeDetailObject.addProperty("HoleName", String.format("R%s/H%s", detailData.getRowNo(), detailData.getHoleNo()));
                    holeDetailObject.addProperty("UserDefineHoleName", String.format("R%sH%s", detailData.getRowNo(), detailData.getHoleNo()));
                    holeDetailObject.addProperty("HoleDiameter", detailData.getHoleDiameter());
                    holeDetailObject.addProperty("Burden", String.valueOf(detailData.getBurden() == 0 ? "" : detailData.getBurden()));
                    holeDetailObject.addProperty("Spacing", String.valueOf(detailData.getSpacing()));
                    holeDetailObject.addProperty("HoleAngle", detailData.getHoleAngle());
                    holeDetailObject.addProperty("HoleDeviation", String.valueOf(0));
                    holeDetailObject.addProperty("DrillDepth", detailData.getHoleDepth());
                    holeDetailObject.addProperty("Northing", detailData.getX());
                    holeDetailObject.addProperty("Easting", detailData.getY());
                    holeDetailObject.addProperty("RlTop", 0);
                    holeDetailObject.addProperty("RlBottom", 0);
                    holeDetailObject.addProperty("rockType", 11);
                    holeDetailObject.addProperty("HoleStatus", 1);
                    holeDetailObject.addProperty("OperationalSummary", "");
                    holeDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                    holeDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                    holeDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    holeDetailObject.addProperty("DeviceType", 0);
                    holeDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                    logHoleSectionDetailObject.addProperty("RockType", projectDetailJson != null ? projectDetailJson.get("rock_id").getAsString() : "0");
                    logHoleSectionDetailObject.addProperty("StartTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    logHoleSectionDetailObject.addProperty("EndTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    logHoleSectionDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                    logHoleSectionDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                    logHoleSectionDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    logHoleSectionDetailObject.addProperty("deviceType", "android");
                    logHoleSectionDetailObject.addProperty("RigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsInt() : 0);
                    logHoleSectionDetailObject.addProperty("DrillerCode", projectDetailJson != null ? projectDetailJson.get("driller_code").getAsInt() : 4);
                    logHoleSectionDetailObject.addProperty("DrillMethod", projectDetailJson != null ? projectDetailJson.get("drill_method_code").getAsInt() : 1);
                    logHoleSectionDetailObject.addProperty("ShiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                    logHoleSectionDetailObject.addProperty("DrillLogActivityDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

                    holeDetailObject.add("LogHoleSectionDetails", logHoleSectionDetailObject);
                    holeDetailArray.add(holeDetailObject);
                }
            }

            mapObject.add("HoleDetails", holeDetailArray);

            MainService.insertUpdateAppHoleDetailsmultipleSyncApiCaller(this, mapObject).observe(this, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement response) {
                    jsonObjectMutableLiveData.setValue(response);
                    hideLoader();
                    if (!StringUtill.isEmpty(projectCode)) {
                        if (!Constants.isListEmpty(allTablesData.getTable())) {
                            if (StringUtill.isEmpty(String.valueOf(allTablesData.getTable().get(0).getDrimsId())))
                                updateDesignIdBimsDrimsApiCaller(bladesRetrieveData.getDesignId(), projectCode, false, false).observe(BaseActivity.this, new Observer<JsonElement>() {
                                    @Override
                                    public void onChanged(JsonElement element) {
                                        if (element != null) {
                                            if (!element.isJsonNull()) {
                                                List<ResponseProjectModelFromAllInfoApi> apiList = allTablesData.getTable();
                                                ResponseProjectModelFromAllInfoApi api = apiList.get(0);
                                                api.setDrimsId(projectCode);
                                                apiList.set(0, api);
                                                allTablesData.setTable(apiList);
                                                AppDelegate.getInstance().setAllTablesData(allTablesData);
                                            }
                                        }
                                    }
                                });
                        }
                    }
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
        return jsonObjectMutableLiveData;
    }


    public MutableLiveData<JsonElement> setInsertUpdateHoleDetailMultipleSync3D(Response3DTable1DataModel bladesRetrieveData, List<Response3DTable4HoleChargingDataModel> holeDetailData, String projectCode) {
        MutableLiveData<JsonElement> jsonObjectMutableLiveData = new MutableLiveData<>();
        try {
            showLoader();
            JsonObject mapObject = new JsonObject();
            mapObject.addProperty("ProjectCode", projectCode);
            JsonArray holeDetailArray = new JsonArray();

            UpdatedProjectDetailEntity projectDetailEntity = new UpdatedProjectDetailEntity();
            if (appDatabase.updatedProjectDataDao().isExistItem(bladesRetrieveData.getDesignId())) {
                projectDetailEntity = appDatabase.updatedProjectDataDao().getSingleItemEntity(bladesRetrieveData.getDesignId());
            }
            JsonObject projectDetailJson = new Gson().fromJson(projectDetailEntity.getData(), JsonObject.class);

            if (!Constants.isListEmpty(holeDetailData)) {
                for (Response3DTable4HoleChargingDataModel detailData : holeDetailData) {
                    JsonObject holeDetailObject = new JsonObject();
                    holeDetailObject.addProperty("ProjectCode", projectCode);
                    holeDetailObject.addProperty("RowNo", detailData.getRowNo());
                    holeDetailObject.addProperty("HoleNo", detailData.getHoleNo());
                    holeDetailObject.addProperty("HoleName", String.format("R%s/H%s", detailData.getRowNo(), detailData.getHoleNo()));
                    holeDetailObject.addProperty("UserDefineHoleName", String.format("R%sH%s", detailData.getRowNo(), detailData.getHoleNo()));
                    holeDetailObject.addProperty("HoleDiameter", detailData.getHoleDiameter());
                    holeDetailObject.addProperty("Burden", String.valueOf(StringUtill.isEmpty(detailData.getBurden()) ? "" : detailData.getBurden()));
                    holeDetailObject.addProperty("Spacing", String.valueOf(detailData.getSpacing()));
                    holeDetailObject.addProperty("HoleAngle", detailData.getVerticalDip());
                    holeDetailObject.addProperty("HoleDeviation", String.valueOf(0));
                    holeDetailObject.addProperty("DrillDepth", detailData.getHoleDepth());
                    holeDetailObject.addProperty("Northing", detailData.getTopX());
                    holeDetailObject.addProperty("Easting", detailData.getTopY());
                    holeDetailObject.addProperty("RlTop", 0);
                    holeDetailObject.addProperty("RlBottom", 0);
                    holeDetailObject.addProperty("rockType", 11);
                    holeDetailObject.addProperty("HoleStatus", 1);
                    holeDetailObject.addProperty("OperationalSummary", "");
                    holeDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                    holeDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                    holeDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    holeDetailObject.addProperty("DeviceType", 0);
                    holeDetailObject.addProperty("shiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
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
                    logHoleSectionDetailObject.addProperty("RockType", projectDetailJson != null ? projectDetailJson.get("rock_id").getAsString() : "0");
                    logHoleSectionDetailObject.addProperty("StartTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    logHoleSectionDetailObject.addProperty("EndTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    logHoleSectionDetailObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
                    logHoleSectionDetailObject.addProperty("UserId", manger.getUserDetails().getUserid());
                    logHoleSectionDetailObject.addProperty("ModificationDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
                    logHoleSectionDetailObject.addProperty("deviceType", "android");
                    logHoleSectionDetailObject.addProperty("RigCode", projectDetailJson != null ? projectDetailJson.get("rig_id").getAsInt() : 0);
                    logHoleSectionDetailObject.addProperty("DrillerCode", projectDetailJson != null ? projectDetailJson.get("driller_code").getAsInt() : 4);
                    logHoleSectionDetailObject.addProperty("DrillMethod", projectDetailJson != null ? projectDetailJson.get("drill_method_code").getAsInt() : 1);
                    logHoleSectionDetailObject.addProperty("ShiftCode", projectDetailJson != null ? projectDetailJson.get("shift_code").getAsInt() : 18);
                    logHoleSectionDetailObject.addProperty("DrillLogActivityDateTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));

                    holeDetailObject.add("LogHoleSectionDetails", logHoleSectionDetailObject);
                    holeDetailArray.add(holeDetailObject);
                }
            }

            mapObject.add("HoleDetails", holeDetailArray);

            MainService.insertUpdateAppHoleDetailsmultipleSyncApiCaller(this, mapObject).observe(this, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement response) {
                    jsonObjectMutableLiveData.setValue(response);
                    hideLoader();
                    if (!StringUtill.isEmpty(projectCode)) {
                        if (StringUtill.isEmpty(String.valueOf(bladesRetrieveData.getDrimsId()))) {
                            updateDesignIdBimsDrimsApiCaller(bladesRetrieveData.getDesignId(), projectCode, true, false).observe(BaseActivity.this, new Observer<JsonElement>() {
                                @Override
                                public void onChanged(JsonElement element) {
                                    if (element != null) {
                                        if (!element.isJsonNull()) {
                                            bladesRetrieveData.setDrimsId(projectCode);
                                            List<Response3DTable1DataModel> modelList = AppDelegate.instance.getResponse3DTable1DataModel();
                                            modelList.set(0, bladesRetrieveData);
                                            AppDelegate.getInstance().setResponse3DTable1DataModel(modelList);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
        return jsonObjectMutableLiveData;
    }


    // Bims Insert Sync
    public MutableLiveData<JsonElement> blastInsertSyncRecordApiCaller(ResponseBladesRetrieveData bladesRetrieveData, AllTablesData tablesData, int rowCount, int holeCount, String blastCode) {
        MutableLiveData<JsonElement> data = new MutableLiveData<>();
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

            if (!Constants.isListEmpty(tablesData.getTable9())) {
                for (int i = 0; i < tablesData.getTable9().size(); i++) {
                    Table9Item item = tablesData.getTable9().get(i);
                    if (item.getProdType().equals("Base") || item.getProdType().equals("Bottom")) {
                        JsonObject expUsedDetailObject = new JsonObject();
                        if (item.getProdId() != 0 && !StringUtill.isEmpty(item.getProdName())) {
                            expUsedDetailObject.addProperty("expcode", String.valueOf(item.getProdId()));
                            expUsedDetailObject.addProperty("qty", String.valueOf(item.getProdQty()));
                        } else {
                            expUsedDetailObject.addProperty("expcode", "");
                            expUsedDetailObject.addProperty("qty", "");
                        }
                        expUsedDetailArray.add(expUsedDetailObject);
                    }
                    if (item.getProdType().equals("Column") || item.getProdType().equals("Bulk")) {
                        JsonObject expUsedDetailObject = new JsonObject();
                        if (item.getProdId() != 0 && !StringUtill.isEmpty(item.getProdName())) {
                            expUsedDetailObject.addProperty("expcode1", String.valueOf(item.getProdId()));
                            expUsedDetailObject.addProperty("qty", String.valueOf(item.getProdQty()));
                        } else {
                            expUsedDetailObject.addProperty("expcode1", "");
                            expUsedDetailObject.addProperty("qty", "");
                        }
                        expUsedDetailArray.add(expUsedDetailObject);
                    }
                    if (item.getProdType().equals("Booster")) {
                        JsonObject expUsedDetailObject = new JsonObject();
                        if (item.getProdId() != 0 && !StringUtill.isEmpty(item.getProdName())) {
                            expUsedDetailObject.addProperty("expcode2", String.valueOf(item.getProdId()));
                            expUsedDetailObject.addProperty("qty", String.valueOf(item.getProdQty()));
                        } else {
                            expUsedDetailObject.addProperty("expcode2", "");
                            expUsedDetailObject.addProperty("qty", "");
                        }
                        expUsedDetailArray.add(expUsedDetailObject);
                    }
                }
            }

            // RowDetails Array
            JsonArray rowDetailsArray = new JsonArray();
            if (!Constants.isListEmpty(tablesData.getTable2())) {
                List<MapHoleDataModel> mapHoleDataModels = getRowWiseHoleList(tablesData.getTable2());
                for (int i = 0; i < mapHoleDataModels.size(); i++) {
                    JsonObject rowDetailsObject = new JsonObject();
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
                    chargeDetailsObject.addProperty("RowNo", String.valueOf(tablesData.getTable2().get(i).getRowNo()));
                    chargeDetailsObject.addProperty("HoleNo", String.valueOf(tablesData.getTable2().get(i).getHoleNo()));
                    chargeDetailsObject.addProperty("HoleName", String.format("R%sH%s", tablesData.getTable2().get(i).getRowNo(), tablesData.getTable2().get(i).getHoleNo()));
                    chargeDetailsObject.addProperty("RowType", "Production");
                    chargeDetailsObject.addProperty("HoleDia", String.valueOf(tablesData.getTable2().get(i).getHoleDiameter()));

                    chargeDetailsObject.addProperty("ExpCode", "");
                    chargeDetailsObject.addProperty("Weight", "");
                    chargeDetailsObject.addProperty("ExpLength", "");
                    chargeDetailsObject.addProperty("CostPerUnit", "");

                    chargeDetailsObject.addProperty("ExpCode1", "");
                    chargeDetailsObject.addProperty("Weight1", "");
                    chargeDetailsObject.addProperty("ExpLength1", "");
                    chargeDetailsObject.addProperty("CostPerUnit1", "");

                    chargeDetailsObject.addProperty("ExpCode2", "");
                    chargeDetailsObject.addProperty("Weight2", "");
                    chargeDetailsObject.addProperty("ExpLength2", "");
                    chargeDetailsObject.addProperty("CostPerUnit2", "");

                    chargeDetailsObject.addProperty("SteamLen", "");
                    chargeDetailsObject.addProperty("DeckDepth", "");

                    if (!Constants.isListEmpty(tablesData.getTable2().get(i).getChargeTypeArray())) {
                        for (int j = 0; j < tablesData.getTable2().get(i).getChargeTypeArray().size(); j++) {
                            ChargeTypeArrayItem arrayItem = tablesData.getTable2().get(i).getChargeTypeArray().get(j);
                            if (arrayItem.getType().contains("Base") || arrayItem.getType().contains("Bottom")) {
                                chargeDetailsObject.addProperty("ExpCode", String.valueOf(arrayItem.getProdId()));
                                chargeDetailsObject.addProperty("Weight", arrayItem.getWeight());
                                chargeDetailsObject.addProperty("ExpLength", arrayItem.getLength());
                                chargeDetailsObject.addProperty("CostPerUnit", String.valueOf(arrayItem.getCost()));
                            }
                            if (arrayItem.getType().contains("Column") || arrayItem.getType().contains("Bulk")) {
                                chargeDetailsObject.addProperty("ExpCode1", String.valueOf(arrayItem.getProdId()));
                                chargeDetailsObject.addProperty("Weight1", arrayItem.getWeight());
                                chargeDetailsObject.addProperty("ExpLength1", arrayItem.getLength());
                                chargeDetailsObject.addProperty("CostPerUnit1", String.valueOf(arrayItem.getCost()));
                            }
                            if (arrayItem.getType().contains("Booster")) {
                                chargeDetailsObject.addProperty("ExpCode2", String.valueOf(arrayItem.getProdId()));
                                chargeDetailsObject.addProperty("Weight2", arrayItem.getWeight());
                                chargeDetailsObject.addProperty("ExpLength2", arrayItem.getLength());
                                chargeDetailsObject.addProperty("CostPerUnit2", String.valueOf(arrayItem.getCost()));
                            }
                            if (arrayItem.getType().contains("Stemming")) {
                                chargeDetailsObject.addProperty("SteamLen", String.valueOf(arrayItem.getLength()));
                            }
                            if (arrayItem.getType().contains("Decking")) {
                                chargeDetailsObject.addProperty("DeckDepth", String.valueOf(arrayItem.getLength()));
                            }
                        }
                    }

                    chargeDetailsObject.addProperty("Burden", String.valueOf(tablesData.getTable2().get(i).getBurden() == 0 ? "" : tablesData.getTable2().get(i).getBurden()));
                    chargeDetailsObject.addProperty("Spacing", String.valueOf(tablesData.getTable2().get(i).getSpacing()));
                    chargeDetailsObject.addProperty("Delay1", "0");
                    chargeDetailsObject.addProperty("Delay2", String.valueOf(tablesData.getTable2().get(i).getDelay()));
                    chargeDetailsObject.addProperty("TopBaseChargePercent", "0");
                    chargeDetailsObject.addProperty("BottomBaseChargePercent", "100");
                    chargeDetailsObject.addProperty("DeckStart", "");
                    chargeDetailsObject.addProperty("WaterDepth", String.valueOf(tablesData.getTable2().get(i).getWaterDepth()));
                    chargeDetailsObject.addProperty("HoleDepth", String.valueOf(tablesData.getTable2().get(i).getHoleDepthDouble()));
                    chargeDetailsObject.addProperty("Subgrade", String.valueOf(tablesData.getTable2().get(i).getSubgrade() == 0 ? "" : tablesData.getTable2().get(i).getSubgrade()));
                    chargeDetailsObject.addProperty("IsHoleBlock", "");
                    chargeDetailsObject.addProperty("HoleBlockLength", "");
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
                                dthDetailsObject.addProperty("DTHCost", deviceModel.getCost());
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
            mapObject.addProperty("blastno", "");

            mapObject.addProperty("mineCode", AppDelegate.getInstance().getCodeIdObject().get("MineId").getAsString());
            mapObject.addProperty("pitCode", AppDelegate.getInstance().getCodeIdObject().get("pitId").getAsString());
            mapObject.addProperty("zoneCode", AppDelegate.getInstance().getCodeIdObject().get("zoneId").getAsString());
            mapObject.addProperty("benchCode", AppDelegate.getInstance().getCodeIdObject().get("benchId").getAsString());
            mapObject.addProperty("rockCode",  AppDelegate.getInstance().getCodeIdObject().get("rockCode").getAsString());
            mapObject.addProperty("rockDensity", "2.5");
            mapObject.addProperty("blastDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("blastTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("BenchHeight", "10");
            mapObject.addProperty("FaceLength", "40");
            if (!Constants.isListEmpty(tablesData.getTable2())) {
                mapObject.addProperty("Burden", String.valueOf(tablesData.getTable2().get(0).getBurden() == 0 ? "" : tablesData.getTable2().get(0).getBurden()));
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
            mapObject.addProperty("Heavy", 1);
            mapObject.addProperty("BoulderCount", (blastPerformanceObject != null && blastPerformanceObject.has("BounderCount")) ? blastPerformanceObject.get("BounderCount").getAsString() : "");
            mapObject.addProperty("Displacement", (blastPerformanceObject != null && blastPerformanceObject.has("DisPlace")) ? blastPerformanceObject.get("DisPlace").getAsString() : "");
            mapObject.addProperty("StemEject", 1);
            mapObject.addProperty("Muck", 1);
            mapObject.addProperty("BlastFumes", 1);
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
                                appDatabase.blastCodeDao().updateItem(bladesRetrieveData.getDesignId(), blastCode);
                            } else {
                                BlastCodeEntity blastCodeEntity = new BlastCodeEntity(blastCode, bladesRetrieveData.getDesignId());
                                appDatabase.blastCodeDao().insertItem(blastCodeEntity);
                            }
                            if (!Constants.isListEmpty(tablesData.getTable()))
                                if (StringUtill.isEmpty(String.valueOf(tablesData.getTable().get(0).getDrimsId()))) {
                                    updateDesignIdBimsDrimsApiCaller(bladesRetrieveData.getDesignId(), blastCode, false, true).observe(BaseActivity.this, new Observer<JsonElement>() {
                                        @Override
                                        public void onChanged(JsonElement element) {
                                            ResponseProjectModelFromAllInfoApi api = tablesData.getTable().get(0);
                                            api.setBimsId(blastCode);
                                            List<ResponseProjectModelFromAllInfoApi> apiList = tablesData.getTable();
                                            apiList.set(0, api);
                                            tablesData.setTable(apiList);
                                            AppDelegate.getInstance().setAllTablesData(tablesData);
                                            ProjectHoleDetailRowColDao entity = appDatabase.projectHoleDetailRowColDao();
                                            if (entity.isExistProject(bladesRetrieveData.getDesignId())) {
                                                entity.updateProject(bladesRetrieveData.getDesignId(), new Gson().toJson(tablesData));
                                            } else {
                                                ProjectHoleDetailRowColEntity colEntity = new ProjectHoleDetailRowColEntity();
                                                colEntity.setDesignId(bladesRetrieveData.getDesignId());
                                                colEntity.setIs3DBlades(false);
                                                colEntity.setProjectHole(new Gson().toJson(tablesData));
                                                entity.insertProject(colEntity);
                                            }
                                            data.setValue(element);
                                        }
                                    });
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
        return data;
    }

    public MutableLiveData<JsonElement> blastInsertSyncRecord3DApiCaller(Response3DTable1DataModel bladesRetrieveData, List<Response3DTable4HoleChargingDataModel> tablesData, List<Response3DTable2DataModel> response3DTable2DataModelList, int rowCount, int holeCount, String blastCode) {
        MutableLiveData<JsonElement> data = new MutableLiveData<>();
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
            if (!Constants.isListEmpty(tablesData.get(0).getChargeTypeArray())) {
                if (!Constants.isListEmpty(tablesData.get(0).getChargeTypeArray())) {
                    double length = 0;
                    ChargeTypeArrayItem arrayItem = new ChargeTypeArrayItem();
                    List<ChargeTypeArrayItem> chargeTypeArray = tablesData.get(0).getChargeTypeArray();
                    for (int j = 0; j < chargeTypeArray.size(); j++) {
                        ChargeTypeArrayItem item = chargeTypeArray.get(j);
                        if (item.getType().equals("Cartridge")) {
                            if (StringUtill.isEmpty(arrayItem.getType()))
                                arrayItem = item;
                            length = length + item.getLength();
                        }
                    }

                    JsonObject expUsedDetailObject = new JsonObject();
                    expUsedDetailObject.addProperty("expcode", String.valueOf(arrayItem.getProdId()));
                    expUsedDetailObject.addProperty("qty", String.valueOf(decimalFormat.format(length)));
                    expUsedDetailArray.add(expUsedDetailObject);

                    length = 0;
                    arrayItem = new ChargeTypeArrayItem();
                    for (int j = 0; j < chargeTypeArray.size(); j++) {
                        ChargeTypeArrayItem item = chargeTypeArray.get(j);
                        if (item.getType().equals("Bulk")) {
                            if (StringUtill.isEmpty(arrayItem.getType()))
                                arrayItem = item;
                            length = length + item.getLength();
                        }
                    }

                    expUsedDetailObject = new JsonObject();
                    expUsedDetailObject.addProperty("expcode1", String.valueOf(arrayItem.getProdId()));
                    expUsedDetailObject.addProperty("qty", String.valueOf(decimalFormat.format(length)));
                    expUsedDetailArray.add(expUsedDetailObject);

                    length = 0;
                    arrayItem = new ChargeTypeArrayItem();
                    for (int j = 0; j < chargeTypeArray.size(); j++) {
                        ChargeTypeArrayItem item = chargeTypeArray.get(j);
                        if (item.getType().equals("Booster")) {
                            if (StringUtill.isEmpty(arrayItem.getType()))
                                arrayItem = item;
                            length = length + item.getLength();
                        }
                    }

                    expUsedDetailObject = new JsonObject();
                    expUsedDetailObject.addProperty("expcode2", String.valueOf(arrayItem.getProdId()));
                    expUsedDetailObject.addProperty("qty", String.valueOf(decimalFormat.format(length)));
                    expUsedDetailArray.add(expUsedDetailObject);

                }
                /*
                for (int j = 0; j < tablesData.get(0).getChargeTypeArray().size(); j++) {
                    JsonObject expUsedDetailObject = new JsonObject();
                    expUsedDetailObject.addProperty("expcode" + (j == 0 ? "" : j), String.valueOf(tablesData.get(0).getChargeTypeArray().get(j).getProdId()));
                    expUsedDetailObject.addProperty("qty" + (j == 0 ? "" : j), String.valueOf(tablesData.get(0).getChargeTypeArray().get(j).getLength()));
                    expUsedDetailArray.add(expUsedDetailObject);
                }*/
            }

            // RowDetails Array
            JsonArray rowDetailsArray = new JsonArray();
            if (!Constants.isListEmpty(tablesData)) {
                List<MapHole3DDataModel> mapHoleDataModels = getRowWiseHoleIn3dList(tablesData);
                for (int i = 0; i < mapHoleDataModels.size(); i++) {
                    JsonObject rowDetailsObject = new JsonObject();
                    rowDetailsObject.addProperty("rowno", String.valueOf(i + 1));
                    if (!Constants.isListEmpty(mapHoleDataModels.get(i).getHoleDetailDataList()))
                        rowDetailsObject.addProperty("holeno", String.valueOf(mapHoleDataModels.get(i).getHoleDetailDataList().size()));
                    else rowDetailsObject.addProperty("holeno", String.valueOf(0));
                    rowDetailsObject.addProperty("rowtype", String.valueOf(1) /*"Production"*/);
                    rowDetailsArray.add(rowDetailsObject);
                }
            }

            double aveSpacing = 0.0, aveBurden = 0.0;

            // Charge Details Array
            JsonArray chargeDetailsArray = new JsonArray();
            if (!Constants.isListEmpty(tablesData)) {
                for (int i = 0; i < tablesData.size(); i++) {
                    JsonObject chargeDetailsObject = new JsonObject();
                    chargeDetailsObject.addProperty("BlastCode", blastCode);
                    chargeDetailsObject.addProperty("RowNo", String.valueOf(tablesData.get(i).getRowNo()));
                    chargeDetailsObject.addProperty("HoleNo", String.valueOf(tablesData.get(i).getHoleNo()));
                    chargeDetailsObject.addProperty("HoleName", String.format("R%sH%s", tablesData.get(i).getRowNo(), tablesData.get(i).getHoleNo()));
                    chargeDetailsObject.addProperty("RowType", "Production");
                    chargeDetailsObject.addProperty("HoleDia", String.valueOf(tablesData.get(i).getHoleDiameter()));

                    /*if (!Constants.isListEmpty(tablesData.get(i).getChargeTypeArray())) {
                        for (int j = 0; j < tablesData.get(i).getChargeTypeArray().size(); j++) {
                            chargeDetailsObject.addProperty("ExpCode" + (j == 0 ? "" : j), String.valueOf(tablesData.get(i).getChargeTypeArray().get(j).getProdId()));
                            chargeDetailsObject.addProperty("Weight" + (j == 0 ? "" : j), String.valueOf(tablesData.get(i).getChargeTypeArray().get(j).getWeight()));
                            chargeDetailsObject.addProperty("ExpLength" + (j == 0 ? "" : j), String.valueOf(tablesData.get(i).getChargeTypeArray().get(j).getLength()));
                            chargeDetailsObject.addProperty("CostPerUnit" + (j == 0 ? "" : j), String.valueOf(tablesData.get(i).getChargeTypeArray().get(j).getCost()));
                        }
                    }*/

                    if (!Constants.isListEmpty(tablesData.get(i).getChargeTypeArray())) {
                        double length = 0, weight = 0;
                        boolean isCartridge = false;
                        ChargeTypeArrayItem arrayItem = new ChargeTypeArrayItem();
                        List<ChargeTypeArrayItem> chargeTypeArray = tablesData.get(i).getChargeTypeArray();
                        for (int j = 0; j < chargeTypeArray.size(); j++) {
                            ChargeTypeArrayItem item = chargeTypeArray.get(j);
                            if (item.getType().equals("Cartridge")) {
                                isCartridge = true;
                                if (StringUtill.isEmpty(arrayItem.getType()))
                                    arrayItem = item;
                                length = length + item.getLength();
                                weight = weight + item.getWeight();
                            }
                        }

                        chargeDetailsObject.addProperty("ExpCode", isCartridge ? String.valueOf(arrayItem.getProdId()) : "");
                        chargeDetailsObject.addProperty("Weight", isCartridge ? Double.valueOf(decimalFormat.format(weight)).doubleValue() : 0.0);
                        chargeDetailsObject.addProperty("ExpLength", isCartridge ? Double.valueOf(decimalFormat.format(length)).doubleValue() : 0.0);
                        chargeDetailsObject.addProperty("CostPerUnit", isCartridge ? arrayItem.getCost() : 0);

                    }

                    if (!Constants.isListEmpty(tablesData.get(i).getChargeTypeArray())) {
                        double length = 0, weight = 0;
                        boolean isCartridge = false;
                        ChargeTypeArrayItem arrayItem = new ChargeTypeArrayItem();
                        List<ChargeTypeArrayItem> chargeTypeArray = tablesData.get(i).getChargeTypeArray();
                        for (int j = 0; j < chargeTypeArray.size(); j++) {
                            ChargeTypeArrayItem item = chargeTypeArray.get(j);
                            if (item.getType().equals("Bulk")) {
                                isCartridge = true;
                                if (StringUtill.isEmpty(arrayItem.getType()))
                                    arrayItem = item;
                                length = length + item.getLength();
                                weight = weight + item.getWeight();
                            }
                        }

                        chargeDetailsObject.addProperty("ExpCode1", isCartridge ? String.valueOf(arrayItem.getProdId()) : "");
                        chargeDetailsObject.addProperty("Weight1", isCartridge ? Double.valueOf(decimalFormat.format(weight)).doubleValue() : 0);
                        chargeDetailsObject.addProperty("ExpLength1", isCartridge ? Double.valueOf(decimalFormat.format(length)).doubleValue() : 0);
                        chargeDetailsObject.addProperty("CostPerUnit1", isCartridge ? arrayItem.getCost() : 0);

                    }

                    if (!Constants.isListEmpty(tablesData.get(i).getChargeTypeArray())) {
                        double length = 0, weight = 0;
                        boolean isCartridge = false;
                        ChargeTypeArrayItem arrayItem = new ChargeTypeArrayItem();
                        List<ChargeTypeArrayItem> chargeTypeArray = tablesData.get(i).getChargeTypeArray();
                        for (int j = 0; j < chargeTypeArray.size(); j++) {
                            ChargeTypeArrayItem item = chargeTypeArray.get(j);
                            if (item.getType().equals("Booster")) {
                                isCartridge = true;
                                if (StringUtill.isEmpty(arrayItem.getType()))
                                    arrayItem = item;
                                length = length + item.getLength();
                                weight = weight + item.getWeight();
                            }
                        }

                        chargeDetailsObject.addProperty("ExpCode2", isCartridge ? String.valueOf(arrayItem.getProdId()) : "");
                        chargeDetailsObject.addProperty("Weight2", isCartridge ? Double.valueOf(decimalFormat.format(weight)).doubleValue() : 0);
                        chargeDetailsObject.addProperty("ExpLength2", isCartridge ? Double.valueOf(decimalFormat.format(length)).doubleValue() : 0);
                        chargeDetailsObject.addProperty("CostPerUnit2", isCartridge ? arrayItem.getCost() : 0);

                    }

                    aveBurden = aveBurden + Double.parseDouble(StringUtill.isEmpty(tablesData.get(i).getBurden()) ? "0.0" : tablesData.get(i).getBurden());
                    aveSpacing = aveSpacing + Double.parseDouble(StringUtill.isEmpty(tablesData.get(i).getSpacing()) ? "0.0" : tablesData.get(i).getSpacing());

                    chargeDetailsObject.addProperty("Burden", String.valueOf(StringUtill.isEmpty(tablesData.get(i).getBurden()) ? "" : tablesData.get(i).getBurden()));
                    chargeDetailsObject.addProperty("Spacing", String.valueOf(tablesData.get(i).getSpacing()));
                    chargeDetailsObject.addProperty("Delay1", "0");
                    chargeDetailsObject.addProperty("Delay2", String.valueOf(tablesData.get(i).getHoleDelay()));
                    chargeDetailsObject.addProperty("TopBaseChargePercent", "0");
                    chargeDetailsObject.addProperty("BottomBaseChargePercent", "100");

                    if (!Constants.isListEmpty(tablesData.get(i).getChargeTypeArray())) {
                        double length = 0;
                        ChargeTypeArrayItem arrayItem = new ChargeTypeArrayItem();
                        List<ChargeTypeArrayItem> chargeTypeArray = tablesData.get(i).getChargeTypeArray();
                        for (int j = 0; j < chargeTypeArray.size(); j++) {
                            ChargeTypeArrayItem item = chargeTypeArray.get(j);
                            if (item.getType().equals("Decking")) {
                                if (StringUtill.isEmpty(arrayItem.getType()))
                                    arrayItem = item;
                                length = length + item.getLength();
                            }
                        }

                        chargeDetailsObject.addProperty("DeckDepth", length);
                    }

                    chargeDetailsObject.addProperty("DeckStart", "");

                    if (!Constants.isListEmpty(tablesData.get(i).getChargeTypeArray())) {
                        double length = 0;
                        ChargeTypeArrayItem arrayItem = new ChargeTypeArrayItem();
                        List<ChargeTypeArrayItem> chargeTypeArray = tablesData.get(i).getChargeTypeArray();
                        for (int j = 0; j < chargeTypeArray.size(); j++) {
                            ChargeTypeArrayItem item = chargeTypeArray.get(j);
                            if (item.getType().equals("Stemming")) {
                                if (StringUtill.isEmpty(arrayItem.getType()))
                                    arrayItem = item;
                                length = length + item.getLength();
                            }
                        }

                        chargeDetailsObject.addProperty("SteamLen", String.valueOf(length));
                    }
                    chargeDetailsObject.addProperty("WaterDepth", String.valueOf(tablesData.get(i).getWaterDepth()));
                    chargeDetailsObject.addProperty("HoleDepth", String.valueOf(tablesData.get(i).getHoleDepth()));
                    chargeDetailsObject.addProperty("Subgrade", String.valueOf(StringUtill.isEmpty(tablesData.get(i).getSubgrade()) ? "" : tablesData.get(i).getSubgrade()));
                    chargeDetailsObject.addProperty("IsHoleBlock", "");
                    chargeDetailsObject.addProperty("HoleBlockLength", "");
                    chargeDetailsObject.addProperty("HoleAngle", String.valueOf(tablesData.get(i).getVerticalDip()));
                    chargeDetailsObject.addProperty("InHoleDelay", String.valueOf(tablesData.get(i).getInHoleDelay()));
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
                                dthDetailsObject.addProperty("DTHCost", deviceModel.getCost());
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
            mapObject.addProperty("blastno", bladesRetrieveData.getDesignCode());/* bladesRetrieveData.getDesignCode());*/

            mapObject.addProperty("mineCode", AppDelegate.getInstance().getCodeIdObject().get("MineId").getAsString());
            mapObject.addProperty("pitCode", AppDelegate.getInstance().getCodeIdObject().get("pitId").getAsString());
            mapObject.addProperty("zoneCode", AppDelegate.getInstance().getCodeIdObject().get("zoneId").getAsString());
            mapObject.addProperty("benchCode", AppDelegate.getInstance().getCodeIdObject().get("benchId").getAsString());
            mapObject.addProperty("rockCode", AppDelegate.getInstance().getCodeIdObject().get("rockCode").getAsString());
            mapObject.addProperty("rockDensity", AppDelegate.getInstance().getCodeIdObject().get("rockDensity").getAsString());
            mapObject.addProperty("blastDate", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss.SSS"));
            mapObject.addProperty("blastTime", DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss.SSS"));
            if (!Constants.isListEmpty(response3DTable2DataModelList)) {
                mapObject.addProperty("BenchHeight", response3DTable2DataModelList.get(0).getBenchHeight());
            } else {
                mapObject.addProperty("BenchHeight", "");
            }
            mapObject.addProperty("FaceLength", "40");
            if (!Constants.isListEmpty(tablesData)) {
                aveBurden = aveBurden / tablesData.size();
                aveSpacing = aveSpacing / tablesData.size();
                mapObject.addProperty("Burden", String.valueOf(aveBurden));
                mapObject.addProperty("Spacing", String.valueOf(aveSpacing));
                mapObject.addProperty("DrillPattern", "1");
            }
            mapObject.addProperty("HoleDelay", "17");
            mapObject.addProperty("RowDelay", "25");
            mapObject.addProperty("Rows", String.valueOf(rowCount));
            mapObject.addProperty("TotalHoles", String.valueOf(tablesData.size()));
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
            mapObject.addProperty("Heavy", 1);
            mapObject.addProperty("BoulderCount", (blastPerformanceObject != null && blastPerformanceObject.has("BounderCount")) ? blastPerformanceObject.get("BounderCount").getAsString() : "");
            mapObject.addProperty("Displacement", (blastPerformanceObject != null && blastPerformanceObject.has("DisPlace")) ? blastPerformanceObject.get("DisPlace").getAsString() : "");
            mapObject.addProperty("StemEject", 1);
            mapObject.addProperty("Muck", 1);
            mapObject.addProperty("BlastFumes", 1);
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
                                appDatabase.blastCodeDao().updateItem(bladesRetrieveData.getDesignId(), blastCode);
                            } else {
                                BlastCodeEntity blastCodeEntity = new BlastCodeEntity(blastCode, bladesRetrieveData.getDesignId());
                                appDatabase.blastCodeDao().insertItem(blastCodeEntity);
                            }
                            showToast("Project Sync Successfully");
                            if (StringUtill.isEmpty(String.valueOf(bladesRetrieveData.getBimsId())))
                                updateDesignIdBimsDrimsApiCaller(bladesRetrieveData.getDesignId(), blastCode, true, true).observe(BaseActivity.this, new Observer<JsonElement>() {
                                    @Override
                                    public void onChanged(JsonElement element) {
                                        bladesRetrieveData.setBimsId(blastCode);
                                        List<Response3DTable1DataModel> modelList = AppDelegate.getInstance().getResponse3DTable1DataModel();
                                        modelList.set(0, bladesRetrieveData);
                                        AppDelegate.getInstance().setResponse3DTable1DataModel(modelList);

                                        ProjectHoleDetailRowColDao entity = appDatabase.projectHoleDetailRowColDao();
                                        ProjectHoleDetailRowColEntity rowColEntity = entity.getAllBladesProject(bladesRetrieveData.getDesignId());

                                        /*if (entity.isExistProject(bladesRetrieveData.getDesignId())) {
                                            entity.updateProject(bladesRetrieveData.getDesignId(), new Gson().toJson(tablesData));
                                        } else {
                                            ProjectHoleDetailRowColEntity colEntity = new ProjectHoleDetailRowColEntity();
                                            colEntity.setDesignId(bladesRetrieveData.getDesignId());
                                            colEntity.setIs3DBlades(true);
                                            colEntity.setProjectHole(new Gson().toJson(tablesData));
                                            entity.insertProject(colEntity);
                                        }*/

                                        List<Response3DTable1DataModel> response3DTable1DataModels = new ArrayList<>();
                                        JsonArray array = new Gson().fromJson(new Gson().fromJson((new Gson().fromJson(rowColEntity.getProjectHole(), JsonObject.class)).get(Constants._3D_TBALE_NAME).getAsJsonPrimitive(), String.class), JsonArray.class);
                                        for (JsonElement e : new Gson().fromJson(new Gson().fromJson(array.get(0), String.class), JsonArray.class)) {
                                            response3DTable1DataModels.add(new Gson().fromJson(e, Response3DTable1DataModel.class));
                                        }

                                        Response3DTable1DataModel infoApi = response3DTable1DataModels.get(0);
                                        infoApi.setBimsId(blastCode);
                                        response3DTable1DataModels.set(0, infoApi);

                                        array.set(0, new Gson().fromJson(new Gson().toJson(response3DTable1DataModels), JsonElement.class));
                                        JsonObject jsonObject = new JsonObject();
                                        JsonPrimitive primitive = new JsonPrimitive(new Gson().toJson(array));
                                        jsonObject.add(Constants._3D_TBALE_NAME, primitive);

                                        if (!entity.isExistProject(String.valueOf(bladesRetrieveData.getDesignId()))) {
                                            entity.insertProject(new ProjectHoleDetailRowColEntity(String.valueOf(bladesRetrieveData.getDesignId()), true, new Gson().toJson(jsonObject)));
                                        } else {
                                            entity.updateProject(String.valueOf(bladesRetrieveData.getDesignId()), new Gson().toJson(jsonObject));
                                        }
                                        data.setValue(element);
                                    }
                                });
                        }
                    }
                    hideLoader();
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.printStackTrace();
        }
        return data;
    }

    public void insertActualDesignChartSheetApiCaller(AllTablesData tablesData, ResponseBladesRetrieveData bladesRetrieveData) {
        try {
            showLoader();
            JsonArray mapObjectArray = new JsonArray();
            if (!Constants.isListEmpty(tablesData.getTable2())) {
                for (ResponseHoleDetailData detailData : tablesData.getTable2()) {
                    JsonObject object = new JsonObject();
                    object.addProperty("BsterId", detailData.getBsterId());
                    object.addProperty("BsterLength", detailData.getBsterLength());
                    object.addProperty("BsterWt", detailData.getBsterWt());
                    object.addProperty("BtmBasePrcnt", 0);
                    object.addProperty("BtmId", detailData.getBtmId());
                    object.addProperty("BtmLength", detailData.getBtmLength());
                    object.addProperty("BtmWt", detailData.getBtmWt());
                    object.addProperty("Burden", detailData.getBurden());
                    object.addProperty("ColId", detailData.getColId());
                    object.addProperty("ColLength", detailData.getColLength());
                    object.addProperty("ColWt", detailData.getColWt());
                    object.addProperty("DeckLength", 0);
                    object.addProperty("StemLngth", 0.0);
                    if (!Constants.isListEmpty(detailData.getChargeTypeArray())) {
                        for (int j = 0; j < detailData.getChargeTypeArray().size(); j++) {
                            ChargeTypeArrayItem arrayItem = detailData.getChargeTypeArray().get(j);
                            if (arrayItem.getType().contains("Column") || arrayItem.getType().contains("Bulk")) {
                                object.addProperty("ColId", arrayItem.getProdId());
                                object.addProperty("ColLength", arrayItem.getLength());
                                object.addProperty("ColWt", arrayItem.getWeight());
                            }
                            if (arrayItem.getType().contains("Base") || arrayItem.getType().contains("Bottom")) {
                                object.addProperty("BtmId", arrayItem.getProdId());
                                object.addProperty("BtmLength", arrayItem.getLength());
                                object.addProperty("BtmWt", arrayItem.getWeight());
                            }
                            if (arrayItem.getType().contains("Booster")) {
                                object.addProperty("BsterId", arrayItem.getProdId());
                                object.addProperty("BsterLength", arrayItem.getLength());
                                object.addProperty("BsterWt", arrayItem.getWeight());
                            }
                            if (arrayItem.getType().contains("Decking")) {
                                object.addProperty("DeckLength", Double.valueOf(arrayItem.getLength()).intValue());
                            }
                            if (arrayItem.getType().contains("Stemming")) {
                                object.addProperty("StemLngth", Double.parseDouble(String.valueOf(arrayItem.getLength())));
                            }
                        }
                    }
                    object.addProperty("DeckLength1", detailData.getDeckLength1());
                    object.addProperty("DeckLength2", detailData.getDeckLength2());
                    object.addProperty("DeckLength3", detailData.getDeckLength3());
                    object.addProperty("DeckLength4", detailData.getDeckLength4());
                    object.addProperty("DeckLength5", detailData.getDeckLength5());
                    object.addProperty("DeckStartAt", 0);
                    object.addProperty("DeckStartAt1", detailData.getDeckStartAt1());
                    object.addProperty("DeckStartAt2", detailData.getDeckStartAt2());
                    object.addProperty("DeckStartAt3", detailData.getDeckStartAt3());
                    object.addProperty("DeckStartAt4", detailData.getDeckStartAt4());
                    object.addProperty("DeckStartAt5", detailData.getDeckStartAt5());
                    object.addProperty("Delay", detailData.getDelay());
                    object.addProperty("DesignId", detailData.getDesignId());
                    object.addProperty("DrillX", detailData.getDrillX());
                    object.addProperty("DrillY", detailData.getDrillY());
                    object.addProperty("HHDelay", detailData.getHHDelay());
                    object.addProperty("HoleDepth", detailData.getHoleDepth());
                    object.addProperty("HoleID", detailData.getHoleID());
                    object.addProperty("HoleNo", detailData.getHoleNo());
                    object.addProperty("HoleType", detailData.getHoleType());
                    object.addProperty("InHoleDelay", detailData.getInHoleDelay());
                    object.addProperty("NoOfDecks", detailData.getNoOfDecks());
                    object.addProperty("RRDelay", detailData.getRRDelay());
                    object.addProperty("RowNo", detailData.getRowNo());
                    object.addProperty("Spacing", detailData.getSpacing());
                    object.addProperty("Subgrade", detailData.getSubgrade());
                    object.addProperty("TopBasePrcnt", 100);
                    object.addProperty("WaterDepth", String.valueOf(StringUtill.isEmpty(String.valueOf(detailData.getWaterDepth())) ? 0 : detailData.getWaterDepth()));
                    object.addProperty("X", detailData.getX());
                    object.addProperty("Y", detailData.getY());
                    object.addProperty("holeAngle", detailData.getHoleAngle());
                    object.addProperty("inHoleDelayQty", detailData.getInHoleDelayQty());
                    mapObjectArray.add(object);
                }
            }

            MainService.insertActualDesignChartSheetApiCaller(this, mapObjectArray).observe(this, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement jsonObject) {
                    if (jsonObject.getAsJsonPrimitive().isString()) {
                        Log.e("Abd : ", jsonObject.getAsString());
                    }
                    showToast("Project Sync Successfully");
                    hideLoader();
                }
            });
        } catch (Exception e) {
            hideLoader();
            e.getLocalizedMessage();
        }

    }

    public void insertUpdate3DActualDesignHoleDetailApiCaller(List<Response3DTable4HoleChargingDataModel> allTablesData, List<Response3DTable1DataModel> bladesRetrieveData) {
        try {
            showLoader();
            JsonArray mapObjectArray = new JsonArray();
            for (Response3DTable4HoleChargingDataModel dataModel : allTablesData) {
                JsonObject object = new JsonObject();
                object.addProperty("Block", dataModel.getBlock());
                object.addProperty("BlockLength", dataModel.getBlockLength());
                object.addProperty("BottomX", dataModel.getBottomX());
                object.addProperty("BottomY", dataModel.getBottomY());
                object.addProperty("BottomZ", dataModel.getBottomZ());
                object.addProperty("Burden", dataModel.getBurden());

                JsonArray chargeTypeArray = new JsonArray();
                if (!Constants.isListEmpty(dataModel.getChargeTypeArray())) {
                    for (ChargeTypeArrayItem arrayItem : dataModel.getChargeTypeArray()) {
                        JsonObject chargeTypeObject = new JsonObject();
                        chargeTypeObject.addProperty("type", StringUtill.isEmpty(arrayItem.getType()) ? "" : arrayItem.getType());
                        chargeTypeObject.addProperty("name", StringUtill.isEmpty(arrayItem.getName()) ? "" : arrayItem.getName());
                        chargeTypeObject.addProperty("cost", arrayItem.getCost());
                        chargeTypeObject.addProperty("weight", arrayItem.getWeight());
                        chargeTypeObject.addProperty("length", arrayItem.getLength());
                        chargeTypeObject.addProperty("prodType", arrayItem.getProdType());
                        chargeTypeObject.addProperty("prodId", arrayItem.getProdId());
                        chargeTypeObject.addProperty("color", StringUtill.isEmpty(arrayItem.getColor()) ? "" : arrayItem.getColor());
                        chargeTypeObject.addProperty("percentage", StringUtill.isEmpty(String.valueOf(arrayItem.getPercentage())) ? "" : String.valueOf(arrayItem.getPercentage()));
                        chargeTypeArray.add(chargeTypeObject);
                    }
                }
                object.addProperty("ChargeTypeArray", new Gson().toJson(chargeTypeArray));
                object.addProperty("ChargeLength", dataModel.getChargeLength());

                object.addProperty("DeckLength", dataModel.getDeckLength());
                object.addProperty("DesignId", dataModel.getDesignId());
                object.addProperty("HoleDelay", dataModel.getHoleDelay());
                object.addProperty("HoleDepth", dataModel.getHoleDepth());
                object.addProperty("HoleDiameter", dataModel.getHoleDiameter());
                object.addProperty("HoleID", String.format("R%sH%s", dataModel.getRowNo(), dataModel.getHoleNo()));
                object.addProperty("HoleNo", dataModel.getHoleNo());
                object.addProperty("HoleType", dataModel.getHoleType());
                object.addProperty("InHoleDelay", dataModel.getInHoleDelay());
                object.addProperty("RowNo", dataModel.getRowNo());
                object.addProperty("Spacing", dataModel.getSpacing());
                object.addProperty("StemmingLength", dataModel.getStemmingLength());
                object.addProperty("Subgrade", dataModel.getSubgrade());
                object.addProperty("TielineId", dataModel.getTielineID());
                object.addProperty("TopX", dataModel.getTopX());
                object.addProperty("TopY", dataModel.getTopY());
                object.addProperty("TopZ", dataModel.getTopZ());
                object.addProperty("TotalCharge", dataModel.getTotalCharge());
                object.addProperty("VerticalDip", dataModel.getVerticalDip());
                object.addProperty("WaterDepth", dataModel.getWaterDepth());

                mapObjectArray.add(object);
            }

            MainService.insertUpdate3DActualDesignHoleDetailApiCaller(this, mapObjectArray).observe(this, new Observer<JsonElement>() {
                @Override
                public void onChanged(JsonElement jsonObject) {
                    if (!jsonObject.isJsonNull())
                        showToast("Project Sync Successfully");
                    hideLoader();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private MutableLiveData<JsonElement> updateDesignIdBimsDrimsApiCaller(String designId, String id, boolean is3D, boolean isBims) {
        MutableLiveData<JsonElement> data = new MutableLiveData<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("DesignId", designId);
        jsonObject.addProperty("Id", id);
        jsonObject.addProperty("CompanyId", manger.getUserDetails().getCompanyid());
        jsonObject.addProperty("UserId", manger.getUserDetails().getUserid());
        MainService.update3DDesignBIMSIdApiCaller(this, jsonObject, is3D, isBims).observe(this, new Observer<JsonElement>() {
            @Override
            public void onChanged(JsonElement jsonElement) {
                if (jsonElement != null) {
                    if (!jsonElement.isJsonNull())
                        if (jsonElement.getAsString().equals("True"))
                            Log.e("Code", "Updated");
                }
                hideLoader();
                data.setValue(jsonElement);
            }
        });
        return data;
    }
}
