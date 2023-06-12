package com.smart_blasting_drilling.android.app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseProjectModelFromAllInfoApi;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable17DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable2DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable3DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable7DesignElementDataModel;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.ui.models.MediaDataModel;

import java.util.ArrayList;
import java.util.List;

public class AppDelegate {

    public static AppDelegate instance;

    public static void setInstance(AppDelegate instance) {
        AppDelegate.instance = instance;
    }
    public List<MediaDataModel> mediaDataModelList = new ArrayList<>();

    public JsonObject codeIdObject = new JsonObject();
    public ResponseProjectModelFromAllInfoApi projectModelFromAllInfoApi = new ResponseProjectModelFromAllInfoApi();
    public Response3DTable1DataModel project3DModelFromAllInfoApi = new Response3DTable1DataModel();

    public AllTablesData allTablesData = new AllTablesData();
    public ResponseBladesRetrieveData bladesRetrieveData = new ResponseBladesRetrieveData();

    public List<Response3DTable4HoleChargingDataModel> holeChargingDataModel = new ArrayList<>();
    public List<Response3DTable1DataModel> response3DTable1DataModel = new ArrayList<>();
    public List<Response3DTable2DataModel> response3DTable2DataModel = new ArrayList<>();
    public List<Response3DTable3DataModel> response3DTable3DataModel = new ArrayList<>();
    public List<Response3DTable7DesignElementDataModel> designElementDataModel = new ArrayList<>();
    public List<Response3DTable17DataModel> response3DTable17DataModelList = new ArrayList<>();

    public JsonElement hole3DDataElement;

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

    public Response3DTable1DataModel getProject3DModelFromAllInfoApi() {
        return project3DModelFromAllInfoApi;
    }

    public void setProject3DModelFromAllInfoApi(Response3DTable1DataModel project3DModelFromAllInfoApi) {
        this.project3DModelFromAllInfoApi = project3DModelFromAllInfoApi;
    }

    public List<MediaDataModel> getMediaDataModelList() {
        return mediaDataModelList;
    }

    public void setMediaDataModelList(List<MediaDataModel> mediaDataModelList) {
        this.mediaDataModelList = mediaDataModelList;
    }

    public void setSingleMediaIntoList(MediaDataModel filePath) {
        if (mediaDataModelList == null) {
            mediaDataModelList = new ArrayList<>();
        }
        mediaDataModelList.add(filePath);
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

    public List<Response3DTable4HoleChargingDataModel> getHoleChargingDataModel() {
        return holeChargingDataModel;
    }

    public void setHoleChargingDataModel(List<Response3DTable4HoleChargingDataModel> holeChargingDataModel) {
        this.holeChargingDataModel = holeChargingDataModel;
    }

    public void addHoleChargingDataModel(Response3DTable4HoleChargingDataModel holeChargingDataModel) {
        if (Constants.isListEmpty(getHoleChargingDataModel())) {
            this.holeChargingDataModel = new ArrayList<>();
            this.holeChargingDataModel.add(holeChargingDataModel);
            setHoleChargingDataModel(this.holeChargingDataModel);
        } else {
            this.holeChargingDataModel.add(holeChargingDataModel);
        }
    }

    public List<Response3DTable1DataModel> getResponse3DTable1DataModel() {
        return response3DTable1DataModel;
    }

    public void setResponse3DTable1DataModel(List<Response3DTable1DataModel> response3DTable1DataModel) {
        this.response3DTable1DataModel = response3DTable1DataModel;
    }

    public void addResponse3DTable1DataModel(Response3DTable1DataModel holeChargingDataModel) {
        if (Constants.isListEmpty(getHoleChargingDataModel())) {
            this.response3DTable1DataModel = new ArrayList<>();
            this.response3DTable1DataModel.add(holeChargingDataModel);
            setResponse3DTable1DataModel(this.response3DTable1DataModel);
        } else {
            this.response3DTable1DataModel.add(holeChargingDataModel);
        }
    }


    public List<Response3DTable2DataModel> getResponse3DTable2DataModel() {
        return response3DTable2DataModel;
    }

    public void setResponse3DTable2DataModel(List<Response3DTable2DataModel> response3DTable2DataModel) {
        this.response3DTable2DataModel = response3DTable2DataModel;
    }

    public List<Response3DTable3DataModel> getResponse3DTable3DataModel() {
        return response3DTable3DataModel;
    }

    public void setResponse3DTable3DataModel(List<Response3DTable3DataModel> response3DTable3DataModel) {
        this.response3DTable3DataModel = response3DTable3DataModel;
    }

    public List<Response3DTable7DesignElementDataModel> getDesignElementDataModel() {
        return designElementDataModel;
    }

    public void setDesignElementDataModel(List<Response3DTable7DesignElementDataModel> designElementDataModel) {
        this.designElementDataModel = designElementDataModel;
    }

    public List<Response3DTable17DataModel> getResponse3DTable17DataModelList() {
        return response3DTable17DataModelList;
    }

    public void setResponse3DTable17DataModelList(List<Response3DTable17DataModel> response3DTable17DataModelList) {
        this.response3DTable17DataModelList = response3DTable17DataModelList;
    }

    public JsonElement getHole3DDataElement() {
        return hole3DDataElement;
    }

    public void setHole3DDataElement(JsonElement hole3DDataElement) {
        this.hole3DDataElement = hole3DDataElement;
    }
}