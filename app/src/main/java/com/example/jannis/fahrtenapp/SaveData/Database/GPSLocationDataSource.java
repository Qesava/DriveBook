package com.example.jannis.fahrtenapp.SaveData.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jannis on 28.01.2017.
 */

public class GPSLocationDataSource {

    private SQLiteDatabase database;
    private GPSLocationDBHelper dbHelper;

    private String[] columns = {
            GPSLocationDBHelper.FIELD_ROW_ID,
            GPSLocationDBHelper.FIELD_LNG,
            GPSLocationDBHelper.FIELD_LAT
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

    public Location createLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put(GPSLocationDBHelper.FIELD_LNG, location.getLongitude());
        values.put(GPSLocationDBHelper.FIELD_LAT, location.getLatitude());

        long insertId = database.insert(GPSLocationDBHelper.DATABASE_TABLE, null, values);

        Cursor cursor = database.query(GPSLocationDBHelper.DATABASE_TABLE,
                columns, GPSLocationDBHelper.FIELD_ROW_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        Location location_m = cursorToGPSLocation(cursor);
        cursor.close();

        return location;
    }

    private Location cursorToGPSLocation(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_ROW_ID);
        int idLng = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_LNG);
        int idLat = cursor.getColumnIndex(GPSLocationDBHelper.FIELD_LAT);

        double lat = cursor.getDouble(idLat);
        double lng = cursor.getDouble(idLng);
        long id = cursor.getLong(idIndex);

        Location location = new Location("semiProvider");
        location.setLatitude(lat);
        location.setLongitude(lng);

        return location;
    }

    public List<Location> getAllGPSLocations() {
        List<Location> locations = new ArrayList<>();

        Cursor cursor = database.query(GPSLocationDBHelper.DATABASE_TABLE,
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
}
