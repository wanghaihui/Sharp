package com.conquer.exoplayer.core.scheduler;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a set of device state(一组设备状态) requirements.
 */
public final class Requirements {

    /**
     * Network types.
     * One of {@link #NETWORK_TYPE_NONE}, {@link #NETWORK_TYPE_ANY},
     * {@link #NETWORK_TYPE_UNMETERED}(不按流量计费的), {@link #NETWORK_TYPE_NOT_ROAMING}(不是漫游的)
     * or {@link #NETWORK_TYPE_METERED}(按流量计费的).
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
        NETWORK_TYPE_NONE,
        NETWORK_TYPE_ANY,
        NETWORK_TYPE_UNMETERED,
        NETWORK_TYPE_NOT_ROAMING,
        NETWORK_TYPE_METERED
    })
    public @interface NetworkType {

    }

    /** This job doesn't require network connectivity. */
    public static final int NETWORK_TYPE_NONE = 0;
    /** This job requires network connectivity. */
    public static final int NETWORK_TYPE_ANY = 1;
    /** This job requires network connectivity that is unmetered. */
    public static final int NETWORK_TYPE_UNMETERED = 2;
    /** This job requires network connectivity that is not roaming. */
    public static final int NETWORK_TYPE_NOT_ROAMING = 3;
    /** This job requires metered connectivity such as most cellular data networks(大多数蜂窝数据网络). */
    public static final int NETWORK_TYPE_METERED = 4;

    /** This job requires the device to be idle(设备处于空闲状态). */
    private static final int DEVICE_IDLE = 8;
    /** This job requires the device to be charging(设备充电). */
    private static final int DEVICE_CHARGING = 16;

    private static final int NETWORK_TYPE_MASK = 7;

    private static final String TAG = "Requirements";

    private static final String[] NETWORK_TYPE_STRINGS;

    static {
        if (Scheduler.DEBUG) {
            NETWORK_TYPE_STRINGS = new String[] {
                "NETWORK_TYPE_NONE",
                "NETWORK_TYPE_ANY",
                "NETWORK_TYPE_UNMETERED",
                "NETWORK_TYPE_NOT_ROAMING",
                "NETWORK_TYPE_METERED"
            };
        } else {
            NETWORK_TYPE_STRINGS = null;
        }
    }

    private final int requirements;

    /**
     * @param networkType Required network type.
     * @param charging Whether the device should be charging.
     * @param idle Whether the device should be idle.
     */
    public Requirements(@NetworkType int networkType, boolean charging, boolean idle) {
        this(networkType | (charging ? DEVICE_CHARGING : 0) | (idle ? DEVICE_IDLE : 0));
    }

    /** @param requirementsData The value returned by {@link #getRequirementsData()}. */
    public Requirements(int requirementsData) {
        this.requirements = requirementsData;
    }

    /** Returns required network type. */
    public int getRequiredNetworkType() {
        return requirements & NETWORK_TYPE_MASK;
    }

    /** Returns whether the device should be charging. */
    public boolean isChargingRequired() {
        return (requirements & DEVICE_CHARGING) != 0;
    }

    /** Returns whether the device should be idle. */
    public boolean isIdleRequired() {
        return (requirements & DEVICE_IDLE) != 0;
    }

    /**
     * Returns whether the requirements are met.
     *
     * @param context Any context.
     */
    public boolean checkRequirements(Context context) {
        return checkNetworkRequirements(context)
                && checkChargingRequirement(context)
                && checkIdleRequirement(context);
    }

    /** Returns the encoded requirements data which can be used with {@link #Requirements(int)}. */
    public int getRequirementsData() {
        return requirements;
    }


}
