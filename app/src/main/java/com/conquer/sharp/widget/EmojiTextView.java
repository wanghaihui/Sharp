package com.conquer.sharp.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.conquer.sharp.emoji.EmojiParser;

public class EmojiTextView extends AppCompatTextView implements Drawable.Callback {

    public boolean isShowGif = false;

    public EmojiTextView(Context context) {
        super(context);
    }

    public EmojiTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setText(String text) {
        CharSequence emoji = EmojiParser.getInstance().addEmojiGifSpans(text, getContext(), this, isShowGif);
        setText(emoji);
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable who) {
        invalidate();
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        postDelayed(what, when);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        removeCallbacks(what);
    }
}

