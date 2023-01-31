package com.smart_blasting_drilling.android.utils;

import android.util.Log;

import com.smart_blasting_drilling.android.BuildConfig;


public class Logger {

    public static void ErrorLog(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message);
        }
    }
    public static void DebugLog(String tag, String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }
}
