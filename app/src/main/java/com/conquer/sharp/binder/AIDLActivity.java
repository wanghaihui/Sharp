package com.conquer.sharp.binder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.conquer.sharp.IRemoteService;
import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AIDLActivity extends BaseActivity {
    private static final String TAG = "BinderSimple";

    @BindView(R.id.bind)
    Button bind;
    @BindView(R.id.unbind)
    Button unbind;
    @BindView(R.id.kill)
    Button kill;

    private IRemoteService mRemoteService;
    private boolean mIsBound;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, AIDLActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_aidl);
        ButterKnife.bind(this);

        setListeners();
    }

    private void setListeners() {
        bind.setOnClickListener(v -> bindRemoteService());
        unbind.setOnClickListener(v -> unbindRemoteService());
        kill.setOnClickListener(v -> killRemoteService());
    }

    /**
     * 用语监控远程服务连接的状态
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteService = IRemoteService.Stub.asInterface(service);
            String pidInfo = "";
            try {
                MyData myData = mRemoteService.getMyData();
                pidInfo = "pid="+ mRemoteService.getPid() +
                        ", data1 = "+ myData.getData1() +
                        ", data2="+ myData.getData2();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "[ClientActivity] o" + "nServiceConnected  "+ pidInfo);
            Toast.makeText(AIDLActivity.this, "远程服务连接", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "[ClientActivity] onServiceDisconnected");
            mRemoteService = null;
            Toast.makeText(AIDLActivity.this, "远程服务断开连接", Toast.LENGTH_SHORT).show();
        }
    };

    private void bindRemoteService() {
        Log.d(TAG, "[ClientActivity] bindRemoteService");
        Intent intent = new Intent(AIDLActivity.this, RemoteService.class);
        intent.setAction(IRemoteService.class.getName());
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mIsBound = true;
        Toast.makeText(this, "bind", Toast.LENGTH_SHORT).show();
    }

    private void unbindRemoteService() {
        if (!mIsBound) {
            return;
        }
        Log.d(TAG, "[ClientActivity] unbindRemoteService ==>");
        unbindService(mConnection);
        mIsBound = false;
        Toast.makeText(this, "unbind", Toast.LENGTH_SHORT).show();
    }

    private void killRemoteService() {
        Log.d(TAG, "[ClientActivity] killRemoteService");
        try {
            android.os.Process.killProcess(mRemoteService.getPid());
            Toast.makeText(this, "杀死服务成功", Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(AIDLActivity.this, "杀死服务失败", Toast.LENGTH_SHORT).show();
        }
    }
}
