package com.conquer.sharp.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;

import java.util.Map;

public class LocalPreferenceHelper {
    // 缓存String格式
    private static Map<String, String> sharedData = new ArrayMap<>();

    public synchronized static SharedPreferences getInstance(Context context) {
        if (context != null) {
            // 以包名为名
            return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
        return null;
    }

    public synchronized static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public synchronized static void putInt(Context context, String key, int value) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public synchronized static void putLong(Context context, String key, long value) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public synchronized static void putString(Context context, String key, String value) {
        SharedPreferences sp = getInstance(context);
        sharedData.put(key, value);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public synchronized static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public synchronized static String getString(Context context, String key, String value) {
        SharedPreferences sp = getInstance(context);
        if (sharedData.containsKey(key)) {
            return sharedData.get(key);
        }

        if (sp != null) {
            return sp.getString(key, value);
        }

        return value;
    }

    public synchronized static long getInt(Context context, String key, int value) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            return sp.getInt(key, value);
        }

        return 0;
    }

    public synchronized static long getLong(Context context, String key, long value) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            return sp.getLong(key, value);
        }

        return 0;
    }

    public synchronized static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    public synchronized static boolean getBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            return sp.getBoolean(key, value);
        }

        return false;
    }

    public synchronized static void putFloat(Context context, String key, float value) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(key, value);
            editor.apply();
        }
    }

    public synchronized static void remove(Context context, String key) {
        SharedPreferences sp = getInstance(context);
        if (sp != null) {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.apply();

            if (sharedData.containsKey(key)) {
                sharedData.remove(key);
            }
        }
    }

    public synchronized static boolean contains(Context context, String key) {
        SharedPreferences sp = getInstance(context);
        return sp != null && sp.contains(key);
    }
}
