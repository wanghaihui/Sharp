package com.conquer.sharp.keyboard;

import android.content.Context;

import com.conquer.sharp.R;
import com.conquer.sharp.recycler.BaseRecyclerAdapter;
import com.conquer.sharp.recycler.RecyclerViewHolder;
import com.conquer.sharp.widget.EmojiTextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseRecyclerAdapter<String> {

    MessageAdapter(Context context, int layoutId) {
        super(context, layoutId);
        mDataList = new ArrayList<>();
    }

    public void convert(RecyclerViewHolder holder, String message, int position) {
        EmojiTextView emojiTextView = holder.getView(R.id.emojiTextView);
        emojiTextView.setText(message);
    }
}
