package com.conquer.sharp.surface;

import android.util.Log;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * An EGL helper class.
 */
public class EglHelper implements IEglHelper {
    private static final String TAG = "EglHelper";

    private final static boolean LOG_EGL = false;

    private EGL10 mEgl;
    private EGLDisplay mEglDisplay;
    private EGLSurface mEglSurface;
    private EGLConfig mEglConfig;
    private EGLContext mEglContext;

    private GLThread.EGLConfigChooser mEglConfigChooser;
    private GLThread.EGLContextFactory mEglContextFactory;
    private GLThread.EGLWindowSurfaceFactory mEglWindowSurfaceFactory;

    public EglHelper(GLThread.EGLConfigChooser eglConfigChooser, GLThread.EGLContextFactory eglContextFactory,
                     GLThread.EGLWindowSurfaceFactory eglWindowSurfaceFactory) {
        mEglConfigChooser = eglConfigChooser;
        mEglContextFactory = eglContextFactory;
        mEglWindowSurfaceFactory = eglWindowSurfaceFactory;
    }

    /**
     * Initialize EGL for a given configuration spec.
     */
    @Override
    public EglContextWrapper start(EglContextWrapper eglContext) {
        if (LOG_EGL) {
            Log.w(TAG, "start() tid=" + Thread.currentThread().getId());
        }

        /*
         * Get an EGL instance
         */
        mEgl = (EGL10) EGLContext.getEGL();

        /*
         * Get to the default display.
         */
        mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {
            throw new RuntimeException("eglGetDisplay failed");
        }

        /*
         * We can now initialize EGL for that display
         */
        int[] version = new int[2];
        if(!mEgl.eglInitialize(mEglDisplay, version)) {
            throw new RuntimeException("eglInitialize failed");
        }

        mEglConfig = mEglConfigChooser.chooseConfig(mEgl, mEglDisplay);

        /*
         * Create an EGL context. We want to do this as rarely as we can, because an
         * EGL context is a somewhat heavy object.
         */
        mEglContext = mEglContextFactory.createContext(mEgl, mEglDisplay, mEglConfig, eglContext.getEglContext());

        if (mEglContext == null || mEglContext == EGL10.EGL_NO_CONTEXT) {
            mEglContext = null;
            throwEglException("createContext");
        }

        if (LOG_EGL) {
            Log.w("EglHelper", "createContext " + mEglContext + " tid=" + Thread.currentThread().getId());
        }

        mEglSurface = null;

        EglContextWrapper eglContextWrapper = new EglContextWrapper();
        eglContextWrapper.setEglContext(mEglContext);
        return eglContextWrapper;
    }

    /**
     * Create an egl surface for the current SurfaceHolder surface. If a surface
     * already exists, destroy it before creating the new surface.
     *
     * @param windowSurface SurfaceHolder or TextureView#SurfaceTexture or MediaCodec#Surface
     *
     * @return true if the surface was created successfully.
     */
    @Override
    public boolean createSurface(Object windowSurface) {
        if (LOG_EGL) {
            Log.w("EglHelper", "createSurface()  tid=" + Thread.currentThread().getId());
        }

        /*
         * Check preconditions.
         */
        if (mEgl == null) {
            throw new RuntimeException("egl not initialized");
        }
        if (mEglDisplay == null) {
            throw new RuntimeException("eglDisplay not initialized");
        }
        if (mEglConfig == null) {
            throw new RuntimeException("mEglConfig not initialized");
        }

        /*
         *  The window size has changed, so we need to create a new
         *  surface.
         */
        destroySurfaceImp();

        /*
         * Create an EGL surface we can render into.
         */
        mEglSurface = mEglWindowSurfaceFactory.createWindowSurface(mEgl, mEglDisplay, mEglConfig, windowSurface);

        if (mEglSurface == null || mEglSurface == EGL10.EGL_NO_SURFACE) {
            int error = mEgl.eglGetError();
            if (error == EGL10.EGL_BAD_NATIVE_WINDOW) {
                Log.e("EglHelper", "createWindowSurface returned EGL_BAD_NATIVE_WINDOW.");
            }
            return false;
        }

        /*
         * Before we can issue GL commands, we need to make sure
         * the context is current and bound to a surface.
         */
        if (!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface, mEglContext)) {
            /*
             * Could not make the context current, probably because the underlying
             * SurfaceView surface has been destroyed.
             */
            logEglErrorAsWarning("EGLHelper", "eglMakeCurrent", mEgl.eglGetError());
            return false;
        }

        return true;
    }

    /**
     * Display the current render surface.
     * @return the EGL error code from eglSwapBuffers.
     */
    @Override
    public int swap() {
        if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
            return mEgl.eglGetError();
        }
        return EGL10.EGL_SUCCESS;
    }

    @Override
    public void destroySurface() {
        if (LOG_EGL) {
            Log.w("EglHelper", "destroySurface()  tid=" + Thread.currentThread().getId());
        }
        destroySurfaceImp();
    }

    private void destroySurfaceImp() {
        if (mEglSurface != null && mEglSurface != EGL10.EGL_NO_SURFACE) {
            mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_SURFACE,
                    EGL10.EGL_NO_CONTEXT);
            mEglWindowSurfaceFactory.destroySurface(mEgl, mEglDisplay, mEglSurface);
            mEglSurface = null;
        }
    }

    @Override
    public void finish() {
        if (LOG_EGL) {
            Log.w("EglHelper", "finish() tid=" + Thread.currentThread().getId());
        }
        if (mEglContext != null) {
            mEglContextFactory.destroyContext(mEgl, mEglDisplay, mEglContext);
            mEglContext = null;
        }
        if (mEglDisplay != null) {
            mEgl.eglTerminate(mEglDisplay);
            mEglDisplay = null;
        }
    }

    private void throwEglException(String function) {
        throwEglException(function, mEgl.eglGetError());
    }

    public static void throwEglException(String function, int error) {
        String message = formatEglError(function, error);
        if (LOG_EGL) {
            Log.e("EglHelper", "throwEglException tid=" + Thread.currentThread().getId() + " " + message);
        }
        throw new RuntimeException(message);
    }

    public static void logEglErrorAsWarning(String tag, String function, int error) {
        Log.w(tag, formatEglError(function, error));
    }

    private static String formatEglError(String function, int error) {
        return function + " failed: " + EGLLogWrapper.getErrorString(error);
    }
}