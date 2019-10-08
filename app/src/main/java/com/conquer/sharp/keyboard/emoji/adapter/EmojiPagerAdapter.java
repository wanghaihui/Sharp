package com.conquer.sharp.keyboard.emoji.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.conquer.sharp.R;
import com.conquer.sharp.emoji.EmojiParser;
import com.conquer.sharp.util.ScreenUtils;

import java.util.Arrays;
import java.util.List;

public class EmojiPagerAdapter extends PagerAdapter {
    private int rows; // 行数
    private int columns; // 列数
    private int countPerPage; // 每页最大item数

    private int start; // 原数组起始位置
    private int length; // 复制原数组中数据的长度

    private Integer[] mDatas; // 该页可使用的数据

    public EmojiPagerAdapter(int rows, int columns, int start, int length) {
        this.rows = rows;
        this.columns = columns;
        this.start = start;
        this.length = length;
        init();
    }

    private void init() {
        if (EmojiParser.DEFAULT_EMOJI_RES_IDS == null) {
            EmojiParser.getInstance();
        }
        countPerPage = rows * columns;
        mDatas = new Integer[length];
        System.arraycopy(EmojiParser.DEFAULT_EMOJI_RES_IDS, start, mDatas, 0, length);
    }

    @Override
    public int getCount() {
        if (mDatas == null)
            return 0;
        int i = mDatas.length % countPerPage;
        int i1 = mDatas.length / countPerPage;
        return i == 0 ? i1 : i1 + 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        View view = View.inflate(context, R.layout.frg_recycleview, null);
        RecyclerView rcv = view.findViewById(R.id.rcv);
        rcv.setLayoutManager(new GridLayoutManager(context, columns));
        EmojiItemAdapter emojiItemAdapter = new EmojiItemAdapter(context,
                R.layout.layout_emoji_item, position * countPerPage + start);

        int start = position * countPerPage;
        int end = (position + 1) * countPerPage;
        end = end > mDatas.length ? mDatas.length : end;
        List<Integer> resList = Arrays.asList(mDatas).subList(start, end);

        emojiItemAdapter.addAll(resList);
        rcv.setAdapter(emojiItemAdapter);
        rcv.setOverScrollMode(View.OVER_SCROLL_NEVER);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

