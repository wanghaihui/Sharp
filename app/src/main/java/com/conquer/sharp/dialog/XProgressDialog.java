package com.conquer.sharp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.conquer.sharp.R;

public class XProgressDialog extends Dialog {
    private Context mContext;

    private String mMessage;
    private int mLayoutId;
    private TextView tvMessage;

    public XProgressDialog(Context context, int style, int layout) {
        super(context, style);
        mContext = context;
        if (getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(params);
        }
        mLayoutId = layout;
    }

    public XProgressDialog(Context context, int layout, String msg) {
        this(context, R.style.progress_dialog_style, layout);
        setMessage(msg);
    }

    public XProgressDialog(Context context, String msg) {
        this(context, R.style.progress_dialog_style, R.layout.layout_progress_dialog);
        setMessage(msg);
    }

    public XProgressDialog(Context context) {
        this(context, R.style.progress_dialog_style, R.layout.layout_progress_dialog);
    }

    public void setMessage(String msg) {
        mMessage = msg;
    }

    public void updateLoadingMessage(String msg) {
        mMessage = msg;
        showMessage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(mLayoutId);
        tvMessage = findViewById(R.id.x_progress_dialog_message);
        showMessage();
    }

    private void showMessage() {
        if (tvMessage != null && !TextUtils.isEmpty(mMessage)) {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(mMessage);
        }
    }
}
