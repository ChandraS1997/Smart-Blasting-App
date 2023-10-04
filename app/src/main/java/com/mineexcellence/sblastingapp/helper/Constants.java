package com.mineexcellence.sblastingapp.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.mineexcellence.sblastingapp.app.BaseApplication;
import com.mineexcellence.sblastingapp.interfaces.Hole3DBgListener;
import com.mineexcellence.sblastingapp.interfaces.HoleBgListener;
import com.mineexcellence.sblastingapp.interfaces.OnDataEditTable;
import com.mineexcellence.sblastingapp.interfaces.OnHoleClickListener;

import java.util.List;

public class Constants {
    public static OnHoleClickListener onHoleClickListener;
    public static OnDataEditTable onDataEditTable;
    public static HoleBgListener holeBgListener;
    public static Hole3DBgListener hole3DBgListener;

    public static final String DATABASE_NAME = "drilling_blasting_db";

    public static String API_DEFAULT_URL = "login";
    public static String API_BLADES_URL = "blades";
    public static String API_IMAGE_VIDEO_BASE_URL = "imagevideo";
    public static String API_UPLOAD_BASE_URL = "upload";
    public static String API_DRIMS_BASE_URL = "drims";
    public static String BLAST_S_BLAST_BASE_URL = "blast_s_blast";
    public static String TEST_BLAST_S_BLAST_BASE_URL = "test_blast_s_blast";

    /*// Dev Url
    public static final String LOGIN_BASE_URL = "https://www.mineexcellence.com/smartdrilling/";
    public static final String BLADES_BASE_URL = "http://testblades.mineexcellence.com/";
    public static final String IMAGE_VIDEO_BASE_URL = "http://devsblastapi.mineexcellence.com/api/sblast/";
    public static final String UPLOAD_BASE_URL = "https://centralbims.mineexcellence.com/";
    public static final String DRIMS_BASE_URL = "https://devdrims.mineexcellence.com/DRIMS_API/Service1.svc/";
    public static final String BLAST_S_BLAST = "https://devsblastapi.mineexcellence.com/api/sblast/";
    public static final String TEST_BLAST_S_BLAST = "http://testblades.mineexcellence.com/BLADES_API/Service1.svc/";
    public static final String DB_NAME = "dev_centralmineinfo";
    public static final String _3D_TBALE_NAME = "GetAll3DDesignInfoResult";*/

    // Live Url
    public static final String LOGIN_BASE_URL = "https://www.mineexcellence.com/smartdrilling/";
    public static final String BLADES_BASE_URL = "http://blastdesigner.mineexcellence.com/";
    public static final String IMAGE_VIDEO_BASE_URL = "http://sblastapi.mineexcellence.com/api/sblast/";
    public static final String UPLOAD_BASE_URL = "https://centralbims.mineexcellence.com/";
    public static final String DRIMS_BASE_URL = "https://drims.mineexcellence.com/DRIMS_API/Service1.svc/";
    public static final String BLAST_S_BLAST = "http://sblastapi.mineexcellence.com/api/sblast/";
    //    public static final String BLAST_S_BLAST = "https://sblastapi.mineexcellence.com/api/sblast/";
    public static final String TEST_BLAST_S_BLAST = "http://blastdesigner.mineexcellence.com/BLADES_API/Service1.svc/";
    public static final String DB_NAME = "centralmineinfo";
    public static final String _3D_TBALE_NAME = "GetAll3DDesignwithActualInfoResult";

/*
    // Live Cil Url
    public static final String LOGIN_BASE_URL = "https://www.cil.mineexcellence.com/smartdrilling/";
    public static final String BLADES_BASE_URL = "http://blastdesigner.cil.mineexcellence.com/";
    public static final String IMAGE_VIDEO_BASE_URL = "http://sblastapi.cil.mineexcellence.com/api/sblast/";
    public static final String UPLOAD_BASE_URL = "https://centralbims.cil.mineexcellence.com/";
    public static final String DRIMS_BASE_URL = "https://drims.cil.mineexcellence.com/DRIMS_API/Service1.svc/";
    public static final String BLAST_S_BLAST = "http://sblastapi.cil.mineexcellence.com/api/sblast/";
//    public static final String BLAST_S_BLAST = "https://sblastapi.cil.mineexcellence.com/api/sblast/";
    public static final String TEST_BLAST_S_BLAST = "http://blastdesigner.cil.mineexcellence.com/BLADES_API/Service1.svc/";
    public static final String DB_NAME = "centralmineinfo";
    public static final String _3D_TBALE_NAME = "GetAll3DDesignwithActualInfoResult";*/

    public static final int NORMAL = 0;
    public static final int PILOT = 1;
    public static final int PRE_SPLIT = 2;

    // Hole type for API
    public static final int PRODUCTION_HOLE = 1;
    public static final int PILOT_HOLE = 3;
    public static final int PRE_SPLIT_HOLE = 2;

    public enum TABLE_TYPE {
        NORMAL(0),
        PILOT(1),
        PRE_SPLIT(2);

        final int type;

        TABLE_TYPE(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }


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

    public static long getScreenWidthResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.e("width : ", String.valueOf(width));
        return width;
    }

    public static long getScreenHeightResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.e("height : ", String.valueOf(height));
        return height;
    }

}
