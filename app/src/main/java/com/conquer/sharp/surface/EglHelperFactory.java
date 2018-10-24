package com.conquer.sharp.surface;

public class EglHelperFactory {
    public static IEglHelper create(GLThread.EGLConfigChooser eglConfigChooser, GLThread.EGLContextFactory eglContextFactory,
                                    GLThread.EGLWindowSurfaceFactory eglWindowSurfaceFactory) {
        return new EglHelper(eglConfigChooser, eglContextFactory, eglWindowSurfaceFactory);
    }
}
