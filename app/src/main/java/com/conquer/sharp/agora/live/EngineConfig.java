package com.conquer.sharp.agora.live;

import io.agora.rtc.video.VideoEncoderConfiguration;

public class EngineConfig {
    public int mClientRole;
    public VideoEncoderConfiguration.VideoDimensions mVideoDimension;
    public int mUid;
    public String mChannel;

    EngineConfig() {}

    public void reset() {
        mChannel = null;
    }
}
