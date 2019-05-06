package com.conquer.sharp.glide;

import android.os.Bundle;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlideActivity extends BaseActivity {

    @BindView(R.id.ivImage)
    ImageView ivImage;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_glide);

        ButterKnife.bind(this);

        GlideApp.with(this)
                .load("")
                .placeholder(R.drawable.image_error)
                .into(ivImage);

    }
}
