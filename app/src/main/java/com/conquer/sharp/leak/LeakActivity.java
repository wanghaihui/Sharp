package com.conquer.sharp.leak;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeakActivity extends BaseActivity {

    @BindView(R.id.btnFinish)
    Button btnFinish;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_leak);
        ButterKnife.bind(this);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
