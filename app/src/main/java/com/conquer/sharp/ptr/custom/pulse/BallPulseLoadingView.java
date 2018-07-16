package com.conquer.sharp.ptr.custom.pulse;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.conquer.sharp.R;
import com.conquer.sharp.util.system.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ac on 18/7/16.
 *
 */

public class BallPulseLoadingView extends View {
    private static final String TAG = "BallPulseLoadingView";

    public static final int DEFAULT_SIZE = 48;

    protected boolean mManualNormalColor;
    protected boolean mManualAnimationColor;

    protected Paint mPaint;

    protected int mNormalColor = 0xffeeeeee;
    protected int mAnimatingColor = 0xffe75946;

    protected float mCircleSpacing;
    protected float[] mScaleFloats = new float[]{1f, 1f, 1f};


    protected boolean mIsStarted = false;
    protected ArrayList<ValueAnimator> mAnimators;
    protected Map<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mUpdateListeners = new HashMap<>();;

    public BallPulseLoadingView(@NonNull Context context) {
        this(context, null);
    }

    public BallPulseLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallPulseLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final View thisView = this;
        thisView.setMinimumHeight(ScreenUtil.dip2px(DEFAULT_SIZE));

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.BallPulseLoadingView);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        if (t.hasValue(R.styleable.BallPulseLoadingView_srlNormalColor)) {
            setNormalColor(t.getColor(R.styleable.BallPulseLoadingView_srlNormalColor, 0));
        }
        if (t.hasValue(R.styleable.BallPulseLoadingView_srlAnimatingColor)) {
            setAnimatingColor(t.getColor(R.styleable.BallPulseLoadingView_srlAnimatingColor, 0));
        }

        t.recycle();

        mCircleSpacing = ScreenUtil.dip2px(4);

        mAnimators = new ArrayList<>();
        final int[] delays = new int[]{120, 240, 360};
        for (int i = 0; i < 3; i++) {
            final int index = i;

            ValueAnimator animator = ValueAnimator.ofFloat(1, 0.3f, 1);

            animator.setDuration(750);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setTarget(i);
            animator.setStartDelay(delays[i]);

            mUpdateListeners.put(animator, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mScaleFloats[index] = (float) animation.getAnimatedValue();
                    thisView.postInvalidate();
                }
            });
            mAnimators.add(animator);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimators != null) for (int i = 0; i < mAnimators.size(); i++) {
            mAnimators.get(i).cancel();
            mAnimators.get(i).removeAllListeners();
            mAnimators.get(i).removeAllUpdateListeners();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final View thisView = this;
        final int width = thisView.getWidth();
        final int height = thisView.getHeight();
        float radius = (Math.min(width, height) - mCircleSpacing * 2) / 9;
        float x = width / 2 - (radius * 2 + mCircleSpacing);
        float y = height / 2;
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + mCircleSpacing * i;
            canvas.translate(translateX, y);
            canvas.scale(mScaleFloats[i], mScaleFloats[i]);
            canvas.drawCircle(0, 0, radius, mPaint);
            canvas.restore();
        }
        super.dispatchDraw(canvas);

        onStartAnimator();
    }

    public void refresh() {
        invalidate();
    }

    public void onStartAnimator() {
        if (mIsStarted) return;

        for (int i = 0; i < mAnimators.size(); i++) {
            ValueAnimator animator = mAnimators.get(i);

            // when the animator restart , add the updateListener again because they was removed by animator stop .
            ValueAnimator.AnimatorUpdateListener updateListener = mUpdateListeners.get(animator);
            if (updateListener != null) {
                animator.addUpdateListener(updateListener);
            }
            animator.start();
        }
        mIsStarted = true;
        mPaint.setColor(mAnimatingColor);
    }

    public int onFinish() {
        if (mAnimators != null && mIsStarted) {
            mIsStarted = false;
            mScaleFloats = new float[]{1f, 1f, 1f};
            for (ValueAnimator animator : mAnimators) {
                if (animator != null) {
                    animator.removeAllUpdateListeners();
                    animator.end();
                }
            }
        }
        mPaint.setColor(mNormalColor);
        return 0;
    }

    public BallPulseLoadingView setNormalColor(@ColorInt int color) {
        mNormalColor = color;
        mManualNormalColor = true;
        if (!mIsStarted) {
            mPaint.setColor(color);
        }
        return this;
    }

    public BallPulseLoadingView setAnimatingColor(@ColorInt int color) {
        mAnimatingColor = color;
        mManualAnimationColor = true;
        if (mIsStarted) {
            mPaint.setColor(color);
        }
        return this;
    }
}
