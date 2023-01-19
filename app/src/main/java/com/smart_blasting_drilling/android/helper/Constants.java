package com.smart_blasting_drilling.android.helper;

import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.interfaces.onHoleClickListener;

public class Constants {
    public static com.smart_blasting_drilling.android.interfaces.onHoleClickListener onHoleClickListener;

    public static String getUserAuthToken() {
        String token = "";
        if (BaseApplication.getPreferenceManger() != null) {
            if (!StringHelper.isEmpty(BaseApplication.getPreferenceManger().getAuthToken())) {
                token = BaseApplication.getPreferenceManger().getAuthToken();
            }
        }
        return  "Bearer " + token;
    }
}
