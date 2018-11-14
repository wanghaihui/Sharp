package com.conquer.sharp.recycler;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * Created by ac on 18/6/25.
 *
 */

public abstract class BaseMultiRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {

    private MultiItemTypeSupport<T> mMultiItemTypeSupport;
    protected Bundle extra;

    public BaseMultiRecyclerAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context);
        mMultiItemTypeSupport = multiItemTypeSupport;
        extra = new Bundle();
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mDataList.get(position), extra);
    }

    @Override
    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        return RecyclerViewHolder.get(mContext, parent, layoutId, viewType);
    }

}
