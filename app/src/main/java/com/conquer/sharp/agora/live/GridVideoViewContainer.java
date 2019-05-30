package com.conquer.sharp.agora.live;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.SurfaceView;

public class GridVideoViewContainer extends RecyclerView {

    private GridVideoViewContainerAdapter mGridVideoViewContainerAdapter;
    private VideoViewEventListener mEventListener;

    public GridVideoViewContainer(Context context) {
        super(context);
    }

    public GridVideoViewContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GridVideoViewContainer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItemEventHandler(VideoViewEventListener listener) {
        mEventListener = listener;
    }

    public void initViewContainer(Context context, int localUid, SparseArray<SurfaceView> uids) {
        boolean newCreated = initAdapter(localUid, uids);
        if (!newCreated) {
            mGridVideoViewContainerAdapter.setLocalUid(localUid);
            mGridVideoViewContainerAdapter.init(uids, localUid, true);
        }

        setAdapter(mGridVideoViewContainerAdapter);
        // 此处设置每行的个数
        setLayoutManager(new GridLayoutManager(context, 3, RecyclerView.VERTICAL, false));
        mGridVideoViewContainerAdapter.notifyDataSetChanged();
    }

    private boolean initAdapter(int localUid, SparseArray<SurfaceView> uids) {
        if (mGridVideoViewContainerAdapter == null) {
            mGridVideoViewContainerAdapter = new GridVideoViewContainerAdapter(getContext(), localUid, uids, mEventListener);
            mGridVideoViewContainerAdapter.setHasStableIds(true);
            return true;
        }
        return false;
    }

    public SurfaceView getSurfaceView(int index) {
        return mGridVideoViewContainerAdapter.getItem(index).mSurfaceView;
    }

    public VideoUserStatus getItem(int position) {
        return mGridVideoViewContainerAdapter.getItem(position);
    }
}
