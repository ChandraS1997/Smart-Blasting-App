package com.smart_blasting_drilling.android.app;

import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.api.apis.response.ResponseProjectModelFromAllInfoApi;

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
}