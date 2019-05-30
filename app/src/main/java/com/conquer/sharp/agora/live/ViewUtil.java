package com.conquer.sharp.agora.live;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.conquer.sharp.agora.Constant;

public class ViewUtil {

    private static final int DEFAULT_TOUCH_TIMESTAMP = -1; // first time
    private static final int TOUCH_COOL_DOWN_TIME = 500; // ms

    private static long mLastTouchTime = DEFAULT_TOUCH_TIMESTAMP;

    public static boolean checkDoubleTouchEvent(MotionEvent event, View view) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) { // only check touch down event
            if (mLastTouchTime == DEFAULT_TOUCH_TIMESTAMP || (SystemClock.elapsedRealtime() - mLastTouchTime) >= TOUCH_COOL_DOWN_TIME) {
                // 系统从启动到现在的时间
                mLastTouchTime = SystemClock.elapsedRealtime();
            } else {
                Log.w(Constant.LOG_TAG, "too many touch events " + view + " " + MotionEvent.ACTION_DOWN);
                return true;
            }
        }
        return false;
    }

    public static boolean checkDoubleKeyEvent(KeyEvent event, View view) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (mLastTouchTime != DEFAULT_TOUCH_TIMESTAMP && (SystemClock.elapsedRealtime() - mLastTouchTime) < TOUCH_COOL_DOWN_TIME) {
                Log.w(Constant.LOG_TAG, "too many key events " + view + " " + KeyEvent.ACTION_DOWN);
                return true;
            }
            mLastTouchTime = SystemClock.elapsedRealtime();
        }
        return false;
    }

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
