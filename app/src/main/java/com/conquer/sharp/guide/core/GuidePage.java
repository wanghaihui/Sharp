package com.conquer.sharp.guide.core;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;

import com.conquer.sharp.guide.listener.OnLayoutInflatedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuidePage {

    private List<HighLight> highLights = new ArrayList<>();
    private boolean everywhereCancelable = true;
    private int backgroundColor;

    private int layoutResId;
    private int[] clickToDismissIds;
    private OnLayoutInflatedListener onLayoutInflatedListener;
    private Animation enterAnimation, exitAnimation;

    public static GuidePage newInstance() {
        return new GuidePage();
    }

    public GuidePage addHighLight(View view) {
        return addHighLight(view, HighLight.Shape.RECTANGLE, 0, 0, null);
    }

    public GuidePage addHighLight(View view, RelativeGuide relativeGuide) {
        return addHighLight(view, HighLight.Shape.RECTANGLE, 0, 0, relativeGuide);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape) {
        return addHighLight(view, shape, 0, 0, null);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape, RelativeGuide relativeGuide) {
        return addHighLight(view, shape, 0, 0, relativeGuide);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape, int padding) {
        return addHighLight(view, shape, 0, padding, null);
    }

    public GuidePage addHighLight(View view, HighLight.Shape shape, int padding, RelativeGuide relativeGuide) {
        return addHighLight(view, shape, 0, padding, relativeGuide);
    }

    /**
     * 添加需要高亮的View
     *
     * @param view          需要高亮的view
     * @param shape         高亮形状{@link HighLight.Shape}
     * @param round         圆角尺寸，单位dp，仅{@link HighLight.Shape#ROUND_RECTANGLE}有效
     * @param padding       高亮相对view的padding, 单位px
     * @param relativeGuide 相对于高亮的引导布局
     */
    public GuidePage addHighLight(View view, HighLight.Shape shape, int round, int padding, @Nullable RelativeGuide relativeGuide) {
        HighlightView highlight = new HighlightView(view, shape, round, padding);
        if (relativeGuide != null) {
            relativeGuide.highLight = highlight;
            highlight.setOptions(new HighlightOptions.Builder().setRelativeGuide(relativeGuide).build());
        }
        highLights.add(highlight);
        return this;
    }

    public GuidePage setLayoutRes(@LayoutRes int resId, int... id) {
        this.layoutResId = resId;
        clickToDismissIds = id;
        return this;
    }

    public List<HighLight> getHighLights() {
        return highLights;
    }

    public boolean isEverywhereCancelable() {
        return everywhereCancelable;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public Animation getEnterAnimation() {
        return enterAnimation;
    }

    public Animation getExitAnimation() {
        return exitAnimation;
    }

    public int getLayoutResId() {
        return layoutResId;
    }

    public int[] getClickToDismissIds() {
        return clickToDismissIds;
    }

    public OnLayoutInflatedListener getOnLayoutInflatedListener() {
        return onLayoutInflatedListener;
    }

    public List<RelativeGuide> getRelativeGuides() {
        List<RelativeGuide> relativeGuides = new ArrayList<>();
        for (HighLight highLight : highLights) {
            HighlightOptions options = highLight.getOptions();
            if (options != null) {
                if (options.relativeGuide != null) {
                    relativeGuides.add(options.relativeGuide);
                }
            }
        }
        return relativeGuides;
    }
}
