package com.conquer.sharp.danmaku.ksong;

import android.os.Bundle;
import android.os.Handler;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.danmaku.DanMuAdapter;
import com.conquer.sharp.danmaku.DanMuMultiItemTypeSupport;
import com.conquer.sharp.danmaku.bean.DanMu;
import com.conquer.sharp.danmaku.internal.AutoPlayRecyclerView;
import com.conquer.sharp.danmaku.internal.ViewPagerLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ac on 18/7/20.
 *
 */

public class DanMuKSongActivity extends BaseActivity {
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

        DanMu danMuShort = new DanMu("商品记录啊啊啊啊啊");
        danMuAdapter.getDataList().add(danMuShort);

        DanMuKSongLayoutManager layoutManager = new DanMuKSongLayoutManager(this, ViewPagerLayoutManager.VERTICAL);
        danMuRecyclerView.setLayoutManager(layoutManager);

        danMuRecyclerView.setAdapter(danMuAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DanMu danMuLong = new DanMu("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
                danMuAdapter.getDataList().add(danMuLong);
                danMuAdapter.notifyDataSetChanged();
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DanMu danMuLong = new DanMu("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
                danMuAdapter.getDataList().add(danMuLong);
                danMuAdapter.notifyDataSetChanged();
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    DanMu danMuShort1 = new DanMu("商品记录啊啊啊啊啊" + i);
                    danMuAdapter.getDataList().add(danMuShort1);
                    DanMu danMuLong = new DanMu("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊" + i);
                    danMuAdapter.getDataList().add(danMuLong);
                }
                for (int i = 0; i < 3; i++) {
                    DanMu danMu = new DanMu("");
                    danMu.type = DanMu.DanMuType.DAN_MU_PLACE_HOLDER;
                    danMuAdapter.getDataList().add(danMu);
                }

                danMuAdapter.notifyDataSetChanged();

                danMuRecyclerView.startNoDelay();
            }
        }, 3000);

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
