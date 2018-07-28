package com.conquer.sharp.danmaku.recycler;

import android.animation.Animator;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by ac on 18/7/28.
 *
 */

public class WrapContentSnapHelper extends BaseSnapHelper {

    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;

    public final static int TIME_INTERVAL = 1000;
    private static final int START_POSITION = 1;

    private int timeInterval;

    private Handler handler;

    private Runnable autoPlayRunnable;
    private boolean runnableAdded;

    // 起始位置
    private int mCurrentPosition = START_POSITION;

    WrapContentSnapHelper(int timeInterval) {
        checkTimeInterval(timeInterval);
        handler = new Handler(Looper.getMainLooper());
        this.timeInterval = timeInterval;
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        if (mRecyclerView == recyclerView) {
            return;
        }

        if (mRecyclerView != null) {
            destroyCallbacks();
        }

        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();

            setupCallbacks();
            autoPlayRunnable = new Runnable() {
                @Override
                public void run() {
                    final int currentPosition = mCurrentPosition;
                    if (currentPosition == mRecyclerView.getAdapter().getItemCount() - 1) {
                        mCurrentPosition = START_POSITION;
                        pause();
                    } else {
                        mRecyclerView.smoothScrollToPosition(currentPosition + 1);
                        mCurrentPosition = currentPosition + 1;

                        handler.postDelayed(autoPlayRunnable, timeInterval);
                    }
                }
            };
            startNoDelay();
            runnableAdded = true;
        }
    }

    void start() {
        if (!runnableAdded) {
            handler.postDelayed(autoPlayRunnable, timeInterval);
            runnableAdded = true;
        }
    }

    void startNoDelay() {
        if (!runnableAdded) {
            handler.postDelayed(autoPlayRunnable, 0);
            runnableAdded = true;
        }
    }

    void pause() {
        if (runnableAdded) {
            handler.removeCallbacks(autoPlayRunnable);
            runnableAdded = false;
        }
    }

    public void setTimeInterval(int timeInterval) {
        checkTimeInterval(timeInterval);
        this.timeInterval = timeInterval;
    }
    public int getTimeInterval() {
        return timeInterval;
    }

    private void checkTimeInterval(int timeInterval) {
        if (timeInterval <= 0) {
            throw new IllegalArgumentException("time interval should greater than 0");
        }
    }

    void startAnim(Animator anim) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }
}
