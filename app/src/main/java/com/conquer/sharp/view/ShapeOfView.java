package com.conquer.sharp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.conquer.sharp.manager.ClipManager;
import com.conquer.sharp.manager.ClipPathManager;

/**
 * 特殊形状的View
 */
public abstract class ShapeOfView extends FrameLayout {

    // 裁剪Paint
    private final Paint clipPaint = new Paint();
    // 裁剪路径
    private final Path clipPath = new Path();

    protected PorterDuffXfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

    private boolean requiresShapeUpdate = true;

    private ClipManager clipManager = new ClipPathManager();

    private Bitmap clipBitmap;
    protected Drawable drawable = null;

    // 矩形的Path
    final Path rectView = new Path();

    public ShapeOfView(@NonNull Context context) {
        super(context);
        init();
    }

    public ShapeOfView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShapeOfView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        clipPaint.setAntiAlias(true);
        clipPaint.setStyle(Paint.Style.FILL);
        clipPaint.setStrokeWidth(1);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            // 图像混合模式
            clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            // 禁用硬件加速
            setLayerType(LAYER_TYPE_SOFTWARE, clipPaint);
        } else {
            clipPaint.setXfermode(pdMode);
            // 禁用硬件加速
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    public void setBackground(Drawable background) {
        // disabled here, please set a background to to this view child
        // super.setBackground(background);
    }

    @Override
    public void setBackgroundResource(int resid) {
        // disabled here, please set a background to to this view child
        // super.setBackgroundResource(resid);
    }

    @Override
    public void setBackgroundColor(int color) {
        // disabled here, please set a background to to this view child
        // super.setBackgroundColor(color);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            requiresShapeUpdate();
        }
    }

    public void requiresShapeUpdate() {
        this.requiresShapeUpdate = true;
        postInvalidate();
    }

    private boolean requiresBitmap() {
        return isInEditMode() || (clipManager != null && clipManager.requiresBitmap()) || drawable != null;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (requiresShapeUpdate) {
            calculateLayout(canvas.getWidth(), canvas.getHeight());
            requiresShapeUpdate = false;
        }

        if (requiresBitmap()) {
            clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(clipBitmap, 0, 0, clipPaint);
        } else {
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1){
                canvas.drawPath(clipPath, clipPaint);
            } else {
                canvas.drawPath(rectView, clipPaint);
            }
        }

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            setLayerType(LAYER_TYPE_HARDWARE, null);
        }
    }

    private void calculateLayout(int width, int height) {
        // 首先绘制一个矩形
        rectView.reset();
        rectView.addRect(0f, 0f, 1f * getWidth(), 1f * getHeight(), Path.Direction.CW);

        if (clipManager != null) {
            if (width > 0 && height > 0) {
                clipManager.setupClipLayout(width, height);
                clipPath.reset();
                clipPath.set(clipManager.createMask(width, height));

                if (requiresBitmap()) {
                    if (clipBitmap != null) {
                        clipBitmap.recycle();
                    }
                    clipBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    final Canvas canvas = new Canvas(clipBitmap);

                    if (drawable != null) {
                        drawable.setBounds(0, 0, width, height);
                        drawable.draw(canvas);
                    } else {
                        canvas.drawPath(clipPath, clipManager.getPaint());
                    }
                }

                // invert the path for android P
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                    final boolean success = rectView.op(clipPath, Path.Op.DIFFERENCE);
                }

                // this needs to be fixed for 25.4.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(this) > 0f) {
                    try {
                        // 好像是阴影设置
                        setOutlineProvider(getOutlineProvider());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        postInvalidate();
    }

    // 核心--设置路径创建器
    public void setClipPathCreator(ClipPathManager.ClipPathCreator createClipPath) {
        ((ClipPathManager) clipManager).setClipPathCreator(createClipPath);
        requiresShapeUpdate();
    }
}
