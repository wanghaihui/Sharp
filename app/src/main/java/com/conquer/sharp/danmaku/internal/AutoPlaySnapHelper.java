package com.conquer.sharp.danmaku.internal;

import android.animation.Animator;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import com.conquer.sharp.recycler.difficult.animation.AlphaInAnimation;
import com.conquer.sharp.recycler.difficult.animation.AlphaOutAnimation;

/**
 * Created by ac on 18/7/18.
 *
 */

public class AutoPlaySnapHelper extends BaseSnapHelper {

    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;

    final static int TIME_INTERVAL = 1000;

    final static int LEFT = 1;
    final static int RIGHT = 2;
    final static int TOP = 3;
    final static int BOTTOM = 4;

    final static int NORMAL = 1;
    final static int K_SONG = 2;

    int timeInterval;
    private int direction;

    Handler handler;

    Runnable autoPlayRunnable;
    boolean runnableAdded;

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
                        // old version
                        mRecyclerView.smoothScrollToPosition(currentPosition + 1);

                        // 可以扩展--是否支持动画，以及支持怎样的动画
                        // 此时过去View做渐隐动画
                        if (currentPosition >= 2) {
                            View beforeView = layoutManager.findViewByPosition(currentPosition - 2);
                            AlphaOutAnimation animation = new AlphaOutAnimation();
                            for (Animator anim : animation.getAnimators(beforeView)) {
                                startAnim(anim);
                            }
                        }
                        // 此时未来View做渐入动画
                        View nextView = layoutManager.findViewByPosition(currentPosition + 1);
                        AlphaInAnimation animation = new AlphaInAnimation();
                        for (Animator anim : animation.getAnimators(nextView)) {
                            startAnim(anim);
                        }

                        handler.postDelayed(autoPlayRunnable, timeInterval);
                    }
                }
            };
            startNoDelay();
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

    void startAnim(Animator anim) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }

}
