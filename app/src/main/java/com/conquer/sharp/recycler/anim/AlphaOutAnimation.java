package com.conquer.sharp.recycler.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class AlphaOutAnimation implements BaseAnimation {
    private static final float DEFAULT_ALPHA_TO = 0f;
    private final float mTo;

    public AlphaOutAnimation() {
        this(DEFAULT_ALPHA_TO);
    }

    public AlphaOutAnimation(float to) {
        mTo = to;
    }

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "alpha", 1f, mTo)
        };
    }
}
