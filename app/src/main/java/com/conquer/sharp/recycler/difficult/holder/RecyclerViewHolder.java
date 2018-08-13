package com.conquer.sharp.recycler.difficult.holder;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ac on 18/8/13.
 *
 */

public abstract class RecyclerViewHolder<T extends RecyclerView.Adapter, H extends BaseViewHolder, K> {
    private final T adapter;

    public RecyclerViewHolder(T adapter) {
        this.adapter = adapter;
    }

    public T getAdapter() {
        return adapter;
    }

    public abstract void convert(H holder, K data, int position, boolean isScrolling);
}
