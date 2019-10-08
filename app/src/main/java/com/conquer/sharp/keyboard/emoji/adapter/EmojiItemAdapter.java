package com.conquer.sharp.keyboard.emoji.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.api.SharpUIKit;
import com.conquer.sharp.emoji.EmojiParser;
import com.conquer.sharp.keyboard.emoji.event.EmojiClickEvent;
import com.conquer.sharp.recycler.BaseRecyclerAdapter;
import com.conquer.sharp.recycler.RecyclerViewHolder;
import com.conquer.sharp.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

public class EmojiItemAdapter extends BaseRecyclerAdapter<Integer> {

    private int firstRealIndex; // 此页第一个emoji在整个emoji数组中的索引

    EmojiItemAdapter(Context context, int layoutId, int firstRealIndex) {
        super(context, layoutId);
        this.firstRealIndex = firstRealIndex;
    }

    @Override
    public void convert(RecyclerViewHolder holder, Integer iconRes, int position) {
        holder.setImageResource(R.id.iv_emoji, iconRes);

        ImageView iv_emoji = holder.getView(R.id.iv_emoji);
        iv_emoji.getLayoutParams().height = ScreenUtils.dip2px(48);
        iv_emoji.getLayoutParams().width = ScreenUtils.dip2px(48);

        holder.setOnClickListener(R.id.iv_emoji, v -> {
            int posInEmoji = firstRealIndex + position;
            CharSequence emoji = EmojiParser.getInstance()
                    .addEmojiSpans(EmojiParser.mEmojiTexts[posInEmoji], SharpUIKit.getContext());
            EventBus.getDefault().post(new EmojiClickEvent(EmojiParser.mEmojiTexts[posInEmoji], emoji));
        });
    }
}
