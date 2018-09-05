package com.conquer.sharp.recycler.difficult.adapter;

import android.support.v7.widget.RecyclerView;

import com.conquer.sharp.recycler.difficult.holder.BaseViewHolder;

public abstract class BaseAdapter<T, K extends BaseViewHolder> extends RecyclerView.Adapter<K> implements IRecyclerView {
    private static final String TAG = BaseAdapter.class.getSimpleName();



}
