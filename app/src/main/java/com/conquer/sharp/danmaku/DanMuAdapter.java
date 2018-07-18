package com.conquer.sharp.danmaku;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conquer.sharp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ac on 18/7/17.
 *
 */

public class DanMuAdapter extends RecyclerView.Adapter<DanMuViewHolder> {

    private Context context;
    private final List<String> list;
    public DanMuAdapter(Context context) {
        this.context = context;
        list =  new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("商品记录" + i);
        }

    }

    public DanMuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dan_mu, parent,false);
        return new DanMuViewHolder(view);
    }
    public void onBindViewHolder(DanMuViewHolder holder, int position) {
        String item = list.get(position);
        holder.textView.setText(item);
    }

    public int getItemCount() {
        return list.size();
    }
}
