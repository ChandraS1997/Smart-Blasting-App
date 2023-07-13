package com.mineexcellence.sblastingapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean validateEmail(String emailStr) {
        return !TextUtils.isEmpty(emailStr) && Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
    }

    public static boolean isValidMobile(String phone) {
        if (phone.matches("[0-9]+") && phone.length() ==10) {
            return  true;
        }

        return false;
    }
    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
    public static boolean isValidID(CharSequence target) {

        return Patterns.PHONE.matcher(target).matches();

    }

}
