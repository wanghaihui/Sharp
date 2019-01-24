package com.wc.leak.internal;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.Nullable;

public class ForegroundService extends IntentService {

    public ForegroundService(String name, int notificationContentTitleResId) {
        super(name);
        this.notificationContentTitleResId = notificationContentTitleResId;
        notificationId = (int) SystemClock.uptimeMillis();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        onHandleIntentInForeground(intent);
    }


}
