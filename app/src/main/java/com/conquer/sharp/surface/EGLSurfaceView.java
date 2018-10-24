package com.conquer.sharp.surface;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * GLSurfaceView的扩展
 *
 * 1.共用一个EGLContext
 *
 */
public class EGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback2 {
    private static final String TAG = "EGLSurfaceView";

    public EGLSurfaceView(Context context) {
        super(context);
        init();
    }

    public EGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
    }

    public void onPause() {

    }

    public void onResume() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void surfaceCreated(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return

    }

    @Override
    public void surfaceRedrawNeededAsync(SurfaceHolder holder, Runnable finishDrawing) {

    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {
        // Since we are part of the framework we know only surfaceRedrawNeededAsync
        // will be called.
    }
}
