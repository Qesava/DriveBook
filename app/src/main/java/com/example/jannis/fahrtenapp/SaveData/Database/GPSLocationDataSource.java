package com.example.jannis.fahrtenapp.SaveData.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.example.jannis.fahrtenapp.Entity.Distance;

import java.util.ArrayList;
import java.util.List;

public class GPSLocationDataSource {

    private SQLiteDatabase database;
    private GPSLocationDBHelper dbHelper;

    private String[] columns = {
            GPSLocationDBHelper.FIELD_ROW_ID,
            GPSLocationDBHelper.FIELD_TIME,
            GPSLocationDBHelper.FIELD_LNG,
            GPSLocationDBHelper.FIELD_LAT,
            GPSLocationDBHelper.FIELD_ROW_ID_DIST
    };

    private String[] columns_dist = {
            GPSLocationDBHelper.FIELD_ROW_ID_DIST,
            GPSLocationDBHelper.FIELD_DISTANCE,
            GPSLocationDBHelper.FIELD_STARTTIME,
            GPSLocationDBHelper.FIELD_STOPTIME
    };

    public GPSLocationDataSource(Context context) {
        dbHelper = new GPSLocationDBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void openRead() {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createLocation(Location location, long foreignkey) {
        ContentValues values = new ContentValues();
        values.put(GPSLocationDBHelper.FIELD_TIME, location.getTime());
        values.put(GPSLocationDBHelper.FIELD_LNG, location.getLongitude());
        values.put(GPSLocationDBHelper.FIELD_LAT, location.getLatitude());
        values.put(GPSLocationDBHelper.FIELD_ROW_ID_DIST, foreignkey);
        return database.insert(GPSLocationDBHelper.DATABASE_TABLE_LOCA, null, values);
    }

    public long createDistance(double distance, long startTime, long stopTime) {
        ContentValues values = new ContentValues();
        values.put(GPSLocationDBHelper.FIELD_DISTANCE, distance);
        values.put(GPSLocationDBHelper.FIELD_STARTTIME, startTime);
        values.put(GPSLocationDBHelper.FIELD_STOPTIME, stopTime);

        return database.insert(GPSLocationDBHelper.DATABASE_TABLE_DIST, null, values);
    }

    private Distance cursorToDistance(Cursor cursor) {
        int distance = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_DISTANCE);
        int startTime = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_STARTTIME);
        int stoptime = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_STOPTIME);
        int idInc = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_ROW_ID_DIST);

        long id = cursor.getLong(idInc);

        return new Distance(cursor.getFloat(distance), cursor.getLong(startTime), cursor.getLong(stoptime), id);
    }

    private Location cursorToGPSLocation(Cursor cursor) {
        int idLng = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_LNG);
        int idLat = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_LAT);
        int idTime = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_TIME);


        double lat = cursor.getDouble(idLat);
        double lng = cursor.getDouble(idLng);
        long time = cursor.getLong(idTime);


        Location location = new Location("semiProvider");
        location.setTime(time);
        location.setLatitude(lat);
        location.setLongitude(lng);

        return location;
    }

    public List<Location> getLocationsByKey(long key) {
        List<Location> locations = new ArrayList<>();
        Cursor cursor = database.query(GPSLocationDBHelper.DATABASE_TABLE_LOCA,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Location location;

        while (!cursor.isAfterLast()) {
            long id = cursor.getLong(cursor.getColumnIndex(GPSLocationDBHelper.FIELD_ROW_ID_DIST));
            if (id == key) {
                location = cursorToGPSLocation(cursor);
                locations.add(location);
            }
            cursor.moveToNext();
        }
        cursor.close();
        return locations;
    }

    public List<Location> getAllGPSLocations() {
        List<Location> locations = new ArrayList<>();

        Cursor cursor = database.query(GPSLocationDBHelper.DATABASE_TABLE_LOCA,
                columns, null, null, null, null, null);

        cursor.moveToFirst();
        Location location;

        while (!cursor.isAfterLast()) {
            location = cursorToGPSLocation(cursor);
            locations.add(location);
            cursor.moveToNext();
        }
        cursor.close();
        return locations;
    }

    public List<Distance> getAllDistances() {
        List<Distance> m_distance = new ArrayList<>();

        Cursor cursor = database.query(GPSLocationDBHelper.DATABASE_TABLE_DIST,
                columns_dist, null, null, null, null, null);

        cursor.moveToFirst();
        Distance distance;

        while (!cursor.isAfterLast()) {
            distance = cursorToDistance(cursor);
            m_distance.add(distance);
            cursor.moveToNext();
        }
        cursor.close();
        return m_distance;
    }
}
