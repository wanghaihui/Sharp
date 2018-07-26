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

    // ViewGroup的宽和高
    private int viewWidth;
    private int viewHeight;

    // 子View的宽和高
    private int childViewWidth;
    private int childViewHeight;

    // 处理padding
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    // 处理margin
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBottom;

    /**
     * 所有子View
     */
    private List<View> childViews = new ArrayList<>();

    /**
     * 绘制的View栈--倒序--弹幕实现的关键
     */
    Stack<View> drawChildren = new Stack<>();

    public DanMuQueueView(Context context) {
        this(context, null);
    }

    public DanMuQueueView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initWidthAndHeight() {
        childViewWidth = 0;
        childViewHeight = 0;

        marginLeft = 0;
        marginRight = 0;
        marginTop = 0;
        marginBottom = 0;

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        paddingBottom = getPaddingBottom();
    }

    // Padding和Margin的处理
    // 测量时--处理Margin
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        initWidthAndHeight();

        // 子View的数量
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            // 测量子View
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            childViewWidth = Math.max(childViewWidth, childView.getMeasuredWidth());
            childViewHeight = childViewHeight + childView.getMeasuredHeight();

            marginLeft = layoutParams.leftMargin;
            marginRight = layoutParams.rightMargin;
            marginTop = marginTop + layoutParams.topMargin;
            marginBottom = marginBottom + layoutParams.bottomMargin;
        }

        // 用于处理ViewGroup的wrap_content的情况
        viewWidth = paddingLeft + childViewWidth + paddingRight + marginLeft + marginRight;
        viewHeight = paddingTop + childViewHeight + paddingBottom + marginTop + marginBottom;
        setMeasuredDimension(measureWidthSize(widthMeasureSpec, viewWidth), measureSize(heightMeasureSpec, viewHeight));
    }

    private int measureWidthSize(int widthMeasureSpec, int viewWidth) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            // 子容器可以是声明大小内的任意大小
            case MeasureSpec.AT_MOST:
                result = Math.min(viewWidth, specSize);
                break;
            // 父容器已经为子容器设置了尺寸, 子容器应当服从这些边界, 不论子容器想要多大的空间, 比如EditTextView中的DrawLeft
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            // 父容器对于子容器没有任何限制, 子容器想要多大就多大, 所以完全取决于子view的大小
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            default:
                break;
        }
        return result;
    }

    private int measureSize(int heightMeasureSpec, int viewHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            // 子容器可以是声明大小内的任意大小
            case MeasureSpec.AT_MOST:
                result = Math.min(viewHeight, specSize);
                break;
            // 父容器已经为子容器设置了尺寸, 子容器应当服从这些边界, 不论子容器想要多大的空间, 比如EditTextView中的DrawLeft
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            // 父容器对于子容器没有任何限制, 子容器想要多大就多大, 所以完全取决于子view的大小
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
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
        drawChildren.clear();
        for (View view : childViews) {
            drawChildren.add(view);
        }

        // 布局子View的高度
        int dHeight = b;
        while (drawChildren.size() >= 1) {
            View child = drawChildren.pop();
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int mLeft = paddingLeft + layoutParams.leftMargin;

            Log.d(TAG, "topMargin: " + layoutParams.topMargin);
            Log.d(TAG, "bottomMargin: " + layoutParams.bottomMargin);
            child.layout(mLeft, dHeight - child.getMeasuredHeight() - layoutParams.topMargin - layoutParams.bottomMargin, mLeft + child.getMeasuredWidth(), dHeight);
            dHeight = dHeight - child.getHeight() - layoutParams.topMargin - layoutParams.bottomMargin;
        }
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }


        public LayoutParams(int width, int height) {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }


        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
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
