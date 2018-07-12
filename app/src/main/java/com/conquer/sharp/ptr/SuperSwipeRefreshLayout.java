package com.conquer.sharp.ptr;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by ac on 18/7/9.
 *
 */

public class SuperSwipeRefreshLayout extends ViewGroup {
    private static final String TAG = "SuperSwipeRefreshLayout";

    // Header View Height(dp)
    private static final int HEADER_VIEW_HEIGHT = 48;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    private static final int INVALID_POINTER = -1;

    private static final int DEFAULT_CIRCLE_TARGET = 64;

    // SuperSwipeRefreshLayout内的目标View--RecyclerView--ListView--ScrollView--GridView
    private View mTarget;

    // 下拉刷新
    private OnPullRefreshListener mOnPullRefreshListener;
    // 上拉加载
    private OnPushLoadMoreListener mOnPushLoadMoreListener;

    private boolean mRefreshing = false;
    private boolean mLoadMore = false;

    private int mTouchSlop;
    // 整个拖拽的距离
    private float mTotalDragDistance = -1;

    // 中等动画的持续时间
    private int mMediumAnimationDuration;

    // 减速插值器
    private final DecelerateInterpolator mDecelerateInterpolator;
    // 布局属性
    private static final int[] LAYOUT_ATTRS = new int[] { android.R.attr.enabled };

    private HeaderViewContainer mHeaderViewContainer;
    private RelativeLayout mFooterViewContainer;
    private int mHeaderViewIndex = -1;
    private int mFooterViewIndex = -1;

    // 最后停住时的偏移量px，与DEFAULT_CIRCLE_TARGET成正比
    private float mSpinnerFinalOffset;

    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    private int mFooterViewWidth;
    private int mFooterViewHeight;

    // 是否使用自定义的Start
    private boolean mUsingCustomStart;
    private boolean targetScrollWithLayout = true;

    private int pushDistance = 0;

    private boolean mOriginalOffsetCalculated = false;

    protected int mOriginalOffsetTop;
    private int mCurrentTargetOffsetTop;

    private SwipeCircleProgressView defaultProgressView;

    private boolean usingDefaultHeader = true;

    private boolean mIsBingDragged;
    private int mActivePointerId = INVALID_POINTER;
    private float mInitialMotionY;

    private boolean isProgressEnable = true;
    private boolean pullDownEnable = true;
    private boolean pullUpEnable = true;

    /**
     * 更新回调
     */
    private void updateListenerCallBack() {
        int distance = mCurrentTargetOffsetTop + mHeaderViewContainer.getHeight();
        if (mOnPullRefreshListener != null) {
            mOnPullRefreshListener.onPullDistance(distance);
        }

        if (usingDefaultHeader && isProgressEnable) {
            defaultProgressView.setPullDistance(distance);
        }
    }

    /**
     * 添加头布局
     * @param child
     */
    public void setHeaderView(View child) {
        if (child == null) {
            return;
        }

        if (mHeaderViewContainer == null) {
            return;
        }

        usingDefaultHeader = false;
        mHeaderViewContainer.removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                mHeaderViewWidth,
                mHeaderViewHeight
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mHeaderViewContainer.addView(child, layoutParams);
    }

