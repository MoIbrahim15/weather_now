package com.mohamedibrahim.weathernow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_CITY_NAME;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_CURRENT_TEMP;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_DESC;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_HUM;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_MAX_TEMP;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_MIN_TEMP;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_PRESS;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_PRIMARY_ID;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_WIND_DEGREE;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_WIND_SPEED;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.TABLE_CITIES;

public class CityDBHelper extends SQLiteOpenHelper {

    private static CityDBHelper sInstance;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "placesManager";


    public static synchronized CityDBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new CityDBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private CityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CITIES + "("
                + KEY_PRIMARY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CITY_NAME + " TEXT,"
                + KEY_CURRENT_TEMP + " TEXT,"
                + KEY_MAX_TEMP + " TEXT,"
                + KEY_MIN_TEMP + " TEXT,"
                + KEY_HUM + " TEXT,"
                + KEY_PRESS + " TEXT,"
                + KEY_WIND_DEGREE + " TEXT,"
                + KEY_WIND_SPEED + " TEXT,"
                + KEY_DESC + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + );
//        onCreate(db);
        //        TODO when updatind DB must make ulter query for updating table instead of drop table and recreeate it again:)
        if (oldVersion < 1) {
//            db.execSQL(DATABASE_ALTER_TEAM_1);
        }
    }
}
