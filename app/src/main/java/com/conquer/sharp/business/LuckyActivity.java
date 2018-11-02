package com.conquer.sharp.business;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.GlideApp;
import com.conquer.sharp.bean.LuckyBean;
import com.conquer.sharp.widget.LuckyLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LuckyActivity extends BaseActivity {

    @BindView(R.id.luckyLayout)
    LuckyLayout luckyLayout;

    @BindView(R.id.turn)
    Button turn;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lucky);

        ButterKnife.bind(this);

        final List<LuckyBean> luckyBeanList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LuckyBean luckyBean = new LuckyBean();
            luckyBeanList.add(luckyBean);
        }

        luckyLayout.setRotationType(LuckyLayout.ROTATION_TYPE_POINTER);
        luckyLayout.getLuckyWheelView().setCount(luckyBeanList.size());
        luckyLayout.getLuckyWheelView().setData(luckyBeanList);

        for (int i = 0; i < luckyBeanList.size(); i++) {
            GlideApp.with(this)
                    .load(luckyBeanList.get(i).url)
                    .into(luckyLayout.getLuckyWheelView().getImageList().get(i));
        }

        turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int turnCount = 8 * (5 + random.nextInt(5)) + random.nextInt(8);
                luckyLayout.turnByCount(turnCount);
            }
        });
    }
}
