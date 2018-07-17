package com.conquer.sharp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by conquer on 2018/1/19.
 *
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews(savedInstanceState);
    }

    protected abstract void initViews(Bundle savedInstanceState);

}
