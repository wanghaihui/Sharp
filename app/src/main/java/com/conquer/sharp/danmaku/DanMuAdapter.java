package com.conquer.sharp.danmaku;

import android.content.Context;

import com.conquer.sharp.R;
import com.conquer.sharp.danmaku.bean.DanMu;
import com.conquer.sharp.recycler.easy.BaseMultiRecyclerAdapter;
import com.conquer.sharp.recycler.easy.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * Created by ac on 18/7/17.
 *
 */

public class DanMuAdapter extends BaseMultiRecyclerAdapter<DanMu> {

    public DanMuAdapter(Context context, DanMuMultiItemTypeSupport danMuMultiItemTypeSupport) {
        super(context, danMuMultiItemTypeSupport);
        mDataList = new ArrayList<>();
    }

    @Override
    public void convert(RecyclerViewHolder holder, DanMu danMu, int position) {
        if (getItemViewType(position) == DanMuMultiItemTypeSupport.TYPE_NORMAL) {
            holder.setText(R.id.item_tv_title, danMu.danMu);
        }
    }
}
