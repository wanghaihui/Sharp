package com.conquer.sharp.agora;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface AgoraApi {
    @GET("apiv5/live/agoraio.php")
    Observable<Token> getToken(@QueryMap Map<String, String> map);
}
