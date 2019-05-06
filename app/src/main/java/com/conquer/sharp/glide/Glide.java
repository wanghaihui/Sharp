package com.conquer.sharp.glide;

import android.content.Context;
import android.support.annotation.NonNull;

// Double-Check(双重检查)的单例
public class Glide {
    private static volatile Glide glide;
    private static volatile boolean isInitializing;

    private Glide() {
    }

    @NonNull
    public static Glide get(@NonNull Context context) {
        if (glide == null) {
            synchronized (Glide.class) {
                if (glide == null) {
                    checkAndInitializeGlide(context);
                }
            }
        }
        return glide;
    }

    private static void checkAndInitializeGlide(Context context) {
        if (isInitializing) {
            throw new IllegalStateException("");
        }
        isInitializing = true;
        // initializeGlide(context);
        isInitializing = false;
    }
}
