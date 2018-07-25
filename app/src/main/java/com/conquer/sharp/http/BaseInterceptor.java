package com.conquer.sharp.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseInterceptor implements Interceptor {

    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        return chain.proceed(oldRequest);
    }
    
}
