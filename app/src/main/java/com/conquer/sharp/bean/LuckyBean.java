package com.conquer.sharp.bean;

import android.graphics.Bitmap;

public class LuckyBean {
    public String id;
    public Bitmap bitmap;
    // 图片
    public String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541688230562&di=ad610f29ed331aea7dba20f1159c8f5e&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0107b659ad274da8012028a91d68a1.png%401280w_1l_2o_100sh.png";
    // 文字
    public String text = "谢谢参与";

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
