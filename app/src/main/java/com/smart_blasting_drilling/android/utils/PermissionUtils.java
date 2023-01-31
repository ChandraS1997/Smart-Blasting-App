package com.smart_blasting_drilling.android.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

import com.smart_blasting_drilling.android.R;

public class PermissionUtils {
    public static boolean isGpsDialogShow = false;

    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            isGpsDialogShow = true;
        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static void showGPSNotEnabledDialog(final Context context) {
        if (!isGpsDialogShow) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.enable_gps))
                    .setMessage(context.getString(R.string.required_for_this_app))
                    .setCancelable(false)
                    .setPositiveButton(context.getString(R.string.enable_now), (dialog, which) -> {
                        dialog.dismiss();
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }).show();
            isGpsDialogShow = true;
        }
    }

}
