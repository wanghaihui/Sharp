package com.wc.leak;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.wc.leak.watcher.RefWatcher;

public final class LeakCanary {

    /**
     * Creates a {@link RefWatcher} that works out of the box, and starts watching activity
     * references (on ICS+).
     */
    public static @NonNull
    RefWatcher install(@NonNull Application application) {
        return refWatcher(application).listenerServiceClass(DisplayLeakService.class)
                .excludedRefs(AndroidExcludedRefs.createAppDefaults().build())
                .buildAndInstall();
    }

    public static @NonNull AndroidRefWatcherBuilder refWatcher(@NonNull Context context) {
        return new AndroidRefWatcherBuilder(context);
    }
}
