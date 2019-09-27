package com.conquer.sharp;

import android.app.Application;

import com.conquer.sharp.agora.live.WorkerThread;
import com.conquer.sharp.api.SharpUIKit;
import com.conquer.sharp.util.LogHelper;

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
        if (BuildConfig.DEBUG) {
            LogHelper.setDebug(true);
        }
    }

    private WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public synchronized void deInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

}
