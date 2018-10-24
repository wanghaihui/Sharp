package com.conquer.sharp.surface;

public interface IEglHelper {
    EglContextWrapper start(EglContextWrapper eglContext);

    boolean createSurface(Object windowSurface);

    int swap();

    void destroySurface();

    void finish();
}
