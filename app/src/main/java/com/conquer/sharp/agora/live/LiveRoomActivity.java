package com.conquer.sharp.agora.live;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.conquer.sharp.R;
import com.conquer.sharp.agora.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class LiveRoomActivity extends BaseRoomActivity implements AGEventHandler {

    public static final int VIEW_TYPE_DEFAULT = 0;
    public static final int VIEW_TYPE_SMALL = 1;
    public int mViewType = VIEW_TYPE_DEFAULT;

    @BindView(R.id.grid_video_view_container)
    GridVideoViewContainer mGridVideoViewContainer;

    @BindView(R.id.request_broadcast)
    ImageView requestBroadcast;
    @BindView(R.id.switch_camera)
    ImageView switchCamera;
    @BindView(R.id.mute)
    ImageView mute;

    @BindView(R.id.room_name)
    TextView tvRoomName;

    private int mClientRole;
    private String mRoomName; // channel name
    private int mUid; // user id
    private String mToken;

    private final SparseArray<SurfaceView> mUidList = new SparseArray<>(); // uid == EngineConfig.mUid

    private Handler mHandler = new Handler();

    public static void launch(Context context, int clientRole, String channelName, int uid, String token) {
        Intent intent = new Intent(context, LiveRoomActivity.class);
        // 角色--观众还是主播
        intent.putExtra(ConstantAgoraRoom.ACTION_KEY_CROLE, clientRole);
        intent.putExtra(ConstantAgoraRoom.ACTION_KEY_ROOM_NAME, channelName);
        intent.putExtra(ConstantAgoraRoom.ACTION_KEY_UID, uid);
        intent.putExtra(ConstantAgoraRoom.ACTION_KEY_TOKEN, token);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_live_room);
        ButterKnife.bind(this);
    }

    @Override
    protected void initUIandEvent() {
        Intent intent = getIntent();
        mClientRole = intent.getIntExtra(ConstantAgoraRoom.ACTION_KEY_CROLE, 0);
        if (mClientRole == 0) {
            throw new RuntimeException("Should not reach here!");
        }
        mRoomName = intent.getStringExtra(ConstantAgoraRoom.ACTION_KEY_ROOM_NAME);
        mUid = intent.getIntExtra(ConstantAgoraRoom.ACTION_KEY_UID, 0);
        mToken = intent.getStringExtra(ConstantAgoraRoom.ACTION_KEY_TOKEN);
        //mToken = null;

        mGridVideoViewContainer.setItemEventHandler(new VideoViewEventListener() {
            @Override
            public void onItemDoubleClick(View view, Object item) {
                // 双击

            }

            @Override
            public void onItemClick(View view, Object item) {
                // 单击
                if (mUidList.size() < 2) {
                    return;
                }

                if (mViewType == VIEW_TYPE_DEFAULT) {
                    // switchToSmallVideoView(((VideoUserStatus) item).mUid);
                } else {
                    // switchToDefaultVideoView();
                }
            }
        });

        tvRoomName.setText(mRoomName);
    }

    @Override
    protected void deInitUIandEvent() {
        doLeaveChannel();
        event().removeEventHandler(this);
        mUidList.clear();
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadcaster()) {
            worker().preview(false, null, 0);
        }
    }

    protected void workThreadInited() {
        event().addEventHandler(this);
        doConfigEngine(mClientRole);

        if (isBroadcaster(mClientRole)) {
            SurfaceView surfaceView = RtcEngine.CreateRendererView(getApplicationContext());
            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, mUid));

            mUidList.put(mUid, surfaceView);
            mGridVideoViewContainer.initViewContainer(getApplicationContext(), mUid, mUidList);
            worker().preview(true, surfaceView, mUid);
            broadcasterUI(requestBroadcast, switchCamera, mute);
        } else {
            audienceUI(requestBroadcast, switchCamera, mute);
        }

        config().mUid = mUid;
        worker().joinChannel(mRoomName, config().mUid, mToken);
    }

    private void doConfigEngine(int clientRole) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int prefIndex = pref.getInt(ConstantAgoraRoom.PrefManager.PREF_PROPERTY_PROFILE_IDX, ConstantAgoraRoom.DEFAULT_PROFILE_IDX);
        if (prefIndex > ConstantAgoraRoom.VIDEO_DIMENSIONS.length - 1) {
            prefIndex = ConstantAgoraRoom.DEFAULT_PROFILE_IDX;
        }
        VideoEncoderConfiguration.VideoDimensions dimension = ConstantAgoraRoom.VIDEO_DIMENSIONS[prefIndex];
        worker().configEngine(clientRole, dimension);
    }

    // 是否是主播
    private boolean isBroadcaster(int clientRole) {
        return clientRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }

    private void broadcasterUI(ImageView requestBroadcast, ImageView switchCamera, ImageView mute) {
        requestBroadcast.setTag(true);
        requestBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = view.getTag();
                requestBroadcast.setEnabled(false);
                if (tag != null && (boolean) tag) {

                }
            }
        });
        requestBroadcast.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
    }

    private void audienceUI(ImageView requestBroadcast, ImageView switchCamera, ImageView mute) {
        requestBroadcast.setTag(null);
        requestBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Object tag = view.getTag();
                requestBroadcast.setEnabled(false);
                if (tag != null && (boolean) tag) {
                    doSwitchToBroadcaster(false);
                } else {
                    doSwitchToBroadcaster(true);
                }
            }
        });
        requestBroadcast.clearColorFilter();
    }

    private void doSwitchToBroadcaster(boolean broadcaster) {
        final int currentHostCount = mUidList.size();
        final int uid = config().mUid;
        Log.d(Constant.LOG_TAG, "doSwitchToBroadcaster " + currentHostCount + " " + (uid & 0XFFFFFFFFL) + " " + broadcaster);

        if (broadcaster) {
            doConfigEngine(Constants.CLIENT_ROLE_BROADCASTER);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doRenderRemoteUi(uid);
                    broadcasterUI(requestBroadcast, switchCamera, mute);
                    requestBroadcast.setEnabled(true);
                    doShowButtons(false);
                }
            }, 1000); // wait for reconfig engine
        } else {
            requestBroadcast.setEnabled(true);
            stopInteraction(currentHostCount, uid);
        }
    }

    private void stopInteraction(final int currentHostCount, final int uid) {

    }

    private void doShowButtons(boolean hide) {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {

    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {
        Log.d(Constant.LOG_TAG, "Live Room on user joined");
        doRenderRemoteUi(uid);
    }

    @Override
    public void onLastmileQuality(final int quality) {

    }

    @Override
    public void onLastmileProbeResult(final IRtcEngineEventHandler.LastmileProbeResult result) {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private void doRenderRemoteUi(final int uid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                SurfaceView surfaceView = RtcEngine.CreateRendererView(getApplicationContext());
                mUidList.put(uid, surfaceView);
                if (config().mUid == uid) {
                    rtcEngine().setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                } else {
                    rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                }

                addToRecyclerView();
            }
        });
    }

    private void addToRecyclerView() {
        mGridVideoViewContainer.initViewContainer(getApplicationContext(), config().mUid, mUidList);

        mViewType = VIEW_TYPE_DEFAULT;

        int sizeLimit = mUidList.size();
        if (sizeLimit > ConstantAgoraRoom.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantAgoraRoom.MAX_PEER_COUNT + 1;
        }
        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (config().mUid != uid) {
                rtcEngine().setRemoteVideoStreamType(uid, Constants.VIDEO_STREAM_HIGH);
                Log.d(Constant.LOG_TAG, "setRemoteVideoStreamType VIDEO_STREAM_HIGH " + mUidList.size() + " " + (uid & 0xFFFFFFFFL));
            }
        }
        boolean setRemoteUserPriorityFlag = false;
        for (int i = 0; i < sizeLimit; i++) {
            int uid = mGridVideoViewContainer.getItem(i).mUid;
            if (config().mUid != uid) {
                if (!setRemoteUserPriorityFlag) {
                    setRemoteUserPriorityFlag = true;
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_HIGH);
                    Log.d(Constant.LOG_TAG, "setRemoteUserPriority USER_PRIORITY_HIGH " + mUidList.size() + " " + (uid & 0xFFFFFFFFL));
                } else {
                    rtcEngine().setRemoteUserPriority(uid, Constants.USER_PRIORITY_NORANL);
                    Log.d(Constant.LOG_TAG, "setRemoteUserPriority USER_PRIORITY_NORANL " + mUidList.size() + " " + (uid & 0xFFFFFFFFL));
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
