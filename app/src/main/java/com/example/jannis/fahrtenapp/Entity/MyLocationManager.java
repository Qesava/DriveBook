package com.example.jannis.fahrtenapp.Entity;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyLocationManager {
    private List<Location> locations;

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

    public void setLocationList(List<Location> list) {
        locations = list;
    }
}
