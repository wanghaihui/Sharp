package com.conquer.sharp.keyboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.keyboard.base.EmotionKeyboardFragment;
import com.conquer.sharp.keyboard.input.BaseInputFragment;
import com.conquer.sharp.keyboard.input.MessageInputFragment;

import butterknife.ButterKnife;

public class EmotionKeyboardActivity extends BaseActivity {

    private MessageInputFragment mMessageKeyboard;

    public static void launch(Context context) {
        Intent intent = new Intent(context, EmotionKeyboardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_emotion_keyboard);
        ButterKnife.bind(this);

        // 手动设置沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        mMessageKeyboard = MessageInputFragment.newInstance();
        mMessageKeyboard.setCloseMode(BaseInputFragment.CloseMode.CLOSE_NORMAL);
        mMessageKeyboard.setInputListener(new MessageInputFragment.InputListener() {
            @Override
            public void onSend(String msg) {
                sendMessage(msg);
            }
        });
        mMessageKeyboard.setOnCancelListener(new BaseInputFragment.OnCancelListener() {
            @Override
            public void onCancel(boolean isReal) {
                mMessageKeyboard.setBlankEnable(false);
            }
        });
        mMessageKeyboard.setKeyboardListener(new EmotionKeyboardFragment.KeyboardListener() {
            @Override
            public void onBoardHeightChanged(int height) {

            }

            @Override
            public void onBoardStatusChanged(EmotionKeyboardFragment.KeyboardStatus status) {
                if (status != EmotionKeyboardFragment.KeyboardStatus.NONE) {
                    mMessageKeyboard.setBlankEnable(true);
                }
            }
        });
        mMessageKeyboard.prepare(getSupportFragmentManager(), R.id.fl_input);

    }

    protected void initImmersionBar() {
        // 禁用沉浸式的库
    }

    /**
     * 隐藏软键盘和表情
     */
    private void hideInput() {
        if (mMessageKeyboard != null) {
            mMessageKeyboard.closeKeyboard();
        }
    }

    private void sendMessage(String message) {
        // 发送消息

    }
}
