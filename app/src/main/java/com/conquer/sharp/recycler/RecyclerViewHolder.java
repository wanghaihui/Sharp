package com.conquer.sharp.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.conquer.sharp.base.glide.GlideApp;

/**
 * Created by ac on 18/6/22.
 *
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private int mViewType;

    /**
     * Package private field to retain the associated user object and detect a change
     */
    Object associatedObject;

    public RecyclerViewHolder(View itemView, int viewType) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
        mViewType = viewType;
    }

    public static RecyclerViewHolder get(Context context, ViewGroup parent, int layoutId, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RecyclerViewHolder(itemView, viewType);
    }

    public int getViewType() {
        return mViewType;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public Context getContext() {
        if (mConvertView == null) {
            return null;
        }
        return mConvertView.getContext();
    }

    public View getConvertView() {
        return mConvertView;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public RecyclerViewHolder setMaxWidth(int viewId, int width) {
        TextView view = getView(viewId);
        view.setMaxWidth(width);
        return this;
    }

    public RecyclerViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public RecyclerViewHolder setText(int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public RecyclerViewHolder setLeftCompoundDrawables(int viewId, Drawable drawable) {
        TextView view = getView(viewId);
        view.setCompoundDrawables(drawable, null, null, null);
        return this;
    }

    public RecyclerViewHolder setImageResource(int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public RecyclerViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public RecyclerViewHolder setBackgroundRes(int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public RecyclerViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public RecyclerViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public RecyclerViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public RecyclerViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alphaAnimation = new AlphaAnimation(value, value);
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            getView(viewId).startAnimation(alphaAnimation);
        }
        return this;
    }

    public RecyclerViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public RecyclerViewHolder setVisible2(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    /**
     * 链接
     */
    public RecyclerViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public RecyclerViewHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    public RecyclerViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public RecyclerViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public RecyclerViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public RecyclerViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public RecyclerViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public RecyclerViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public RecyclerViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public RecyclerViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public RecyclerViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public RecyclerViewHolder setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemClickListener(listener);
        return this;
    }

    public RecyclerViewHolder setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemLongClickListener(listener);
        return this;
    }

    public RecyclerViewHolder setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    public RecyclerViewHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    public RecyclerViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public RecyclerViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public RecyclerViewHolder setEnabled(int viewId, boolean enable) {
        View view = getView(viewId);
        view.setEnabled(enable);
        return this;
    }

    public RecyclerViewHolder setChecked(int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setChecked(checked);
        } else if (view instanceof CheckedTextView) {
            ((CheckedTextView) view).setChecked(checked);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public RecyclerViewHolder setAdapter(int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    public void setAssociatedObject(Object associatedObject) {
        this.associatedObject = associatedObject;
    }

    public Object getAssociatedObject() {
        return associatedObject;
    }

    // Glide设置图片
    public void setCircleImage(int viewId, String url, int placeHolder, int width, int height) {
        ImageView view = getView(viewId);
        GlideApp.with(getContext())
                .load(url)
                .centerCrop()
                .circleCrop()
                .placeholder(placeHolder)
                .error(placeHolder)
                .override(width, height)
                .into(view);
    }

    public void setNoHolderImage(int viewId, String url) {
        ImageView view = getView(viewId);
        GlideApp.with(getContext())
                .load(url)
                .centerCrop()
                .into(view);
    }

    public void setNoHolderImage(int viewId, String url, int width, int height) {
        ImageView view = getView(viewId);
        GlideApp.with(getContext())
                .load(url)
                .centerCrop()
                .override(width, height)
                .into(view);
    }

    public void setHolderImage(int viewId, String url, int placeHolder, int width, int height) {
        ImageView view = getView(viewId);
        GlideApp.with(getContext())
                .load(url)
                .centerCrop()
                .placeholder(placeHolder)
                .error(placeHolder)
                .override(width, height)
                .into(view);
    }

    public void setHolderErrorListenerImage(int viewId, String url, String errorUrl, int placeHolder, int width, int height) {
        ImageView view = getView(viewId);
        GlideApp.with(getContext())
                .load(url)
                .placeholder(placeHolder)
                .error(GlideApp.with(getContext()).load(errorUrl))
                .override(width, height)
                .into(view);
    }
}
