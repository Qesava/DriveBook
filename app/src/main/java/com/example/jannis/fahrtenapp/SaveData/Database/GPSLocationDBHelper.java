package com.example.jannis.fahrtenapp.SaveData.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//TODO: Add FK for distance locationlist connection

public class GPSLocationDBHelper extends SQLiteOpenHelper {

    /**
     * Database name
     */
    private static String DBNAME = "GPSLocationData";

    /**
     * Version number of the database
     */
    private static int VERSION = 1;

    /**
     * Field 1 of the table locations, which is the primary key
     */
    static final String FIELD_ROW_ID = "_id";

    static final String FIELD_ROW_ID_DIST = "id";

    /**
     * Field 2 of the table locations, stores the latitude
     */
    static final String FIELD_LAT = "lat";

    /**
     * Field 3 of the table locations, stores the longitude
     */
    static final String FIELD_LNG = "lng";

    static final String FIELD_TIME = "time";

    static final String FIELD_DISTANCE = "dist";

    static final String FIELD_STARTTIME = "startTime";

    static final String FIELD_STOPTIME = "stopTime";

    static final String DATABASE_TABLE_LOCA = "location_list";

    static final String DATABASE_TABLE_DIST = "distance_list";

    private static final String SQL_CREATE =
            "create table " + DATABASE_TABLE_LOCA + " ( " +
                    FIELD_ROW_ID + " integer primary key autoincrement , " +
                    FIELD_TIME + " long , " +
                    FIELD_LNG + " double , " +
                    FIELD_LAT + " double " + " ) ";

    private static final String SQL_CREATE_D =
            "create table " + DATABASE_TABLE_DIST + " ( " +
                    FIELD_DISTANCE + " double , " +
                    FIELD_STARTTIME + " long , " +
                    FIELD_STOPTIME + " long , " +
                    FIELD_ROW_ID_DIST + " integer primary key autoincrement " + " ) ";


    public GPSLocationDBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
        db.execSQL(SQL_CREATE_D);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
