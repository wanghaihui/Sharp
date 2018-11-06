package com.conquer.sharp.dialog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conquer.sharp.R;
import com.conquer.sharp.base.GlideApp;
import com.conquer.sharp.bean.LuckyBean;
import com.conquer.sharp.widget.LuckyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LuckyDialogFragment extends BaseCenterDialogFragment {

    @BindView(R.id.luckyLayout)
    LuckyLayout luckyLayout;

    public static LuckyDialogFragment newInstance() {
        return new LuckyDialogFragment();
    }

    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_dialog_lucky, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
    }
}
