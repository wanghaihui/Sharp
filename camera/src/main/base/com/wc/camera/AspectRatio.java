package com.wc.camera;

import android.os.Parcelable;
import android.support.v4.util.SparseArrayCompat;

/**
 * Immutable(不可改变的) class for describing proportional(成比例的) relationship between width and height.
 */
public abstract class AspectRatio implements Comparable<AspectRatio>, Parcelable {
    private final static SparseArrayCompat<SparseArrayCompat<AspectRatio>> sCache = new SparseArrayCompat<>(16);

}
