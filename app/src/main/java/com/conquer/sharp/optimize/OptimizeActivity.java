package com.conquer.sharp.optimize;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import butterknife.ButterKnife;

public class OptimizeActivity extends BaseActivity {

    public static void launch(Context context) {
        Intent intent = new Intent(context, OptimizeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_optimize);
        ButterKnife.bind(this);
    }

    protected void initImmersionBar() {
        mImmersionBar.keyboardEnable(true).init();
    }

}
