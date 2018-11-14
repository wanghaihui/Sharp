package com.conquer.sharp.recycler.extend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.conquer.sharp.recycler.RecyclerViewHolder;

import java.util.ArrayList;

/**
 * 支持Header和Footer
 */
public class HFRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final int TYPE_HEADER_VIEW = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER_VIEW = Integer.MIN_VALUE + 1;

    /**
     * RecyclerView真正使用的Adapter
     */
    private RecyclerView.Adapter<RecyclerViewHolder> mInnerAdapter;

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            notifyItemRangeChanged(positionStart + getHeaderViewsCount(), itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeaderViewsCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition + getHeaderViewsCount(), toPosition + getHeaderViewsCount() + itemCount);
        }
    };

    // 构造函数
    public HFRecyclerAdapter(RecyclerView.Adapter<RecyclerViewHolder> innerAdapter) {
        setAdapter(innerAdapter);
    }

    private void setAdapter(RecyclerView.Adapter<RecyclerViewHolder> adapter) {
        if (mInnerAdapter != null) {
            // 数据移除
            notifyItemRangeRemoved(getHeaderViewsCount(), mInnerAdapter.getItemCount());
            // 取消注册
            mInnerAdapter.unregisterAdapterDataObserver(mDataObserver);
        }

        mInnerAdapter = adapter;
        mInnerAdapter.registerAdapterDataObserver(mDataObserver);
        notifyItemRangeInserted(getHeaderViewsCount(), mInnerAdapter.getItemCount());
    }

    public RecyclerView.Adapter<RecyclerViewHolder> getInnerAdapter() {
        return mInnerAdapter;
    }

    public boolean containsHeaderView(View header) {
        return mHeaderViews.contains(header);
    }

    /**
     * 添加头部
     * @param header 头部View
     */
    public void addHeaderView(View header) {
        if (header == null) {
            throw new RuntimeException("Header is null");
        }

        mHeaderViews.add(header);
        notifyDataSetChanged();
    }

    /**
     * 添加底部
     * @param footer 底部View
     */
    public void addFooterView(View footer) {
        if (footer == null) {
            throw new RuntimeException("Footer is null");
        }

        mFooterViews.add(footer);
        notifyDataSetChanged();
    }

    /**
     * 第一个HeaderView
     */
    public View getHeaderView() {
        return getHeaderViewsCount() > 0 ? mHeaderViews.get(0) : null;
    }

    /**
     * 第一个FooterView
     */
    public View getFooterView() {
        return getFooterViewsCount() > 0 ? mFooterViews.get(0) : null;
    }

    /**
     * 移除HeaderView
     */
    public void removeHeaderView(View view) {
        mHeaderViews.remove(view);
        notifyDataSetChanged();
    }

    public void removeAllHeadViews() {
        if (mHeaderViews.size() > 0) {
            for (int i = 0; i < mHeaderViews.size(); i++) {
                mHeaderViews.remove(i);
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 移除FooterView
     */
    public void removeFooterView(View view) {
        mFooterViews.remove(view);
        notifyDataSetChanged();
    }

    private int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public boolean isHeader(int position) {
        return getHeaderViewsCount() > 0 && position == 0;
    }

    public boolean isFooter(int position) {
        int lastPosition = getItemCount() - 1;
        return getFooterViewsCount() > 0 && position == lastPosition;
    }

    @Override
    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType < TYPE_HEADER_VIEW + getHeaderViewsCount()) {
            return new RecyclerViewHolder(mHeaderViews.get(viewType - TYPE_HEADER_VIEW), viewType);
        } else if (viewType >= TYPE_FOOTER_VIEW && viewType < Integer.MAX_VALUE / 2 - 1) {
            return new RecyclerViewHolder(mFooterViews.get(viewType - TYPE_FOOTER_VIEW), viewType);
        } else {
            return mInnerAdapter.onCreateViewHolder(parent, viewType - Integer.MAX_VALUE / 2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        if (position >= getHeaderViewsCount() && position < getHeaderViewsCount() + mInnerAdapter.getItemCount()) {
            mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewsCount());
        } else {
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) params).setFullSpan(true);
            } else if (params == null && mLayoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams lp = new StaggeredGridLayoutManager.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                lp.setFullSpan(true);
                holder.itemView.setLayoutParams(lp);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderViewsCount() + getFooterViewsCount() + mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        int innerCount = mInnerAdapter.getItemCount();
        if (position < getHeaderViewsCount()) {
            return TYPE_HEADER_VIEW + position;
        } else if (position >= getHeaderViewsCount() && position < (getHeaderViewsCount() + innerCount)) {
            int innerItemViewType = mInnerAdapter.getItemViewType(position - getHeaderViewsCount());
            if (innerItemViewType >= Integer.MAX_VALUE / 2) {
                throw new IllegalArgumentException("Your adapter's getViewTypeCount must < Integer.MAX_VALUE / 2");
            }
            // 最小从-1开始
            return innerItemViewType + Integer.MAX_VALUE / 2;
        } else {
            return TYPE_FOOTER_VIEW + position - getHeaderViewsCount() - innerCount;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mInnerAdapter.onAttachedToRecyclerView(mRecyclerView);

        if (mLayoutManager == null) {
            mLayoutManager = mRecyclerView.getLayoutManager();
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (mLayoutManager == null && mRecyclerView != null) {
            mLayoutManager = mRecyclerView.getLayoutManager();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }
}
