package com.conquer.sharp.camera;

import android.os.Bundle;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.source.WCSparseArrayCompat;

/**
 * 难点--尺寸和方向
 */

/**
 * 尺寸：
 * 1.预览帧的尺寸
 * 2.拍摄帧的尺寸
 * 3.控件的尺寸
 */

/**
 * 方向:
 * 1.预览帧的方向
 * 2.拍摄帧的方向
 * 3.手机自身的方向
 */
public class CameraActivity extends BaseActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_camera);

        WCSparseArrayCompat<String> test4 = new WCSparseArrayCompat<>(4);
        WCSparseArrayCompat<String> test16 = new WCSparseArrayCompat<>(16);
    }


}
