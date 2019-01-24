package com.conquer.exoplayer.core.scheduler;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;

/** Schedules(调度) a service to be started in the foreground(在前台) when some {@link Requirements} are met. */
public interface Scheduler {

    boolean DEBUG = false;

    /**
     * Schedules a service to be started in the foreground when some {@link Requirements} are met.
     * Anything that was previously scheduled will be canceled.
     *
     * <p>The service to be started must be declared in the manifest of {@code servicePackage} with an
     * intent filter containing {@code serviceAction}. Note that when started with {@code
     * serviceAction}, the service must call {@link Service#startForeground(int, Notification)} to
     * make itself a foreground service, as documented by {@link
     * Service#startForegroundService(Intent)}.
     *
     * @param requirements The requirements.
     * @param servicePackage The package name.
     * @param serviceAction The action with which the service will be started.
     * @return Whether scheduling was successful.
     */
    boolean schedule(Requirements requirements, String servicePackage, String serviceAction);

    /**
     * Cancels anything that was previously scheduled, or else does nothing.
     *
     * @return Whether cancellation was successful.
     */
    boolean cancel();
}
