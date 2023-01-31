package com.smart_blasting_drilling.android.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class SingleShotLocationProvider {

    public static interface LocationCallback {
        public void onNewLocationAvailable(Location location);
    }

    // ASSUMING YOU ALREADY ASK FOR ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION PERMISSIONS

    public static void requestSingleUpdate(final Context context, final LocationCallback callback) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            callback.onNewLocationAvailable(null);
            return;
        }

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Criteria criteria = null;

        if (isGPSEnabled) {
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
        } else if (isNetworkEnabled) {
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        }

        if (criteria != null) {
            locationManager.requestSingleUpdate(criteria, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    callback.onNewLocationAvailable(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    Log.e("Status Change Provider : ", provider);
                }

                @Override
                public void onProviderEnabled(String provider) {
                    Log.e("Enable Provider : ", provider);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    Log.e("Disable Provider : ", provider);
                }
            }, null);

        } else {
            callback.onNewLocationAvailable(null);
        }
    }
}