package com.conquer.sharp.keyboard.emoji;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.keyboard.emoji.event.EmojiClickEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmojiParentFragment extends Fragment {

    @BindView(R.id.view_pager)
    public ViewPager viewPager;

    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;

    @BindView(R.id.emoji_btn_delete)
    public View emoji_btn_delete;

    @BindView(R.id.tv_emoji_send)
    public View tv_emoji_send;

    @BindView(R.id.v_line_send)
    public View v_line_send;

    public enum TYPE {
        EMOJI // 普通输入法的emoji: 只支持emoji
    }

    private List<EmojiChildFragment> mFragments = new ArrayList<>();

    private OnEmojiListener onEmojiListener;

    private boolean dismissSend;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emoji_parent, null);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        initView();
        initViewPager();
        initTabLayout();
        initListener();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public boolean isShowing(Class<? extends EmojiChildFragment> clazz) {
        if (mFragments == null || mFragments.size() == 0) {
            return false;
        }
        Iterator<EmojiChildFragment> iterator = mFragments.iterator();
        while (iterator.hasNext()) {
            EmojiChildFragment next = iterator.next();
            if (next.getClass() == clazz) {
                return true;
            }
        }
        return false;
    }

    public EmojiParentFragment showEmoji() {
        mFragments.add(new EmojiFragment());
        return this;
    }

    public EmojiParentFragment setInputListener(OnEmojiListener onEmojiListener) {
        this.onEmojiListener = onEmojiListener;
        return this;
    }

    private void initView() {
        if (dismissSend) {
            v_line_send.setVisibility(View.GONE);
            tv_emoji_send.setVisibility(View.GONE);
        } else {
            v_line_send.setVisibility(View.VISIBLE);
            tv_emoji_send.setVisibility(View.VISIBLE);
        }
    }

    private void initViewPager() {
        PagerAdapter adapter = new PagerAdapter(getChildFragmentManager(), mFragments);
        viewPager.setAdapter(adapter);
    }

    private void initTabLayout() {
        if (mFragments == null) {
            return;
        }
        for (int i = 0; i < mFragments.size(); i++) {
            EmojiChildFragment fragment = mFragments.get(i);
            if (fragment.icon() == 0) {
                continue;
            }
            TabLayout.Tab tab = tabLayout.newTab();
            View view = View.inflate(getContext(), R.layout.layout_emoji_tab_item, null);
            ImageView iv_icon = view.findViewById(R.id.iv_icon);
            iv_icon.setImageResource(fragment.icon());
            tab.setCustomView(view);
            tabLayout.addTab(tab);
        }
    }

    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if (tabAt != null) {
                    tabAt.select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tv_emoji_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEmojiListener != null) {
                    onEmojiListener.onSendClick();
                }
            }
        });
        emoji_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEmojiListener != null) {
                    onEmojiListener.onDeleteClick();
                }
            }
        });
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private List<? extends EmojiChildFragment> fragments;

        PagerAdapter(FragmentManager fm, List<? extends EmojiChildFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }

    public static abstract class OnEmojiListener {
        /**
         * 过滤字符
         *
         * @param emoji 输入的emoji unicode编码
         * @return true会调用onInput, false不调onInput
         */
        public boolean onFilter(String emoji) {
            return true;
        }

        public abstract void onInput(CharSequence sequence);

        public abstract void onDeleteClick();

        public abstract void onSendClick();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEmojiClick(EmojiClickEvent event) {
        if (onEmojiListener != null) {
            boolean accept = onEmojiListener.onFilter(event.emojiText);
            if (accept) {
                onEmojiListener.onInput(event.emojiSpanText);
            }
        }
    }
}
