package com.conquer.sharp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment {
    private boolean isInit = false;
    protected View mainView = null;

    protected int layoutId;

    private Unbinder unbinder;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflate(layoutId, inflater, container);
    }

    protected View inflate(int layoutId, @NonNull LayoutInflater inflater, ViewGroup container) {
        if (mainView != null) {
            if (mainView.getParent() != null) {
                ((ViewGroup) mainView.getParent()).removeView(mainView);
            }
        } else {
            mainView = inflater.inflate(layoutId, container, false);
        }

        unbinder = ButterKnife.bind(this, mainView);
        return mainView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (!isInit) {
            isInit = true;
            initViews();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public abstract void initViews();

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        compositeDisposable.dispose();
    }
}
