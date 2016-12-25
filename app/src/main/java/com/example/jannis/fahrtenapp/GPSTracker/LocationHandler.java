package com.example.jannis.fahrtenapp.GPSTracker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class LocationHandler {
    private LocationManager locationmanager;
    private LocationListener locationlistener;
    private Context mContext;
    private LocationCalculator locationCalculator;
    private LocationChecker locationChecker;

    public LocationHandler(Context pContext) {
        this.locationCalculator = new LocationCalculator();
        this.mContext = pContext;
        this.locationCalculator = new LocationCalculator();
        this.locationChecker = new LocationChecker();

        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //TODO: kill myself for this shit code
                if (locationCalculator.isLocationNull() || locationCalculator.isNewerLocationNull()) {
                    if (locationCalculator.isLocationNull()) {
                        locationCalculator.setLocation(location);
                    } else {
                        locationCalculator.setNewer_location(location);
                    }
                } else {
                    if (locationChecker.isBetterLocation(locationCalculator.getLocation(), locationCalculator.getNewer_location())) {
                        locationCalculator.setNewer_location(location);
                    } else {
                        locationCalculator.setLocation(location);
                    }
                }
                if (locationCalculator.isLocationNull() || locationCalculator.isNewerLocationNull()) {

                } else {
                    locationCalculator.calculateDistance();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationmanager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startListening() {
        locationCalculator.clearAll();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);
    }

    public void stopListening() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationmanager.removeUpdates(locationlistener);
        locationCalculator.clearAll();
    }

}
