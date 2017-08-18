package com.mohamedibrahim.weathernow.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.mohamedibrahim.weathernow.R;

import java.lang.ref.WeakReference;

/**
 * Created by Mohamed Ibrahim
 * on 8/18/2017.
 */


public class SharedPreferencesManager {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private WeakReference<Context> contextReference;

    @SuppressLint("CommitPrefEdits")
    private SharedPreferencesManager(Context context) {
        contextReference = new WeakReference<>(context.getApplicationContext());
        if (prefs == null) {
            prefs = getContext().getSharedPreferences(getContext().getString(R.string.shared_pref),
                    Context.MODE_PRIVATE);
        }

        if (editor == null) {
            editor = prefs.edit();
        }
    }

    public static SharedPreferencesManager getInstance(Context context) {
        return new SharedPreferencesManager(context);
    }

    private Context getContext() {
        return contextReference.get();
    }

    public boolean contains(String key) {
        return prefs.contains(key);
    }


    public String getString(int stringRes, String defValue) {
        return getString(getContext().getString(stringRes), defValue);
    }

    public String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public void putString(int stringRes, String defValue) {
        putString(getContext().getString(stringRes), defValue);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        apply();
    }

    public int getInt(int stringRes, int defValue) {
        return prefs.getInt(getContext().getString(stringRes), defValue);
    }

    public int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public void putInt(int stringRes, int value) {
        putInt(getContext().getString(stringRes), value);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        apply();
    }

    public boolean getBoolean(int stringRes, boolean defValue) {
        return getBoolean(getContext().getString(stringRes), defValue);
    }


    public boolean getBoolean(String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public void putBoolean(int stringRes, boolean value) {
        putBoolean(getContext().getString(stringRes), value);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        apply();
    }

    private void apply() {
        editor.apply();
    }

    public void clear() {
        editor.clear().apply();
    }
}