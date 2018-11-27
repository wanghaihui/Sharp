package com.conquer.sharp.camera.base;

import android.view.Surface;
import android.view.View;

/**
 * Encapsulates all the operations related to camera preview in a backward-compatible manner.
 */
public abstract class PreviewImpl {

    public abstract Surface getSurface();
    public abstract View getView();
    public abstract Class getOutputClass();
    public abstract void setDisplayOrientation(int displayOrientation);


    private int mWidth;
    private int mHeight;

    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onSurfaceChanged();
    }
}
