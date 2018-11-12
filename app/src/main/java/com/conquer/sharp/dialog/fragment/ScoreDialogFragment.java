package com.conquer.sharp.dialog.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.conquer.sharp.R;
import com.conquer.sharp.base.glide.GlideApp;
import com.conquer.sharp.widget.StarLayout;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreDialogFragment extends BaseTopDialogFragment {

    @BindView(R.id.starLayout)
    StarLayout starLayout;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.avatar)
    ImageView avatar;

    public static ScoreDialogFragment newInstance() {
        return new ScoreDialogFragment();
    }

    protected View createView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_dialog_score, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().setCanceledOnTouchOutside(false);

        starLayout.setLevel(3);

        DecimalFormat decimalFormat = new DecimalFormat(",###");
        score.setText(decimalFormat.format(Double.parseDouble("12123344")));

        GlideApp.with(this)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541084565388&di=2a53429265557c0479a412fe24d8e507&imgtype=0&src=http%3A%2F%2Fcdnq.duitang.com%2Fuploads%2Fitem%2F201207%2F23%2F20120723154623_Nh4WF.thumb.700_0.jpeg")
                .circleCrop()
                .into(avatar);
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
