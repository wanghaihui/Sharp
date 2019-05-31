package com.conquer.sharp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.conquer.sharp.R;
import com.conquer.sharp.util.system.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 涟漪效果
 *
 * layout_width和layout_height--只能设置match_parent或者具体数值，不能设置wrap_content
 */
public class RippleView extends View {
    private static final int DESIRED_WIDTH_HEIGHT = 96;

    // 颜色
    private int mRippleColor;
    // 速度
    private int mRippleSpeed;
    // 密度
    private int mRippleDensity;
    // 透明度变化--随半径而变化

    // 画圆

    // 画笔
    private Paint mPaint;

    // View的宽和高
    private int mWidth;
    private int mHeight;

    // 圆圈集合
    private List<Circle> mRippleList;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RippleView);
        mRippleColor = ta.getColor(R.styleable.RippleView_ripple_color, ContextCompat.getColor(context,
                R.color.color_da3156));
        mRippleSpeed = ta.getInteger(R.styleable.RippleView_ripple_speed, 1);
        mRippleDensity = ta.getInteger(R.styleable.RippleView_ripple_density, 6);
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mRippleColor);

        mRippleList = new ArrayList<>();
        mRippleList.add(new Circle(0, 255));
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算宽高
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private int measureWidth(int widthMeasureSpec) {
        int desiredWidth = ScreenUtils.dip2px(DESIRED_WIDTH_HEIGHT);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            return widthSpecSize;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            return Math.min(widthSpecSize, desiredWidth);
        } else {
            return desiredWidth;
        }
    }

    private int measureHeight(int heightMeasureSpec) {
        int desiredHeight = ScreenUtils.dip2px(DESIRED_WIDTH_HEIGHT);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            return heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            return Math.min(heightSpecSize, desiredHeight);
        } else {
            return desiredHeight;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制正方形的内切圆
        drawInCircle(canvas);

    }

    private void drawInCircle(Canvas canvas) {
        canvas.save();

        for (int i = 0; i < mRippleList.size(); i++) {
            Circle circle = mRippleList.get(i);
            mPaint.setAlpha(circle.alpha);
            // 循环画圆
            canvas.drawCircle(mWidth / 2, mHeight / 2, circle.radius, mPaint);

            if (circle.radius > (mWidth / 2)) {
                // 重置
                circle.radius = 0;
                circle.alpha = 255;
            } else {
                circle.alpha = (int) (255 - circle.radius * (255 / (double) (mWidth / 2)));
                circle.radius += mRippleSpeed;
            }
        }

        if (mRippleList.size() > 0 && (mRippleList.size() <= (mWidth / (2 * ScreenUtils.dip2px(mRippleDensity))))) {
            if (mRippleList.get(mRippleList.size() - 1).radius > ScreenUtils.dip2px(mRippleDensity)) {
                mRippleList.add(new Circle(0, 255));
            }
        }

        invalidate();

        canvas.restore();
    }

    public static class Circle {
        // 半径
        int radius;
        // 透明度
        int alpha;

        Circle(int radius, int alpha) {
            this.radius = radius;
            this.alpha = alpha;
        }
    }
}
