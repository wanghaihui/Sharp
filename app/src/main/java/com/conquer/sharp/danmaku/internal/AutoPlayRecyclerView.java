package com.conquer.sharp.danmaku.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.conquer.sharp.R;

/**
 * Created by ac on 18/7/18.
 *
 */

public class AutoPlayRecyclerView extends RecyclerView {
    private AutoPlaySnapHelper autoPlaySnapHelper;

    public AutoPlayRecyclerView(Context context) {
        this(context, null);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoPlayRecyclerView);
        final int timeInterval = t.getInt(R.styleable.AutoPlayRecyclerView_timeInterval, AutoPlaySnapHelper.TIME_INTERVAL);
        final int direction = t.getInt(R.styleable.AutoPlayRecyclerView_direction, AutoPlaySnapHelper.BOTTOM);
        final int snapHelper = t.getInt(R.styleable.AutoPlayRecyclerView_snapHelper, AutoPlaySnapHelper.NORMAL);
        t.recycle();

        if (snapHelper == AutoPlaySnapHelper.NORMAL) {
            autoPlaySnapHelper = new AutoPlaySnapHelper(timeInterval, direction);
        }
        if (snapHelper == AutoPlaySnapHelper.K_SONG) {
            autoPlaySnapHelper = new AutoPlayKSongSnapHelper(timeInterval, direction);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
    }

    public void start() {
        autoPlaySnapHelper.start();
    }

    public void startNoDelay() {
        autoPlaySnapHelper.startNoDelay();
    }

    public void pause() {
        autoPlaySnapHelper.pause();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        // 设置布局管理器的时候，附着到RecyclerView上
        autoPlaySnapHelper.attachToRecyclerView(this);
    }
}
