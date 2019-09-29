package com.conquer.sharp.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘
 */
public class SoftInputUtils {
    /**
     * 隐藏软键盘
     */
    public static void hideSoftInputFromWindow(Context context, View view) {
        if (view != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
            }
        }
    }

    /**
     * 根据activity当前获取焦点的控件显示软键盘 the input window will always be displayed
     */
    public static void showSoftInputFromActivity(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view != null && imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 根据activity当前获取焦点的控件隐藏软键盘
     */
    public static void hideSoftInputFromActivity(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }
}
