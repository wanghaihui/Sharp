package com.conquer.sharp.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressLayout extends LinearLayout {

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.star)
    ImageView star;
    @BindView(R.id.tipStart)
    TextView tipStart;
    @BindView(R.id.level)
    TextView tvLevel;
    @BindView(R.id.tipEnd)
    TextView tipEnd;

    public ProgressLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ProgressLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_score_progress, this);
        ButterKnife.bind(view);
    }

    public void setProgress(String url, int progress, int level) {
        GlideApp.with(getContext())
                .load(url)
                .circleCrop()
                .into(avatar);

        progressBar.setSecondaryProgress(100);
        progressBar.setProgress(progress);

        switch (level) {
            case 1:
                setBackgroundResource(R.drawable.bg_score_progress_1);
                star.setImageResource(R.drawable.ic_star_one);
                tipStart.setTextColor(ContextCompat.getColor(getContext(), R.color.color_477eb3));
                tvLevel.setTextColor(ContextCompat.getColor(getContext(), R.color.color_477eb3));
                tipEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.color_477eb3));
                break;
            case 2:
                setBackgroundResource(R.drawable.bg_score_progress_2);
                star.setImageResource(R.drawable.ic_star_two);
                tipStart.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b8852e));
                tvLevel.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b8852e));
                tipEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.color_b8852e));
                break;
            case 3:
                setBackgroundResource(R.drawable.bg_score_progress_3);
                star.setImageResource(R.drawable.ic_star_three);
                tipStart.setTextColor(ContextCompat.getColor(getContext(), R.color.color_da3156));
                tvLevel.setTextColor(ContextCompat.getColor(getContext(), R.color.color_da3156));
                tipEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.color_da3156));
                break;
        }
    }

    public void setText(String text, int level) {
        tipStart.setText(getResources().getString(R.string.need_score, text));
        switch (level) {
            case 1:
                tvLevel.setText(getResources().getString(R.string.first_level));
                break;
            case 2:
                tvLevel.setText(getResources().getString(R.string.second_level));
                break;
            case 3:
                tvLevel.setText(getResources().getString(R.string.third_level));
                break;
        }
    }
}
