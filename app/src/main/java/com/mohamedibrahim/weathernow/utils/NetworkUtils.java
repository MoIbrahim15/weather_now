package com.mohamedibrahim.weathernow.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.mohamedibrahim.weathernow.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static URL buildUrl(Context context, LatLng latLng) {
        String BASE_URL = context.getString(R.string.main_url);
        String base_param = "APPID=" + context.getResources().getString(R.string.client_id)
                + "&lat=" + latLng.latitude + "&lon=" + latLng.longitude +
                "&cnt=50" + "&units=metric";

        URL url = null;
        try {
            url = new URL(BASE_URL.concat(base_param));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000); //TIMEOUT connections :)
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
