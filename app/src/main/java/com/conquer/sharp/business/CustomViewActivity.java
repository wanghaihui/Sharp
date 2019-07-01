package com.conquer.sharp.business;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.glide.GlideApp;
import com.conquer.sharp.dialog.fragment.CardViewDialogFragment;
import com.conquer.sharp.glide.GlideCircleWithBorder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomViewActivity extends BaseActivity {

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.avatar2)
    ImageView avatar2;
    @BindView(R.id.innerCircle)
    ImageView innerCircle;
    @BindView(R.id.outerCircle)
    ImageView outerCircle;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);

        /*GlideApp.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561808804651&di=cb002dfa288e98434518b63e16269443&imgtype=0&src=http%3A%2F%2F09imgmini.eastday.com%2Fmobile%2F20190623%2F20190623125041_523375e7e77e234f8a5d54e479c73a0c_4_mwpm_03201609.jpg")
                .circleCrop()
                .transform(new GlideCircleWithBorder(1, Color.parseColor("#FFFFFF")))
                .into(avatar);*/
        GlideApp.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561808804651&di=cb002dfa288e98434518b63e16269443&imgtype=0&src=http%3A%2F%2F09imgmini.eastday.com%2Fmobile%2F20190623%2F20190623125041_523375e7e77e234f8a5d54e479c73a0c_4_mwpm_03201609.jpg")
                .apply(new RequestOptions().centerCrop().transform(new GlideCircleWithBorder(2, Color.parseColor("#FFFFFF"))))
                .into(avatar);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFragment();
            }
        });

        GlideApp.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561808804651&di=cb002dfa288e98434518b63e16269443&imgtype=0&src=http%3A%2F%2F09imgmini.eastday.com%2Fmobile%2F20190623%2F20190623125041_523375e7e77e234f8a5d54e479c73a0c_4_mwpm_03201609.jpg")
                .apply(new RequestOptions().centerCrop().transform(new GlideCircleWithBorder(2, Color.parseColor("#FFFFFF"))))
                .into(avatar2);
        ObjectAnimator animInner = ObjectAnimator.ofFloat(innerCircle, "alpha", 0f, 1f, 0f);
        animInner.setDuration(1600);
        animInner.setInterpolator(new LinearInterpolator());
        animInner.setRepeatCount(Animation.INFINITE);
        animInner.start();
        ObjectAnimator animOuter = ObjectAnimator.ofFloat(outerCircle, "alpha", 0f, 1f, 0f);
        animOuter.setStartDelay(300);
        animOuter.setDuration(1600);
        animInner.setInterpolator(new LinearInterpolator());
        animOuter.setRepeatCount(Animation.INFINITE);
        animOuter.start();
    }

    private void openDialogFragment() {
        CardViewDialogFragment.newInstance().show(getSupportFragmentManager(), "CardViewDialogFragment");
    }
}
