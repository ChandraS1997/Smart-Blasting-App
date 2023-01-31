package com.smart_blasting_drilling.android.api;

import com.smart_blasting_drilling.android.helper.Constants;

public class RestUtils {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    public static String getEndPoint(String urlType) {
        if (urlType.equalsIgnoreCase(Constants.API_DEFAULT_URL)) {
            return Constants.LOGIN_BASE_URL;
        } else if (urlType.equalsIgnoreCase(Constants.API_BLADES_URL)) {
            return Constants.BLADES_BASE_URL;
        } else {
            return Constants.API_DEFAULT_URL;
        }
    }
}
