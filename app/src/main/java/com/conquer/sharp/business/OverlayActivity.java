package com.conquer.sharp.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.base.glide.GlideApp;
import com.conquer.sharp.recycler.BaseRecyclerAdapter;
import com.conquer.sharp.recycler.RecyclerViewHolder;
import com.conquer.sharp.recycler.decoration.OverlayHorizontalDecoration;
import com.conquer.sharp.util.ScreenUtils;
import com.conquer.sharp.widget.CircleImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OverlayActivity extends BaseActivity {

    private List<String> mLeftList = new ArrayList<>();
    private List<String> mRightList = new ArrayList<>();

    private OverlayAdapter mLeftAdapter;
    private OverlayAdapter mRightAdapter;

    @BindView(R.id.leftRView)
    RecyclerView leftRView;
    @BindView(R.id.rightRView)
    RecyclerView rightRView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, OverlayActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_overlay);
        ButterKnife.bind(this);

        mLeftList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573810711985&di=7284f7dd29cbc4470c3a4d9d87e0c5d9&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201507%2F13%2F20150713153609_YKU8V.jpeg");
        mLeftList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573810711962&di=81f0df08d913626bf325212ef8270d16&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201401%2F10%2F20140110155629_XHwna.jpeg");
        mLeftList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573810711959&di=7c429c35c38a2197376b31daa6c3190a&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201210%2F04%2F20121004231502_NrBQG.jpeg");
        mLeftList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573810711958&di=73de8270c33fa25739b1bb514cc7f57a&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201312%2F09%2F20131209151602_evtcw.jpeg");

        mRightList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573811002365&di=3f7d922469d56ae8c1dd4a08e52f37c0&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201402%2F18%2F20140218155137_wrHEt.jpeg");
        mRightList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573811084289&di=091ad4b4e50a5ca4ef9a38100031469f&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201311%2F21%2F20131121160247_4xcHP.jpeg");
        mRightList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573811084289&di=d93b01e1f16fd74be6c14c42e25f3261&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201509%2F27%2F20150927101534_vefxJ.jpeg");
        mRightList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573811252943&di=fe0061a1eceb8b226be0883f21776eb5&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F15b769d5eaef51ce93447a792c256924f4bdf80b1e6a97-nDz2Zx_fw658");

        mLeftAdapter = new OverlayAdapter(this, R.layout.item_overlay);
        mRightAdapter = new OverlayAdapter(this, R.layout.item_overlay);

        leftRView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        leftRView.addItemDecoration(new OverlayHorizontalDecoration(-ScreenUtils.dip2px(12),
                OverlayHorizontalDecoration.ORIENTATION_RIGHT));
        leftRView.setAdapter(mLeftAdapter);

        rightRView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, true));
        rightRView.addItemDecoration(new OverlayHorizontalDecoration(-ScreenUtils.dip2px(12),
                OverlayHorizontalDecoration.ORIENTATION_LEFT));
        rightRView.setAdapter(mRightAdapter);

        updateLeftAvatar(mLeftList);
        updateRightAvatar(mRightList);
    }

    public void updateLeftAvatar(List<String> leftList) {
        if (leftList == null) {
            return;
        }
        Collections.reverse(leftList);
        mLeftAdapter.getDataList().clear();
        mLeftAdapter.getDataList().addAll(leftList);
        mLeftAdapter.notifyDataSetChanged();
    }

    public void updateRightAvatar(List<String> rightList) {
        if (rightList == null) {
            return;
        }
        Collections.reverse(rightList);
        mRightAdapter.getDataList().clear();
        mRightAdapter.getDataList().addAll(rightList);
        mRightAdapter.notifyDataSetChanged();
    }

    public static class OverlayAdapter extends BaseRecyclerAdapter<String> {

        public OverlayAdapter(Context context, int layoutId) {
            super(context, layoutId);
            mDataList = new ArrayList<>();
        }

        @Override
        public void convert(RecyclerViewHolder holder, String url, int position) {
            CircleImageView ivAvatar = holder.getView(R.id.ivAvatar);
            GlideApp.with(mContext)
                    .load(url)
                    .into(ivAvatar);
        }
    }
}
