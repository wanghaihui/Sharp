package com.conquer.sharp;

import android.app.Application;

import com.conquer.sharp.api.SharpUIKit;

/**
 * Created by ac on 18/7/16.
 *
 */

public class SharpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        SharpUIKit.init(this);
    }

}
