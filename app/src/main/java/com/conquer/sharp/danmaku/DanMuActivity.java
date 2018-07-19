package com.conquer.sharp.danmaku;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.danmaku.internal.AutoPlayRecyclerView;
import com.conquer.sharp.danmaku.internal.ViewPagerLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ac on 18/7/17.
 *
 */

public class DanMuActivity extends BaseActivity {

    @BindView(R.id.danMuRecyclerView)
    AutoPlayRecyclerView danMuRecyclerView;

    private DanMuAdapter danMuAdapter;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dan_mu);
        ButterKnife.bind(this);
        initDanMu();
    }

    private void initDanMu() {
        DanMuLayoutManager layoutManager = new DanMuLayoutManager(this, ViewPagerLayoutManager.VERTICAL);
        danMuRecyclerView.setLayoutManager(layoutManager);
        danMuAdapter = new DanMuAdapter(this);
        danMuRecyclerView.setAdapter(danMuAdapter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 自定义弹幕
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
