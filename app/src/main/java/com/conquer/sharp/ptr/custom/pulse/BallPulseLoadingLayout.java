package com.conquer.sharp.ptr.custom.pulse;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.conquer.sharp.R;
import com.conquer.sharp.ptr.custom.LoadingLayout;
import com.conquer.sharp.util.system.ScreenUtil;

/**
 * Created by ac on 18/7/16.
 *
 */

public class BallPulseLoadingLayout extends LoadingLayout {
    private FrameLayout mInnerLayout;
    private BallPulseLoadingView loadingView;

    public BallPulseLoadingLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.ptr_pull_to_refresh_header_custom, this);
        mInnerLayout = (FrameLayout) findViewById(R.id.ptr_fl_inner);
        loadingView = (BallPulseLoadingView) mInnerLayout.findViewById(R.id.ptr_custom_loading);
    }

    @Override
    public void hideAllViews() {
        if (VISIBLE == loadingView.getVisibility()) {
            loadingView.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onPull(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefresh() {

    }

    @Override
    public void refreshing() {
        loadingView.refresh();
    }

    @Override
    protected void releaseToRefresh() {

    }

    @Override
    public void reset() {
        loadingView.onFinish();
    }

    @Override
    protected void showInvisibleViews() {
        if (INVISIBLE == loadingView.getVisibility()) {
            loadingView.setVisibility(VISIBLE);
        }
    }

    @Override
    public int getContentSize() {
        return ScreenUtil.dip2px(30);
    }


    @Override
    public void setTextColor(ColorStateList color) {

    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {

    }

    @Override
    public void setLoadingDrawable(Drawable drawable) {

    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {

    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {

    }

    @Override
    public void setReleaseLabel(CharSequence releaseLabel) {

    }

    @Override
    public void setTextTypeface(Typeface tf) {

    }
}
