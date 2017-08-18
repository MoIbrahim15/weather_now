package com.mohamedibrahim.weathernow.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class CityContract {

    public static final String AUTHORITY = "com.mohamedibrahim.weathernow";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_PLACES = "CITIES";

    public static final class CityEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACES).build();

        public static final String TABLE_CITIES = "cities";
        public static final String KEY_PRIMARY_ID = "primary_id";
        public static final String KEY_CITY_NAME = "city_name";
        public static final String KEY_CURRENT_TEMP = "current_temp";
        public static final String KEY_MAX_TEMP = "max_temp";
        public static final String KEY_MIN_TEMP = "min_temp";
        public static final String KEY_HUM = "humidity";
        public static final String KEY_PRESS = "press";
        public static final String KEY_WIND_DEGREE = "wind_degree";
        public static final String KEY_WIND_SPEED = "wind_speed";
        public static final String KEY_DESC = "desc";


    }
}
