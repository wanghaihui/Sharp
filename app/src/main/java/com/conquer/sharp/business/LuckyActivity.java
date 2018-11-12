package com.conquer.sharp.business;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.glide.GlideApp;
import com.conquer.sharp.bean.LuckyBean;
import com.conquer.sharp.dialog.fragment.LuckyDialogFragment;
import com.conquer.sharp.dialog.fragment.ScoreDialogFragment;
import com.conquer.sharp.widget.LuckyLayout;
import com.conquer.sharp.widget.ProgressLayout;
import com.conquer.sharp.widget.StarLayout;

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

    @BindView(R.id.starLayout)
    StarLayout starLayout;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.luckyBtn)
    Button luckyBtn;
    @BindView(R.id.scoreBtn)
    Button scoreBtn;
    @BindView(R.id.progressLayout)
    ProgressLayout progressLayout;
    @BindView(R.id.progressLayout2)
    ProgressLayout progressLayout2;
    @BindView(R.id.progressLayout3)
    ProgressLayout progressLayout3;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lucky);

        ButterKnife.bind(this);

        final List<LuckyBean> luckyBeanList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LuckyBean luckyBean = new LuckyBean();
            luckyBeanList.add(luckyBean);
        }

        luckyLayout.setBackground(2);
        luckyLayout.setRotationType(LuckyLayout.ROTATION_TYPE_WHEEL);
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

        starLayout.setLevel(3);

        progressBar.setMax(100);
        progressBar.setProgress(50);
        progressBar.setSecondaryProgress(100);

        luckyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LuckyDialogFragment.newInstance().show(getSupportFragmentManager(), "LuckyDialogFragment");
            }
        });

        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreDialogFragment.newInstance().show(getSupportFragmentManager(), "ScoreDialogFragment");
            }
        });

        progressLayout.setProgress("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541084565388&di=2a53429265557c0479a412fe24d8e507&imgtype=0&src=http%3A%2F%2Fcdnq.duitang.com%2Fuploads%2Fitem%2F201207%2F23%2F20120723154623_Nh4WF.thumb.700_0.jpeg"
        , 50, 1);
        progressLayout.setText("1000", 1);

        progressLayout2.setProgress("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541084565388&di=2a53429265557c0479a412fe24d8e507&imgtype=0&src=http%3A%2F%2Fcdnq.duitang.com%2Fuploads%2Fitem%2F201207%2F23%2F20120723154623_Nh4WF.thumb.700_0.jpeg"
                , 50, 2);
        progressLayout2.setText("1000", 2);

        progressLayout3.setProgress("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541084565388&di=2a53429265557c0479a412fe24d8e507&imgtype=0&src=http%3A%2F%2Fcdnq.duitang.com%2Fuploads%2Fitem%2F201207%2F23%2F20120723154623_Nh4WF.thumb.700_0.jpeg"
                , 50, 3);
        progressLayout3.setText("1000", 3);
    }
}
