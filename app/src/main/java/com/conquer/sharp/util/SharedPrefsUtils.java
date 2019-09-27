package com.conquer.sharp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

public class SharedPrefsUtils {
    private static final String SHARED_PREFS_SHARP = "shared_prefs_sharp";

    private static SharedPreferences getSharedPrefs(Context context) {
        return getSharedPrefs(context, SHARED_PREFS_SHARP);
    }

    @Nullable
    public static SharedPreferences getSharedPrefs(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key, String defVal) {
        return getSharedPrefs(context).getString(key, defVal);
    }

    public static void putString(Context context, String key, String value) {
        putString(context, SHARED_PREFS_SHARP, key, value);
    }

    public static void putString(Context context, String table, String key, String value) {
        SharedPreferences sharedPreferences = getSharedPrefs(context, table);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(key, value).apply();
        }
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (context == null) {
            return;
        }
        putBoolean(context, SHARED_PREFS_SHARP, key, value);
    }

    public static void putBoolean(Context context, String table, String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPrefs(context, table);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putBoolean(key, value).apply();
        }
    }

    public static boolean getBoolean(Context context, String key, boolean defVal) {
        return context != null && getSharedPrefs(context).getBoolean(key, defVal);
    }

    public static void putInt(Context context, String key, int value) {
        putInt(context, SHARED_PREFS_SHARP, key, value);
    }

    public static void putInt(Context context, String table, String key, Integer value) {
        SharedPreferences sharedPreferences = getSharedPrefs(context, table);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(key, value).apply();
        }
    }

    public static int getInt(Context context, String key, int defVal) {
        return getSharedPrefs(context).getInt(key, defVal);
    }

    public static void putFloat(Context context, String table, String key, Float value) {
        SharedPreferences sharedPreferences = getSharedPrefs(context, table);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putFloat(key, value).apply();
        }
    }

    public static void putFloat(Context context, String key, Float value) {
        putFloat(context, SHARED_PREFS_SHARP, key, value);
    }

    public static void putLong(Context context, String table, String key, Long value) {
        SharedPreferences sharedPreferences = getSharedPrefs(context, table);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putLong(key, value).apply();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public static final String KEYBOARD_HEIGHT = "keyboard_height";
}
