package com.example.jannis.fahrtenapp.GPSTracker;

import android.location.Location;

//TODO: put entities to another class
//maybe this class will be useless after doing so
public class LocationCalculator {
    private Location location;
    private Location newer_location;
    private double distance;

    public LocationCalculator() {
        location = null;
        newer_location = null;
        distance = 0;
    }

    public void calculateDistance() {
        if (!areLocationsNull()) {
            distance += location.distanceTo(newer_location);
        }
    }

    public Location getLocation() {
        return location;
    }

    public Location getNewer_location() {
        return newer_location;
    }

    public void setLocation(Location loc) {
        this.location = loc;
    }

    public void setNewer_location(Location loc) {
        this.newer_location = loc;
    }

    public void clearAll() {
        location = null;
        newer_location = null;
        distance = 0;
    }

    public boolean isLocationNull() {
        return location == null;
    }

    public boolean isNewerLocationNull() {
        return newer_location == null;
    }

    private boolean areLocationsNull() {
        return !(location != null && newer_location != null);
    }
}
