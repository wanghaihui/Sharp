package com.conquer.exoplayer.core;

/**
 * Defines constants used by the library.
 */
public final class C {
    private C() {

    }


    /** A type constant for tracks of unknown type. */
    public static final int TRACK_TYPE_UNKNOWN = -1;
    /** A type constant for tracks of some default type, where the type itself is unknown. */
    public static final int TRACK_TYPE_DEFAULT = 0;
    /** A type constant for audio tracks. */
    public static final int TRACK_TYPE_AUDIO = 1;
    /** A type constant for video tracks. */
    public static final int TRACK_TYPE_VIDEO = 2;
    /** A type constant for text tracks. */
    public static final int TRACK_TYPE_TEXT = 3;
    /** A type constant for metadata(元数据) tracks. */
    public static final int TRACK_TYPE_METADATA = 4;
    /** A type constant for camera motion(运动) tracks. */
    public static final int TRACK_TYPE_CAMERA_MOTION = 5;
    /** A type constant for a dummy(虚假的) or empty(空的) track. */
    public static final int TRACK_TYPE_NONE = 6;
}
