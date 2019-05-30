package com.conquer.sharp.agora.live;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class TouchClickFrameLayout extends FrameLayout {

    public TouchClickFrameLayout(Context context) {
        this(context, null);
    }

    public TouchClickFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchClickFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performClick();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
