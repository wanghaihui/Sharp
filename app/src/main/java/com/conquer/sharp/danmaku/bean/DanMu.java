package com.conquer.sharp.danmaku.bean;

/**
 * Created by ac on 18/7/19.
 *
 */

public class DanMu {
    public enum DanMuType {
        DAN_MU, DAN_MU_PLACE_HOLDER
    }

    public DanMuType type = DanMuType.DAN_MU;
    public String danMu;

    public DanMu(String danMu) {
        this.danMu = danMu;
    }
}
