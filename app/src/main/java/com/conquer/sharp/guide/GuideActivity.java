package com.conquer.sharp.guide;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;
import com.conquer.sharp.guide.core.Controller;
import com.conquer.sharp.guide.core.Guide;
import com.conquer.sharp.guide.core.GuidePage;
import com.conquer.sharp.guide.listener.OnGuideChangedListener;

public class GuideActivity extends BaseActivity {

    public static final String LABEL_GUIDE = "label_guide";

    private TextView mShowGuide;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guide);

        mShowGuide = findViewById(R.id.showGuide);

        Guide.with(this)
                .setLabel(LABEL_GUIDE)
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShow(Controller controller) {
                        Toast.makeText(GuideActivity.this, "Show", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        Toast.makeText(GuideActivity.this, "Remove", Toast.LENGTH_SHORT).show();
                    }
                })
                .addGuidePage(GuidePage.newInstance().addHighLight(mShowGuide).setLayoutRes(R.layout.guide_view))
                .show();
    }
}