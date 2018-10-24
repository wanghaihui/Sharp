package com.conquer.exoplayer.core;

import com.conquer.exoplayer.core.source.TrackGroupArray;

/**
 * A media player interface defining(定义) traditional(传统的) high-level(高级) functionality(功能), such as the ability to
 * play(播放), pause(停止), seek(定位) and query properties(查询属性) of the currently playing media.
 * <p>
 * Some important properties of media players that implement this interface are:
 * <ul>
 *     <li>They can provide a {@link Timeline}(时间轴) representing the structure of the media being played,
 *     which can be obtained by calling {@link #getCurrentTimeline()}.</li>
 *
 *     <li>They can provide a {@link TrackGroupArray}(轨道组的数组) defining the currently available tracks,
 *     which can be obtained by calling {@link #getCurrentTrackGroups()}.</li>
 *
 *     <li>They contain a number of renderers(渲染器), each of which is able to render tracks of a single
 *     type (e.g. audio, video or text). The number of renderers and their respective(各自的) track types
 *     can be obtained by calling {@link #getRendererCount()} and {@link #getRendererType(int)}.
 *     </li>
 *
 *     <li>They can provide a {@link TrackSelectionArray} defining which of the currently available
 *     tracks are selected to be rendered by each renderer. This can be obtained by calling
 *     {@link #getCurrentTrackSelections()}}.</li>
 * </ul>
 */
public interface Player {

    /** The video component(Video组件) of a {@link Player}. */
    interface VideoComponent {
        /**
         * Sets the video scaling mode(Video缩放模式).
         *
         * @param videoScalingMode The video scaling mode.
         */
        //void setVideoScalingMode(@C.VideoScalingMode int videoScalingMode);
    }

    /**
     * Returns the number of renderers.
     */
    int getRendererCount();

    /**
     * Returns the track type(轨道的类型:audio,video,text) that the renderer at a given index(索引) handles(句柄).
     *
     * @see Renderer#getTrackType()
     * @param index The index of the renderer.
     * @return One of the {@code TRACK_TYPE_*} constants(常量) defined in {@link C}.
     */
    int getRendererType(int index);

    /**
     * Returns the available track groups.
     */
    TrackGroupArray getCurrentTrackGroups();

    /**
     * Returns the current {@link Timeline}. Never null, but may be empty.
     */
    Timeline getCurrentTimeline();
}
