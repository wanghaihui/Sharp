package com.conquer.sharp.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CameraView extends FrameLayout {

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (isInEditMode()) {

            return;
        }
    }
}
