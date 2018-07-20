package com.conquer.sharp.danmaku.internal;

import android.animation.Animator;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.conquer.sharp.recycler.difficult.animation.AlphaInAnimation;
import com.conquer.sharp.recycler.difficult.animation.AlphaOutAnimation;

/**
 * Created by ac on 18/7/20.
 *
 */

public class AutoPlayKSongSnapHelper extends AutoPlaySnapHelper {

    AutoPlayKSongSnapHelper(int timeInterval, int direction) {
        super(timeInterval, direction);
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

                    }
                }
            };
            startNoDelay();
            runnableAdded = true;
        }
    }
}
