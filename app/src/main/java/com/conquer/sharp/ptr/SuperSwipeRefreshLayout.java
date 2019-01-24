package com.conquer.sharp.ptr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.conquer.sharp.util.system.ScreenUtils;

/**
 * Created by ac on 18/7/9.
 *
 */

public class SuperSwipeRefreshLayout extends ViewGroup {
    private static final String TAG = "SuperSwipeRefreshLayout";

    // Header View Height(dp)
    protected static final int HEADER_VIEW_HEIGHT = 48;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = 0.5f;

    private static final int DEFAULT_CIRCLE_TARGET = 48;
    private static final int SCALE_DOWN_DURATION = 150;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int ANIMATE_TO_START_DURATION = 200;

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

    protected int mFrom;

    // 最后停住时的偏移量px，与DEFAULT_CIRCLE_TARGET成正比
    private float mSpinnerFinalOffset;

    // 是否通知
    private boolean mNotify;

    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    private int mFooterViewWidth;
    private int mFooterViewHeight;

    private boolean targetScrollWithLayout = true;

    private int pushDistance = 0;

    private boolean mOriginalOffsetCalculated = false;

    protected int mOriginalOffsetTop;
    private int mCurrentTargetOffsetTop;

    private SwipeCircleProgressView defaultProgressView;

    private boolean usingDefaultHeader = true;

    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    private float mInitialMotionY;
    // 下拉刷新时，是否进行缩放
    private boolean mScale;

    private float mStartingScale;
    private Animation mScaleAnimation;
    private Animation mScaleDownAnimation;
    private Animation mScaleDownToStartAnimation;

    private boolean isProgressEnable = true;
    private boolean pullDownEnable = true;
    private boolean pullUpEnable = true;

    private int mDistanceHeight;

    private boolean mAutoLoadMore = false;

