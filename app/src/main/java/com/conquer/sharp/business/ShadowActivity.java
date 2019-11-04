package com.conquer.sharp.business;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.util.ScreenUtils;
import com.conquer.sharp.widget.ShadowDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * UI设计有三宝--阴影+透明+圆角
 */
public class ShadowActivity extends BaseActivity {

    @BindView(R.id.iv_circle)
    TextView ivCircle;

    @BindView(R.id.text3)
    TextView text3;

    @BindView(R.id.ivTi)
    TextView ivTi;
    @BindView(R.id.ivBao)
    TextView ivBao;

    @BindView(R.id.tiOuterLayout)
    FrameLayout tiOuterLayout;
    @BindView(R.id.baoOuterLayout)
    FrameLayout baoOuterLayout;

    private int[] bgColorTi = new int[2];
    private int[] bgColorBao = new int[2];

    private int mPKWidth;

    public static void launch(Context context) {
        Intent intent = new Intent(context, ShadowActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shadow);
        ButterKnife.bind(this);
        bgColorTi[0] = ActivityCompat.getColor(this, R.color.color_e037a5);
        bgColorTi[1] = ActivityCompat.getColor(this, R.color.color_e7394b);
        bgColorBao[0] = ActivityCompat.getColor(this, R.color.color_17bfe6);
        bgColorBao[1] = ActivityCompat.getColor(this, R.color.color_7e82e7);

        mPKWidth = ScreenUtils.screenWidth - ScreenUtils.dip2px(60);

        ShadowDrawable.setShadowDrawable(ivCircle, ShadowDrawable.SHAPE_CIRCLE, bgColorTi, ScreenUtils.dip2px(0),
                Color.parseColor("#AAE7394B"), ScreenUtils.dip2px(4), 0, 0);

        ShadowDrawable.setShadowDrawable(text3, ShadowDrawable.SHAPE_CIRCLE, Color.parseColor("#2979FF"),
                0, Color.parseColor("#aa536DFE"), ScreenUtils.dip2px(10), 0, 0);

        ShadowDrawable.setShadowDrawable(ivTi, ShadowDrawable.SHAPE_CIRCLE, bgColorTi,
                ActivityCompat.getColor(this, R.color.color_aae7394b), ScreenUtils.dip2px(6),
                Color.WHITE, ScreenUtils.dip2px(2));
        ShadowDrawable.setShadowDrawable(ivBao, ShadowDrawable.SHAPE_CIRCLE, bgColorBao,
                ActivityCompat.getColor(this, R.color.color_aa7e82e7), ScreenUtils.dip2px(6),
                Color.WHITE, ScreenUtils.dip2px(2));

        updateOutLayoutWidth(0);
        updateOutLayoutBackground();
    }

    private void updateOutLayoutWidth(int score) {
        int tiOutWidht;
        LinearLayout.LayoutParams tiOuterParams = (LinearLayout.LayoutParams) tiOuterLayout.getLayoutParams();
        tiOuterParams.width = mPKWidth / 2;
        tiOutWidht = tiOuterParams.width;
        tiOuterLayout.setLayoutParams(tiOuterParams);

        LinearLayout.LayoutParams baoOuterParams = (LinearLayout.LayoutParams) baoOuterLayout.getLayoutParams();
        baoOuterParams.width = mPKWidth - tiOutWidht;
        baoOuterLayout.setLayoutParams(baoOuterParams);
    }

    private void updateOutLayoutBackground() {
        tiOuterLayout.setBackgroundResource(R.drawable.bg_ti_no_shadow);
        baoOuterLayout.setBackgroundResource(R.drawable.bg_bao_no_shadow);
    }

}
