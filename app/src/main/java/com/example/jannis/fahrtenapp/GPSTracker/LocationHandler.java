package com.example.jannis.fahrtenapp.GPSTracker;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.jannis.fahrtenapp.Entity.DistanceManager;
import com.example.jannis.fahrtenapp.Entity.MyLocationManager;
import com.example.jannis.fahrtenapp.MainActivity;
import com.example.jannis.fahrtenapp.R;
import com.example.jannis.fahrtenapp.SaveData.Database.GPSLocationDataSource;
import com.example.jannis.fahrtenapp.SaveData.ExternalFiles.TestFile;

public class LocationHandler extends Service {

    private static final int ONE_MINUTE = 1000 * 60 * 1;

    private LocationManager mLocationManager;
    public LocationUpdaterListener mLocationListener;
    private MyLocationManager m_locationManger;
    private DistanceManager m_distanceManager;
    public Location previousBestLocation = null;

    public static Boolean isRunning = false;
    private int firsttime = 0;
    PowerManager.WakeLock wakeLock;

    private GPSLocationDataSource dataSource;

    public LocationHandler() {
    }

    public class LocationUpdaterListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, previousBestLocation)) {
                previousBestLocation = location;
                try {
                    if (firsttime > 0) {
                        m_distanceManager.addDistance(location.distanceTo(m_locationManger.getLocationByIndex(m_locationManger.getLocationsSize() - 1)));
                    } else {
                        firsttime++;
                    }
                    m_locationManger.addLocation(location);
                    dataSource.createLocation(location);
                    Log.i("service distance walked", String.valueOf(m_distanceManager.getDistance()));
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationUpdaterListener();
        m_locationManger = MyLocationManager.getLocationInstance();
        m_distanceManager = DistanceManager.getDistanceMangerInstance();

        dataSource = new GPSLocationDataSource(getApplicationContext());
        super.onCreate();
        //PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);

        //wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");

        //Toast.makeText(getApplicationContext(), "Service Created",
        //Toast.LENGTH_SHORT).show();

        Log.e("Google", "Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            startListening();
            dataSource.open();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopListening();
        float datapassed = DistanceManager.testdistancemanager;
        TestFile test = new TestFile();
        test.saveToFile(datapassed);
        dataSource.close();
        super.onDestroy();
        //wakeLock.release();
    }


    private void startListening() {
        Log.i("service", "startListening()");
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("service", "permission granted");
            //if (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            //    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
            //    Log.i("register","newtworkprovider started listening");
            //}

            if (mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
                Log.i("register", "gpsprovider started listening");
            }

        }
        isRunning = true;
    }

    private void stopListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationManager.removeUpdates(mLocationListener);
            Log.i("unregister", "stoped listening");
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
