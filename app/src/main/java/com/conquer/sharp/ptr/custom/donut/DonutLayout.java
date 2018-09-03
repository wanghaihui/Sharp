package com.conquer.sharp.ptr.custom.donut;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.conquer.sharp.R;
import com.conquer.sharp.ptr.PullToRefreshLayout;
import com.conquer.sharp.ptr.custom.LoadingLayout;
import com.conquer.sharp.util.system.ScreenUtil;

public class DonutLayout extends LoadingLayout {

    private DonutProgress mPrepareLoading;
    private ProgressBar mLoading;

    public DonutLayout(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.ptr_pull_to_refresh_header_donut, this);
        FrameLayout mInnerLayout = findViewById(R.id.ptr_inner);
        mPrepareLoading = mInnerLayout.findViewById(R.id.ptr_custom_loading);
        mLoading = mInnerLayout.findViewById(R.id.pull_to_loading_progress);
    }

    @Override
    public void hideAllViews() {

    }

    @Override
    public void onPull(float scaleOfLayout) {
        Log.d("haihui", "scale layout: " + scaleOfLayout);
        mPrepareLoading.setProgress((int) (scaleOfLayout >= 1 ? 100 : scaleOfLayout * 100));
    }

    @Override
    public void pullToRefresh() {

    }

    @Override
    public void refreshing() {
        mPrepareLoading.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void releaseToRefresh() {

    }

    @Override
    public void reset() {
        mPrepareLoading.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        mPrepareLoading.setProgress(0);
    }

    @Override
    public void showInvisibleViews() {

    }

    @Override
    public int getContentSize() {
        return ScreenUtil.dip2px(PullToRefreshLayout.HEADER_VIEW_HEIGHT);
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
