package com.conquer.sharp.guide.listener;

import com.conquer.sharp.guide.core.Controller;

public interface OnGuideChangedListener {
    /**
     * 当引导层显示时回调
     *
     * @param controller
     */
    void onShow(Controller controller);

    /**
     * 当引导层消失时回调
     *
     * @param controller
     */
    void onRemoved(Controller controller);
}
