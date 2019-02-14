package com.conquer.sharp.main;

import android.content.Context;
import android.view.View;

import com.conquer.sharp.R;
import com.conquer.sharp.recycler.BaseRecyclerAdapter;
import com.conquer.sharp.recycler.OnRVItemClickListener;
import com.conquer.sharp.recycler.RecyclerViewHolder;

import java.util.ArrayList;

public class MainAdapter extends BaseRecyclerAdapter<String> {

    private OnRVItemClickListener onRVItemClickListener;

    public void setOnRVItemClickListener(OnRVItemClickListener listener) {
        onRVItemClickListener = listener;
    }

    public MainAdapter(Context context, int layoutId) {
        super(context, layoutId);
        mDataList = new ArrayList<>();
    }

    public void convert(RecyclerViewHolder holder, String name, int position) {
        holder.setText(R.id.tvName, name);

        holder.getConvertView().setTag(position);
        holder.getConvertView().setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onRVItemClickListener != null) {
                onRVItemClickListener.onItemClick((Integer) view.getTag());
            }
        }
    };
}
