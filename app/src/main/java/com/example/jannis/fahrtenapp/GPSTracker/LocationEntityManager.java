package com.example.jannis.fahrtenapp.GPSTracker;

import android.location.Location;

import java.io.Serializable;
import java.util.List;

public class LocationEntityManager implements Serializable {
    private List<Location> locations;
    private long distance;

    public LocationEntityManager() {
        clearLocations();
        distance = 0;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long dist) {
        distance = dist;
    }

    public void addDistance(long dist) {
        distance += dist;
    }

    public List getLocations() {
        return locations;
    }

    public void addLocation(Location loc) {
        locations.add(loc);
    }

    public Location getLocation(int index) {
        return locations.get(index);
    }

    public boolean isLocEmpty() {
        return locations.isEmpty();
    }

    public void clearLocations() {
        locations.clear();
    }

    public void removeLocation(Location loc) {
        locations.remove(loc);
    }
}
