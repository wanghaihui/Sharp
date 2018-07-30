package com.conquer.sharp.dialog;

import android.os.Bundle;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseActivity;

import butterknife.ButterKnife;

public class DialogActivity extends BaseActivity {

    private XProgressDialog xProgressDialog;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);

        xProgressDialog = new XProgressDialog(this);
        xProgressDialog.show();
    }
}
