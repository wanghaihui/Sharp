package com.conquer.sharp.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectorActivity extends BaseActivity {

    @BindView(R.id.funcUseAudio)
    ImageButton funcUseAudio;

    public static void launch(Context context) {
        Intent intent = new Intent(context, SelectorActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_selector);
        ButterKnife.bind(this);

        funcUseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (funcUseAudio.isSelected()) {
                    funcUseAudio.setSelected(false);
                } else {
                    funcUseAudio.setSelected(true);
                }
            }
        });
    }

}
