package com.conquer.sharp.surface;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;

public class EglContextWrapper {
    private EGLContext eglContext = EGL10.EGL_NO_CONTEXT;

    public void setEglContext(EGLContext eglContext) {
        this.eglContext = eglContext;
    }

    public EGLContext getEglContext() {
        return eglContext;
    }
}
