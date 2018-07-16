package com.conquer.sharp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.conquer.sharp.ptr.PullToRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {

    private static final int REFRESH_PERIOD = 700;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;

    private static final String[] strDatas = new String[] {
            "first", "second", "third", "fourth", "fifth"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        pullToRefreshLayout.setOnRefreshListener(this);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strDatas));
    }

    @Override
    public void onPullDownToRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshLayout.setRefreshing(false);
            }
        }, REFRESH_PERIOD);
    }

    @Override
    public void onPullUpToRefresh() {

    }
}
