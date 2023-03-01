package com.smart_blasting_drilling.android.app;

import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseProjectModelFromAllInfoApi;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable3DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable7DesignElementDataModel;

import java.util.ArrayList;
import java.util.List;

public class AppDelegate {

    public static AppDelegate instance;

    public static void setInstance(AppDelegate instance) {
        AppDelegate.instance = instance;
    }
    public List<String> imgList = new ArrayList<>();

    public JsonObject codeIdObject = new JsonObject();
    public ResponseProjectModelFromAllInfoApi projectModelFromAllInfoApi = new ResponseProjectModelFromAllInfoApi();

    public AllTablesData allTablesData = new AllTablesData();
    public ResponseBladesRetrieveData bladesRetrieveData = new ResponseBladesRetrieveData();

    public Response3DTable4HoleChargingDataModel holeChargingDataModel = new Response3DTable4HoleChargingDataModel();
    public Response3DTable1DataModel response3DTable1DataModel = new Response3DTable1DataModel();
    public Response3DTable2DataModel response3DTable2DataModel = new Response3DTable2DataModel();
    public Response3DTable3DataModel response3DTable3DataModel = new Response3DTable3DataModel();
    public Response3DTable7DesignElementDataModel designElementDataModel = new Response3DTable7DesignElementDataModel();

    public static AppDelegate getInstance() {
        if (instance == null) {
            instance = new AppDelegate();
        }
        return instance;
    }

    public JsonObject getCodeIdObject() {
        return codeIdObject;
    }

    public void setCodeIdObject(JsonObject codeIdObject) {
        this.codeIdObject = codeIdObject;
    }

    public ResponseProjectModelFromAllInfoApi getProjectModelFromAllInfoApi() {
        return projectModelFromAllInfoApi;
    }

    public void setProjectModelFromAllInfoApi(ResponseProjectModelFromAllInfoApi projectModelFromAllInfoApi) {
        this.projectModelFromAllInfoApi = projectModelFromAllInfoApi;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public void setSingleImageIntoList(String filePath) {
        if (imgList == null) {
            imgList = new ArrayList<>();
        }
        imgList.add(filePath);
    }

    public AllTablesData getAllTablesData() {
        return allTablesData;
    }

    public void setAllTablesData(AllTablesData allTablesData) {
        this.allTablesData = allTablesData;
    }

    public ResponseBladesRetrieveData getBladesRetrieveData() {
        return bladesRetrieveData;
    }

    public void setBladesRetrieveData(ResponseBladesRetrieveData bladesRetrieveData) {
        this.bladesRetrieveData = bladesRetrieveData;
    }

    public Response3DTable4HoleChargingDataModel getHoleChargingDataModel() {
        return holeChargingDataModel;
    }

    public void setHoleChargingDataModel(Response3DTable4HoleChargingDataModel holeChargingDataModel) {
        this.holeChargingDataModel = holeChargingDataModel;
    }

    public Response3DTable1DataModel getResponse3DTable1DataModel() {
        return response3DTable1DataModel;
    }

    public void setResponse3DTable1DataModel(Response3DTable1DataModel response3DTable1DataModel) {
        this.response3DTable1DataModel = response3DTable1DataModel;
    }

    public Response3DTable2DataModel getResponse3DTable2DataModel() {
        return response3DTable2DataModel;
    }

    public void setResponse3DTable2DataModel(Response3DTable2DataModel response3DTable2DataModel) {
        this.response3DTable2DataModel = response3DTable2DataModel;
    }

    public Response3DTable3DataModel getResponse3DTable3DataModel() {
        return response3DTable3DataModel;
    }

    public void setResponse3DTable3DataModel(Response3DTable3DataModel response3DTable3DataModel) {
        this.response3DTable3DataModel = response3DTable3DataModel;
    }

    public Response3DTable7DesignElementDataModel getDesignElementDataModel() {
        return designElementDataModel;
    }

    public void setDesignElementDataModel(Response3DTable7DesignElementDataModel designElementDataModel) {
        this.designElementDataModel = designElementDataModel;
    }
}