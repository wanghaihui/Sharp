package com.conquer.sharp.widget.compat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class TintTypedArray {

    private final Context mContext;
    private final TypedArray mWrapped;

    private TintManager mTintManager;

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs) {
        TypedArray array = context.obtainStyledAttributes(set, attrs);
        return new TintTypedArray(context, array);
    }

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs,
                                            int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
        return new TintTypedArray(context, array);
    }

    private TintTypedArray(Context context, TypedArray array) {
        mContext = context;
        mWrapped = array;
    }

    public Drawable getDrawable(int index) {
        if (mWrapped.hasValue(index)) {
            final int resourceId = mWrapped.getResourceId(index, 0);
            if (resourceId != 0) {
                return getTintManager().getDrawable(resourceId);
            }
        }
        return mWrapped.getDrawable(index);
    }

    public void recycle() {
        mWrapped.recycle();
    }

    public TintManager getTintManager() {
        if (mTintManager == null) {
            mTintManager = new TintManager(mContext);
        }
        return mTintManager;
    }
}
