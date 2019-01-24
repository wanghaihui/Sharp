package com.wc.camera;

import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * Encapsulates(囊括) all the operations related to camera preview in a backward-compatible manner(以一种向后兼容的方式).
 */
public abstract class PreviewImpl {

    public abstract Surface getSurface();
    public abstract View getView();
    public abstract Class getOutputClass();
    public abstract void setDisplayOrientation(int displayOrientation);
    public abstract boolean isReady();

    // 只有子类才可以继承
    protected void dispatchSurfaceChanged() {
        mCallback.onSurfaceChanged();
    }

    public SurfaceHolder getSurfaceHolder() {
        return null;
    }

    public Object getSurfaceTexture() {
        return null;
    }

    public void setBufferSize(int width, int height) {

    }

    private int mWidth;
    private int mHeight;

    void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onSurfaceChanged();
    }
}
