package com.conquer.sharp.danmaku.internal;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.Scroller;

/**
 * Created by ac on 18/7/18.
 *
 */

public class BaseSnapHelper extends RecyclerView.OnFlingListener {

    RecyclerView mRecyclerView;
    Scroller mGravityScroller;

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        ViewPagerLayoutManager layoutManager = (ViewPagerLayoutManager) mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            return false;
        }

        if (!layoutManager.getInfinite() &&
                (layoutManager.mOffset == layoutManager.getMaxOffset()
                        || layoutManager.mOffset == layoutManager.getMinOffset())) {
            return false;
        }

        final int minFlingVelocity = mRecyclerView.getMinFlingVelocity();
        mGravityScroller.fling(0, 0, velocityX, velocityY,
                Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);

        if (layoutManager.mOrientation == ViewPagerLayoutManager.VERTICAL
                && Math.abs(velocityY) > minFlingVelocity) {
            final int currentPosition = layoutManager.getCurrentPosition();
            final int offsetPosition = (int) (mGravityScroller.getFinalY() /
                    layoutManager.mInterval / layoutManager.getDistanceRatio());
            mRecyclerView.smoothScrollToPosition(layoutManager.getReverseLayout() ?
                    currentPosition - offsetPosition : currentPosition + offsetPosition);
            return true;
        } else if (layoutManager.mOrientation == ViewPagerLayoutManager.HORIZONTAL
                && Math.abs(velocityX) > minFlingVelocity) {
            final int currentPosition = layoutManager.getCurrentPosition();
            final int offsetPosition = (int) (mGravityScroller.getFinalX() /
                    layoutManager.mInterval / layoutManager.getDistanceRatio());
            mRecyclerView.smoothScrollToPosition(layoutManager.getReverseLayout() ?
                    currentPosition - offsetPosition : currentPosition + offsetPosition);
            return true;
        }

        return true;
    }

    /**
     * Please attach after {{@link android.support.v7.widget.RecyclerView.LayoutManager} is setting}
     * Attaches the {@link BaseSnapHelper} to the provided RecyclerView, by calling
     * {@link RecyclerView#setOnFlingListener(RecyclerView.OnFlingListener)}.
     * You can call this method with {@code null} to detach it from the current RecyclerView.
     *
     * @param recyclerView The RecyclerView instance to which you want to add this helper or
     *                     {@code null} if you want to remove CenterSnapHelper from the current
     *                     RecyclerView.
     * @throws IllegalArgumentException if there is already a {@link RecyclerView.OnFlingListener}
     *                                  attached to the provided {@link RecyclerView}.
     */
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {

    }

    void snapToCenterView(ViewPagerLayoutManager layoutManager, ViewPagerLayoutManager.OnPageChangeListener listener) {
        final int delta = layoutManager.getOffsetToCenter();
        if (delta != 0) {
            if (layoutManager.getOrientation() == ViewPagerLayoutManager.VERTICAL) {
                mRecyclerView.smoothScrollBy(0, delta);
            } else {
                mRecyclerView.smoothScrollBy(delta, 0);
            }
        }

        if (listener != null) {
            listener.onPageSelected(layoutManager.getCurrentPosition());
        }
    }

    void setupCallbacks() throws IllegalStateException {
        if (mRecyclerView.getOnFlingListener() != null) {
            throw new IllegalStateException("An instance of OnFlingListener already set.");
        }
        mRecyclerView.setOnFlingListener(this);
    }

    void destroyCallbacks() {
        mRecyclerView.setOnFlingListener(null);
    }
}
