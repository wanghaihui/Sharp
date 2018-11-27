package com.conquer.sharp.photo.pager;

import android.content.Context;
import android.view.View;

import com.conquer.sharp.photo.pager.internal.ViewPagerLayoutManager;
import com.conquer.sharp.util.system.ScreenUtils;

/**
 * Created by ac on 18/7/18.
 *
 */

public class DanMuLayoutManager extends ViewPagerLayoutManager {
    private static final String TAG = "DanMuLayoutManager";

    private static final int MAX_COUNT = 3;

    public DanMuLayoutManager(Context context) {
        super(context);
    }

    public DanMuLayoutManager(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    protected float setInterval() {
        return mDecoratedMeasurement + ScreenUtils.dip2px(4);
    }

    @Override
    protected void setItemViewProperty(View itemView, float targetOffset) {

    }

    protected int calItemLeft(float targetOffset) {
        return mOrientation == VERTICAL ? ScreenUtils.dip2px(16) : (int) targetOffset;
    }

    @Override
    protected void layoutScrap(View scrap, float targetOffset, int adapterPosition) {
        final int left = calItemLeft(targetOffset);
        final int top = calItemTop(targetOffset);

        if (mOrientation == VERTICAL) {
            // old version
            /*layoutDecorated(scrap, mSpaceInOther + left, mSpaceMain + top,
                    mSpaceInOther + left + mDecoratedMeasurementInOther, mSpaceMain + top + mDecoratedMeasurement);*/

            // understand version -- 天天K歌 version
            // layoutDecorated(scrap, left, top, left + scrap.getMeasuredWidth(), top + mDecoratedMeasurement);

            // 底部一个开始弹 version
            int startTop = (MAX_COUNT - 1) * mDecoratedMeasurement;
            layoutDecorated(scrap, left, startTop + top, left + scrap.getMeasuredWidth(), startTop + top + mDecoratedMeasurement);
            // layoutDecorated(scrap, left, ScreenUtils.dip2px(120) - scrap.getMeasuredHeight() + top, left + scrap.getMeasuredWidth(), ScreenUtils.dip2px(120));

            // 底部两个开始弹 version
            /*int startTop = mDecoratedMeasurement;
            layoutDecorated(scrap, left, startTop + top, left + scrap.getMeasuredWidth(), startTop + top + mDecoratedMeasurement);*/
        }
        setItemViewProperty(scrap, targetOffset);
    }


}
