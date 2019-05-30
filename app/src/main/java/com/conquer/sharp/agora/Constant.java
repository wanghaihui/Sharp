package com.conquer.sharp.agora;

import io.agora.rtc.RtcEngine;

public class Constant {
    public static final String MEDIA_SDK_VERSION;

    static {
        String sdk = "undefined";
        try {
            sdk = RtcEngine.getSdkVersion();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            MEDIA_SDK_VERSION = sdk;
        }
    }

    // public static final String AGORA_APP_ID = "0de7a0c3c3fa4bfaafb0094b818764f3";

    public static final String AGORA_APP_ID = "52c9afa2ac4f40ab8132448a170f6f5e";

    public static final String LOG_TAG = "Agora";

    public static final String ACTION_KEY_ROOM_NAME = "ecHANEL";
    public static final String ACTION_KEY_UID = "uid";
    public static final String ACTION_KEY_TOKEN = "token";
}
