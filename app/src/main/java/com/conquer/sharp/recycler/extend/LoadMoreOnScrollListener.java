package com.conquer.sharp.recycler.extend;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class LoadMoreOnScrollListener extends RecyclerView.OnScrollListener implements OnLoadMoreListener {
    /**
     * 当前RecyclerView类型
     */
    protected LayoutManagerType layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;

    /**
     * 当前滑动的状态
     */
    private int currentScrollState = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LinearLayout:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                break;
            case GridLayout:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        currentScrollState = newState;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        if (visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE) {
            // 处理item高度超过一屏幕时的情况
            View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
            if (lastVisibleChild != null && lastVisibleChild.getMeasuredHeight() >= recyclerView.getMeasuredHeight()) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    if (!(ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < 0)) {
                        onLoadMore(recyclerView);
                    }
                } else {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        onLoadMore(recyclerView);
                    }
                }
            }
        }

        if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1)) {
            onLoadMore(recyclerView);
        }
    }

    /**
     * 取数组中最大值
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    @Override
    public void onLoadMore(final View view) {

    }

    public enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }
}
