package com.smart_blasting_drilling.android.app;

public class AppDelegate {

    public static AppDelegate instance;
    public static void setInstance(AppDelegate instance) {
        AppDelegate.instance = instance;
    }

    public static AppDelegate getInstance() {
        if (instance == null) {
            instance = new AppDelegate();
        }
        return instance;
    }




}