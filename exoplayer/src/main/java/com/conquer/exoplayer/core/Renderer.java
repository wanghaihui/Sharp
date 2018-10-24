package com.conquer.exoplayer.core;

public interface Renderer {

    /**
     * Returns the track type that the {@link Renderer} handles(句柄). For example, a video renderer will
     * return {@link C#TRACK_TYPE_VIDEO}, an audio renderer will return {@link C#TRACK_TYPE_AUDIO}, a
     * text renderer will return {@link C#TRACK_TYPE_TEXT}, and so on.
     *
     * @return One of the {@code TRACK_TYPE_*} constants defined in {@link C}.
     */
    int getTrackType();
}
