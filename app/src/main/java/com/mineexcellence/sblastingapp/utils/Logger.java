package com.mineexcellence.sblastingapp.utils;

import android.util.Log;

import com.mineexcellence.sblastingapp.BuildConfig;


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
