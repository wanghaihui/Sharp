package com.conquer.sharp.ptr;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;

import com.conquer.sharp.R;
import com.conquer.sharp.ptr.custom.LoadMoreLayout;
import com.conquer.sharp.ptr.custom.donut.DonutLayout;

/**
 * Created by ac on 18/7/16.
 *
 */

public class PullToRefreshLayout extends SuperSwipeRefreshLayout {

    public static final int HEADER_VIEW_HEIGHT = 48;

    private DonutLayout loadingLayoutDown;
    private LoadMoreLayout loadingLayoutUp;

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshLayout);
        boolean pullUp = ta.getBoolean(R.styleable.PullToRefreshLayout_pullUp, true);
        ta.recycle();
        initLoadingView(true, pullUp);
    }

    // 一般用于进页面第一次刷新
    public void autoRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                targetToTop();
                setRefreshing(true);
                if (loadingLayoutDown != null) {
                    loadingLayoutDown.refreshing();
                }
                if (onRefreshListener != null) {
                    onRefreshListener.onPullDownToRefresh();
                }
            }
        }, 100);
    }

    private void initLoadingView(boolean pullDown, boolean pullUp) {
        if (pullDown) {
            loadingLayoutDown = new DonutLayout(getContext());
            setHeaderView(loadingLayoutDown);
            setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {

                @Override
                public void onRefresh() {
                    loadingLayoutDown.refreshing();
                    if (onRefreshListener != null) {
                        onRefreshListener.onPullDownToRefresh();
                    }
                }

                @Override
                public void onPullDistance(int distance) {
                    if (distance == 0) {
                        loadingLayoutDown.reset();
                    }
                    loadingLayoutDown.onPull(distance * 1.0f / loadingLayoutDown.getContentSize());
                }

                @Override
                public void onPullEnable(boolean enable) {

                }
            });
        }

        if (pullUp) {
            loadingLayoutUp = new LoadMoreLayout(getContext());
            setFooterView(loadingLayoutUp);
            setOnPushLoadMoreListener(new OnPushLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    loadingLayoutUp.refreshing();
                    if (onRefreshListener != null) {
                        onRefreshListener.onPullUpToRefresh();
                    }
                }

                @Override
                public void onPushDistance(int distance) {
                    if (distance == 0) {
                        loadingLayoutUp.reset();
                    }
                    loadingLayoutUp.onPull(distance * 1.0f / loadingLayoutUp.getContentSize());
                }

                @Override
                public void onPushEnable(boolean enable) {

                }
            });
        } else {
            // 设置禁用上拉加载
            setPullUpUnable();
        }
    }

    public interface OnRefreshListener {
        void onPullDownToRefresh();
        void onPullUpToRefresh();
    }
}
