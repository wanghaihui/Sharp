package com.conquer.sharp.util;

import android.util.Log;

public class LogHelper {
    private static boolean DEBUG = false;
    private static final String TAG = "Sharp";

    public static void d(String subTag, String msg) {
        if (DEBUG) {
            Log.d(TAG, "[" + subTag + "]: " + msg);
        }
    }

    public static void i(String subTag, String msg) {
        if (DEBUG) {
            Log.i(TAG, "[" + subTag + "]: " + msg);
        }
    }

    public static void e(String subTag, String msg) {
        if (DEBUG) {
            Log.e(TAG, "[" + subTag + "]: " + msg);
        }
    }

    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }
}
