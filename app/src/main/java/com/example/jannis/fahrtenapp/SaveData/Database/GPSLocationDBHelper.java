package com.example.jannis.fahrtenapp.SaveData.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jannis on 28.01.2017.
 */

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
    public static final String FIELD_ROW_ID = "_id";

    /**
     * Field 2 of the table locations, stores the latitude
     */
    public static final String FIELD_LAT = "lat";

    /**
     * Field 3 of the table locations, stores the longitude
     */
    public static final String FIELD_LNG = "lng";

    public static final String DATABASE_TABLE = "location_list";

    public static final String SQL_CREATE =
            "create table " + DATABASE_TABLE + " ( " +
                    FIELD_ROW_ID + " integer primary key autoincrement , " +
                    FIELD_LNG + " double , " +
                    FIELD_LAT + " double " + " ) ";

    public GPSLocationDBHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
