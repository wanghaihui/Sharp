package com.conquer.sharp.keyboard.base;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 借助PopupWindow检测
 */
public class KeyboardDetector extends PopupWindow {

    private View popupView;
    private View rootView;
    private Activity mActivity;

    private KeyboardObserver mObserver;
    private int preHeight = 0;

    KeyboardDetector(Activity activity) {
        super(activity);
        this.mActivity = activity;
        popupView = new View(activity);
        setContentView(popupView);

        // 划重点--Activity中不能设置SOFT_INPUT_ADJUST_RESIZE
        // 只有在这里设置一次SOFT_INPUT_ADJUST_RESIZE
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        rootView = activity.findViewById(android.R.id.content);

        setWidth(0); // 这样既能测量高度，又不会导致界面不能点击
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);

        popupView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (popupView != null) {
                    handleOnGlobalLayout();
                }
            }
        });
    }

    private void handleOnGlobalLayout() {
        Rect rect = new Rect();
        popupView.getWindowVisibleDisplayFrame(rect);
        int orientation = getScreenOrientation();
        int keyboardHeight = rootView.getHeight() - rect.bottom;
        if (preHeight == keyboardHeight) {
            return;
        }
        preHeight = keyboardHeight;
        notifyKeyboardHeightChanged(keyboardHeight, orientation);
    }

    /**
     * 必须在activity或fragment的view准备就绪后才可以调用此方法弹出popup
     */
    public void start() {
        if (!isShowing() && rootView.getWindowToken() != null) {
            setBackgroundDrawable(new ColorDrawable(0));
            showAtLocation(rootView, Gravity.NO_GRAVITY, 0, 0);
        }
    }

    public void close() {
        mObserver = null;
        dismiss();
    }

    public void setKeyboardObserver(KeyboardObserver observer) {
        mObserver = observer;
    }

    private int getScreenOrientation() {
        return mActivity.getResources().getConfiguration().orientation;
    }

    private void notifyKeyboardHeightChanged(int height, int orientation) {
        if (mObserver != null) {
            if (height > 0) {
                mObserver.onKeyboardShow(height, orientation);
            } else {
                mObserver.onKeyboardHide();
            }
        }
    }

    /**
     * 监听键盘弹出状态
     */
    public interface KeyboardObserver {
        void onKeyboardShow(int height, int orientation);
        void onKeyboardHide();
    }
}
