package com.conquer.sharp.agora.live;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.conquer.sharp.SharpApplication;
import com.conquer.sharp.agora.Constant;
import com.conquer.sharp.base.BaseActivity;

import java.util.Arrays;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public abstract class BaseRoomActivity extends BaseActivity implements AGEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View layout = findViewById(Window.ID_ANDROID_CONTENT);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                initUIandEvent();
            }
        });
    }

    @Override
    protected void onDestroy() {
        deInitUIandEvent();
        super.onDestroy();
    }

    protected abstract void initUIandEvent();
    protected abstract void deInitUIandEvent();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                boolean checkPermissionResult = checkSelfPermissions();
            }
        }, 200);
    }

    private boolean checkSelfPermissions() {
        return checkSelfPermission(Manifest.permission.RECORD_AUDIO, ConstantAgoraRoom.PERMISSION_REQ_ID_RECORD_AUDIO) &&
                checkSelfPermission(Manifest.permission.CAMERA, ConstantAgoraRoom.PERMISSION_REQ_ID_CAMERA) &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantAgoraRoom.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
            return false;
        }

        // 如果相机权限可用
        if (Manifest.permission.CAMERA.equals(permission)) {
            ((SharpApplication) getApplication()).initWorkerThread();
            workThreadInited();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(Constant.LOG_TAG, "onRequestPermissionsResult " + requestCode + " " + Arrays.toString(permissions) + " " + Arrays.toString(grantResults));
        switch (requestCode) {
            case ConstantAgoraRoom.PERMISSION_REQ_ID_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.CAMERA, ConstantAgoraRoom.PERMISSION_REQ_ID_CAMERA);
                } else {
                    finish();
                }
                break;
            case ConstantAgoraRoom.PERMISSION_REQ_ID_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantAgoraRoom.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                    ((SharpApplication) getApplication()).initWorkerThread();
                    workThreadInited();
                } else {
                    finish();
                }
                break;
            case ConstantAgoraRoom.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish();
                }
                break;
        }
    }

    protected void workThreadInited() {

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

    }

    @Override
    public void onLastmileQuality(final int quality) {

    }

    @Override
    public void onLastmileProbeResult(final IRtcEngineEventHandler.LastmileProbeResult result) {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    protected RtcEngine rtcEngine() {
        return ((SharpApplication) getApplication()).getWorkerThread().getRtcEngine();
    }

    protected final WorkerThread worker() {
        return ((SharpApplication) getApplication()).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((SharpApplication) getApplication()).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((SharpApplication) getApplication()).getWorkerThread().eventHandler();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // IME
    public final void closeIME(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0); // 0 force close IME
        }
        view.clearFocus();
    }

    public final void closeIMEWithoutFocus(View view) {
        InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (mgr != null) {
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0); // 0 force close IME
        }
    }

    public void openIME(final EditText view) {
        final boolean focus = view.requestFocus();
        if (view.hasFocus()) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (mgr != null) {
                        boolean result = mgr.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                        Log.d(Constant.LOG_TAG, "openIME " + focus + " " + result);
                    }
                }
            });
        }
    }
}
