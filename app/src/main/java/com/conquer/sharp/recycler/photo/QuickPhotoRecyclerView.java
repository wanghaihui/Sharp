package com.conquer.sharp.recycler.photo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.conquer.sharp.base.glide.GlideApp;

public class QuickPhotoRecyclerView extends RecyclerView {

    private boolean mIsScrolling = false;

    public QuickPhotoRecyclerView(Context context) {
        super(context);
    }

    public QuickPhotoRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickPhotoRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        // 优化: 在滑动时停止加载图片，在滑动停止时开始加载图片
        if (state == RecyclerView.SCROLL_STATE_DRAGGING || state == RecyclerView.SCROLL_STATE_SETTLING) {
            // SCROLL_STATE_DRAGGING--上升
            // SCROLL_STATE_SETTLING--下落
            mIsScrolling = true;
            GlideApp.with(getContext()).pauseRequests();
        } else if (state == RecyclerView.SCROLL_STATE_IDLE) {
            // SCROLL_STATE_IDLE--静止
            if (mIsScrolling) {
                GlideApp.with(getContext()).resumeRequests();
            }
            mIsScrolling = false;
        }
    }
}
