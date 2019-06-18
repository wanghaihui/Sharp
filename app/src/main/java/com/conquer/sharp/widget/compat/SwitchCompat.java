package com.conquer.sharp.widget.compat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.CompoundButton;

public class SwitchCompat extends CompoundButton {
    private static final int THUMB_ANIMATION_DURATION = 250;

    private static final int TOUCH_MODE_IDLE = 0;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;

    private static final int[] TEXT_APPEARANCE_ATTRS = {
            android.R.attr.textColor,
            android.R.attr.textSize,
            android.support.v7.appcompat.R.attr.textAllCaps
    };

    // Enum for the "typeface" XML parameter.
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int MONOSPACE = 3;

    private Drawable mThumbDrawable;
    private Drawable mTrackDrawable;

    private TextPaint mTextPaint;

    public SwitchCompat(Context context) {
        this(context, null);
    }

    public SwitchCompat(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.switchStyle);
    }

    public SwitchCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        final Resources res = getResources();
        mTextPaint.density = res.getDisplayMetrics().density;

        /*final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.SwitchCompat2, defStyleAttr, 0);
        mThumbDrawable = a.getDrawable(R.styleable.SwitchCompat2_android_thumb);
        mTrackDrawable = a.getDrawable(R.styleable.SwitchCompat2_track);
        mTextOn = a.getText(R.styleable.SwitchCompat_android_textOn);
        mTextOff = a.getText(R.styleable.SwitchCompat_android_textOff);
        mShowText = a.getBoolean(R.styleable.SwitchCompat_showText, true);
        mThumbTextPadding = a.getDimensionPixelSize(
                R.styleable.SwitchCompat_thumbTextPadding, 0);
        mSwitchMinWidth = a.getDimensionPixelSize(
                R.styleable.SwitchCompat_switchMinWidth, 0);
        mSwitchPadding = a.getDimensionPixelSize(
                R.styleable.SwitchCompat_switchPadding, 0);
        mSplitTrack = a.getBoolean(R.styleable.SwitchCompat_splitTrack, false);
        final int appearance = a.getResourceId(
                R.styleable.SwitchCompat_switchTextAppearance, 0);
        if (appearance != 0) {
            setSwitchTextAppearance(context, appearance);
        }
        mTintManager = a.getTintManager();
        a.recycle();*/

    }



}
