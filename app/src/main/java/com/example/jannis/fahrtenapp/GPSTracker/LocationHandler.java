package com.example.jannis.fahrtenapp.GPSTracker;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.jannis.fahrtenapp.SaveData.ExternalFiles.TestFile;

import junit.framework.Test;

public class LocationHandler extends Service {

    private static final int ONE_MINUTE = 1000 * 60 * 1;

    private LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    private LocationEntityManager mLocationEntityManager;
    public Location previousBestLocation = null;

    public static Boolean isRunning = false;
    PowerManager.WakeLock wakeLock;

    public final static String MY_ACTION = "MY_ACTION";

    public LocationHandler() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
        mLocationEntityManager = new LocationEntityManager();
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");

        // Toast.makeText(getApplicationContext(), "Service Created",
        // Toast.LENGTH_SHORT).show();

        Log.e("Google", "Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            startListening();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopListening();
        MyThread myThread = new MyThread();
        myThread.start();
        mLocationEntityManager = new LocationEntityManager();
        super.onDestroy();
        wakeLock.release();
    }

    public class MyThread extends Thread {

        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("DATAPASSED", mLocationEntityManager.getDistance());
            sendBroadcast(intent);
            stopSelf();
        }

    }

    public class LocationUpdaterListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, previousBestLocation)) {
                previousBestLocation = location;
                try {
                    mLocationEntityManager.addLocation(location);
                    mLocationEntityManager.addDistance((long) location.distanceTo(mLocationEntityManager.getLocation(mLocationEntityManager.getLocationsSize() - 1)));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    stopListening();

                }
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
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

            if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        }
        isRunning = true;
    }

    private void stopListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
        }
        isRunning = false;
    }

    public boolean isBetterLocation(Location left, Location right) {
        if (right == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = left.getTime() - right.getTime();
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTE;
        boolean isSignificantlyOlder = timeDelta < -ONE_MINUTE;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (left.getAccuracy() - right.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(left.getProvider(),
                right.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
}
