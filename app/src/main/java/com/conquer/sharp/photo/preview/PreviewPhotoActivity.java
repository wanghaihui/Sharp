package com.conquer.sharp.photo.preview;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.Window;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.glide.GlideApp;
import com.conquer.sharp.util.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ac on 18/7/17.
 *
 */

public class PreviewPhotoActivity extends BaseActivity {

    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.activity_preview_photo);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            getWindow().setReenterTransition(new Explode());
            getWindow().setEnterTransition(new Explode());
            getWindow().setReturnTransition(new Explode());
        }

        GlideApp.with(this)
                .asBitmap()
                .override(ScreenUtils.screenWidth, ScreenUtils.screenWidth)
                .load(R.mipmap.ic_cs1)
                .into(ivPhoto);
    }
}
