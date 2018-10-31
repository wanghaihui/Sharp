package com.conquer.sharp.dialog.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.conquer.sharp.R;

public abstract class BaseDialogFragment extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomToTopDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM; // 底部
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.dimAmount = 0.0f;
            window.setAttributes(lp);
        }

        return createView(inflater, container);
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container);
}
