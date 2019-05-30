package com.conquer.sharp.agora;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class AgoraActivity extends BaseActivity {

    private String mRoomName;
    private int mUid;
    private String mToken;

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onFirstLocalVideoFrame(int width, int height, int elapsed) {
            Log.d(Constant.LOG_TAG, "onFirstLocalVideoFrame");
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                }
            });
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserVideoMuted(uid, muted);
                }
            });
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.d(Constant.LOG_TAG, "onJoinChannelSuccess " + channel + " uid " + uid);
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            Log.d(Constant.LOG_TAG, "onUserJoined " + uid);
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            Log.d(Constant.LOG_TAG, "onError " + err);
        }

        @Override
        public void onWarning(int warn) {
            Log.d(Constant.LOG_TAG,"onWarning " + warn);
        }
    };

    public static void launch(Context context, String roomName, int uid, String token) {
        Intent intent = new Intent(context, AgoraActivity.class);
        intent.putExtra(Constant.ACTION_KEY_ROOM_NAME, roomName);
        intent.putExtra(Constant.ACTION_KEY_UID, uid);
        intent.putExtra(Constant.ACTION_KEY_TOKEN, token);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_agora);

        mRoomName = getIntent().getStringExtra(Constant.ACTION_KEY_ROOM_NAME);
        mUid = getIntent().getIntExtra(Constant.ACTION_KEY_UID, 0);
        mToken = getIntent().getStringExtra(Constant.ACTION_KEY_TOKEN);

        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initAgoraEngineAndJoinChannel();
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();
        setupVideoProfile();
        setupLocalVideo();
        joinChannel();
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.d(Constant.LOG_TAG, "checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // 如果未授权
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(Constant.LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode);
        switch (requestCode) {
            case PERMISSION_REQ_ID:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                        grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO + "/" +
                                    Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    finish();
                    break;
                }

                initAgoraEngineAndJoinChannel();
                break;
        }
    }

    public final void showLongToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), Constant.AGORA_APP_ID,
                    mRtcEventHandler);
        } catch (Exception e) {
            Log.e(Constant.LOG_TAG, Log.getStackTraceString(e));
            throw new RuntimeException("Need to check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        // 远程镜像问题--私有接口
        mRtcEngine.setParameters("{\"che.video.enableRemoteViewMirror\":true}");
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        ));
    }

    private void setupLocalVideo() {
        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        // 将其覆盖在其他媒体之上
        surfaceView.setZOrderMediaOverlay(true);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, mUid));
    }

    private void joinChannel() {
        // if you do not specify the uid, we will generate the uid for you
        mRtcEngine.joinChannel(mToken, mRoomName, "Android User", mUid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        leaveChannel();
        RtcEngine.destroy();
        mRtcEngine = null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void onLocalVideoMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalVideoStream(iv.isSelected());

        FrameLayout container = findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);
        surfaceView.setZOrderMediaOverlay(!iv.isSelected());
        surfaceView.setVisibility(iv.isSelected() ? View.GONE : View.VISIBLE);
    }

    public void onLocalAudioMuteClicked(View view) {
        ImageView iv = (ImageView) view;
        if (iv.isSelected()) {
            iv.setSelected(false);
            iv.clearColorFilter();
        } else {
            iv.setSelected(true);
            iv.setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        }

        mRtcEngine.muteLocalAudioStream(iv.isSelected());
    }

    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    public void onEncCallClicked(View view) {
        finish();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }

    private void setupRemoteVideo(int uid) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        if (container.getChildCount() >= 1) {
            return;
        }

        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid));

        surfaceView.setTag(uid);

        View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk);
        tipMsg.setVisibility(View.GONE);
    }

    private void onRemoteUserLeft() {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        container.removeAllViews();

        View tipMsg = findViewById(R.id.quick_tips_when_use_agora_sdk);
        tipMsg.setVisibility(View.VISIBLE);
    }

    private void onRemoteUserVideoMuted(int uid, boolean muted) {
        FrameLayout container = findViewById(R.id.remote_video_view_container);
        SurfaceView surfaceView = (SurfaceView) container.getChildAt(0);

        Object tag = surfaceView.getTag();
        if (tag != null && (Integer) tag == uid) {
            surfaceView.setVisibility(muted ? View.GONE : View.VISIBLE);
        }
    }
}