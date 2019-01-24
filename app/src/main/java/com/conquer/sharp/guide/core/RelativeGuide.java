package com.conquer.sharp.guide.core;

import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RelativeGuide {

    @LayoutRes
    public int layout;

    public int gravity;
    public int padding;

    public HighLight highLight;

    @IntDef({android.view.Gravity.START,
            android.view.Gravity.TOP,
            android.view.Gravity.END,
            android.view.Gravity.BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    @interface LimitGravity {

    }

    public static class MarginInfo {
        public int leftMargin;
        public int topMargin;
        public int rightMargin;
        public int bottomMargin;
        public int gravity;

        @Override
        public String toString() {
            return "MarginInfo{" +
                    "leftMargin=" + leftMargin +
                    ", topMargin=" + topMargin +
                    ", rightMargin=" + rightMargin +
                    ", bottomMargin=" + bottomMargin +
                    ", gravity=" + gravity +
                    '}';
        }
    }

    public final View getGuideLayout(ViewGroup viewGroup, Controller controller) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        onLayoutInflated(view, controller);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        MarginInfo marginInfo = getMarginInfo(gravity, viewGroup, view);
        offsetMargin(marginInfo, viewGroup, view);
        layoutParams.gravity = marginInfo.gravity;
        layoutParams.leftMargin += marginInfo.leftMargin;
        layoutParams.topMargin += marginInfo.topMargin;
        layoutParams.rightMargin += marginInfo.rightMargin;
        layoutParams.bottomMargin += marginInfo.bottomMargin;
        view.setLayoutParams(layoutParams);
        return view;
    }

    private MarginInfo getMarginInfo(@LimitGravity int gravity, ViewGroup viewGroup, View view) {
        MarginInfo marginInfo = new MarginInfo();
        RectF rectF = highLight.getRectF(viewGroup);
        switch (gravity) {
            case Gravity.START:
                marginInfo.gravity = Gravity.END;
                marginInfo.rightMargin = (int) (viewGroup.getWidth() - rectF.left + padding);
                marginInfo.topMargin = (int) rectF.top;
                break;
            case Gravity.TOP:
                marginInfo.gravity = Gravity.BOTTOM;
                marginInfo.bottomMargin = (int) (viewGroup.getHeight() - rectF.top + padding);
                marginInfo.leftMargin = (int) rectF.left;
                break;
            case Gravity.END:
                marginInfo.leftMargin = (int) (rectF.right + padding);
                marginInfo.topMargin = (int) rectF.top;
                break;
            case Gravity.BOTTOM:
                marginInfo.topMargin = (int) (rectF.bottom + padding);
                marginInfo.leftMargin = (int) rectF.left;
                break;
        }
        return marginInfo;
    }

    protected void offsetMargin(MarginInfo marginInfo, ViewGroup viewGroup, View view) {
        // do nothing
    }

    protected void onLayoutInflated(View view, Controller controller) {
        // do nothing
    }

}
