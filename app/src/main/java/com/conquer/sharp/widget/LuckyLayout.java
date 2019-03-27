package com.conquer.sharp.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.util.system.ScreenUtils;
import com.conquer.sharp.view.LuckyWheelView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 支持两种旋转模式--转盘或者指针
 */
public class LuckyLayout extends FrameLayout {

    public final static int ROTATION_TYPE_WHEEL = 0;
    public final static int ROTATION_TYPE_POINTER = 1;

    @IntDef({ROTATION_TYPE_WHEEL, ROTATION_TYPE_POINTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RotationType {

    }

    private LuckyWheelView mLuckyWheelView;
    private ImageView mLuckyPointer;

    private int rotationType = ROTATION_TYPE_WHEEL;
    private boolean isRotating = false;

    private ObjectAnimator mObjectAnimator;

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isRotating = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            View target = rotationType == ROTATION_TYPE_WHEEL ? mLuckyWheelView : mLuckyPointer;
            // 回收view在GPU的off-screen中的缓存。
            target.setLayerType(LAYER_TYPE_NONE, null);

            isRotating = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isRotating = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public LuckyLayout(Context context) {
        this(context, null);
    }

    public LuckyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mLuckyWheelView = new LuckyWheelView(getContext());
        // 根据图片的宽度来设置大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(getResources(), R.drawable.bg_lucky_champion, options);
        int desireWidth = options.outWidth - 96;
        if (ScreenUtils.screenWidth < desireWidth) {
            desireWidth = ScreenUtils.screenWidth - 132;
        }
        Log.d("LuckyLayout", "desireWidth : " + desireWidth);
        LayoutParams layoutParams = new LayoutParams(desireWidth, desireWidth);
        layoutParams.gravity = Gravity.CENTER;
        addView(mLuckyWheelView, layoutParams);
        mLuckyPointer = new ImageView(getContext());
        mLuckyPointer.setImageResource(R.drawable.ic_lucky_pointer);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(mLuckyPointer, params);
    }

    public void turnByAngle() {

    }

    public void turnByCount(int turnCount, int seconds) {
        int giftCount = mLuckyWheelView.getData().size();
        if (isRotating || giftCount == 0 || turnCount == 0) {
            return;
        }

        View target = rotationType == ROTATION_TYPE_WHEEL ? mLuckyWheelView : mLuckyPointer;
        int perAngle = 360 / giftCount;

        turning(target, turnCount, perAngle, seconds);
    }

    private void turning(View target, int turnCount, float perAngle, int seconds) {
        float startAngle = target.getRotation();
        float endAngle = startAngle + turnCount * perAngle;

        // 开启GPU的off-screen缓存区, 提高动画的流畅度, 一定要记得在动画完成之后回收该缓存
        target.setLayerType(LAYER_TYPE_HARDWARE, null);

        mObjectAnimator = ObjectAnimator.ofFloat(target, View.ROTATION, startAngle, endAngle).setDuration(seconds * 1000);
        mObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mObjectAnimator.addListener(mAnimatorListener);
        mObjectAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // stop animation
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }
    }

    public LuckyWheelView getLuckyWheelView() {
        return mLuckyWheelView;
    }

    public int getRotationType() {
        return rotationType;
    }

    public void setRotationType(@RotationType int rotationType) {
        this.rotationType = rotationType;
    }

    public void setBackground(int level) {
        switch (level) {
            case 1:
                setBackgroundResource(R.drawable.bg_lucky_third);
                break;
            case 2:
                setBackgroundResource(R.drawable.bg_lucky_second);
                break;
            case 3:
                setBackgroundResource(R.drawable.bg_lucky_champion);
                break;
        }
    }

    public void reset() {
        // stop animation
        if (mObjectAnimator != null && mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }

        // reset rotation.
        mLuckyWheelView.setRotation(0);
        mLuckyPointer.setRotation(0);
    }
}
