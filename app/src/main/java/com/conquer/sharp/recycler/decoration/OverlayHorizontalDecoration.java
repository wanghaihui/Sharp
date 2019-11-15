package com.conquer.sharp.recycler.decoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作用：布局间隔
 */
public class OverlayHorizontalDecoration extends RecyclerView.ItemDecoration {
    public static final int ORIENTATION_LEFT = 1;
    public static final int ORIENTATION_RIGHT = 2;

    private int mHorizontalSpacing;
    private int orientation;

    public OverlayHorizontalDecoration(int hSpacing, int orientation) {
        mHorizontalSpacing = hSpacing;
        this.orientation = orientation;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (orientation == ORIENTATION_LEFT) {
                if (parent.getChildCount() == 1) {
                    outRect.left = 0;
                    outRect.right = 0;
                } else {
                    if (position == 0) {
                        outRect.left = mHorizontalSpacing;
                        outRect.right = 0;
                    } else {
                        if (position == parent.getChildCount() - 1) {
                            outRect.left = 0;
                            outRect.right = mHorizontalSpacing;
                        } else {
                            outRect.left = mHorizontalSpacing;
                            outRect.right = 0;
                        }
                    }
                }
            } else if (orientation == ORIENTATION_RIGHT) {
                if (parent.getChildCount() == 1) {
                    outRect.left = 0;
                    outRect.right = 0;
                } else {
                    if (position == 0) {
                        outRect.left = 0;
                        outRect.right = mHorizontalSpacing;
                    } else {
                        if (position == parent.getChildCount() - 1) {
                            outRect.left = mHorizontalSpacing;
                            outRect.right = 0;
                        } else {
                            outRect.left = 0;
                            outRect.right = mHorizontalSpacing;
                        }
                    }
                }
            }
        }
    }
}
