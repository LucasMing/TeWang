package com.qing.tewang.widget.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.util.DisplayUtils;


/**
 * Created by 伍燎 on 2015/8/18.
 * 邮箱：dazhengxie@gmail.com
 */
public class SwipeRefreshListView extends SwipeRefreshAdapterViewBase<ListView> {
    protected View mLoadMoreBar;

    private TextView noMoreTv;
    private Context context;
    private View loadBar;

    private View targetView;

    public SwipeRefreshListView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ScrollTrickListener listener;
    public ScrollYListener scrollYListener;

    public void setListener(ScrollTrickListener listener) {
        this.listener = listener;
    }

    public void setScrollYListener(ScrollYListener scrollYListener) {
        this.scrollYListener = scrollYListener;
    }

    private void init() {
        this.mLoadMoreBar = LayoutInflater.from(getContext()).inflate
                (R.layout.layout_load, null);
        AbsListView.LayoutParams params1 = new AbsListView.LayoutParams(
                DisplayUtils.getInstance().getWidth(context),
                DisplayUtils.getInstance().dp2px(context, 42));
        mLoadMoreBar.setLayoutParams(params1);
        loadBar = mLoadMoreBar.findViewById(R.id.load_bar);

        noMoreTv = mLoadMoreBar.findViewById(R.id.no_more_tv);

    }


    public void setAdapter(final BaseAdapter adapter) {
        setRefreshing(false);
        if (isLoadEmptyView) {
            if (adapter.getCount() == 0) {
                mSwipeEmpty.setVisibility(VISIBLE);
                mSwipeParent.setVisibility(GONE);
            } else {
                mSwipeEmpty.setVisibility(GONE);
                mSwipeParent.setVisibility(VISIBLE);
            }
        }
        getListView().setAdapter(adapter);
    }

    @Override
    protected void addFooterView() {

        if (getListView().getFooterViewsCount() == 0) {
            getListView().addFooterView(mLoadMoreBar, null, false);
            showChildLoadMoreBar(false);
            hideBottomBar();
        }
    }

