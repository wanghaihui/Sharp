package com.conquer.sharp.agora.live;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.conquer.sharp.R;
import com.conquer.sharp.agora.Constant;

import java.util.ArrayList;
import java.util.Iterator;

public class GridVideoViewContainerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final Context mContext;
    protected final LayoutInflater mInflater;
    protected final VideoViewEventListener mListener;

    protected int mItemWidth;
    protected int mItemHeight;

    // 当前的用户id
    private int mLocalUid;
    private ArrayList<VideoUserStatus> mUsers;

    public GridVideoViewContainerAdapter(Context context, int localUid, SparseArray<SurfaceView> uids,
                                         VideoViewEventListener listener) {
        mContext = context;
        mInflater = ((Activity) context).getLayoutInflater();
        mListener = listener;
        mUsers = new ArrayList<>();
        init(uids, localUid, false);
    }

    public void init(SparseArray<SurfaceView> uids, int localUid, boolean force) {
        for (int i = 0; i < uids.size(); i++) {
            if (uids.keyAt(i) == mLocalUid) {
                boolean found = false;
                for (VideoUserStatus userStatus : mUsers) {
                    if (userStatus.mUid == mLocalUid) {
                        userStatus.mUid = mLocalUid;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    mUsers.add(0, new VideoUserStatus(mLocalUid, uids.valueAt(i),
                            VideoUserStatus.DEFAULT_STATUS, VideoUserStatus.DEFAULT_VOLUME));
                }
            } else {
                boolean found = false;
                for (VideoUserStatus userStatus : mUsers) {
                    if (userStatus.mUid == uids.keyAt(i)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    mUsers.add(new VideoUserStatus(uids.keyAt(i), uids.valueAt(i),
                            VideoUserStatus.DEFAULT_STATUS, VideoUserStatus.DEFAULT_VOLUME));
                }
            }
        }

        Iterator<VideoUserStatus> it = mUsers.iterator();
        while (it.hasNext()) {
            VideoUserStatus userStatus = it.next();
            if (uids.get(userStatus.mUid) == null) {
                Log.w(Constant.LOG_TAG, "after_changed remove not exited members " + (userStatus.mUid & 0xFFFFFFFFL)
                        + " " + userStatus.mSurfaceView);
                it.remove();
            }
        }

        // 宽高的设置
        if (force || mItemWidth == 0 || mItemHeight == 0) {
            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                DisplayMetrics outMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(outMetrics);

                mItemWidth = outMetrics.widthPixels / 3;
                mItemHeight = mItemWidth * 4 / 3;
            }
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.video_view_container, parent, false);
        view.getLayoutParams().width = mItemWidth;
        view.getLayoutParams().height = mItemHeight;
        return new VideoUserStatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        VideoUserStatusHolder myHolder = (VideoUserStatusHolder) holder;
        final VideoUserStatus userStatus = mUsers.get(position);
        Log.d(Constant.LOG_TAG, "onBindViewHolder " + position + " " + userStatus + " " + myHolder + " " + myHolder.itemView);
        TouchClickFrameLayout holderView = (TouchClickFrameLayout) myHolder.itemView;

        holderView.setOnTouchListener(new OnDoubleTapListener(mContext) {
            @Override
            public void onDoubleTap(View view, MotionEvent event) {
                if (mListener != null) {
                    mListener.onItemDoubleClick(view, userStatus);
                }
            }

            @Override
            public void onSingleTapUp(View view, MotionEvent event) {
                if (mListener != null) {
                    mListener.onItemClick(view, userStatus);
                }
            }
        });

        if (holderView.getChildCount() == 0) {
            SurfaceView target = userStatus.mSurfaceView;
            stripSurfaceView(target);
            holderView.addView(target, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    public void setLocalUid(int uid) {
        mLocalUid = uid;
    }

    public int getLocalUid() {
        return mLocalUid;
    }

    protected final void stripSurfaceView(SurfaceView view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((FrameLayout) parent).removeView(view);
        }
    }

    @Override
    public int getItemCount() {
        int sizeLimit = mUsers.size();
        if (sizeLimit >= ConstantAgoraRoom.MAX_PEER_COUNT + 1) {
            sizeLimit = ConstantAgoraRoom.MAX_PEER_COUNT + 1;
        }
        return sizeLimit;
    }

    @Override
    public long getItemId(int position) {
        VideoUserStatus userStatus = mUsers.get(position);
        SurfaceView surfaceView = userStatus.mSurfaceView;
        if (surfaceView == null) {
            throw new NullPointerException("SurfaceView destroyed for user " + userStatus.mUid + " "
                    + userStatus.mStatus + " " + userStatus.mVolume);
        }
        return (String.valueOf(userStatus.mUid) + System.identityHashCode(surfaceView)).hashCode();
    }

    public VideoUserStatus getItem(int position) {
        return mUsers.get(position);
    }
}
