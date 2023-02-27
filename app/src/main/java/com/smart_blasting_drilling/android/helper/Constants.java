package com.smart_blasting_drilling.android.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.interfaces.OnDataEditTable;
import com.smart_blasting_drilling.android.interfaces.OnHoleClickListener;

import java.lang.reflect.Type;
import java.util.List;

public class Constants {
    public static OnHoleClickListener onHoleClickListener;
    public static OnDataEditTable onDataEditTable;

    public static final String DATABASE_NAME = "drilling_blasting_db";

    public static String API_DEFAULT_URL = "login";
    public static String API_BLADES_URL = "blades";
    public static String API_IMAGE_VIDEO_BASE_URL = "imagevideo";
    public static String API_UPLOAD_BASE_URL = "upload";
    public static String API_DRIMS_BASE_URL = "drims";
    public static String BLAST_S_BLAST_BASE_URL = "blast_s_blast";
    public static String TEST_BLAST_S_BLAST_BASE_URL = "test_blast_s_blast";
    public static final String LOGIN_BASE_URL = "https://www.mineexcellence.com/smartdrilling/";
    public static final String BLADES_BASE_URL = "http://testblades.mineexcellence.com/";
    public static final String IMAGE_VIDEO_BASE_URL = "http://devsblastapi.mineexcellence.com/api/sblast/";
    public static final String UPLOAD_BASE_URL = "https://centralbims.mineexcellence.com/";
    public static final String DRIMS_BASE_URL = "https://devdrims.mineexcellence.com/DRIMS_API/Service1.svc/";
    public static final String BLAST_S_BLAST = "https://devsblastapi.mineexcellence.com/api/sblast/";
    public static final String TEST_BLAST_S_BLAST = "http://testblades.mineexcellence.com/BLADES_API/Service1.svc/";


    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getUserAuthToken() {
        String token = "";
        if (BaseApplication.getPreferenceManger() != null) {
            if (!StringHelper.isEmpty(BaseApplication.getPreferenceManger().getAuthToken())) {
                token = BaseApplication.getPreferenceManger().getAuthToken();
            }
        }
        return "Bearer " + token;
    }

    public static <T> boolean isListEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T> int getListSize(List<T> list) {
        int size = 0;
        if (isListEmpty(list)) {
            return size;
        } else {
            return list.size();
        }
    }

    public static <T> boolean isListNull(List<T> list) {
        return list == null;
    }

    public static <T> boolean isModelEmpty(T model) {
        return model == null;
    }

    public static ListAdapter getAdapter(Context mContext, String[] list) {
        return new ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, list);
    }

}
