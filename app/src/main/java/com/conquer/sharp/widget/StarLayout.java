package com.conquer.sharp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.conquer.sharp.R;

public class StarLayout extends LinearLayout {

    public StarLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public StarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public void setLevel(int level) {
        if (level > 0) {
            for (int i = 0; i < level; i++) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.ic_star);
                addView(imageView);
            }
        }
    }
}
