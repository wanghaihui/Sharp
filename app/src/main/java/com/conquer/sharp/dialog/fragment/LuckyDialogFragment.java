package com.conquer.sharp.dialog.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.GlideApp;
import com.conquer.sharp.bean.LuckyBean;
import com.conquer.sharp.widget.LuckyLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LuckyDialogFragment extends BaseCenterDialogFragment {

    @BindView(R.id.luckyLayout)
    LuckyLayout luckyLayout;

    @BindView(R.id.start)
    ImageView startTurn;

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

        getDialog().setCanceledOnTouchOutside(false);
        /*getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });*/

        final List<LuckyBean> luckyBeanList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            LuckyBean luckyBean = new LuckyBean();
            luckyBeanList.add(luckyBean);
        }

        luckyLayout.setBackground(3);
        luckyLayout.setRotationType(LuckyLayout.ROTATION_TYPE_POINTER);
        luckyLayout.getLuckyWheelView().setCount(luckyBeanList.size());
        luckyLayout.getLuckyWheelView().setData(luckyBeanList);

        for (int i = 0; i < luckyBeanList.size(); i++) {
            GlideApp.with(this)
                    .load(luckyBeanList.get(i).url)
                    .into(luckyLayout.getLuckyWheelView().getImageList().get(i));
        }

        startTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int turnCount = 8 * (5 + random.nextInt(5)) + random.nextInt(8);
                luckyLayout.turnByCount(turnCount);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0.0f;
            window.setAttributes(windowParams);
        }
    }

}
