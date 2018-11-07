package com.conquer.sharp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.conquer.sharp.R;

public class LuckyBgLayout extends FrameLayout {

    public LuckyBgLayout(@NonNull Context context) {
        super(context);
    }

    public LuckyBgLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LuckyBgLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBackground(int level) {
        setBackgroundResource(R.drawable.bg_lucky_champion);
    }
}
