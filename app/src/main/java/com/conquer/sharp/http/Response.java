package com.conquer.sharp.http;

import com.conquer.sharp.bean.Data;
import com.google.gson.annotations.SerializedName;

public class Response<T extends Data> {
    @SerializedName("errorCode")
    public int code = 0;
    @SerializedName("errorMsg")
    public String msg = "";
    @SerializedName("data")
    public T data = null;
}
