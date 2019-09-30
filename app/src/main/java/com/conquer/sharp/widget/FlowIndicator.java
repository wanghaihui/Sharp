package com.conquer.sharp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.conquer.sharp.R;

public class FlowIndicator extends View {

    private int mCount;

    private float mSpacing;

    private float mRadius;
    private int mSelected = 0;

    public Bitmap seletedBitmap;

    public Bitmap normalBitmap;

    private Paint mPaint = new Paint();

    public FlowIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator);

        mCount = a.getInteger(R.styleable.FlowIndicator_count, 0);
        mSpacing = a.getDimension(R.styleable.FlowIndicator_spacing, 10);
        mRadius = a.getDimension(R.styleable.FlowIndicator_point_radius, 10);

        if (seletedBitmap == null) {
            seletedBitmap = ((BitmapDrawable)context.getResources().getDrawable(
                    R.drawable.square_indicator_selected)).getBitmap();
        }
        if (normalBitmap == null) {
            normalBitmap = ((BitmapDrawable)context.getResources().getDrawable(
                    R.drawable.square_indicator_normal)).getBitmap();
        }
        a.recycle();
    }

    public void setSeletion(int index) {
        mSelected = index;
        invalidate();
    }

    public void setCount(int count) {
        mCount = count;
        invalidate();
    }

    public void next() {
        if (mSelected < mCount - 1) {
            mSelected++;
        } else {
            mSelected = 0;
        }
        invalidate();
    }

    public void previous() {
        if (mSelected > 0) {
            mSelected--;
        } else {
            mSelected = mCount - 1;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setAntiAlias(true);

        float width = (getWidth() - ((mRadius * 2 * mCount) + (mSpacing * (mCount - 1)))) / 2f;

        for (int i = 0; i < mCount; i++) {
            if (i == mSelected)
                canvas.drawBitmap(seletedBitmap, width + getPaddingLeft() + mRadius + i
                                * (mSpacing + mRadius + mRadius) - seletedBitmap.getWidth() / 2,
                        getHeight() / 2 - seletedBitmap.getHeight() / 2, mPaint);
            else {
                canvas.drawBitmap(normalBitmap, width + getPaddingLeft() + mRadius + i
                                * (mSpacing + mRadius + mRadius) - seletedBitmap.getWidth() / 2,
                        getHeight() / 2 - seletedBitmap.getHeight() / 2, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        } catch (Exception e) {

        }
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int)(getPaddingLeft() + getPaddingRight() + (mCount * 2 * mRadius)
                    + (mCount - 1) * mSpacing + 1);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result + 10; // 为了应对左右两头可能存在的遮挡
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int)(2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}

