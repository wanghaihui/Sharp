package com.conquer.sharp.danmaku;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.conquer.sharp.R;

/**
 * Created by ac on 18/7/17.
 *
 */

public class DanMuViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    //实现的方法
    public DanMuViewHolder(View itemView) {
        super(itemView);
        textView= (TextView) itemView.findViewById(R.id.item_tv_title);
    }
}
