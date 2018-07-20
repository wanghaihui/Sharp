package com.conquer.sharp.danmaku;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.danmaku.bean.DanMu;
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
        danMuAdapter = new DanMuAdapter(this, new DanMuMultiItemTypeSupport());

        for (int i = 0; i < 5; i++) {
            DanMu danMuShort = new DanMu("商品记录啊啊啊啊啊" + i);
            danMuAdapter.getDataList().add(danMuShort);
            DanMu danMuLong = new DanMu("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊" + i);
            danMuAdapter.getDataList().add(danMuLong);
        }
        for (int i = 0; i < 3; i++) {
            DanMu danMu = new DanMu("");
            danMu.type = DanMu.DanMuType.DAN_MU_PLACE_HOLDER;
            danMuAdapter.getDataList().add(danMu);
        }

        DanMuLayoutManager layoutManager = new DanMuLayoutManager(this, ViewPagerLayoutManager.VERTICAL);
        danMuRecyclerView.setLayoutManager(layoutManager);
        danMuRecyclerView.setAdapter(danMuAdapter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 自定义弹幕
    @Override
    protected void onPause() {
        super.onPause();
        danMuRecyclerView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        danMuRecyclerView.start();
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
