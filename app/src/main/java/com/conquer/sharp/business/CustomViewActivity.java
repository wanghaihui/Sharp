package com.conquer.sharp.business;

import android.os.Bundle;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomViewActivity extends BaseActivity {

    @BindView(R.id.avatar)
    ImageView avatar;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);

        GlideApp.with(this)
                .load("http://img.52z.com/upload/news/image/20181108/20181108204521_83402.jpg")
                .circleCrop()
                .into(avatar);
    }
}
