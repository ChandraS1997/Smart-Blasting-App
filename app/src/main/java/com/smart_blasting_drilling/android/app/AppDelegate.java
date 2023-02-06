package com.smart_blasting_drilling.android.app;

import java.util.ArrayList;
import java.util.List;

public class AppDelegate {

    public static AppDelegate instance;

    public static void setInstance(AppDelegate instance) {
        AppDelegate.instance = instance;
    }
    public List<String> imgList = new ArrayList<>();

    public static AppDelegate getInstance() {
        if (instance == null) {
            instance = new AppDelegate();
        }
        return instance;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
}