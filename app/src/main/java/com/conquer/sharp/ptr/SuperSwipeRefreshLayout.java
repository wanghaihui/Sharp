package com.conquer.sharp.ptr;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by ac on 18/7/9.
 *
 */

public class SuperSwipeRefreshLayout extends ViewGroup {

    // Header View Height(dp)
    private static final int HEADER_VIEW_HEIGHT = 48;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;

    // SuperSwipeRefreshLayout内的目标View--RecyclerView--ListView--ScrollView--GridView
    private View mTarget;

    // 下拉刷新
    private OnPullRefreshListener mOnPullRefreshListener;
    // 上拉加载
    private OnPushLoadMoreListener mOnPushLoadMoreListener;

    private boolean mRefreshing = false;
    private boolean mLoadMore = false;

    private int mTouchSlop;

    // 中等动画的持续时间
    private int mMediumAnimationDuration;

    // 减速插值器
    private final DecelerateInterpolator mDecelerateInterpolator;
    // 布局属性
    private static final int[] LAYOUT_ATTRS = new int[] { android.R.attr.enabled };



    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    private int mFooterViewWidth;
    private int mFooterViewHeight;

    private CircleProgressView defaultProgressView;

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

        defaultProgressView = new CircleProgressView(getContext());
        // createHeaderViewContainer();
        // createFooterViewContainer();
        // 自己决定Children的绘制顺序
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);

    }

    // 必须重写
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

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
