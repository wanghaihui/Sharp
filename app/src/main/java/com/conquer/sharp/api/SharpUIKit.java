package com.conquer.sharp.api;

import android.content.Context;

import com.conquer.sharp.impl.SharpUIKitImpl;

/**
 * Created by ac on 18/7/16.
 *
 */

public class SharpUIKit {

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        SharpUIKitImpl.init(context);
    }

    /**
     * 获取上下文
     *
     * @return 必须初始化后才有值
     */
    public static Context getContext() {
        return SharpUIKitImpl.getContext();
    }
}