    public void notifyDataSetChanged(BaseAdapter adapter) {
        if (isLoadEmptyView) {
            if (adapter.getCount() == 0) {
                mSwipeEmpty.setVisibility(VISIBLE);
                mSwipeParent.setRefreshing(false);
                mSwipeParent.setVisibility(GONE);
            } else {
                mSwipeEmpty.setRefreshing(false);
                mSwipeEmpty.setVisibility(GONE);
                mSwipeParent.setVisibility(VISIBLE);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void removeFooterView() {
        if (mLoadMoreBar != null) {
            try {
                getListView().removeFooterView(mLoadMoreBar);
            } catch (Exception e) {
                hideBottomBar();
                if (noMoreTv != null && getListView() != null && getListView().getCount() > 6) {
                    noMoreTv.setVisibility(VISIBLE);
                }
            }
        }
    }

    @Override
    protected void setHasLoadMore(boolean mHasLoadMore) {
        addFooterView();
        super.mHasLoadMore = mHasLoadMore;
    }

    @Override
    protected void showChildLoadMoreBar(boolean isShow) {
        if (isShow) {
            addFooterView();
            showBottomBar();
            return;
        }
        hideBottomBar();
    }

    private void showBottomBar() {
        loadBar.setVisibility(VISIBLE);
        this.mLoadMoreBar.setVisibility(VISIBLE);
        noMoreTv.setVisibility(INVISIBLE);
    }

    private void hideBottomBar() {
        mLoadMoreBar.setVisibility(VISIBLE);
        noMoreTv.setVisibility(INVISIBLE);
    }

    @Override
    protected void hideBottomBarNoText() {
        super.hideBottomBarNoText();
        post(new Runnable() {
            @Override
            public void run() {
                loadBar.setVisibility(GONE);
            }
        });

    }

    @Override
    protected View createRefreshableView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_base_listview, null);
        ListView listView = view.findViewById(R.id.list_view);
        listView.setOnScrollListener(this);
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        super.onScrollStateChanged(view, scrollState);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (targetView != null) {
            int top = getTop(targetView);
            if (top <= 0) {
                targetView.draw(canvas);
                targetView.setTranslationY(-1 * top);
            } else {
                targetView.setTranslationY(0);
            }
        }
    }


    private boolean redirectTouchToStickyView;


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            redirectTouchToStickyView = true && targetView != null;
        }
        if (redirectTouchToStickyView) {
            if (getTop(targetView) <= 0) {
                redirectTouchToStickyView = ev.getY() <= (targetView
                        .getHeight())
                        && ev.getX() >= targetView.getLeft()
                        && ev.getX() <= targetView.getRight();

                if (redirectTouchToStickyView) {
                    targetView.dispatchTouchEvent(ev);
                    return true;
                }
            } else {
                redirectTouchToStickyView = false;
            }

        }
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (redirectTouchToStickyView) {
            targetView.onTouchEvent(event);
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
//            if (listView != null && listView.getHeaderViewsCount() != 0
//                    && listView.getChildAt(0).findViewWithTag(tag) != null) {
//                getTagView((ViewGroup) listView.getChildAt(0));
//            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if (scrollYListener != null && listView != null && listView.getHeaderViewsCount() > 0) {
            if (firstVisibleItem < listView.getHeaderViewsCount()) {//header显示
                if (listView.getChildAt(0) != null)
                    scrollYListener.doSomething(listView.getChildAt(0).getTop());
            } else {
                scrollYListener.doSomething(Integer.MAX_VALUE);
            }
        }
        if (targetView != null) {
            int top = getTop(targetView);
            if (listView != null &&
                    firstVisibleItem < listView.getHeaderViewsCount()) {//header显示
                invalidate();
            }
            if (listener != null) {
                listener.doSomething(targetView, top, originalTop);
            }
        }
    }

    /**
     * 判断view距离this顶部高度
     *
     * @param view
     * @return
     */
    public int getTop(View view) {
        if (view == null) {
            return 0;
        }
        if (view.getParent() instanceof AdapterView) {
            return view.getTop();
        } else {
            return view.getTop() + getTop((View) view.getParent());
        }
    }

    public int getBottom(View view) {
        if (view == null) {
            return 0;
        }
        if (view.getParent() instanceof AdapterView) {
            return view.getTop();
        } else {
            return view.getTop() + getTop((View) view.getParent());
        }
    }


    /**
     * 目标View距离定点距离的监听器
     */
    public interface ScrollTrickListener {
        /**
         * @param targetView  黏贴到顶部的View
         * @param scrollY     targetView距离定点的距离
         * @param originalTop 为滚动之前的距离
         */
        void doSomething(View targetView, int scrollY, int originalTop);
    }

    public interface ScrollYListener {
        void doSomething(float scrollY);
    }


    private final String tag = "top";
    private int originalTop = 0;

    private void getTagView(ViewGroup viewGroup) {
        if (targetView != null || viewGroup == null) {
            return;
        }
        View view = viewGroup.findViewWithTag(tag);
        if (view == null) {
            int count = viewGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View temp = viewGroup.findViewWithTag(tag);
                if (temp != null) {
                    targetView = temp;
                    originalTop = getTop(targetView);
                    return;
                } else {
                    if (temp instanceof ViewGroup) {
                        getTagView((ViewGroup) temp);
                    } else {
                        return;
                    }
                }
            }
        } else {
            targetView = view;
            originalTop = getTop(targetView);
        }
    }

    private ListView listView;

    public ListView getListView() {
        if (listView == null) {
            listView = getRefreshableView().findViewById(R.id.list_view);
            listView.setId(R.id.list_view);
        }
        return listView;
    }


    public void setFooter(View view) {
        showChildLoadMoreBar(false);
        removeFooterView();
        getListView().addFooterView(view, null, false);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {


    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        getListView().setOnItemClickListener(listener);
    }


}
