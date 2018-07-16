package com.conquer.sharp.ptr;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by ac on 18/7/9.
 *
 */

public class SwipeCircleProgressView extends View implements Runnable {
    // 绘制周期
    private static final int PERIOD = 16;
    private Paint progressPaint;
    private Paint bgPaint;
    // View的宽度
    private int width;
    // View的高度
    private int height;

    // 是否正在绘制
    private boolean isOnDraw = false;
    // 是否正在运行
    private boolean isRunning = false;

    private RectF bgRect = null;
    private RectF ovalRect = null;

    private int startAngle = 0;
    private int speed = 8;
    private float density = 1.0f;

    private int swipeAngle;

    private int mProgressColor = 0xFFCCCCCC;
    private int mCircleBackgroundColor = 0xFFFFFFFF;
    private int mShadowColor = 0xFF999999;

    public SwipeCircleProgressView(Context context) {
        super(context);
        init(context);
    }

    public SwipeCircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SwipeCircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 圆形背景带阴影
        // canvas.drawArc(getBgRect(), 0, 360, false, createBgPaint());
        canvas.drawArc(getOvalRect(), 0, 360, false, createCirclePaint());

        int index = startAngle / 360;
        if (index % 2 == 0) {
            swipeAngle = (startAngle % 720) / 2;
        } else {
            swipeAngle = 360 - (startAngle % 720) / 2;
        }

        canvas.drawArc(getOvalRect(), startAngle, swipeAngle, false, createPaint());
    }

    private RectF getBgRect() {
        width = getWidth();
        height = getHeight();

        if (bgRect == null) {
            int offset = (int) (density * 2);
            bgRect = new RectF(offset, offset, width - offset, height - offset);
        }

        return bgRect;
    }

    private RectF getOvalRect() {
        width = getWidth();
        height = getHeight();
        if (ovalRect == null) {
            int offset = (int) (density * 8);
            ovalRect = new RectF(offset, offset, width - offset, height - offset);
        }
        return ovalRect;
    }


    /**
     * 创建画笔
     */
    private Paint createPaint() {
        if (progressPaint == null) {
            progressPaint = new Paint();
            progressPaint.setStrokeWidth((int) (density * 2));
            progressPaint.setStyle(Paint.Style.STROKE);
            progressPaint.setAntiAlias(true);
        }
        progressPaint.setColor(mProgressColor);
        return progressPaint;
    }

    private Paint createCirclePaint() {
        if (progressPaint == null) {
            progressPaint = new Paint();
            progressPaint.setStrokeWidth((int) (density * 2));
            progressPaint.setStyle(Paint.Style.STROKE);
            progressPaint.setAntiAlias(true);
        }
        progressPaint.setColor(mCircleBackgroundColor);
        return progressPaint;
    }

    private Paint createBgPaint() {
        if (bgPaint == null) {
            bgPaint = new Paint();
            bgPaint.setColor(mCircleBackgroundColor);
            bgPaint.setStyle(Paint.Style.FILL);
            bgPaint.setAntiAlias(true);
            // LAYER_TYPE_HARDWARE即为使用硬件加速--GPU
            // LAYER_TYPE_SOFTWARE使用CPU进行绘制
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setLayerType(LAYER_TYPE_SOFTWARE, bgPaint);
            }
            bgPaint.setShadowLayer(4.0f, 0.0f, 2.0f, mShadowColor);
        }
        return bgPaint;
    }

    // 重写run函数
    @Override
    public void run() {
        while (isOnDraw) {
            isRunning = true;
            long startTime = System.currentTimeMillis();
            startAngle += speed;
            postInvalidate();
            long time = System.currentTimeMillis() - startTime;
            if (time < PERIOD) {
                try {
                    Thread.sleep(PERIOD - time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
    }

    public void setCircleBackgroundColor(int circleBackgroundColor) {
        mCircleBackgroundColor = circleBackgroundColor;
    }

    public void setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
    }

    public void setPullDistance(int distance) {
        startAngle = distance * 2;
        postInvalidate();
    }

    public void setOnDraw(boolean isOnDraw) {
        this.isOnDraw = isOnDraw;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isRunning() {
        return isRunning;
    }

    // 当前窗体得到或失去焦点的时候的时候调用--这是这个活动是否是用户可见的最好的指标
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onDetachedFromWindow() {
        isOnDraw = false;
        super.onDetachedFromWindow();
    }
}
