package com.conquer.sharp.recycler.decoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class SpacingDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    // 边距
    private boolean mIncludeEdge;

    public SpacingDecoration(int hSpacing, int vSpacing, boolean includeEdge) {
        mHorizontalSpacing = hSpacing;
        mVerticalSpacing = vSpacing;
        mIncludeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // Only handle the vertical situation
        int position = parent.getChildAdapterPosition(view);

        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            // 列数
            int spanCount = layoutManager.getSpanCount();
            // 列
            int column = position % spanCount;
            getGridItemOffsets(outRect, position, column, spanCount);
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
            int spanCount = layoutManager.getSpanCount();
            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int column = lp.getSpanIndex();
            getGridItemOffsets(outRect, position, column, spanCount);
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            // 这个很好理解
            outRect.left = mHorizontalSpacing;
            outRect.right = mHorizontalSpacing;
            if (mIncludeEdge) {
                if (position == 0) {
                    // 顶部边距为0
                    outRect.top = mVerticalSpacing;
                }
                outRect.bottom = mVerticalSpacing;
            } else {
                if (position > 0) {
                    outRect.top = mVerticalSpacing;
                }
            }
        }
    }

    private void getGridItemOffsets(Rect outRect, int position, int column, int spanCount) {
        if (mIncludeEdge) {
            outRect.left = mHorizontalSpacing * (spanCount - column) / spanCount;
            outRect.right = mHorizontalSpacing * (column + 1) / spanCount;
            if (position < spanCount) {
                outRect.top = mVerticalSpacing;
            }
            outRect.bottom = mVerticalSpacing;
        } else {
            outRect.left = mHorizontalSpacing * column / spanCount;
            outRect.right = mHorizontalSpacing * (spanCount - 1 - column) / spanCount;
            if (position >= spanCount) {
                outRect.top = mVerticalSpacing;
            }
        }
    }
}
