package com.conquer.sharp.danmaku.normal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.danmaku.bean.DanMu;
import com.conquer.sharp.util.system.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ac on 18/7/25.
 *
 */

public class DanMu3Activity extends BaseActivity {

    @BindView(R.id.danMuQueueView)
    DanMuQueueView danMuQueueView;

    private List<DanMu> danMuList = new ArrayList<>();

    private Handler handler = new Handler();

    private int position;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dan_mu3);
        ButterKnife.bind(this);
        initDanMu();
    }

    private void initDanMu() {
        for (int i = 0; i < 5; i++) {
            DanMu danMuShort = new DanMu("商品记录啊啊啊啊啊" + i);
            danMuList.add(danMuShort);
            DanMu danMuLong = new DanMu("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊" + i);
            danMuList.add(danMuLong);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View addView = getLayoutInflater().inflate(R.layout.layout_dan_mu3, null);
                ((TextView) addView.findViewById(R.id.item_tv_title)).setText("商品记录啊啊啊啊啊");
                DanMuQueueView.LayoutParams params = new DanMuQueueView.LayoutParams(DanMuQueueView.LayoutParams.MATCH_PARENT, DanMuQueueView.LayoutParams.WRAP_CONTENT);
                params.topMargin = ScreenUtil.dip2px(3);
                addView.setLayoutParams(params);
                danMuQueueView.addNewView(addView);
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View addView = getLayoutInflater().inflate(R.layout.layout_dan_mu3, null);
                ((TextView) addView.findViewById(R.id.item_tv_title)).setText("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
                DanMuQueueView.LayoutParams params = new DanMuQueueView.LayoutParams(DanMuQueueView.LayoutParams.MATCH_PARENT, DanMuQueueView.LayoutParams.WRAP_CONTENT);
                params.topMargin = ScreenUtil.dip2px(3);
                addView.setLayoutParams(params);
                danMuQueueView.addNewView(addView);
            }
        }, 4000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View addView = getLayoutInflater().inflate(R.layout.layout_dan_mu3, null);
                ((TextView) addView.findViewById(R.id.item_tv_title)).setText("商品记录啊啊啊啊啊");
                DanMuQueueView.LayoutParams params = new DanMuQueueView.LayoutParams(DanMuQueueView.LayoutParams.MATCH_PARENT, DanMuQueueView.LayoutParams.WRAP_CONTENT);
                params.topMargin = ScreenUtil.dip2px(3);
                addView.setLayoutParams(params);
                danMuQueueView.addNewView(addView);
            }
        }, 6000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View addView = getLayoutInflater().inflate(R.layout.layout_dan_mu3, null);
                ((TextView) addView.findViewById(R.id.item_tv_title)).setText("商品记录啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
                DanMuQueueView.LayoutParams params = new DanMuQueueView.LayoutParams(DanMuQueueView.LayoutParams.MATCH_PARENT, DanMuQueueView.LayoutParams.WRAP_CONTENT);
                params.topMargin = ScreenUtil.dip2px(3);
                addView.setLayoutParams(params);
                danMuQueueView.addNewView(addView);
            }
        }, 8000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View addView = getLayoutInflater().inflate(R.layout.layout_dan_mu3, null);
                ((TextView) addView.findViewById(R.id.item_tv_title)).setText("商品记录啊啊啊啊啊");
                DanMuQueueView.LayoutParams params = new DanMuQueueView.LayoutParams(DanMuQueueView.LayoutParams.MATCH_PARENT, DanMuQueueView.LayoutParams.WRAP_CONTENT);
                params.topMargin = ScreenUtil.dip2px(3);
                addView.setLayoutParams(params);
                danMuQueueView.addNewView(addView);
            }
        }, 10000);
    }
}
