package com.conquer.exoplayer.core;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DefaultRenderersFactory implements RenderersFactory {

    /**
     * Do not allow use of extension renderers(不允许使用扩展的渲染器).
     */
    public static final int EXTENSION_RENDERER_MODE_OFF = 0;
    /**
     * Allow use of extension renderers. Extension renderers are indexed after core renderers of the
     * same type. A {@link TrackSelector} that prefers the first suitable renderer will therefore
     * prefer to use a core renderer to an extension renderer in the case that both are able to play
     * a given track.
     */
    public static final int EXTENSION_RENDERER_MODE_ON = 1;
    /**
     * Allow use of extension renderers. Extension renderers are indexed before core renderers of the
     * same type. A {@link TrackSelector} that prefers the first suitable renderer will therefore
     * prefer to use an extension renderer to a core renderer in the case that both are able to play
     * a given track.
     */
    public static final int EXTENSION_RENDERER_MODE_PREFER = 2;

    /**
     * Modes for using extension renderers. One of {@link #EXTENSION_RENDERER_MODE_OFF}, {@link
     * #EXTENSION_RENDERER_MODE_ON} or {@link #EXTENSION_RENDERER_MODE_PREFER}.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({EXTENSION_RENDERER_MODE_OFF, EXTENSION_RENDERER_MODE_ON, EXTENSION_RENDERER_MODE_PREFER})
    public @interface ExtensionRendererMode {}

    public DefaultRenderersFactory(Context context) {
        this(context, EXTENSION_RENDERER_MODE_OFF);
    }

    /**
     * @param context A {@link Context}.
     * @param extensionRendererMode The extension renderer mode, which determines if and how available
     *     extension renderers are used. Note that extensions must be included in the application
     *     build for them to be considered available.
     */
    public DefaultRenderersFactory(Context context, @ExtensionRendererMode int extensionRendererMode) {
        this(context, extensionRendererMode, DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }
}
