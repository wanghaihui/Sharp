package com.conquer.sharp.ptr.custom.ball;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.conquer.sharp.ptr.SuperSwipeRefreshLayout;

/**
 * Created by ac on 18/7/16.
 *
 */

public class PullToRefreshBallLayout extends SuperSwipeRefreshLayout {

    private BallLoadingLayout loadingLayoutDown;
    private BallLoadingLayout loadingLayoutUp;
    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public PullToRefreshBallLayout(Context context) {
        super(context);
        initLoadingView(true, true);
    }

    public PullToRefreshBallLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLoadingView(true, true);
    }

    //一般用于进页面第一次刷新
    public void autoRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(true);
                loadingLayoutDown.refreshing();
                if (onRefreshListener != null) {
                    onRefreshListener.onPullDownToRefresh();
                }
            }
        }, 100);
    }

    private void initLoadingView(boolean pullDown, boolean pullUp) {
        if (pullDown) {
            loadingLayoutDown = new BallLoadingLayout(getContext());
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
            loadingLayoutUp = new BallLoadingLayout(getContext());
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
        }
    }

    public interface OnRefreshListener {
        void onPullDownToRefresh();
        void onPullUpToRefresh();
    }
}
