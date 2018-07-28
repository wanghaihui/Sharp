package com.conquer.sharp.danmaku.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.conquer.sharp.R;

/**
 * Created by ac on 18/7/28.
 *
 */

public class AutoPlayRecyclerView extends RecyclerView {

    private WrapContentSnapHelper contentSnapHelper;

    public AutoPlayRecyclerView(Context context) {
        this(context, null);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.AutoPlayRecyclerView);
        final int timeInterval = t.getInt(R.styleable.AutoPlayRecyclerView_timeInterval, WrapContentSnapHelper.TIME_INTERVAL);
        t.recycle();

        contentSnapHelper = new WrapContentSnapHelper(timeInterval);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return true;
    }

    public void start() {
        contentSnapHelper.start();
    }

    public void startNoDelay() {
        contentSnapHelper.startNoDelay();
    }

    public void pause() {
        contentSnapHelper.pause();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        // 设置布局管理器的时候，附着到RecyclerView上
        contentSnapHelper.attachToRecyclerView(this);
    }
}
