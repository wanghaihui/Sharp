package com.conquer.sharp.login.api;

import com.conquer.sharp.http.SimpleResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface LoginApi {
    /**
     * 获取短信验证码
     */
    @GET("v1/login/vcode")
    Observable<SimpleResponse> getVCode(@QueryMap Map<String, String> map);
}
