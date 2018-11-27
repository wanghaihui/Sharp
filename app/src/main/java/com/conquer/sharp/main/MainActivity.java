package com.conquer.sharp.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.conquer.sharp.IntentManager;
import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.dialog.fragment.DirectoryDialogFragment;
import com.conquer.sharp.ptr.PullToRefreshLayout;
import com.conquer.sharp.recycler.OnRVItemClickListener;
import com.conquer.sharp.recycler.decoration.SpacingDecoration;
import com.conquer.sharp.recycler.extend.HFRecyclerAdapter;
import com.conquer.sharp.recycler.photo.QuickPhotoRecyclerView;
import com.conquer.sharp.util.system.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.refreshLayout)
    PullToRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    QuickPhotoRecyclerView mRecyclerView;

    protected HFRecyclerAdapter hfRecyclerAdapter;

    private static final String[] strData = new String[] {
            "0.弹幕(自定义版)", "1.弹幕(RecyclerView版--推荐)", "2.照片(系统选择和拍照)",
            "3.ProgressDialog", "4.音频(oboe)", "5.Deep Link", "6.Instant Run", "7.HTTP",
            "8.Cocos", "9.OpenGL", "10.DialogFragment", "11.转盘抽奖", "12.Vertical SeekBar",
            "13.Wait/Notify/NotifyAll", "14.Camera", "15.爱唱Camera"
    };

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initActionBar();

        mRefreshLayout.setOnRefreshListener(this);

        MainAdapter mMainAdapter = new MainAdapter(this, R.layout.layout_main);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        hfRecyclerAdapter = new HFRecyclerAdapter(mMainAdapter);
        mRecyclerView.setAdapter(hfRecyclerAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(0, ScreenUtils.dip2px(12),
                false));

        for (String name : strData) {
            mMainAdapter.getDataList().add(name);
        }

        mMainAdapter.notifyDataSetChanged();

        mMainAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onItemClick(int position) {
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
                    case 9:
                        IntentManager.intentOpenGL(MainActivity.this);
                        break;
                    case 10:
                        DirectoryDialogFragment.newInstance().show(getSupportFragmentManager(), "DirectoryDialogFragment");
                        break;
                    case 11:
                        IntentManager.intentLucky(MainActivity.this);
                        break;
                    case 12:
                        IntentManager.intentVerticalSeekBar(MainActivity.this);
                        break;
                    case 13:
                        break;
                    case 14:
                        IntentManager.intentCamera(MainActivity.this);
                        break;
                    case 15:
                        IntentManager.intentAiCamera(MainActivity.this);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ((TextView) findViewById(R.id.toolbar_title)).setText(getResources().getString(R.string.main_page));
    }

    @Override
    public void onPullDownToRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.stopLoading();
                }
            }
        }, 2000);
    }

    @Override
    public void onPullUpToRefresh() {
        // 使用RecyclerView的LoadMore
    }
}
