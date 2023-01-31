package com.smart_blasting_drilling.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;

import com.smart_blasting_drilling.android.app.BaseApplication;

public class ViewUtil {
    public static GradientDrawable drawCircle(Context context,int backgroundColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        shape.setColor(context.getResources().getColor(backgroundColor));
        shape.setStroke(0, 0);
        return shape;
    }
    public static GradientDrawable setViewBgWithBorder(Context context,int radius, int bgcolor, int bordercolor, int borderwidth){
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(dpToPx(radius));
        shape.setColor(context.getResources().getColor(bgcolor));
        shape.setStroke(dpToPx(borderwidth),context.getResources().getColor(bordercolor));
        return shape;
    }
    public static GradientDrawable setViewBg(Context context,int radius,int bgcolor){
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius(dpToPx(radius));
        shape.setColor(context.getResources().getColor(bgcolor));
        return shape;
    }

    public static GradientDrawable setViewBgWithDiffRadius(Context context, int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius, int bgcolor){
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadii(new float [] { topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomLeftRadius, bottomLeftRadius,
                bottomRightRadius, bottomRightRadius});
        shape.setColor(context.getResources().getColor(bgcolor));
        return shape;
    }

    public static GradientDrawable setViewBgDiffRadiusWithBorder(Context context, int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius, int bgcolor, int bordercolor, int borderwidth){
        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadii(new float [] { topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomLeftRadius, bottomLeftRadius,
                bottomRightRadius, bottomRightRadius});
        shape.setColor(context.getResources().getColor(bgcolor));
        shape.setStroke(dpToPx(borderwidth),context.getResources().getColor(bordercolor));
        return shape;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = BaseApplication.getInstance().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getWindowHeight(Context context) {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
