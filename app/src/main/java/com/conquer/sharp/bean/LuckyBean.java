package com.conquer.sharp.bean;

import android.graphics.Bitmap;

public class LuckyBean {
    public String id;
    public Bitmap bitmap;
    // 图片
    public String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541084565388&di=2a53429265557c0479a412fe24d8e507&imgtype=0&src=http%3A%2F%2Fcdnq.duitang.com%2Fuploads%2Fitem%2F201207%2F23%2F20120723154623_Nh4WF.thumb.700_0.jpeg";
    // 文字
    public String text = "谢谢参与";

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
