package com.mineexcellence.sblastingapp.api;

import com.mineexcellence.sblastingapp.helper.Constants;

public class RestUtils {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    public static String getEndPoint(String urlType) {
        if (urlType.equalsIgnoreCase(Constants.API_DEFAULT_URL)) {
            return Constants.LOGIN_BASE_URL;
        } else if (urlType.equalsIgnoreCase(Constants.API_BLADES_URL)) {
            return Constants.BLADES_BASE_URL;
        } else if (urlType.equalsIgnoreCase(Constants.API_IMAGE_VIDEO_BASE_URL)) {
            return Constants.IMAGE_VIDEO_BASE_URL;
        } else if (urlType.equalsIgnoreCase(Constants.API_UPLOAD_BASE_URL)) {
            return Constants.UPLOAD_BASE_URL;
        } else if (urlType.equalsIgnoreCase(Constants.API_DRIMS_BASE_URL)) {
            return Constants.DRIMS_BASE_URL;
        } else if (urlType.equalsIgnoreCase(Constants.BLAST_S_BLAST_BASE_URL)) {
            return Constants.BLAST_S_BLAST;
        } else if (urlType.equalsIgnoreCase(Constants.TEST_BLAST_S_BLAST_BASE_URL)) {
            return Constants.TEST_BLAST_S_BLAST;
        } else {
            return Constants.API_DEFAULT_URL;
        }

    }
}
