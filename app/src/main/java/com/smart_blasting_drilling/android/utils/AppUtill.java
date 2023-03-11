package com.smart_blasting_drilling.android.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.net.MalformedURLException;
import java.net.URL;

public class AppUtill {

    public static String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        }
        catch(MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(
                ()-> view.setEnabled(true),
                500
        );
    }

    public static void checkWidthHeight(View view) {
        try {
            ViewTreeObserver vto = view.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (view.getMeasuredHeight() > 0) {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        int width = view.getMeasuredWidth();
                        int height = view.getMeasuredHeight();

                        Log.e("Width" + width, "  ->  Height" + height);
                    }
                }
            });
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

}
