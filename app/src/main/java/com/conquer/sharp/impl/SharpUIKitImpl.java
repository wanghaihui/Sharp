package com.conquer.sharp.impl;

import android.content.Context;

/**
 * Created by ac on 18/7/16.
 *
 */

public final class SharpUIKitImpl {
    // context
    private static Context context;

    public static void init(Context context) {
        SharpUIKitImpl.context = context.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
