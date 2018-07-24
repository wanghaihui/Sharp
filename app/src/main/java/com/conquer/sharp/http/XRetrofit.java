package com.conquer.sharp.http;

import com.conquer.sharp.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class XRetrofit {
    private static class Holder {
        static XRetrofit instance = new XRetrofit();
    }

    private XRetrofit(){

    }

    public static XRetrofit instance() {
        return XRetrofit.Holder.instance;
    }

    public Retrofit get() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(logInterceptor);
        }

        clientBuilder.addInterceptor(new BaseInterceptor());

        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.API_HOST)
                .client(clientBuilder.build())
                .build();
    }
}
