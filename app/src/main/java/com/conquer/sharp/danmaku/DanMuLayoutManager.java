package com.conquer.sharp.danmaku;

import android.content.Context;
import android.view.View;

import com.conquer.sharp.danmaku.internal.ViewPagerLayoutManager;

/**
 * Created by ac on 18/7/18.
 *
 */

public class DanMuLayoutManager extends ViewPagerLayoutManager {

    public DanMuLayoutManager(Context context) {
        super(context);
    }

    public DanMuLayoutManager(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    protected float setInterval() {
        return mDecoratedMeasurement;
    }

    @Override
    protected void setItemViewProperty(View itemView, float targetOffset) {

    }

}
