package com.qing.tewang.widget.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.qing.tewang.R;


/**
 * Created by 伍燎 on 2015/8/18.
 * 邮箱：dazhengxie@gmail.com
 * 下拉刷新基类
 */
public abstract class SwipeRefreshBase<T extends View> extends FrameLayout
        implements GestureDetector.OnGestureListener {

    protected NewSwipeRefreshLayout mSwipeParent;
    protected NewSwipeRefreshLayout mSwipeEmpty;
    protected OnRefreshListener mOnRefreshListener;
    private View mRefreshableView;
    protected boolean isLoadAll;
    private GestureDetector gestureDetector;

    public SwipeRefreshBase(Context context) {
        this(context, null);
    }

    public SwipeRefreshBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRefreshBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        gestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 初始化SwipeRefreshLayout
     *
     * @param context
     */
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_base_refresh, null);
        this.mSwipeParent = view.findViewById(R.id.swipe_list_view);
        this.mSwipeEmpty = view.findViewById(R.id.swipe_empty_view);
        addView(view, -1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        //empty相同的下拉事件
        mSwipeParent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                if (mOnRefreshListener != null)
                    mOnRefreshListener.onRefresh();
            }
        });
        mSwipeEmpty.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                if (mOnRefreshListener != null && mSwipeEmpty.getVisibility() != View.GONE) {
                    mOnRefreshListener.onRefresh();
                }
            }
        });
        //添加刷新View
        mRefreshableView = createRefreshableView();
        if (mRefreshableView != null) {
            addRefreshableView(mRefreshableView);
        }
    }


    public NewSwipeRefreshLayout getSwipeParent() {
        return mSwipeParent;
    }

    /**
     * 添加RefreshableView
     *
     * @param mRefreshableView
     */
    private void addRefreshableView(View mRefreshableView) {
        mSwipeParent.addView(mRefreshableView, -1, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 创建RefreshableView
     *
     * @return
     */
    protected abstract View createRefreshableView();

    //是否显示空View
    protected boolean isLoadEmptyView = false;

    /**
     * 设置刷新状态,刷新结束后设置为false
     *
     * @param isRefresh
     */
    public void setRefreshing(final boolean isRefresh) {
        if (!isRefresh && !isLoadAll) hideBottomBarNoText();
        if (mSwipeEmpty.getVisibility() == View.VISIBLE) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeEmpty.setRefreshing(isRefresh);
                }
            }, 400);

        }
        if (this.mSwipeParent.getVisibility() == View.VISIBLE) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeParent.setRefreshing(isRefresh);
                }
            }, 400);

        }
    }

    protected void hideBottomBarNoText() {

    }

    /**
     * 获取RefreshableView
     *
     * @return
     */
    public View getRefreshableView() {
        return this.mRefreshableView;
    }

    /**
     * 设置刷新回调
     *
     * @param mOnRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }


    /**
     * 加载更多会带回调
     */
    public static abstract interface OnLoadMoreListener {
        public abstract void onLoadMore();
    }

    /**
     * 刷新回调
     */
    public static abstract interface OnRefreshListener {
        public abstract void onRefresh();
    }

    public interface OnUpDownListener {
        void onDown();

        void onUp();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())) {
            return false;
        }
        if (e2 == null) {
            return false;
        } else if (e1 == null) {
            return false;
        }
        if (e2.getY() - e1.getY() > 20) {
            if (this.upDownListener != null) upDownListener.onUp();
        } else if (e1.getY() - e2.getY() > 20) {
            if (this.upDownListener != null) upDownListener.onDown();
        }
        return false;
    }


    public void setOnUpDownScroller(OnUpDownListener upDownListener) {
        this.upDownListener = upDownListener;
    }

    protected OnUpDownListener upDownListener;
}
