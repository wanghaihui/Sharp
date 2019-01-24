package com.conquer.sharp.cs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.glide.GlideApp;
import com.conquer.sharp.photo.preview.PreviewPhotoActivity;
import com.conquer.sharp.util.system.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CSActivity extends BaseActivity {

    @BindView(R.id.ivChallenge)
    ImageView ivChallenge;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cs);
        ButterKnife.bind(this);

        // Why?--研究原理
        GlideApp.with(this)
                .asBitmap()
                .override(ScreenUtils.screenWidth, ScreenUtils.screenWidth)
                .load(R.mipmap.ic_cs1)
                .into(ivChallenge);

        // 未达到效果--Why?
        /*ivChallenge.setImageBitmap(BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.mipmap.ic_cs1,
                ScreenUtils.screenWidth, ScreenUtils.screenWidth));*/

        ivChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bigImage();
            }
        });
    }

    private void bigImage() {
        Intent intent = new Intent(this, PreviewPhotoActivity.class);
        // 实现原理?
        /*ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeScaleUpAnimation(ivChallenge, ivChallenge.getWidth() / 2,
                        ivChallenge.getHeight() / 2, 0, 0);
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());*/

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, ivChallenge, "challenge");
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
    }

}
