package com.example.jannis.fahrtenapp.Entity;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jannis on 24.01.2017.
 */

public class MyLocationManager {
    //private static final MyLocationManager m_locationManagerInstance = new MyLocationManager();
    private List<Location> locations;

    /*
    public static MyLocationManager getLocationInstance() {
        return m_locationManagerInstance;
    }*/

    public MyLocationManager() {
        locations = new ArrayList<>();
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public Location getLocationByIndex(int index) {
        return locations.get(index);
    }

    public int getLocationsSize() {
        return locations.size();
    }

    public List getLocationList() {
        return locations;
    }

    public void clearLocations() {
        locations.clear();
    }

    public void setLocationList(List list) {
        locations = list;
    }
}
