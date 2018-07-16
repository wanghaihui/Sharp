package com.conquer.sharp.ptr;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.content.res.ColorStateList;

/**
 * Created by ac on 18/7/16.
 *
 */

public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

    public LoadingLayout(Context context) {
        super(context);
    }

    protected abstract void hideAllViews();

    protected abstract void onPull(float scaleOfLayout);

    protected abstract void pullToRefresh();

    protected abstract void refreshing();

    protected abstract void releaseToRefresh();

    protected abstract void reset();

    protected abstract void showInvisibleViews();

    protected abstract int getContentSize();

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        requestLayout();
    }


    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public abstract void setTextColor(ColorStateList color);
}
