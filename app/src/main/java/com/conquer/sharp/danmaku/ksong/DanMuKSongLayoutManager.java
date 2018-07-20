package com.conquer.sharp.danmaku.ksong;

import android.content.Context;
import android.view.View;

import com.conquer.sharp.danmaku.internal.ViewPagerLayoutManager;
import com.conquer.sharp.util.system.ScreenUtil;

/**
 * Created by ac on 18/7/20.
 *
 */

public class DanMuKSongLayoutManager extends ViewPagerLayoutManager {

    private static final int MAX_COUNT = 3;

    public DanMuKSongLayoutManager(Context context) {
        super(context);
    }

    public DanMuKSongLayoutManager(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    protected float setInterval() {
        return mDecoratedMeasurement;
    }

    @Override
    protected void setItemViewProperty(View itemView, float targetOffset) {

    }

    protected int calItemLeft(float targetOffset) {
        return mOrientation == VERTICAL ? ScreenUtil.dip2px(16) : (int) targetOffset;
    }

    @Override
    protected void layoutScrap(View scrap, float targetOffset) {
        final int left = calItemLeft(targetOffset);
        final int top = calItemTop(targetOffset);

        if (mOrientation == VERTICAL) {
            // old version
            /*layoutDecorated(scrap, mSpaceInOther + left, mSpaceMain + top,
                    mSpaceInOther + left + mDecoratedMeasurementInOther, mSpaceMain + top + mDecoratedMeasurement);*/

            // understand version -- 天天K歌 version
            layoutDecorated(scrap, left, top, left + scrap.getMeasuredWidth(), top + mDecoratedMeasurement);

            // 底部一个开始弹 version
            /*int startTop = (MAX_COUNT - 1) * mDecoratedMeasurement;
            layoutDecorated(scrap, left, startTop + top, left + scrap.getMeasuredWidth(), startTop + top + mDecoratedMeasurement);*/

            // 底部两个开始弹 version
            /*int startTop = mDecoratedMeasurement;
            layoutDecorated(scrap, left, startTop + top, left + scrap.getMeasuredWidth(), startTop + top + mDecoratedMeasurement);*/
        }
        setItemViewProperty(scrap, targetOffset);
    }
}
