package com.conquer.sharp.ptr;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.conquer.sharp.R;

/**
 * Created by ac on 18/7/16.
 *
 */

public class PullToRefreshLayout extends SuperSwipeRefreshLayout {

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        initLoadingView(true, true);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLoadingView(true, true);
    }

    // 一般用于进页面第一次刷新
    public void autoRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                if (onRefreshListener != null) {
                    onRefreshListener.onPullDownToRefresh();
                }
            }
        }, 100);
    }

    private void initLoadingView(boolean pullDown, boolean pullUp) {
        if (pullDown) {
            setDefaultCircleProgressColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
            setDefaultCircleBackgroundColor(ContextCompat.getColor(getContext(), R.color.red_light));
            setDefaultCircleShadowColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
            setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

                @Override
                public void onRefresh() {
                    if (onRefreshListener != null) {
                        onRefreshListener.onPullDownToRefresh();
                    }
                }

                @Override
                public void onPullDistance(int distance) {

                }

                @Override
                public void onPullEnable(boolean enable) {

                }
            });
        }

        if (pullUp) {
            setOnPushLoadMoreListener(new OnPushLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (onRefreshListener != null) {
                        onRefreshListener.onPullUpToRefresh();
                    }
                }

                @Override
                public void onPushDistance(int distance) {

                }

                @Override
                public void onPushEnable(boolean enable) {

                }
            });
        }
    }

    public interface OnRefreshListener {
        void onPullDownToRefresh();
        void onPullUpToRefresh();
    }
}
