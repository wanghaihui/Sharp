package com.conquer.sharp.danmaku.normal;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by ac on 18/7/25.
 *
 */

public class DanMuQueueView extends ViewGroup {
    private static final String TAG = "DanMuQueueView";

    private int viewHeight = 0;
    private int childViewHeight = 0;

    /**
     * 所有子View
     */
    private List<View> childViews = new ArrayList<>();

    /**
     * 绘制的View栈
     */
    Stack<View> drawChildren = new Stack<>();

    public DanMuQueueView(Context context) {
        this(context, null);
    }

    public DanMuQueueView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();

        int measureTempHeight = 0;
        for (int i = 0; i < count; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
            measureTempHeight = measureTempHeight + getChildAt(i).getMeasuredHeight();
        }
        childViewHeight = measureTempHeight;

        setMeasuredDimension(measureWidthSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        viewHeight = getHeight();

        Log.e(TAG, "childViewHeight = " + childViewHeight);
        Log.e(TAG, "viewHeight = " + viewHeight);
    }

    private int measureWidthSize(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            // 子容器可以是声明大小内的任意大小
            case MeasureSpec.AT_MOST:
                result = specSize;
                break;
            // 父容器已经为子容器设置了尺寸, 子容器应当服从这些边界, 不论子容器想要多大的空间, 比如EditTextView中的DrawLeft
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            // 父容器对于子容器没有任何限制, 子容器想要多大就多大, 所以完全取决于子view的大小
            case MeasureSpec.UNSPECIFIED:
                result = 1600;
                break;
            default:
                break;
        }
        return result * 2 / 3;
    }

    private int measureSize(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            // 子容器可以是声明大小内的任意大小
            case MeasureSpec.AT_MOST:
                result = specSize;
                break;
            // 父容器已经为子容器设置了尺寸, 子容器应当服从这些边界, 不论子容器想要多大的空间, 比如EditTextView中的DrawLeft
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            // 父容器对于子容器没有任何限制, 子容器想要多大就多大, 所以完全取决于子view的大小
            case MeasureSpec.UNSPECIFIED:
                result = 1600;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 布局，设置左右边距--padding
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l = l + getPaddingLeft();
        r = r - getPaddingRight();
        t = t + getPaddingTop();
        b = b - getPaddingBottom();

        drawChildren.clear();
        for (View view : childViews) {
            drawChildren.add(view);
        }

        // 布局子View的高度
        int dHeight = b;
        while (drawChildren.size() >= 1) {
            View child = drawChildren.pop();
            child.layout(l, dHeight - child.getMeasuredHeight(), r, dHeight);
            dHeight = dHeight - child.getHeight();
        }
    }

    // 添加View
    public void addNewView(View child) {
        ItemTag tag = new ItemTag();
        tag.time = System.currentTimeMillis();
        child.setTag(tag);
        childViews.add(child);
        this.addView(child);
    }

    /*
     * View的tag标记
     */
    private class ItemTag {
        private long time;
        private boolean isMove = false;
    }
}
