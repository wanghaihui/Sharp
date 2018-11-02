package com.conquer.sharp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.conquer.sharp.bean.LuckyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 针对图像是url，配置ImageView展示
 */
public class LuckyWheelView extends ViewGroup {

    // 默认分区为6个分区
    private static final int DEFAULT_COUNT = 6;
    // 默认的半径
    private static final int DEFAULT_RADIUS = 600;

    // 分区的数量
    private int mCount = DEFAULT_COUNT;
    // 分区的角度
    private float mAngle;
    // 圆盘的半径
    private int mRadius = DEFAULT_RADIUS;

    // 绘制圆盘
    private Paint mPaint;
    // 文字Paint
    private Paint mTextPaint;

    // 获取View的宽度
    private int mWidth;
    // 中心
    private int mCenter;
    // 绘制扇形时的外部矩形
    private RectF mRectF;

    private List<ImageView> mImageList = new ArrayList<>();

    private List<LuckyBean> mLuckyList;

    public LuckyWheelView(Context context) {
        this(context, null);
    }

    public LuckyWheelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        // 设置背景透明
        setBackgroundColor(Color.TRANSPARENT);

        mAngle = (float) (360.0 / mCount);

        mRectF = new RectF();
        mPaint = new Paint();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mDesiredWidth = 800;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            // 精确模式--对应具体值和MATCH_PARENT
            mWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            // 最大不能超过mDesiredWidth--对应WRAP_CONTENT--widthSize作为最大值
            mWidth = Math.min(widthSize, mDesiredWidth);
        } else {
            // 未指定大小
            mWidth = mDesiredWidth;
        }

        mCenter = mWidth / 2;

        // 半径--防止边界溢出--可以自测
        mRadius = mWidth / 2 - 50;

        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        float startAngle = - (mAngle / 2) - 90;
        for (int i = 0; i < getChildCount(); i++) {
            int imgWidth = (mRadius * 2) / 5;

            float angle = (float) Math.toRadians(startAngle + mAngle / 2);

            float x = ( float ) (width / 2 + (mRadius / 2 + mRadius / 12) * Math.cos(angle));
            float y = ( float ) (height / 2 + (mRadius / 2 + mRadius / 12) * Math.sin(angle));

            getChildAt(i).layout((int) (x - imgWidth / 2), (int) (y - imgWidth / 2),
                    (int) (x + imgWidth / 2), (int) (y + imgWidth / 2));

            startAngle = startAngle + mAngle;
        }
    }

    @Override
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        // 绘制扇形
        float startAngle = - (mAngle / 2) - 90;
        mPaint.setColor(Color.YELLOW);
        mPaint.setAntiAlias(true);
        for (int i = 0; i < mCount; i++) {
            // 范围是整个圆盘
            mRectF.left = mCenter - mRadius;
            mRectF.top = mCenter - mRadius;
            mRectF.right = mCenter + mRadius;
            mRectF.bottom = mCenter + mRadius;
            canvas.drawArc(mRectF, startAngle, mAngle, true, mPaint);

            startAngle = startAngle + mAngle;
        }

        // 绘制间隔线
        startAngle = - (mAngle / 2) - 90;
        mPaint.reset();
        mPaint.setColor(Color.parseColor("#fd466e"));
        mPaint.setStrokeWidth(3.0f);
        mPaint.setAntiAlias(true);
        for (int i = 0; i < mCount; i++) {
            // 计算公式
            float dstX = (float) (mCenter + mRadius * Math.cos(startAngle * Math.PI / 180));
            float dstY = (float) (mCenter + mRadius * Math.sin(startAngle * Math.PI / 180));
            canvas.drawLine(mCenter, mCenter, dstX, dstY, mPaint);

            startAngle = startAngle + mAngle;
        }
    }

    /**
     * 设置分区的数量
     * @param count 分区的数量
     */
    public void setCount(int count) {
        if (mCount <= 0) {
            throw new IllegalArgumentException("Partition Count Must > 0");
        }
        mCount = count;
        setAngle();
    }

    private void setAngle() {
        mAngle = (float) (360.0 / mCount);
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setData(List<LuckyBean> luckyList) {
        mLuckyList = luckyList;
        // 设置ImageView
        for (int i = 0; i < luckyList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            addView(imageView);
            mImageList.add(imageView);
        }
        // 重绘
        postInvalidate();
    }

    public List<LuckyBean> getData() {
        return mLuckyList;
    }

    public List<ImageView> getImageList() {
        return mImageList;
    }
}
