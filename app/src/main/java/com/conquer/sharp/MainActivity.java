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

    private static final int REFRESH_PERIOD = 3000;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    private static final String[] strDatas = new String[] {
            "弹幕(自定义版)", "弹幕(RecyclerView版)", "HTTP", "图片"
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        pullToRefreshLayout.setOnRefreshListener(this);
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, strDatas));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        IntentManager.intentDanMu3(MainActivity.this);
                        break;
                    case 1:
                        IntentManager.intentDanMuContent(MainActivity.this);
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
