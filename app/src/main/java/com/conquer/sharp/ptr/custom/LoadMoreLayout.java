package com.conquer.sharp.ptr.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.conquer.sharp.R;
import com.conquer.sharp.util.ScreenUtils;

/**
 * Created by ac on 18/7/16.
 *
 */

public class LoadMoreLayout extends LoadingLayout {

    private ProgressBar mLoadMoreProgress;
    private TextView mEndView;

    public LoadMoreLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.ptr_pull_to_refresh_header_custom, this);

        setLayoutParams(new ViewGroup.LayoutParams(ScreenUtils.screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

        mLoadMoreProgress = view.findViewById(R.id.load_more_progress);
        mEndView = view.findViewById(R.id.bottom_end);
    }

    @Override
    public void hideAllViews() {

    }

    @Override
    public void onPull(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefresh() {

    }

    @Override
    public void refreshing() {

    }

    @Override
    protected void releaseToRefresh() {

    }

    @Override
    public void reset() {

    }

    @Override
    protected void showInvisibleViews() {

    }

    @Override
    public int getContentSize() {
        return ScreenUtils.screenWidth;
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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // For RecyclerView LoadMore
    protected State mState = State.Normal;

    public State getState() {
        return mState;
    }

    public void setState(State status) {
        setState(status, true);
    }

    public void setState(State status, boolean showView) {
        if (mState == status) {
            return;
        }
        mState = status;

        switch (status) {
            case Normal:
                setOnClickListener(null);
                setVisibility(GONE);
                break;
            case Loading:
                setOnClickListener(null);
                setVisibility(VISIBLE);
                mLoadMoreProgress.setVisibility(View.VISIBLE);
                mEndView.setVisibility(View.GONE);
                break;
            case TheEnd:
                setOnClickListener(null);
                setVisibility(VISIBLE);
                mLoadMoreProgress.setVisibility(View.GONE);
                mEndView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public enum State {
        // 正常
        Normal,
        // 加载到最后
        TheEnd,
        // 加载中
        Loading
    }
}
