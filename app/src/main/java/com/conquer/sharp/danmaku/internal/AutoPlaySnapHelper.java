package com.conquer.sharp.danmaku.internal;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/**
 * Created by ac on 18/7/18.
 *
 */

public class AutoPlaySnapHelper extends BaseSnapHelper {
    private static final String TAG = "AutoPlaySnapHelper";

    final static int TIME_INTERVAL = 1000;

    final static int LEFT = 1;
    final static int RIGHT = 2;
    final static int TOP = 3;
    final static int BOTTOM = 4;

    private int timeInterval;
    private int direction;

    private Handler handler;

    private Runnable autoPlayRunnable;
    private boolean runnableAdded;

    AutoPlaySnapHelper(int timeInterval, int direction) {
        checkTimeInterval(timeInterval);
        checkDirection(direction);

        handler = new Handler(Looper.getMainLooper());

        this.timeInterval = timeInterval;
        this.direction = direction;
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        return false;
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
            if (!(layoutManager instanceof ViewPagerLayoutManager)) {
                return;
            }

            setupCallbacks();
            mGravityScroller = new Scroller(mRecyclerView.getContext(), new DecelerateInterpolator());

            autoPlayRunnable = new Runnable() {
                @Override
                public void run() {
                    final int currentPosition = ((ViewPagerLayoutManager) layoutManager).getCurrentPosition();
                    if (currentPosition == mRecyclerView.getAdapter().getItemCount() -1) {
                        pause();
                    } else {
                        mRecyclerView.smoothScrollToPosition(currentPosition + 1);
                        handler.postDelayed(autoPlayRunnable, timeInterval);
                    }
                }
            };
            handler.postDelayed(autoPlayRunnable, timeInterval);
            runnableAdded = true;
        }
    }

    @Override
    void destroyCallbacks() {
        super.destroyCallbacks();
        if (runnableAdded) {
            handler.removeCallbacks(autoPlayRunnable);
            runnableAdded = false;
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

    public void setDirection(int direction) {
        checkDirection(direction);
        this.direction = direction;
    }
    public int getDirection() {
        return direction;
    }

    private void checkTimeInterval(int timeInterval) {
        if (timeInterval <= 0) {
            throw new IllegalArgumentException("time interval should greater than 0");
        }
    }

    private void checkDirection(int direction) {
        if (direction != LEFT && direction != RIGHT && direction != TOP && direction != BOTTOM) {
            throw new IllegalArgumentException("direction should be one of left or right or top or bottom");
        }
    }

}