    /**
     * 下拉时，超过距离之后，弹回来的动画监听器
     */
    private Animation.AnimationListener mRefreshListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isProgressEnable = false;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isProgressEnable = true;
            if (mRefreshing) {
                if (mNotify) {
                    if (usingDefaultHeader) {
                        ViewCompat.setAlpha(defaultProgressView, 1.0f);
                        defaultProgressView.setOnDraw(true);
                        new Thread(defaultProgressView).start();
                    }

                    if (mOnPullRefreshListener != null) {
                        mOnPullRefreshListener.onRefresh();
                    }
                }
            } else {
                mHeaderViewContainer.setVisibility(View.GONE);
                if (mScale) {
                    setAnimationProgress(0);
                } else {
                    // 回弹动画
                    final int from = mTarget.getTop();
                    final int to = 0;
                    ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(150);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            float step = (to - from) * value + from;
                            int top = mTarget.getTop();
                            setTargetOffsetTopAndBottom((int) (step - top), true);
                        }
                    });
                    animator.start();
                }
            }

            mCurrentTargetOffsetTop = mHeaderViewContainer.getTop();
            updateListenerCallBack();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

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

    public void setOnPullRefreshListener(OnPullRefreshListener listener) {
        mOnPullRefreshListener = listener;
    }

    public void setOnPushLoadMoreListener(OnPushLoadMoreListener onPushLoadMoreListener) {
        this.mOnPushLoadMoreListener = onPushLoadMoreListener;
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
        // 中等动画时长
        mMediumAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
        // 表示会重写onDraw
        setWillNotDraw(false);
        // 减速插值器
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

        mDistanceHeight = mHeaderViewHeight;
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
        layoutParams.bottomMargin = ScreenUtils.dip2px(6);
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

        if (!mOriginalOffsetCalculated) {
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
        if (!isChildScrollToTop() && !isChildScrollToBottom()) {
            return false;
        }

        // 下拉刷新判断
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!isEnabled() || mRefreshing || mLoadMore) {

                } else {
                    // 恢复HeaderView的初始位置
                    setTargetOffsetTopAndBottom(mOriginalOffsetTop - mHeaderViewContainer.getTop(), true);
                }

                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
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

                if (!isEnabled() || mRefreshing || mLoadMore) {
                    if (mRefreshing) {
                        float dy = y - mInitialMotionY;
                        if (dy < 0) {
                            if (mDistanceHeight >= (int) Math.abs(dy)) {
                                setTargetOffsetTopAndBottom((int) dy, true);
                                mDistanceHeight -= (int) Math.abs(dy);
                            } else {
                                if (mDistanceHeight > 0) {
                                    int top = mTarget.getTop();
                                    setTargetOffsetTopAndBottom(0 - top, true);
                                } else {
                                    setTargetOffsetTopAndBottom(mDistanceHeight, true);
                                }
                                mDistanceHeight = mHeaderViewHeight;
                                setRefreshing(false);
                                return super.onInterceptTouchEvent(ev);
                            }
                        }
                    }
                } else {
                    float yDiff;
                    if (isChildScrollToBottom() && parentAllowPullUp()) {
                        if (mAutoLoadMore) {
                            return super.onInterceptTouchEvent(ev);
                        } else {
                            // 计算上拉距离
                            yDiff = mInitialMotionY - y;
                            // 判断是否上拉的距离足够
                            if (yDiff > mTouchSlop && !mIsBeingDragged && pullUpEnable) {
                                mIsBeingDragged = true;
                                requestParentDisallowInterceptTouchEvent(true);
                            }
                        }
                    } else {
                        // 计算下拉距离
                        yDiff = y - mInitialMotionY;
                        if (yDiff > mTouchSlop && !mIsBeingDragged && pullDownEnable) {
                            mIsBeingDragged = true;
                            requestParentDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                // 当屏幕上有多个点被按住，松开其中一个点时触发---非最后一个点被放开时
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        // 如果正在拖动，则拦截子View的事件
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (!isEnabled() || mRefreshing || mLoadMore) {
            return true;
        }

        if (!isEnabled() && (!isChildScrollToTop() && !isChildScrollToBottom())) {
            return super.onTouchEvent(ev);
        }

        if (mIsBeingDragged) {
            if (isChildScrollToBottom()) {
                // 上拉加载
                return handlePushTouchEvent(ev, action);
            } else {
                // 下拉刷新
                return handlePullTouchEvent(ev, action);
            }
        } else {
            return super.onTouchEvent(ev);
        }
    }

    /**
     * 下拉刷新
     */
    private boolean handlePullTouchEvent(MotionEvent ev, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                if (mIsBeingDragged) {
                    float originalDragPercent = overScrollTop / mTotalDragDistance;
                    if (originalDragPercent < 0) {
                        return false;
                    }

                    // 距离的计算--还需要继续理解
                    float dragPercent = Math.min(1f, Math.abs(originalDragPercent));
                    float extraOS = Math.abs(overScrollTop) - mTotalDragDistance;
                    float slingShotDist = mSpinnerFinalOffset;
                    float tensionSlingShotPercent = Math.max(0, Math.min(extraOS, slingShotDist * 2) / slingShotDist);
                    float tensionPercent = (float) ((tensionSlingShotPercent / 4) - Math.pow((tensionSlingShotPercent / 4), 2)) * 2f;
                    float extraMove = slingShotDist * tensionPercent * 2;

                    int targetY = mOriginalOffsetTop + (int) (slingShotDist * dragPercent + extraMove);

                    if (mHeaderViewContainer.getVisibility() != View.VISIBLE) {
                        mHeaderViewContainer.setVisibility(View.VISIBLE);
                    }

                    if (!mScale) {
                        ViewCompat.setScaleX(mHeaderViewContainer, 1f);
                        ViewCompat.setScaleY(mHeaderViewContainer, 1f);
                    }

                    if (usingDefaultHeader) {
                        float alpha = overScrollTop / mTotalDragDistance;
                        if (alpha >= 1.0f) {
                            alpha = 1.0f;
                        }

                        ViewCompat.setScaleX(defaultProgressView, alpha);
                        ViewCompat.setScaleY(defaultProgressView, alpha);
                        ViewCompat.setAlpha(defaultProgressView, alpha);
                    }

                    if (overScrollTop < mTotalDragDistance) {
                        if (mScale) {
                            setAnimationProgress(overScrollTop / mTotalDragDistance);
                        }
                        if (mOnPullRefreshListener != null) {
                            mOnPullRefreshListener.onPullEnable(false);
                        }
                    } else {
                        if (mOnPullRefreshListener != null) {
                            mOnPullRefreshListener.onPullEnable(true);
                        }
                    }

                    setTargetOffsetTopAndBottom(targetY - mCurrentTargetOffsetTop, true);
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                // 这个是实现多点的关键，当屏幕检测到有多个手指同时按下之后，就触发了这个事件
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = ev.getPointerId(index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mActivePointerId == INVALID_POINTER) {
                    if (action == MotionEvent.ACTION_UP) {
                        Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }

                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float y = ev.getY(pointerIndex);
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overScrollTop > mTotalDragDistance) {
                    setRefreshing(true, true);
                } else {
                    mRefreshing = false;
                    Animation.AnimationListener listener = null;
                    if (!mScale) {
                        listener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (!mScale) {
                                    startScaleDownAnimation(null);
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        };
                    }
                    animateOffsetToStartPosition(mCurrentTargetOffsetTop, listener);
                }

                mActivePointerId = INVALID_POINTER;
                return false;
        }

        return true;
    }

    /**
     * 上拉加载
     */
    private boolean handlePushTouchEvent(MotionEvent ev, int action) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                final float overScrollBottom = (mInitialMotionY - y) * DRAG_RATE;
                if (mIsBeingDragged) {
                    pushDistance = (int) overScrollBottom;
                    updateFooterViewPosition();
                    if (mOnPushLoadMoreListener != null) {
                        mOnPushLoadMoreListener.onPushEnable(pushDistance >= mFooterViewHeight);
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = ev.getPointerId(index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mActivePointerId == INVALID_POINTER) {
                    if (action == MotionEvent.ACTION_UP) {
                        Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }

                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float y = ev.getY(pointerIndex);
                final float overScrollBottom = (mInitialMotionY - y) * DRAG_RATE; // 松手是下拉的距离
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (overScrollBottom < mFooterViewHeight || mOnPushLoadMoreListener == null) {
                    // 直接取消
                    pushDistance = 0;
                } else {
                    // 下拉到mFooterViewHeight
                    pushDistance = mFooterViewHeight;
                }

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    updateFooterViewPosition();
                    if (pushDistance == mFooterViewHeight && mOnPushLoadMoreListener != null) {
                        mLoadMore = true;
                        mOnPushLoadMoreListener.onLoadMore();
                    }
                } else {
                    animatorFooterToBottom((int) overScrollBottom, pushDistance);
                }
                return false;

        }

        return true;
    }

    private void setTargetOffsetTopAndBottom(int offset, boolean requireUpdate) {
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
                // 处理item高度超过一屏幕时的情况
                View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                if (lastVisibleChild != null && lastVisibleChild.getMeasuredHeight() >= recyclerView.getMeasuredHeight()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        return !(ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < 0);
                    } else {
                        return !ViewCompat.canScrollVertically(recyclerView, 1);
                    }
                }

                return linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                int[] lastItems = new int[2];
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastItems);
                int lastItem = Math.max(lastItems[0], lastItems[1]);
                return lastItem == count - 1;
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
            return lastPos > 0 && count > 0 && lastPos == count - 1;

        } else if (mTarget instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) mTarget;
            View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
            if (view != null) {
                int diff = view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY());
                return diff == 0;
            }
        }

        return false;
    }

    private void addTargetListener() {
        ensureTarget();
        if (mTarget == null) {
            return;
        }

        if (mTarget instanceof RecyclerView) {
            ((RecyclerView) mTarget).addOnScrollListener(recyclerListener);
        } else if (mTarget instanceof AbsListView) {

        } else if (mTarget instanceof ScrollView) {

        }

    }

    private RecyclerView.OnScrollListener recyclerListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int lastVisibleItemPosition = 0;
            int[] lastPositions;

            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
            }

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if (visibleItemCount > 0 && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                // 处理item高度超过一屏幕时的情况
                View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                if (lastVisibleChild != null && lastVisibleChild.getMeasuredHeight() >= recyclerView.getMeasuredHeight()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        if (!(ViewCompat.canScrollVertically(recyclerView, 1) || recyclerView.getScrollY() < 0)) {
                            if (!mLoadMore) {
                                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                    updateFooterViewPosition();
                                    if (mOnPushLoadMoreListener != null) {
                                        mLoadMore = true;
                                        mOnPushLoadMoreListener.onLoadMore();
                                    }
                                } else {
                                    animatorFooterToBottom(0, mFooterViewHeight);
                                }
                            }
                        }
                    } else {
                        if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                            if (!mLoadMore) {
                                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                    updateFooterViewPosition();
                                    if (mOnPushLoadMoreListener != null) {
                                        mLoadMore = true;
                                        mOnPushLoadMoreListener.onLoadMore();
                                    }
                                } else {
                                    animatorFooterToBottom(0, mFooterViewHeight);
                                }
                            }
                        }
                    }
                }
            }

            // -1是为了提前预加载，优化体验
            if (visibleItemCount > 0 && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition >= totalItemCount - 1) {
                if (!mLoadMore) {
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                        updateFooterViewPosition();
                        if (mOnPushLoadMoreListener != null) {
                            mLoadMore = true;
                            mOnPushLoadMoreListener.onLoadMore();
                        }
                    } else {
                        animatorFooterToBottom(0, mFooterViewHeight);
                    }
                }
            } else {

            }
        }
    };

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 是否允许上拉
     */
    protected boolean parentAllowPullUp() {
        return true;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // Nope.
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

    private void setAnimationProgress(float progress) {
        if (!usingDefaultHeader) {
            progress = 1;
        }

        ViewCompat.setScaleX(mHeaderViewContainer, progress);
        ViewCompat.setScaleY(mHeaderViewContainer, progress);
    }

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();

            mRefreshing = refreshing;
            if (mRefreshing) {
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshListener);
            } else {
                startScaleDownAnimation(mRefreshListener);
            }
        }
    }

    private void animateOffsetToCorrectPosition(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        if (listener != null) {
            mHeaderViewContainer.setAnimationListener(listener);
        }
        mHeaderViewContainer.clearAnimation();
        mHeaderViewContainer.startAnimation(mAnimateToCorrectPosition);
    }

    private void startScaleDownAnimation(Animation.AnimationListener listener) {
        mScaleDownAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                setAnimationProgress(1 - interpolatedTime);
            }
        };
        mScaleDownAnimation.setDuration(SCALE_DOWN_DURATION);
        mHeaderViewContainer.setAnimationListener(listener);
        mHeaderViewContainer.clearAnimation();
        mHeaderViewContainer.startAnimation(mScaleDownAnimation);
    }

    private void animateOffsetToStartPosition(int from, Animation.AnimationListener listener) {
        if (mScale) {
            startScaleDownReturnToStartAnimation(from, listener);
        } else {
            mFrom = from;
            mAnimateToStartPosition.reset();
            mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
            mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
            if (listener != null) {
                mHeaderViewContainer.setAnimationListener(listener);
            }
            mHeaderViewContainer.clearAnimation();
            mHeaderViewContainer.startAnimation(mAnimateToStartPosition);
        }
    }

    private void startScaleDownReturnToStartAnimation(int from, Animation.AnimationListener listener) {
        mFrom = from;
        mStartingScale = ViewCompat.getScaleX(mHeaderViewContainer);
        mScaleDownToStartAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime,
                                            Transformation t) {
                float targetScale = (mStartingScale + (-mStartingScale * interpolatedTime));
                setAnimationProgress(targetScale);
                moveToStart(interpolatedTime);
            }
        };
        mScaleDownToStartAnimation.setDuration(SCALE_DOWN_DURATION);
        if (listener != null) {
            mHeaderViewContainer.setAnimationListener(listener);
        }
        mHeaderViewContainer.clearAnimation();
        mHeaderViewContainer.startAnimation(mScaleDownToStartAnimation);
    }

    private void moveToStart(float interpolatedTime) {
        int targetTop;
        targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
        int offset = targetTop - mHeaderViewContainer.getTop();
        setTargetOffsetTopAndBottom(offset, false);
    }

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int targetEnd;
            targetEnd = (int) (mSpinnerFinalOffset - Math.abs(mOriginalOffsetTop));
            targetTop = mFrom + (int) ((targetEnd - mFrom) * interpolatedTime);
            int offset = targetTop - mHeaderViewContainer.getTop();
            setTargetOffsetTopAndBottom(offset, false);
        }

        @Override
        public void setAnimationListener(AnimationListener listener) {
            super.setAnimationListener(listener);
        }
    };

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private void updateFooterViewPosition() {
        mFooterViewContainer.setVisibility(View.VISIBLE);
        mFooterViewContainer.bringToFront();
        mFooterViewContainer.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        // 针对4.4及之前版本的兼容
        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            mFooterViewContainer.getParent().requestLayout();
        }
        mFooterViewContainer.offsetTopAndBottom(-pushDistance);
        updatePushDistanceListener();
    }

    private void updatePushDistanceListener() {
        if (mOnPushLoadMoreListener != null) {
            mOnPushLoadMoreListener.onPushDistance(pushDistance);
        }
    }

    /**
     * 松手之后，使用动画将Footer从距离start变化到end
     *
     * @param start
     * @param end
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void animatorFooterToBottom(int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // update
                pushDistance = (Integer) valueAnimator.getAnimatedValue();
                updateFooterViewPosition();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (end > 0 && mOnPushLoadMoreListener != null) {
                    // start loading more
                    mLoadMore = true;
                    mOnPushLoadMoreListener.onLoadMore();
                } else {
                    resetTargetLayout();
                    mLoadMore = false;
                }
            }
        });
        valueAnimator.setInterpolator(mDecelerateInterpolator);
        valueAnimator.start();
    }

    /**
     * 重置Target的位置
     */
    public void resetTargetLayout() {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = child.getWidth() - getPaddingLeft() - getPaddingRight();
        final int childHeight = child.getHeight() - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int headViewWidth = mHeaderViewContainer.getMeasuredWidth();
        int headViewHeight = mHeaderViewContainer.getMeasuredHeight();
        // 更新头布局的位置
        mHeaderViewContainer.layout((width / 2 - headViewWidth / 2),
                -headViewHeight, (width / 2 + headViewWidth / 2), 0);

        int footViewWidth = mFooterViewContainer.getMeasuredWidth();
        int footViewHeight = mFooterViewContainer.getMeasuredHeight();
        mFooterViewContainer.layout((width / 2 - footViewWidth / 2), height,
                (width / 2 + footViewWidth / 2), height + footViewHeight);
    }

    // 自定义
    public void targetToTop() {
        ensureTarget();
        if (mTarget instanceof RecyclerView) {
            ((RecyclerView) mTarget).scrollToPosition(0);
        }
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && mRefreshing != refreshing) {
            // scale and show
            mRefreshing = refreshing;
            int endTarget;
            endTarget = (int) (mSpinnerFinalOffset + mOriginalOffsetTop);

            setTargetOffsetTopAndBottom(endTarget - mCurrentTargetOffsetTop, true);
            mNotify = false;
            startScaleUpAnimation(mRefreshListener);

        } else {
            setRefreshing(refreshing, false);
            if (usingDefaultHeader) {
                defaultProgressView.setOnDraw(false);
            }
        }
    }

    private void startScaleUpAnimation(Animation.AnimationListener listener) {
        mHeaderViewContainer.setVisibility(View.VISIBLE);
        mScaleAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime,
                                            Transformation t) {
                setAnimationProgress(interpolatedTime);
            }
        };
        mScaleAnimation.setDuration(mMediumAnimationDuration);
        if (listener != null) {
            mHeaderViewContainer.setAnimationListener(listener);
        }
        mHeaderViewContainer.clearAnimation();
        mHeaderViewContainer.startAnimation(mScaleAnimation);
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
     * 设置默认下拉刷新进度条的颜色
     *
     * @param color
     */
    public void setDefaultCircleProgressColor(int color) {
        if (usingDefaultHeader) {
            defaultProgressView.setProgressColor(color);
        }
    }

    /**
     * 设置圆圈的背景色
     *
     * @param color
     */
    public void setDefaultCircleBackgroundColor(int color) {
        if (usingDefaultHeader) {
            defaultProgressView.setCircleBackgroundColor(color);
        }
    }

    public void setDefaultCircleShadowColor(int color) {
        if (usingDefaultHeader) {
            defaultProgressView.setShadowColor(color);
        }
    }

    public void setHeaderViewBackgroundColor(int color) {
        mHeaderViewContainer.setBackgroundColor(color);
    }

    /**
     * 设置停止加载
     *
     * @param loadMore
     */
    private void setLoadMore(boolean loadMore) {
        // 停止加载
        if (!loadMore && mLoadMore) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                mLoadMore = false;
                pushDistance = 0;
                updateFooterViewPosition();
            } else {
                animatorFooterToBottom(mFooterViewHeight, 0);
            }
        }
    }

    /**
     * 停止加载
     */
    public void stopLoading() {
        if (mLoadMore) {
            // 正在加载更多
            setLoadMore(false);
        } else if (mRefreshing) {
            // 正在刷新
            setRefreshing(false);
        }
    }

    public boolean isRefreshing() {
        return mRefreshing;
    }

    // 自动加载更多
    public void setAutoLoadMore() {
        mAutoLoadMore = true;
        addTargetListener();
    }

    public void setPullUpUnable() {
        pullUpEnable = false;
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
