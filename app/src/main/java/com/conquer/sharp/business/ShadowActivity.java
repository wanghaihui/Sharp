package com.conquer.sharp.business;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

    private int[] bgColor = new int[2];

    public static void launch(Context context) {
        Intent intent = new Intent(context, ShadowActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shadow);
        ButterKnife.bind(this);
        bgColor[0] = ActivityCompat.getColor(this, R.color.color_e037a5);
        bgColor[1] = ActivityCompat.getColor(this, R.color.color_e7394b);

        ShadowDrawable.setShadowDrawable(ivCircle, ShadowDrawable.SHAPE_CIRCLE, bgColor, ScreenUtils.dip2px(0),
                Color.parseColor("#AAE7394B"), ScreenUtils.dip2px(4), 0, 0);

        ShadowDrawable.setShadowDrawable(text3, ShadowDrawable.SHAPE_CIRCLE, Color.parseColor("#2979FF"),
                0, Color.parseColor("#aa536DFE"), ScreenUtils.dip2px(10), 0, 0);
    }

}
