package com.conquer.sharp.deeplink;

import android.os.Bundle;
import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by ac on 18/8/11.
 *
 */

public class DeepLinkActivity extends BaseActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_deep_link);
        ButterKnife.bind(this);
    }


}
