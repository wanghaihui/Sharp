package com.conquer.sharp.http;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse {
    @SerializedName("errorCode")
    public int code = 0;
    @SerializedName("errorMsg")
    public String msg = "";
}
