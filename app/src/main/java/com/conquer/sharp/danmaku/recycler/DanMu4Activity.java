package com.conquer.sharp.danmaku.recycler;

import android.os.Bundle;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.danmaku.DanMuAdapter;
import com.conquer.sharp.danmaku.DanMuMultiItemTypeSupport;
import com.conquer.sharp.danmaku.bean.DanMu;
import com.conquer.sharp.recycler.decoration.SpacingDecoration;
import com.conquer.sharp.util.system.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ac on 18/7/28.
 *
 */

public class DanMu4Activity extends BaseActivity {

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

        for (int i = 0; i < 2; i++) {
            DanMu danMu = new DanMu("");
            danMu.type = DanMu.DanMuType.DAN_MU_PLACE_HOLDER;
            danMuAdapter.getDataList().add(danMu);
        }

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

        SmoothLinearLayoutManger layoutManager = new SmoothLinearLayoutManger(this);
        layoutManager.setSpeedSlow();
        danMuRecyclerView.setLayoutManager(layoutManager);
        danMuRecyclerView.setAdapter(danMuAdapter);
        danMuRecyclerView.addItemDecoration(new SpacingDecoration(0, ScreenUtils.dip2px(6), false));
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
