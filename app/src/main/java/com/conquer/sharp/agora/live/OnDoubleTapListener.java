package com.conquer.sharp.agora.live;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnDoubleTapListener implements View.OnTouchListener {

    private View mView;
    private GestureDetector mGestureDetector;

    public OnDoubleTapListener(Context context) {
        mGestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        mView = view;

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            view.performClick();
        }

        return mGestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            OnDoubleTapListener.this.onDoubleTap(mView, event);
            return super.onDoubleTap(event);
        }

        public boolean onSingleTapUp(MotionEvent event) {
            OnDoubleTapListener.this.onSingleTapUp(mView, event);
            return super.onSingleTapUp(event);
        }
    }

    public void onDoubleTap(View view, MotionEvent event) {

    }

    public void onSingleTapUp(View view, MotionEvent event) {

    }

}
