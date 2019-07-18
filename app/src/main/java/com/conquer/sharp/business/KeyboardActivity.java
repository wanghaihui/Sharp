package com.conquer.sharp.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import butterknife.ButterKnife;

public class KeyboardActivity extends BaseActivity {

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, KeyboardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_keyboard);
        ButterKnife.bind(this);
    }

    protected void initImmersionBar() {
        mImmersionBar.keyboardEnable(true).init();
    }
}
