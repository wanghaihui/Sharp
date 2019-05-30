package com.conquer.sharp.agora.live;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;

import com.conquer.sharp.agora.Constant;

import java.io.File;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class WorkerThread extends Thread {
    private static final int ACTION_WORKER_THREAD_QUIT = 0X1010; // quit this thread

    private static final int ACTION_WORKER_JOIN_CHANNEL = 0X2010;
    private static final int ACTION_WORKER_LEAVE_CHANNEL = 0X2011;
    private static final int ACTION_WORKER_CONFIG_ENGINE = 0X2012;

    private static final int ACTION_WORKER_PREVIEW = 0X2014;

    private final Context mContext;
    private WorkerThreadHandler mWorkerHandler;
    private boolean mReady;
    private RtcEngine mRtcEngine;
    private final MyEngineEventHandler mEngineEventHandler;
    private EngineConfig mEngineConfig;

    public WorkerThread(Context context) {
        mContext = context;
        mEngineConfig = new EngineConfig();
        mEngineEventHandler = new MyEngineEventHandler(mContext, mEngineConfig);
    }

    @Override
    public void run() {
        Log.d(Constant.LOG_TAG, "Start to run");
        Looper.prepare();
        mWorkerHandler = new WorkerThreadHandler(this);

        ensureRtcEngineReadyLock();

        mReady = true;
        // enter thread looper
        Looper.loop();
    }

    private void ensureRtcEngineReadyLock() {
        if (mRtcEngine == null) {
            String appId = Constant.AGORA_APP_ID;
            if (TextUtils.isEmpty(appId)) {
                throw new RuntimeException("Need to use your App ID, get your own ID at https://dashboard.agora.io/");
            }

            try {
                mRtcEngine = RtcEngine.create(mContext, appId, mEngineEventHandler.mRtcEventHandler);
            } catch (Exception e) {
                Log.e(Constant.LOG_TAG, Log.getStackTraceString(e));
                throw new RuntimeException("Need to check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }

            // 视频通话
            // mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
            // 互动直播
            mRtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(Environment.getExternalStorageDirectory() + File.separator +
                    mContext.getPackageName() + "/log/agora-rtc.log");

            // 外部音频输入
            mRtcEngine.setExternalAudioSource(true, 48000, 1);
            mRtcEngine.setExternalVideoSource(true, false, true);

            // Warning: only enable dual stream mode if there will be more than one broadcaster in the channel
            mRtcEngine.enableDualStreamMode(true);
        }
    }

    public final void waitForReady() {
        while (!mReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(Constant.LOG_TAG, "wait for " + WorkerThread.class.getSimpleName());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public RtcEngine getRtcEngine() {
        return mRtcEngine;
    }

    public final EngineConfig getEngineConfig() {
        return mEngineConfig;
    }

    public MyEngineEventHandler eventHandler() {
        return mEngineEventHandler;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Call this method to exit
     * Should ONLY call this method when this thread is running
     */
    public final void exit() {
        if (Thread.currentThread() != this) {
            Log.w(Constant.LOG_TAG, "exit() - exit app thread asynchronously");
            mWorkerHandler.sendEmptyMessage(ACTION_WORKER_THREAD_QUIT);
            return;
        }

        mReady = false;
        mWorkerHandler.removeCallbacksAndMessages(null);
        Log.d(Constant.LOG_TAG, "exit() > start");
        final Looper looper = Looper.myLooper();
        if (looper != null) {
            looper.quit();
        }
        mWorkerHandler.release();
        Log.d(Constant.LOG_TAG, "exit() > end");
    }

    public final void joinChannel(final String channel, int uid, String token) {
        if (Thread.currentThread() != this) {
            Log.w(Constant.LOG_TAG, "joinChannel() - worker thread asynchronously " + channel + " " + uid);
            Message msg = new Message();
            msg.what = ACTION_WORKER_JOIN_CHANNEL;
            msg.obj = new String[] { channel, token };
            msg.arg1 = uid;
            mWorkerHandler.sendMessage(msg);
            return;
        }

        ensureRtcEngineReadyLock();
        // Token的设置--安全性
        mRtcEngine.joinChannel(token, channel, "Android User", uid);
        mEngineConfig.mChannel = channel;

        // enable美颜等...
    }

    public final void leaveChannel(String channel) {
        if (Thread.currentThread() != this) {
            Log.w(Constant.LOG_TAG, "leaveChannel() - worker thread asynchronously " + channel);
            Message msg = new Message();
            msg.what = ACTION_WORKER_LEAVE_CHANNEL;
            msg.obj = channel;
            mWorkerHandler.sendMessage(msg);
            return;
        }

        if (mRtcEngine != null) {
            mRtcEngine.leaveChannel();
        }

        // disable美颜等...

        int clientRole = mEngineConfig.mClientRole;
        mEngineConfig.reset();
        Log.d(Constant.LOG_TAG, "leaveChannel " + channel + " " + clientRole);
    }

    public final void configEngine(int clientRole, VideoEncoderConfiguration.VideoDimensions videoDimension) {
        if (Thread.currentThread() != this) {
            Log.w(Constant.LOG_TAG, "configEngine() - worker thread asynchronously " + clientRole + " " + videoDimension);
            Message msg = new Message();
            msg.what = ACTION_WORKER_CONFIG_ENGINE;
            msg.obj = new Object[] { clientRole, videoDimension };
            mWorkerHandler.sendMessage(msg);
            return;
        }

        ensureRtcEngineReadyLock();
        mEngineConfig.mClientRole = clientRole;
        mEngineConfig.mVideoDimension = videoDimension;

        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                videoDimension,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE
        ));

        mRtcEngine.setClientRole(clientRole);

        Log.d(Constant.LOG_TAG, "configEngine " + clientRole + " " + mEngineConfig.mVideoDimension);
    }

    public final void preview(boolean start, SurfaceView view, int uid) {
        if (Thread.currentThread() != this) {
            Log.w(Constant.LOG_TAG, "preview() - worker thread asynchronously " + start + " " + view + " " + (uid & 0XFFFFFFFFL));
            Message msg = new Message();
            msg.what = ACTION_WORKER_PREVIEW;
            msg.obj = new Object[] { start, view, uid };
            mWorkerHandler.sendMessage(msg);
            return;
        }

        ensureRtcEngineReadyLock();
        if (start) {
            mRtcEngine.setupLocalVideo(new VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            mRtcEngine.startPreview();
        } else {
            mRtcEngine.stopPreview();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final class WorkerThreadHandler extends Handler {
        private WorkerThread mWorkerThread;
        WorkerThreadHandler(WorkerThread workerThread) {
            this.mWorkerThread = workerThread;
        }

        public void release() {
            mWorkerThread = null;
        }

        @Override
        public void handleMessage(Message msg) {
            if (this.mWorkerThread == null) {
                Log.w(Constant.LOG_TAG, "Worker thread handler is already released " + msg.what);
                return;
            }

            switch (msg.what) {
                case ACTION_WORKER_THREAD_QUIT:
                    mWorkerThread.exit();
                    break;
                case ACTION_WORKER_JOIN_CHANNEL:
                    String[] data = (String[]) msg.obj;
                    mWorkerThread.joinChannel(data[0], msg.arg1, data[1]);
                    break;
                case ACTION_WORKER_LEAVE_CHANNEL:
                    String channel = (String) msg.obj;
                    mWorkerThread.leaveChannel(channel);
                    break;
                case ACTION_WORKER_CONFIG_ENGINE:
                    Object[] configData = (Object[]) msg.obj;
                    mWorkerThread.configEngine((int) configData[0],
                            (VideoEncoderConfiguration.VideoDimensions) configData[1]);
                    break;
                case ACTION_WORKER_PREVIEW:
                    Object[] previewData = (Object[]) msg.obj;
                    mWorkerThread.preview((boolean) previewData[0], (SurfaceView) previewData[1],
                            (int) previewData[2]);
                    break;
            }
        }
    }
}
