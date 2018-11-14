package com.conquer.sharp.danmaku;

import android.os.Bundle;

import com.conquer.sharp.R;
import com.conquer.sharp.danmaku.bean.DanMu;
import com.conquer.sharp.recycler.MultiItemTypeSupport;

/**
 * Created by ac on 18/7/19.
 *
 */

public class DanMuMultiItemTypeSupport implements MultiItemTypeSupport<DanMu> {
    public static final int TYPE_NORMAL = 1;
    private static final int TYPE_HOLDER = 2;

    @Override
    public int getLayoutId(int itemType) {
        if (itemType == TYPE_HOLDER) {
            return R.layout.layout_dan_mu_holder;
        }
        return R.layout.layout_dan_mu;
    }

    @Override
    public int getItemViewType(int position, DanMu danMu, Bundle bundle) {
        if (danMu.type == DanMu.DanMuType.DAN_MU_PLACE_HOLDER) {
            return TYPE_HOLDER;
        } else {
            return TYPE_NORMAL;
        }
    }
}
