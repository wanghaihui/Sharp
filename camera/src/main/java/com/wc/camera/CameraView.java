package com.wc.camera;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CameraView extends FrameLayout {

    private final CallbackBridge mCallbacks;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {
            mCallbacks = null;
            return;
        }

        final PreviewImpl preview = createPreviewImpl(context);
        mCallbacks = new CallbackBridge();
    }

    private class CallbackBridge implements CameraViewImpl.Callback {

        @Override
        public void onCameraOpened() {

        }

        @Override
        public void onCameraClosed() {

        }

        @Override
        public void onPictureTaken(byte[] data) {

        }

    }

    @NonNull
    private PreviewImpl createPreviewImpl(Context context) {
        PreviewImpl preview;
        if (Build.VERSION.SDK_INT < 14) {
            // sdk 14
            preview = new SurfaceViewPreview(context, this);
        } else {
            preview = new TextureViewPreview(context, this);
        }
        return preview;
    }
}
