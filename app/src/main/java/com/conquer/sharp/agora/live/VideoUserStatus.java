package com.conquer.sharp.agora.live;

import android.view.SurfaceView;

public class VideoUserStatus {
    public static final int DEFAULT_STATUS = 0;
    public static final int VIDEO_MUTED = 1;
    public static final int AUDIO_MUTED = VIDEO_MUTED << 1;

    public static final int DEFAULT_VOLUME = 0;

    public VideoUserStatus(int uid, SurfaceView view, int status, int volume) {
        this.mUid = uid;
        this.mSurfaceView = view;
        this.mStatus = status;
        this.mVolume = volume;
    }

    public int mUid;

    public SurfaceView mSurfaceView;

    public int mStatus;

    public int mVolume;

    @Override
    public String toString() {
        return "VideoUserStatus {" +
                "mUid=" + (mUid & 0xFFFFFFFFL) +
                ", mView=" + mSurfaceView +
                ", mStatus=" + mStatus +
                ", mVolume=" + mVolume +
                '}';
    }
}
