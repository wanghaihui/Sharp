package com.conquer.sharp.danmaku.recycler;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

/**
 * Created by ac on 18/7/28.
 *
 */

public class SmoothLinearLayoutManger extends LinearLayoutManager {

    private float MILLISECONDS_PER_INCH = 0.03f;
    private Context context;

    public SmoothLinearLayoutManger(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override
                    public PointF computeScrollVectorForPosition(int targetPosition) {
                        return SmoothLinearLayoutManger.this.computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        // 返回滑动一个pixel需要多少毫秒
                        return MILLISECONDS_PER_INCH / displayMetrics.density;

                    }

                };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }


    public void setSpeedSlow() {
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 1.2f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = context.getResources().getDisplayMetrics().density * 0.03f;
    }
}
