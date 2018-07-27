package com.conquer.sharp.danmaku.normal;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.danmaku.bean.DanMu;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ac on 18/7/25.
 *
 */

public class DanMu3Activity extends BaseActivity {

    @BindView(R.id.danMuQueueView)
    DanMuQueueView danMuQueueView;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dan_mu3);
        ButterKnife.bind(this);
        initDanMu();
    }

    private void initDanMu() {
        LayoutTransition transition = new LayoutTransition();
        ObjectAnimator animAppear = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f);
        transition.setAnimator(LayoutTransition.APPEARING, animAppear);
        ObjectAnimator animChangeDisappear = ObjectAnimator.ofFloat(null, "translationY", 0f, 0f);
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, animChangeDisappear);
        danMuQueueView.setLayoutTransition(transition);

        for (int i = 0; i < 10; i++) {
            DanMu danMuShort = new DanMu("商品记录啊啊啊啊啊" + i);
            danMuQueueView.getDanMuList().add(danMuShort);
            DanMu danMuLong = new DanMu("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊" + i);
            danMuQueueView.getDanMuList().add(danMuLong);
        }

        for (int i = 0; i < 3; i++) {
            DanMu danMu = new DanMu("holder");
            danMu.type = DanMu.DanMuType.DAN_MU_PLACE_HOLDER;
            danMuQueueView.getDanMuList().add(danMu);
        }

        danMuQueueView.setInterval(1000);
        danMuQueueView.startDanMu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        danMuQueueView.release();
    }
}
