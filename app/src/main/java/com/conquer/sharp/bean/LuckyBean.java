package com.conquer.sharp.bean;

import android.graphics.Bitmap;

public class LuckyBean {
    public String id;
    public Bitmap bitmap;
    // 图片
    public String url = "http://starcdn.mengliaoba.cn/starface/yy/gifticon/1134_s.png?1436334088";
    // 文字
    public String text = "谢谢参与";

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