    /**
     * 添加底部布局
     * @param child
     */
    public void setFooterView(View child) {
        if (child == null) {
            return;
        }

        if (mFooterViewContainer == null) {
            return;
        }

        mFooterViewContainer.removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                mFooterViewWidth,
                mFooterViewHeight
        );
        mFooterViewContainer.addView(child, layoutParams);
    }

    public SuperSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public SuperSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 距离--表示滑动的时候，手的移动要大于这个距离才开始移动控件，如果小于这个距离就不触发此控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        // 表示会重写onDraw
        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mHeaderViewWidth = metrics.widthPixels;
        mHeaderViewHeight = (int) (HEADER_VIEW_HEIGHT * metrics.density);
        mFooterViewWidth = metrics.widthPixels;
        mFooterViewHeight = (int) (HEADER_VIEW_HEIGHT * metrics.density);

        defaultProgressView = new SwipeCircleProgressView(getContext());
        createHeaderViewContainer();
        createFooterViewContainer();
        // 自己决定Children的绘制顺序
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        mSpinnerFinalOffset = DEFAULT_CIRCLE_TARGET * metrics.density;
        mTotalDragDistance = mSpinnerFinalOffset;
    }

    /**
     * 孩子节点的绘制顺序
     * @param childCount
     * @param i
     */
    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mHeaderViewIndex < 0 && mFooterViewIndex < 0) {
            return i;
        }

        if (i == childCount - 2) {
            return mHeaderViewIndex;
        }

        if (i == childCount - 1) {
            return mFooterViewIndex;
        }

        int bigIndex = mFooterViewIndex > mHeaderViewIndex ? mFooterViewIndex : mHeaderViewIndex;
        int smallIndex = mFooterViewIndex < mHeaderViewIndex ? mFooterViewIndex : mHeaderViewIndex;

        // why?
        if (i >= smallIndex && i < bigIndex -1) {
            return i + 1;
        }

        if (i >= bigIndex - 1) {
            return i + 2;
        }

        return i;
    }

    /**
     * 创建头布局的容器--默认头布局
     */
    private void createHeaderViewContainer() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                (int) (mHeaderViewHeight * 0.8),
                (int) (mHeaderViewHeight * 0.8)
        );
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mHeaderViewContainer = new HeaderViewContainer(getContext());
        mHeaderViewContainer.setVisibility(View.GONE);
        defaultProgressView.setVisibility(View.VISIBLE);
        defaultProgressView.setOnDraw(false);
        mHeaderViewContainer.addView(defaultProgressView, layoutParams);
        addView(mHeaderViewContainer);
    }

    /**
     * 添加底部布局--默认底部布局
     */
    private void createFooterViewContainer() {
        mFooterViewContainer = new RelativeLayout(getContext());
        mFooterViewContainer.setVisibility(View.GONE);
        addView(mFooterViewContainer);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 确保mTarget不为空
     * mTarget一般是可滑动的ScrollView,ListView,RecyclerView
     */
    private void ensureTarget() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mHeaderViewContainer) && !child.equals(mFooterViewContainer)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    // 必须重写
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }

        if (mTarget == null) {
            ensureTarget();
        }

        if (mTarget == null) {
            return;
        }

        layoutHeader(width);
        layoutTarget(width, height);
        layoutFooter(width, height);
    }

    private void layoutHeader(int width) {
        int headerViewWidth = mHeaderViewContainer.getMeasuredWidth();
        int headerViewHeight = mHeaderViewContainer.getMeasuredHeight();
        mHeaderViewContainer.layout((width / 2 - headerViewWidth / 2),
                mCurrentTargetOffsetTop,
                (width / 2 + headerViewWidth / 2),
                mCurrentTargetOffsetTop + headerViewHeight);
    }

    private void layoutTarget(int width, int height) {
        int distance = mCurrentTargetOffsetTop + mHeaderViewContainer.getHeight();
        if (!targetScrollWithLayout) {
            // 判断标志位，如果目标View不跟随手指的滑动而滑动，则下拉偏移量设置为0
            distance = 0;
        }

        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop() + distance - pushDistance;
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        mTarget.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }

    private void layoutFooter(int width, int height) {
        int footerViewWidth = mFooterViewContainer.getMeasuredWidth();
        int footerViewHeight = mFooterViewContainer.getMeasuredHeight();
        mFooterViewContainer.layout((width / 2 - footerViewWidth / 2),
                height - pushDistance,
                (width / 2 + footerViewWidth / 2),
                height + footerViewHeight - pushDistance);
    }

    // onMeasure
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }

        if (mTarget == null) {
            return;
        }

        // MeasureSpec.EXACTLY--精确模式
        mTarget.measure(
                MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));

        mHeaderViewContainer.measure(
                MeasureSpec.makeMeasureSpec(mHeaderViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mHeaderViewHeight * 3, MeasureSpec.EXACTLY)
        );

        mFooterViewContainer.measure(
                MeasureSpec.makeMeasureSpec(mFooterViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mFooterViewHeight, MeasureSpec.EXACTLY)
        );

        if (!mUsingCustomStart && !mOriginalOffsetCalculated) {
            mOriginalOffsetCalculated = true;
            // 初始状态
            mCurrentTargetOffsetTop = mOriginalOffsetTop = -mHeaderViewContainer.getMeasuredHeight();
            // 更新监听
            updateListenerCallBack();
        }

        mHeaderViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mHeaderViewContainer) {
                mHeaderViewIndex = index;
                break;
            }
        }

        mFooterViewIndex = -1;
        for (int index = 0; index < getChildCount(); index++) {
            if (getChildAt(index) == mFooterViewContainer) {
                mFooterViewIndex = index;
                break;
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 主要判断是否拦截子View的事件，如果拦截，则交给自己的onTouchEvent处理，否则，交给子View处理
     * @param ev
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);
        if (!isEnabled() || mRefreshing || mLoadMore
                || (!isChildScrollToTop() && !isChildScrollToBottom())) {
            // 满足这些条件之一，事件交给子View处理
            return false;
        }

        // 下拉刷新判断
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下，就会恢复HeaderView的初始位置
                setTargetOffsetTopAndBottom(mOriginalOffsetTop - mHeaderViewContainer.getTop(), true);
                mActivePointerId = ev.getPointerId(0);
                mIsBingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                // 记录按下的位置
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }

                float yDiff = 0;
                if (isChildScrollToBottom() && parentAllowPullUp()) {
                    // 计算上拉距离
                    yDiff = mInitialMotionY - y;
                    // 判断是否下拉的距离足够
                    if (yDiff > mTouchSlop && !mIsBingDragged && pullUpEnable) {
                        mIsBingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                    }
                } else {
                    // 计算下拉距离
                    yDiff = y - mInitialMotionY;
                    if (yDiff > mTouchSlop && !mIsBingDragged && pullDownEnable) {
                        mIsBingDragged = true;
                        requestParentDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                // 当屏幕上有多个点被按住，松开其中一个点时触发---非最后一个点被放开时
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        // 如果正在拖动，则拦截子View的事件
        return mIsBingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (!isEnabled() || (!isChildScrollToTop() && !isChildScrollToBottom())) {
            return false;
        }

        if (isChildScrollToBottom()) {
            // 上拉加载
            return handlePushTouchEvent(ev, action);
        } else {
            // 下拉刷新
            return handlePullTouchEvent(ev, action);
        }
    }

    /**
     * 下拉刷新
     */
    private boolean handlePullTouchEvent(MotionEvent ev, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);

                break;
        }
    }

    /**
     * 上拉加载
     */
    private boolean handlePushTouchEvent(MotionEvent ev, int action) {

    }

    private void setTargetOffsetTopAndBottom(int offset, boolean requireUpdate) {
        mHeaderViewContainer.bringToFront();
        mHeaderViewContainer.offsetTopAndBottom(offset);
        mCurrentTargetOffsetTop = mHeaderViewContainer.getTop();
        if (requireUpdate && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            invalidate();
        }

        if (targetScrollWithLayout) {
            ensureTarget();
            mTarget.offsetTopAndBottom(offset);
        }

        updateListenerCallBack();
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1;
        }
        return ev.getY(index);
    }

    /**
     * 判断目标View是否滑动到顶部
     */
    public boolean isChildScrollToTop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return !(absListView.getChildCount() > 0 &&
                        (absListView.getFirstVisiblePosition() > 0 ||
                        absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
            } else {
                return !(mTarget.getScrollY() > 0);
            }
        } else {
            return !ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * 是否滑动到底部
     */
    private boolean isChildScrollToBottom() {
        if (isChildScrollToTop()) {
            return false;
        }

        if (mTarget instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) mTarget;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int count = recyclerView.getAdapter().getItemCount();
            if (layoutManager instanceof LinearLayoutManager && count > 0) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                    return true;
                }
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastItems = new int[2];
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastItems);
                int lastItem = Math.max(lastItems[0], lastItems[1]);
                if (lastItem == count - 1) {
                    return true;
                }
            }

            return false;
        } else if (mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;
            int count = absListView.getAdapter().getCount();
            int firstPos = absListView.getFirstVisiblePosition();
            if (firstPos == 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop()) {
                return false;
            }
            int lastPos = absListView.getLastVisiblePosition();
            if (lastPos > 0 && count > 0 && lastPos == count - 1) {
                return true;
            }

            return false;
        } else if (mTarget instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) mTarget;
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            if (view != null) {
                int diff = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
                if (diff == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 是否允许上拉
     */
    protected boolean parentAllowPullUp() {
        return true;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private class HeaderViewContainer extends RelativeLayout {
        private Animation.AnimationListener mListener;

        public HeaderViewContainer(Context context) {
            super(context);
        }

        public void setAnimationListener(Animation.AnimationListener listener) {
            mListener = listener;
        }

        @Override
        public void onAnimationStart() {
            super.onAnimationStart();
            if (mListener != null) {
                mListener.onAnimationStart(getAnimation());
            }
        }

        @Override
        public void onAnimationEnd() {
            super.onAnimationEnd();
            if (mListener != null) {
                mListener.onAnimationEnd(getAnimation());
            }
        }
    }

    /**
     * 下拉刷新回调
     */
    public interface OnPullRefreshListener {
        void onRefresh();
        void onPullDistance(int distance);
        void onPullEnable(boolean enable);
    }

    /**
     * 上拉加载
     */
    public interface OnPushLoadMoreListener {
        void onLoadMore();
        void onPushDistance(int distance);
        void onPushEnable(boolean enable);
    }
}
