package com.conquer.sharp.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.conquer.sharp.R;
import com.conquer.sharp.base.BaseFragment;
import com.conquer.sharp.http.SimpleResponse;
import com.conquer.sharp.http.XRetrofit;
import com.conquer.sharp.login.api.LoginApi;

import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutId = R.layout.fragment_login;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initViews() {
        sendVCode("18501371784");
    }

    private void sendVCode(String mobile) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("mobile", mobile);

        Disposable disposable = XRetrofit.instance().get()
                .create(LoginApi.class)
                .getVCode(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SimpleResponse>() {
                    @Override
                    public void accept(SimpleResponse response) {
                        if (0 == response.code) {

                        } else {
                            if (!TextUtils.isEmpty(response.msg)) {
                                Toast.makeText(getActivity(), response.msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {

                    }
                });
        compositeDisposable.add(disposable);
    }
}
