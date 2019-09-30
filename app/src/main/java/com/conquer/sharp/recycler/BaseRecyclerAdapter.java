package com.conquer.sharp.recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ac on 18/6/22.
 *
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    protected Context mContext;
    protected List<T> mDataList;
    protected int mLayoutId;

    public BaseRecyclerAdapter(Context context) {
        mContext = context;
    }

    public BaseRecyclerAdapter(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecyclerViewHolder.get(mContext, parent, mLayoutId, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        convert(holder, mDataList.get(position), position);
    }

    public abstract void convert(RecyclerViewHolder holder, T t, int position);

    public List<T> getDataList() {
        return mDataList;
    }

    public void addAll(Collection<? extends T> data) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.addAll(data);
        notifyDataSetChanged();
    }
}
