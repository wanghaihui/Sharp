package com.conquer.sharp.guide.core;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;

import com.conquer.sharp.guide.listener.OnGuideChangedListener;
import com.conquer.sharp.guide.listener.OnPageChangedListener;

import java.util.ArrayList;
import java.util.List;

public class Guide {
    public static final String TAG = "Guide";

    public static final int SUCCESS = 1;
    public static final int FAILED = -1;

    public static Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static Builder with(Fragment fragment) {
        return new Builder(fragment);
    }

    public static Builder with(android.support.v4.app.Fragment v4Fragment) {
        return new Builder(v4Fragment);
    }

    public static void resetLabel(Context context, String label) {
        SharedPreferences sp = context.getSharedPreferences(Guide.TAG, Activity.MODE_PRIVATE);
        sp.edit().putInt(label, 0).apply();
    }

    public static class Builder {
        Activity activity;
        Fragment fragment;
        android.support.v4.app.Fragment v4Fragment;
        String label;
        int showCounts = 1; // 显示次数 default once
        boolean alwaysShow; // 总是显示 default false
        View anchor; // 锚点view
        OnGuideChangedListener onGuideChangedListener;
        OnPageChangedListener onPageChangedListener;
        List<GuidePage> guidePages = new ArrayList<>();

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder(Fragment fragment) {
            this.fragment = fragment;
            this.activity = fragment.getActivity();
        }

        public Builder(android.support.v4.app.Fragment v4Fragment) {
            this.v4Fragment = v4Fragment;
            this.activity = v4Fragment.getActivity();
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setShowCounts(int count) {
            this.showCounts = count;
            return this;
        }

        public Builder alwaysShow(boolean show) {
            this.alwaysShow = show;
            return this;
        }

        public Builder anchor(View anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder setOnGuideChangedListener(OnGuideChangedListener listener) {
            onGuideChangedListener = listener;
            return this;
        }

        public Builder setOnPageChangedListener(OnPageChangedListener listener) {
            onPageChangedListener = listener;
            return this;
        }

        public Builder addGuidePage(GuidePage page) {
            guidePages.add(page);
            return this;
        }

        public Controller show() {
            check();
            Controller controller = new Controller(this);
            controller.show();
            return controller;
        }

        private void check() {
            if (TextUtils.isEmpty(label)) {
                throw new IllegalArgumentException("The param label is missing, please call setLabel()");
            }
            if (activity == null && (fragment != null || v4Fragment != null)) {
                throw new IllegalStateException("activity is null, please make sure that fragment is showing when call Guide");
            }
        }
    }
}
