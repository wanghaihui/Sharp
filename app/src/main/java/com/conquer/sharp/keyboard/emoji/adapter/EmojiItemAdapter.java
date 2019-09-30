package com.conquer.sharp.keyboard.emoji.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.api.SharpUIKit;
import com.conquer.sharp.emoji.EmojiParser;
import com.conquer.sharp.keyboard.emoji.event.EmojiClickEvent;
import com.conquer.sharp.recycler.BaseRecyclerAdapter;
import com.conquer.sharp.recycler.RecyclerViewHolder;

import org.greenrobot.eventbus.EventBus;

public class EmojiItemAdapter extends BaseRecyclerAdapter<Integer> {

    private int firstRealIndex; // 此页第一个emoji在整个emoji数组中的索引
    private int itemHeight;
    private int itemWidth;
    private int itemPadding;

    EmojiItemAdapter(Context context, int layoutId, int firstRealIndex, int itemHeight, int itemWidth, int itemPadding) {
        super(context, layoutId);
        this.firstRealIndex = firstRealIndex;
        this.itemHeight = itemHeight;
        this.itemWidth = itemWidth;
        this.itemPadding = itemPadding;
    }

    @Override
    public void convert(RecyclerViewHolder holder, Integer iconRes, int position) {
        holder.setImageResource(R.id.iv_emoji, iconRes);

        ImageView iv_emoji = holder.getView(R.id.iv_emoji);
        iv_emoji.getLayoutParams().height = itemHeight;
        iv_emoji.getLayoutParams().width = itemWidth;
        if (itemPadding >= 0) {
            iv_emoji.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
        }

        holder.setOnClickListener(R.id.iv_emoji, v -> {
            int posInEmoji = firstRealIndex + position;
            CharSequence emoji = EmojiParser.getInstance()
                    .addEmojiSpans(EmojiParser.mEmojiTexts[posInEmoji], SharpUIKit.getContext());
            EventBus.getDefault().post(new EmojiClickEvent(EmojiParser.mEmojiTexts[posInEmoji], emoji));
        });
    }
}
