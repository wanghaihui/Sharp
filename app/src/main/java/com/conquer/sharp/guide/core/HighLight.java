package com.conquer.sharp.guide.core;

import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.view.View;

public interface HighLight {

    Shape getShape();

    RectF getRectF(View view);

    float getRadius();

    /**
     * 获取圆角，仅当shape = Shape.ROUND_RECTANGLE才调用次方法
     */
    int getRound();

    /**
     * 额外参数
     */
    @Nullable
    HighlightOptions getOptions();

    enum Shape {
        CIRCLE, // 圆形
        RECTANGLE, // 矩形
        OVAL, // 椭圆
        ROUND_RECTANGLE // 圆角矩形
    }
}
