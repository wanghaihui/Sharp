package com.conquer.sharp.recycler.extend;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.conquer.sharp.ptr.custom.LoadMoreLayout;

public class RecyclerStateUtils {

    public static LoadMoreLayout.State getFooterViewState(RecyclerView recyclerView) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HFRecyclerAdapter) {
            if (((HFRecyclerAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadMoreLayout footerView = (LoadMoreLayout) ((HFRecyclerAdapter) outerAdapter).getFooterView();
                return footerView.getState();
            }
        }
        return LoadMoreLayout.State.Normal;
    }

    public static void setFooterViewState(RecyclerView recyclerView, LoadMoreLayout.State state) {
        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter != null && outerAdapter instanceof HFRecyclerAdapter) {
            if (((HFRecyclerAdapter) outerAdapter).getFooterViewsCount() > 0) {
                LoadMoreLayout footerView = (LoadMoreLayout) ((HFRecyclerAdapter) outerAdapter).getFooterView();
                footerView.setState(state);
            }
        }
    }

    public static void setFooterViewState(Activity activity, RecyclerView recyclerView, LoadMoreLayout.State state) {
        if(activity == null || activity.isFinishing()) {
            return;
        }

        RecyclerView.Adapter outerAdapter = recyclerView.getAdapter();
        if (outerAdapter == null || !(outerAdapter instanceof HFRecyclerAdapter)) {
            return;
        }

        HFRecyclerAdapter headerAndFooterAdapter = (HFRecyclerAdapter) outerAdapter;

        LoadMoreLayout footerView;

        if (headerAndFooterAdapter.getFooterViewsCount() > 0) {
            if (headerAndFooterAdapter.getFooterView() instanceof  LoadMoreLayout) {
                footerView = (LoadMoreLayout) headerAndFooterAdapter.getFooterView();
                footerView.setState(state);

                recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1);
            }
        } else {
            footerView = new LoadMoreLayout(activity);
            footerView.setState(state);

            headerAndFooterAdapter.addFooterView(footerView);
            recyclerView.scrollToPosition(headerAndFooterAdapter.getItemCount() - 1);
        }
    }
}
