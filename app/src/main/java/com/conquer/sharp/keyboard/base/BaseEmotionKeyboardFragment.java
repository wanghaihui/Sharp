package com.conquer.sharp.keyboard.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 计算键盘高度, 提供弹出和隐藏键盘等功能
 */
public abstract class BaseEmotionKeyboardFragment extends Fragment implements KeyboardDetector.KeyboardObserver {
    private KeyboardDetector mKeyboardDetector;

    protected Handler mHandler = new Handler();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mKeyboardDetector = new KeyboardDetector(getActivity());
        mKeyboardDetector.setKeyboardObserver(this);
        new Handler().post(() -> mKeyboardDetector.start());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initViews() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onKeyboardShow(int height, int orientation) {

    }

    @Override
    public void onKeyboardHide() {

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDestroy() {
        super.onDestroy();
        mKeyboardDetector.close();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 按下返回
     * @return true表示已经被处理, activity不会响应自己的后退操作
     */
    public boolean onBackPressed() {
        return false;
    }
}
