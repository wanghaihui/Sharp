package com.conquer.sharp.keyboard.emoji;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conquer.sharp.R;
import com.conquer.sharp.keyboard.emoji.adapter.EmojiPagerAdapter;
import com.conquer.sharp.util.ScreenUtils;
import com.conquer.sharp.widget.FlowIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmojiFragment extends EmojiChildFragment {

    @BindView(R.id.view_pager)
    public ViewPager viewPager;

    @BindView(R.id.flow_indicator)
    public FlowIndicator flow_indicator;

    @Override
    public int icon() {
        return R.drawable.btn_emoji1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emoji, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initViewPager();
        initListener();
    }

    private void initViewPager() {
        EmojiPagerAdapter pagerAdapter = new EmojiPagerAdapter(5, 6, 0, 112);
        viewPager.setAdapter(pagerAdapter);
    }

    private void initListener() {
        if (viewPager.getAdapter() != null) {
            int pageCount = viewPager.getAdapter().getCount();
            if (pageCount > 1) {
                flow_indicator.setCount(pageCount);
            }
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {
                    flow_indicator.setSeletion(position);
                }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
}
