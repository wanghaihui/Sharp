package com.conquer.sharp.keyboard.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.conquer.sharp.R;
import com.conquer.sharp.keyboard.KeyboardConstants;
import com.conquer.sharp.util.LogHelper;
import com.conquer.sharp.util.SharedPrefsUtils;
import com.conquer.sharp.util.SoftInputUtils;

/**
 * 键盘与表情切换操作
 */
public abstract class EmotionKeyboardFragment extends BaseEmotionKeyboardFragment {

    public enum KeyboardStatus {
        NONE, // 什么都没显示
        KEYBOARD, // 显示键盘输入
        EMOTION_BOARD // 显示表情输入
    }

    private KeyboardStatus mKeyboardStatus = KeyboardStatus.NONE;

    private KeyboardListener mKeyboardListener;

    private int mKeyboardHeight = 0;

    private ViewGroup mHeightControlView;
    private ViewGroup mEmotionContentView;

    private int duration = 200;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return createView();
    }

    private View createView() {
        LinearLayout root = new LinearLayout(getContext());
        mHeightControlView = new LinearLayout(getContext());
        if (getContext() != null) {
            mHeightControlView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_efefef));
        }
        root.addView(mHeightControlView,
                new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        mEmotionContentView = new LinearLayout(getContext());
        mEmotionContentView.setId(R.id.keyboard_emoji_view);
        mHeightControlView.addView(mEmotionContentView,
                new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return root;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 这段为核心实现
    @Override
    public void onKeyboardShow(int height, int orientation) {
        LogHelper.d(KeyboardConstants.LOG_TAG, "passive show keyboard");
        // 点击输入框弹出的键盘
        if (mKeyboardStatus != KeyboardStatus.KEYBOARD) {
            notifyAndSetStatus(KeyboardStatus.KEYBOARD);
        }

        // 第一次弹出键盘
        if (mKeyboardHeight == 0) {
            mKeyboardHeight = height;
        }

        if (mKeyboardStatus == KeyboardStatus.EMOTION_BOARD) {
            // 此时表情正在显示, 先隐藏表情
            showEmotionView(false);
        }

        int lastSavedKeyboardHeight = getLastSavedKeyboardHeight();
        if (lastSavedKeyboardHeight == 0 || lastSavedKeyboardHeight != height) {
            // 首次弹出键盘或者更换了键盘, 需要在此进行高度的动画
            keyboardHeightChanged(height);
            // 让表情平移下去, 不可见
            showEmotionView(false);
            saveKeyboardHeight(height);
            putConstantKeyboardHeight(height);
        }
    }
    @Override
    public void onKeyboardHide() {
        super.onKeyboardHide();
        saveKeyboardHeight(0);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 主动显示键盘
     */
    public void showKeyBoard() {
        LogHelper.d(KeyboardConstants.LOG_TAG, "initiative show keyboard");
        if (mKeyboardStatus == KeyboardStatus.KEYBOARD) {
            return;
        }
        notifyAndSetStatus(KeyboardStatus.KEYBOARD);

        if (onGetEditText() != null) {
            onGetEditText().requestFocus();
        }

        int lastSavedKeyboardHeight = getLastSavedKeyboardHeight();
        if (lastSavedKeyboardHeight > 0) {
            // 保存过高度, 先进行动画再弹出键盘用户体验好
            keyboardHeightChanged(lastSavedKeyboardHeight);
            showEmotionView(false);
            mHandler.postDelayed(() -> {
                if (getActivity() != null) {
                    SoftInputUtils.showSoftInputFromActivity(getActivity(), onGetEditText());
                }
            }, duration / 2);
        } else {
            if (getActivity() != null) {
                SoftInputUtils.showSoftInputFromActivity(getActivity(), onGetEditText());
            }
        }
    }
    public void showEmojiBoard() {
        if (mKeyboardStatus == KeyboardStatus.EMOTION_BOARD) {
            return;
        }
        notifyAndSetStatus(KeyboardStatus.EMOTION_BOARD);
        SoftInputUtils.hideSoftInputFromWindow(getActivity(), onGetEditText());
        // 当前高度过渡到表情高度
        keyboardHeightChanged(calcEmojiBoardHeight());
        emojiHeightChanged(calcEmojiBoardHeight());
        // 等键盘下去后再让表情区域平移上来
        mHandler.postDelayed(() -> {
            showEmotionView(true);
        }, duration);
    }
    protected int calcEmojiBoardHeight() {
        int emojiKeyboardHeight = onGetEmojiBoardHeight() <= 0 ? mKeyboardHeight : onGetEmojiBoardHeight();
        if (emojiKeyboardHeight == 0) {
            emojiKeyboardHeight = getConstantKeyboardHeight();
        }
        return emojiKeyboardHeight;
    }

    private void emojiHeightChanged(int height) {
        mEmotionContentView.getLayoutParams().height = height;
        mEmotionContentView.setLayoutParams(mEmotionContentView.getLayoutParams());
    }

    /**
     * 主动显示表情
     */
    private void showEmotionView(boolean visible) {
        mEmotionContentView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
    /**
     * 键盘高度发生改变
     */
    protected void keyboardHeightChanged(int endHeight) {
        int startHeight = mHeightControlView.getHeight();
        if (startHeight == endHeight) {
            return;
        } else {
            setKeyboardHeight(endHeight);
        }
        notifyKeyboardHeightChange(endHeight);
    }

    private void setKeyboardHeight(int height) {
        mHeightControlView.getLayoutParams().height = height;
        mHeightControlView.setLayoutParams(mHeightControlView.getLayoutParams());
    }

    private void notifyKeyboardHeightChange(int height) {
        if (mKeyboardListener != null) {
            mKeyboardListener.onBoardHeightChanged(height);
        }
    }

    private int getLastSavedKeyboardHeight() {
        return SharedPrefsUtils.getInt(getContext(), SharedPrefsUtils.KEYBOARD_HEIGHT, 0);
    }

    private void saveKeyboardHeight(int height) {
        SharedPrefsUtils.putInt(getContext(), SharedPrefsUtils.KEYBOARD_HEIGHT, height);
    }

    private int getConstantKeyboardHeight() {
        return SharedPrefsUtils.getInt(getContext(), SharedPrefsUtils.CONSTANT_KEYBOARD_HEIGHT, 0);
    }

    private void putConstantKeyboardHeight(int height) {
        SharedPrefsUtils.putInt(getContext(), SharedPrefsUtils.CONSTANT_KEYBOARD_HEIGHT, height);
    }


    protected void notifyAndSetStatus(KeyboardStatus newStatus) {
        if (mKeyboardStatus != newStatus) {
            mKeyboardStatus = newStatus;
            onKeyboardStatusChanged(newStatus);
            if (mKeyboardListener != null) {
                mKeyboardListener.onBoardStatusChanged(newStatus);
            }
        }
    }

    /**
     * 关闭键盘
     */
    public void closeKeyboard() {
        notifyAndSetStatus(KeyboardStatus.NONE);
        keyboardHeightChanged(0);
        SoftInputUtils.hideSoftInputFromWindow(getActivity(), onGetEditText());
    }

    /**
     * 重写以设置表情container最大高度
     * return <= 0表示使用键盘高度
     */
    protected abstract int onGetEmojiBoardHeight();
    protected abstract void onKeyboardStatusChanged(KeyboardStatus status);
    protected abstract EditText onGetEditText();

    public void setKeyboardListener(KeyboardListener mKeyboardListener) {
        this.mKeyboardListener = mKeyboardListener;
    }

    /**
     * 键盘与表情区域的状态监听器
     */
    public interface KeyboardListener {
        void onBoardHeightChanged(int height);
        void onBoardStatusChanged(KeyboardStatus status);
    }

    public KeyboardStatus getKeyboardStatus() {
        return mKeyboardStatus;
    }

    public int getKeyboardContainerId() {
        return mEmotionContentView.getId();
    }
}
