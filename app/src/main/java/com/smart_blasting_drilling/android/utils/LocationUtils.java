package com.smart_blasting_drilling.android.utils;

import android.location.Location;

public class LocationUtils {

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static double getTimeEstimation(double startLat, double startLng, double endLat, double endLng) {
        Location loc1 = new Location("");
        loc1.setLatitude(startLat);
        loc1.setLongitude(startLng);

        Location loc2 = new Location("");
        loc2.setLatitude(endLat);
        loc2.setLongitude(endLng);

        float distance = loc1.distanceTo(loc2);

        int speed=30;
        return distance/speed;
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}
