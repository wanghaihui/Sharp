package com.conquer.sharp;

import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.ptr.PullToRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private static final int REFRESH_PERIOD = 7000;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    private static final String[] strDatas = new String[] {
            "弹幕", "弹幕(全民K歌版)", "图片", "fourth", "fifth"
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        pullToRefreshLayout.setOnRefreshListener(this);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strDatas));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 弹幕
                        IntentManager.intentDanMu(MainActivity.this);
                        break;
                    case 1:
                        IntentManager.intentDanMuKSong(MainActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onPullDownToRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.stopLoading();
            }
        }, REFRESH_PERIOD);
    }

    @Override
    public void onPullUpToRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.stopLoading();
            }
        }, 1000);
    }
}
