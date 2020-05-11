package com.qing.tewang.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * Created by 伍燎 on 2015/8/18.
 * 邮箱：dazhengxie@gmail.com
 */
public abstract class SwipeRefreshAdapterViewBase<T extends AbsListView> extends SwipeRefreshBase<T> implements AbsListView.OnScrollListener {
    protected OnLoadMoreListener mListener;

    private int mLastVisibleItem;
    private AbsListView.OnScrollListener mChildOnScrollListener;
    //是否显示加载更多
    protected boolean mHasLoadMore;
    private float y1;
    protected boolean isFirst = true;
    private boolean isLastY;

    public SwipeRefreshAdapterViewBase(Context context) {
        this(context, null);
    }

    public SwipeRefreshAdapterViewBase(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    protected abstract void addFooterView();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //允许加载更多    滚动到页尾   当前item数量大于0  监听器不为null  向下滚动  数据未加载完毕
        if ((this.mHasLoadMore)
                && (this.mLastVisibleItem == -1 + view.getCount() && mLastVisibleItem > 0)
                && (this.mListener != null) && isLastY && !isLoadAll) {
            showLoadMoreBar(true);
            this.mListener.onLoadMore();
            isLastY = false;
        }
        if (this.mChildOnScrollListener != null) {
            this.mChildOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                isLastY = y1 > ev.getY();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mLastVisibleItem = firstVisibleItem + visibleItemCount - 1;
        if (this.mChildOnScrollListener != null) {
            this.mChildOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    protected abstract void removeFooterView();

    /**
     * 是否有加载更多
     *
     * @param mHasLoadMore
     */
    protected abstract void setHasLoadMore(boolean mHasLoadMore);

    /**
     * 设置EmptyView
     *
     * @param view
     */
    public void setEmptyView(View view) {
        mSwipeEmpty.addView(view, -1, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        isLoadEmptyView = true;
        mSwipeEmpty.setVisibility(VISIBLE);
        mSwipeParent.setVisibility(GONE);
    }


    /**
     * 数据全部加载完毕
     */
    public void loadDataComplete() {
        isLoadAll = true;
        removeFooterView();
    }

    /**
     * 数据加载更多
     */
    public void loadMore() {
        isLoadAll = false;
        addFooterView();
    }

    /**
     * 加载更多的回掉
     *
     * @param paramOnLoadMoreListener
     */
    public void setOnLoadMoreListener(SwipeRefreshBase.OnLoadMoreListener paramOnLoadMoreListener) {
        setHasLoadMore(true);
        this.mListener = paramOnLoadMoreListener;
    }

    public void setOnChildScrollListener(AbsListView.OnScrollListener onScrollListener) {
        this.mChildOnScrollListener = onScrollListener;
    }

    protected abstract void showChildLoadMoreBar(boolean isShow);

    /**
     * 是否显示加载更多
     *
     * @param mHasLoadMore
     */
    public void showLoadMoreBar(boolean mHasLoadMore) {
        if (mHasLoadMore) {
            showChildLoadMoreBar(true);
            return;
        }
        showChildLoadMoreBar(false);
    }
}
