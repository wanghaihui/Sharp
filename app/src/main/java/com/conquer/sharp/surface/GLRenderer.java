package com.conquer.sharp.surface;

public interface GLRenderer {
    void onSurfaceCreated();

    void onSurfaceChanged(int width, int height);

    void onDrawFrame();
}
