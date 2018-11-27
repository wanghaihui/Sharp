package com.conquer.sharp.danmaku.normal;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conquer.sharp.R;
import com.conquer.sharp.danmaku.bean.DanMu;
import com.conquer.sharp.util.system.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hai hui on 18/7/25.
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

    // 处理子View的margin
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBottom;

    // 处理当前View的Margin


    /**
     * 所有子View
     */
    private List<View> childViews = new ArrayList<>();

    /**
     * 绘制的View栈--倒序--弹幕实现的关键
     */
    Stack<View> drawChildren = new Stack<>();

    private Handler handler = new Handler();

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

        setMeasuredDimension(measureWidthSize(widthMeasureSpec, viewWidth), measureHeightSize(heightMeasureSpec, viewHeight));
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

    private int measureHeightSize(int heightMeasureSpec, int viewHeight) {
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

        // 布局子View
        int dHeight = b;
        while (drawChildren.size() >= 1) {
            View child = drawChildren.pop();
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int mLeft = paddingLeft + layoutParams.leftMargin;

            child.layout(mLeft, dHeight - child.getMeasuredHeight() - layoutParams.topMargin - layoutParams.bottomMargin,
                    mLeft + child.getMeasuredWidth(), dHeight);
            dHeight = dHeight - child.getMeasuredHeight() - layoutParams.topMargin - layoutParams.bottomMargin;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LayoutParams的问题--margin的问题
    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // 添加View
    public void addNewView(View child) {
        ItemTag tag = new ItemTag();
        tag.time = System.currentTimeMillis();
        child.setTag(tag);
        childViews.add(child);
        this.addView(child);
    }

    /*
     * View的tag标记--待扩展
     */
    private class ItemTag {
        private long time;
        private boolean isMove = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 弹幕相关
    private int mCurrentPosition = 0;
    private int mInterval;
    private List<DanMu> danMuList = new ArrayList<>();
    private Disposable disposable;
    // 头View
    private int head = 0;

    public List<DanMu> getDanMuList() {
        return danMuList;
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    private void createChildView(int position) {
        if (position >= 0 && position < danMuList.size()) {
            if (danMuList.get(position).type == DanMu.DanMuType.DAN_MU) {
                View addView = LayoutInflater.from(getContext()).inflate(R.layout.layout_dan_mu3, null);
                ((TextView) addView.findViewById(R.id.item_tv_title)).setText(danMuList.get(position).danMu);
                DanMuQueueView.LayoutParams params = new DanMuQueueView.LayoutParams(DanMuQueueView.LayoutParams.MATCH_PARENT, DanMuQueueView.LayoutParams.WRAP_CONTENT);
                params.topMargin = ScreenUtils.dip2px(3);
                addView.setLayoutParams(params);
                addNewView(addView);
            } else {
                View addView = LayoutInflater.from(getContext()).inflate(R.layout.layout_dan_mu_holder, null);
                DanMuQueueView.LayoutParams params = new DanMuQueueView.LayoutParams(DanMuQueueView.LayoutParams.MATCH_PARENT, DanMuQueueView.LayoutParams.WRAP_CONTENT);
                params.topMargin = ScreenUtils.dip2px(3);
                addView.setLayoutParams(params);
                addNewView(addView);
            }
        }
    }

    public void startDanMu() {
        setVisibility(View.VISIBLE);
        disposable = Observable.interval(0, mInterval, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (mCurrentPosition >= danMuList.size()) {
                            mCurrentPosition = 0;
                            stopDanMu();
                            setVisibility(View.GONE);
                        } else if (mCurrentPosition < 3) {
                            createChildView(mCurrentPosition);
                        } else {
                            createChildView(mCurrentPosition);
                            // 取一半时间再remove，解决动画不流畅问题--关键
                            handler.postDelayed(removeRunnable, mInterval / 2);
                        }
                        mCurrentPosition++;
                    }
                });
    }

    public void stopDanMu() {
        setVisibility(View.GONE);
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private Runnable removeRunnable = new Runnable() {
        @Override
        public void run() {
            removeViewAt(head);
            childViews.remove(head);
        }
    };

    /**
     * 释放资源--onDestroy时调用
     */
    public void release() {
        handler.removeCallbacks(removeRunnable);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
