package com.conquer.sharp.recycler.anim;

import android.animation.Animator;
import android.view.View;

public interface BaseAnimation {
    Animator[] getAnimators(View view);
}
