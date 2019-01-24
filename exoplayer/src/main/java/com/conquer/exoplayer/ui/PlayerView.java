package com.conquer.exoplayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class PlayerView extends FrameLayout {

    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 编辑模式时，支持预览
        if (isInEditMode()) {


            ImageView logo = new ImageView(context);
        }
    }
}
