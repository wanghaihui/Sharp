package com.conquer.sharp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by conquer on 2018/1/19.
 *
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews(savedInstanceState);
        mImmersionBar = ImmersionBar.with(this);
        initImmersionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    protected abstract void initViews(Bundle savedInstanceState);

    protected void initImmersionBar() {
        mImmersionBar.init();
    }

}
