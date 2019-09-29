package com.conquer.sharp.keyboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.keyboard.base.EmotionKeyboardFragment;
import com.conquer.sharp.keyboard.input.BaseInputFragment;
import com.conquer.sharp.keyboard.input.MessageInputFragment;
import com.conquer.sharp.recycler.decoration.SpacingDecoration;
import com.conquer.sharp.util.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmotionKeyboardActivity extends BaseActivity {

    @BindView(R.id.message_list_view)
    RecyclerView messageListView;

    private MessageInputFragment mMessageKeyboard;
    private MessageAdapter mMessageAdapter;

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

        // 消息列表
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        SpacingDecoration mSpacingDecoration = new SpacingDecoration(ScreenUtils.dip2px(8),
                ScreenUtils.dip2px(8), false);
        messageListView.addItemDecoration(mSpacingDecoration);
        mMessageAdapter = new MessageAdapter(this, R.layout.layout_message_text);
        messageListView.setAdapter(mMessageAdapter);
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
        if (!TextUtils.isEmpty(message)) {
            // 发送消息
            mMessageAdapter.getDataList().add(message);
            mMessageAdapter.notifyDataSetChanged();
        }
    }
}
