package com.conquer.sharp.widget;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

import com.conquer.sharp.util.ScreenUtils;

import java.lang.reflect.Field;

public class SwitchButton extends SwitchCompat {

    public SwitchButton(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.switchStyle);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            Field switchWidth = SwitchCompat.class.getDeclaredField("mSwitchWidth");
            switchWidth.setAccessible(true);
            int desiredWidth = ScreenUtils.dip2px(70);
            switchWidth.setInt(this, desiredWidth);
            setMeasuredDimension(desiredWidth, getMeasuredHeight());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
