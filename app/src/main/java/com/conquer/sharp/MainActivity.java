package com.conquer.sharp;

import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.ptr.PullToRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {

    private static final int REFRESH_PERIOD = 5000;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    private static final String[] strDatas = new String[] {
            "弹幕(自定义版)", "弹幕(RecyclerView版--推荐)", "照片(系统选择和拍照)",
            "ProgressDialog", "音频(oboe)", "Deep Link", "Instant Run", "HTTP",
            "弹幕(自定义版)", "弹幕(RecyclerView版--推荐)", "照片(系统选择和拍照)",
            "ProgressDialog", "音频(oboe)", "Deep Link", "Instant Run", "HTTP",
            "弹幕(自定义版)", "弹幕(RecyclerView版--推荐)", "照片(系统选择和拍照)",
            "ProgressDialog", "音频(oboe)", "Deep Link", "Instant Run", "HTTP",
            "弹幕(自定义版)", "弹幕(RecyclerView版--推荐)", "照片(系统选择和拍照)",
            "ProgressDialog", "音频(oboe)", "Deep Link", "Instant Run", "HTTP"
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        pullToRefreshLayout.setOnRefreshListener(this);
        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, strDatas));

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });

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
                    case 2:
                        IntentManager.intentPhotoSelect(MainActivity.this);
                        break;
                    case 3:
                        IntentManager.intentDialog(MainActivity.this);
                        break;
                    case 4:
                        break;
                    case 5:
                        IntentManager.intentDeepLink(MainActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onPullDownToRefresh() {
        pullToRefreshLayout.setHeaderViewBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.stopLoading();
            }
        }, REFRESH_PERIOD);*/
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
