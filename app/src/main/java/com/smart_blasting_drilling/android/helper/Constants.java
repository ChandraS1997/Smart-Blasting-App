package com.smart_blasting_drilling.android.helper;

import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.interfaces.OnHoleClickListener;

import java.util.List;

public class Constants {
    public static OnHoleClickListener onHoleClickListener;

    public static final String DATABASE_NAME = "drilling_blasting_db";

    public static String API_DEFAULT_URL = "login";
    public static String API_BLADES_URL = "blades";
    public static final String LOGIN_BASE_URL = "https://www.mineexcellence.com/smartdrilling/";
    public static final String BLADES_BASE_URL = "http://testblades.mineexcellence.com/BLADES_API/Service1.svc/";

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

}
