package com.conquer.sharp.keyboard.input;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.conquer.sharp.R;
import com.conquer.sharp.keyboard.base.EmotionKeyboardFragment;
import com.conquer.sharp.util.FragmentUtils;
import com.conquer.sharp.util.SoftInputUtils;

public abstract class BaseInputFragment extends EmotionKeyboardFragment {

    private View vEmpty;
    private LinearLayout llContent;
    private LinearLayout llExtra;
    private LinearLayout llKeyboardEmotion;

    private FragmentManager mFragmentManager;

    private boolean blankEnable = true; // 空白区域是否响应关闭输入操作

    /**
     * 输入法关闭模式
     */
    public enum CloseMode {
        CLOSE_NORMAL, // EditText区域不关闭, 只能关闭输入法和表情区域
        CLOSE_INPUT_AFTER_BOARD_CLOSED, // 关闭输入法和表情区域后，再关闭整个输入
        CLOSE_INPUT // 一次后退操作关闭整个输入
    }
    private CloseMode mCloseMode = CloseMode.CLOSE_INPUT; // 关闭模式

    private OnCancelListener onCancelListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_base_input, container, false);
        View keyboardContainer = super.onCreateView(inflater, container, savedInstanceState);

        vEmpty = mainView.findViewById(R.id.v_emtpy);
        llContent = mainView.findViewById(R.id.ll_content);
        llExtra = mainView.findViewById(R.id.ll_extra);
        llKeyboardEmotion = mainView.findViewById(R.id.ll_keyboard_emotion);

        llExtra.addView(onCreateExtraView());
        llKeyboardEmotion.addView(keyboardContainer);
        return mainView;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llContent.setOnClickListener(v -> {/*防止穿透点击*/});
        vEmpty.setOnTouchListener((v, event) -> {
            if (blankEnable) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    notifyOnCancel();
                }
                return true;
            }
            return false;
        });
    }

    public void initViews() {

    }

    // 使用fragment的replace每次创建fragment再添加会很慢, 可以改为先初始化再使用show, hide方式
    // prepare务必执行在activity的view准备就绪时, 否则监测不到键盘弹出
    public void prepare(FragmentManager fragmentManager, int containerId) {
        mFragmentManager = fragmentManager;
        mHandler.post(() -> {
            FragmentUtils.replace(mFragmentManager, this, containerId);
        });
    }

    private void hide() {
        FragmentUtils.hide(mFragmentManager, this);
    }

    private void notifyOnCancel() {
        switch (mCloseMode) {
            case CLOSE_NORMAL:
                if (getKeyboardStatus() == KeyboardStatus.NONE) {
                    return;
                } else {
                    closeKeyboard();
                    notifyCloseListener(false);
                }
                break;
            case CLOSE_INPUT_AFTER_BOARD_CLOSED:
                /*if (getBoradStatus() == BoardStatus.NONE) {
                    closeInput();
                } else {
                    closeBoard();
                    notifyCloseListener(false);
                }*/
                break;
            case CLOSE_INPUT:
                closeInput();
                break;
        }
    }

    public void closeInput() {
        hide();
        saveDraft();
        notifyCloseListener(true);
        SoftInputUtils.hideSoftInputFromWindow(getActivity(), onGetEditText());
    }

    @Override
    protected void onKeyboardStatusChanged(KeyboardStatus status) {

    }

    private void notifyCloseListener(boolean really) {
        if (onCancelListener != null) {
            onCancelListener.onCancel(really);
        }
    }

    // 存草稿
    public void saveDraft() {
        String text = onGetEditText().getText().toString();
    }

    public void setBlankEnable(boolean blankEnable) {
        this.blankEnable = blankEnable;
    }

    public void setCloseMode(CloseMode mCloseMode) {
        this.mCloseMode = mCloseMode;
    }

    // 由子类创建功能条
    public abstract View onCreateExtraView();

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
    /**
     * 输入法关闭监听
     */
    public interface OnCancelListener {
        /**
         * 关闭
         * @param isReal true:关闭整个输入法 false:关闭输入区域，保留EditText
         */
        void onCancel(boolean isReal);
    }
}
