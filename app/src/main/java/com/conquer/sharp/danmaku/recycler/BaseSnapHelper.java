package com.conquer.sharp.danmaku.recycler;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ac on 18/7/28.
 *
 */
public class BaseSnapHelper extends RecyclerView.OnFlingListener {

    RecyclerView mRecyclerView;

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        return false;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {

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
