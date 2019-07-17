package com.conquer.sharp.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class KeyboardPanLayout extends RelativeLayout {
    private int mHeight;
    private int mWidth;
    private Rect mRect;
    private int mWidthMeasureSpec;
    private int mHeightMeasureSpec;

    public KeyboardPanLayout(Context context) {
        super(context);
        this.mHeight = 0;
        this.mWidth = 0;
        this.mRect = new Rect();
    }

    public KeyboardPanLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHeight = 0;
        this.mWidth = 0;
        this.mRect = new Rect();
    }


    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.getWindowVisibleDisplayFrame(this.mRect);
        if (this.mWidth == 0 && this.mHeight == 0) {
            this.mWidth = this.getRootView().getWidth();
            this.mHeight = this.getRootView().getHeight();
        }

        int windowHeight = this.mRect.bottom - this.mRect.top;
        if (this.mHeight - windowHeight > this.mHeight / 4) {
            // 键盘弹出，默认用保存的测量值
            super.onMeasure(this.mWidthMeasureSpec, this.mHeightMeasureSpec);
        } else {
            this.mWidthMeasureSpec = widthMeasureSpec;
            this.mHeightMeasureSpec = heightMeasureSpec;
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}