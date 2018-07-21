package com.conquer.sharp.recycler.easy;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by ac on 18/6/25.
 *
 */

public abstract class BaseMultiRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {

    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    public BaseMultiRecyclerAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport) {
        super(context);
        mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        return mMultiItemTypeSupport.getItemViewType(position, mDataList.get(position));
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
        return RecyclerViewHolder.get(mContext, parent, layoutId);
    }

}
