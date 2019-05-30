package com.conquer.sharp.agora.live;

import android.content.Context;
import android.util.Log;

import com.conquer.sharp.agora.Constant;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;

public class MyEngineEventHandler {
    private final Context mContext;
    private final EngineConfig mConfig;

    // 保证线程安全
    private final ConcurrentHashMap<AGEventHandler, Integer> mEventHandlerMap = new ConcurrentHashMap<>();

    public MyEngineEventHandler(Context context, EngineConfig config) {
        mContext = context;
        mConfig = config;
    }

    public void addEventHandler(AGEventHandler handler) {
        mEventHandlerMap.put(handler, 0);
    }

    public void removeEventHandler(AGEventHandler handler) {
        mEventHandlerMap.remove(handler);
    }

    final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
            Log.d(Constant.LOG_TAG, "onFirstRemoteVideoDecoded " + (uid & 0xFFFFFFFFL) + " " + width + " " + height + " " + elapsed);

            Iterator<AGEventHandler> it = mEventHandlerMap.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
            }
        }

        @Override
        public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
            Log.d(Constant.LOG_TAG, "onFirstRemoteVideoFrame " + (uid & 0xFFFFFFFFL) + " " + width + " " + height + " " + elapsed);
        }

        @Override
        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
            Log.d(Constant.LOG_TAG, "onFirstLocalVideoFrame " + width + " " + height + " " + elapsed);
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            Log.d(Constant.LOG_TAG, "onUserJoined " + (uid & 0xFFFFFFFFL) + " " + elapsed);

            Iterator<AGEventHandler> it = mEventHandlerMap.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onUserJoined(uid, elapsed);
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            // FIXME this callback may return times
            Iterator<AGEventHandler> it = mEventHandlerMap.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onUserOffline(uid, reason);
            }
        }

        @Override
        public void onUserMuteVideo(int uid, boolean muted) {

        }

        @Override
        public void onRtcStats(RtcStats stats) {

        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            Log.d(Constant.LOG_TAG, "onLeaveChannel " + stats);
        }

        @Override
        public void onLastmileQuality(int quality) {
            Log.d(Constant.LOG_TAG, "onLastmileQuality " + quality);
            Iterator<AGEventHandler> it = mEventHandlerMap.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onLastmileQuality(quality);
            }
        }

        @Override
        public void onLastmileProbeResult(IRtcEngineEventHandler.LastmileProbeResult result) {
            Log.d(Constant.LOG_TAG, "onLastmileProbeResult " + result);
            Iterator<AGEventHandler> it = mEventHandlerMap.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onLastmileProbeResult(result);
            }
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            Log.d(Constant.LOG_TAG, "onError " + err);
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.d(Constant.LOG_TAG, "onJoinChannelSuccess " + channel + " " + uid + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);

            Iterator<AGEventHandler> it = mEventHandlerMap.keySet().iterator();
            while (it.hasNext()) {
                AGEventHandler handler = it.next();
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.d(Constant.LOG_TAG, "onRejoinChannelSuccess " + channel + " " + uid + " " + elapsed);
        }

        @Override
        public void onWarning(int warn) {
            Log.d(Constant.LOG_TAG,"onWarning " + warn);
        }
    };
}
