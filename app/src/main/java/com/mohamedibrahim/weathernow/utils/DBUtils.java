package com.mohamedibrahim.weathernow.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.mohamedibrahim.weathernow.data.CityContract;
import com.mohamedibrahim.weathernow.models.Item;
import com.mohamedibrahim.weathernow.models.Main;
import com.mohamedibrahim.weathernow.models.Weather;
import com.mohamedibrahim.weathernow.models.Wind;

import java.util.ArrayList;

import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_CITY_NAME;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_CURRENT_TEMP;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_DESC;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_HUM;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_MAX_TEMP;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_MIN_TEMP;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_PRESS;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_WIND_DEGREE;
import static com.mohamedibrahim.weathernow.data.CityContract.CityEntry.KEY_WIND_SPEED;

public class DBUtils {

    private static final String ACTION_WIDGET_UPDATED = "android.appwidget.action.APPWIDGET_UPDATE";

    public static void addCities(ArrayList<Item> items, Context context) {

        ContentValues[] valuesArray = new ContentValues[items.size()];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            ContentValues values = new ContentValues();
            values.put(KEY_CITY_NAME, item.getName());
            values.put(KEY_CURRENT_TEMP, item.getMain().getTemp());
            values.put(KEY_MAX_TEMP, item.getMain().getTemp_max());
            values.put(KEY_MIN_TEMP, item.getMain().getTemp_min());
            values.put(KEY_HUM, item.getMain().getHumidity());
            values.put(KEY_PRESS, item.getMain().getPressure());
            values.put(KEY_WIND_DEGREE, item.getWind().getDeg());
            values.put(KEY_WIND_SPEED, item.getWind().getSpeed());
            if (!item.getWeather().isEmpty()) {
                values.put(KEY_DESC, item.getWeather().get(0).getDescription());
            } else {
                values.put(KEY_DESC, "");
            }
            valuesArray[i] = values;
        }
        if (!items.isEmpty()) {
            context.getContentResolver().bulkInsert(CityContract.CityEntry.CONTENT_URI,
                    valuesArray);
            updateWidget(context);
        }
    }

    public static void deleteCities(Context context) {
        context.getContentResolver().delete(CityContract.CityEntry.CONTENT_URI,
                null, null);
        updateWidget(context);
    }

    public static ArrayList<Item> getAllCities(Context context) {
        ArrayList<Item> items = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(CityContract.CityEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setName(cursor.getString(1));
                Main main = new Main();
                main.setTemp(cursor.getString(2));
                main.setTemp_max(cursor.getString(3));
                main.setTemp_min(cursor.getString(4));
                main.setHumidity(cursor.getString(5));
                main.setPressure(cursor.getString(6));
                item.setMain(main);
                Wind wind = new Wind();
                wind.setDeg(cursor.getString(7));
                wind.setSpeed(cursor.getString(8));
                item.setWind(wind);
                Weather weather = new Weather();
                weather.setDescription(cursor.getString(9));
                ArrayList<Weather> weathers = new ArrayList<>();
                weathers.add(weather);
                item.setWeather(weathers);

                items.add(item);

            } while (cursor.moveToNext());
        }
        if (cursor != null)
            cursor.close();
        return items;
    }

    private static void updateWidget(Context context) {
        Intent intent = new Intent(ACTION_WIDGET_UPDATED);
        context.sendBroadcast(intent);
    }
}
