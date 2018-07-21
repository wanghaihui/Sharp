package com.conquer.sharp.recycler.easy;

/**
 * Created by ac on 18/6/25.
 *
 */

public interface MultiItemTypeSupport<T> {
    int getLayoutId(int itemType);
    int getItemViewType(int position, T t);
}
