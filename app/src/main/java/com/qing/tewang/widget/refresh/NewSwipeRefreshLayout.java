package com.qing.tewang.widget.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by 伍燎 on 2015/8/21.
 * 邮箱：dazhengxie@gmail.com
 * 与viewpager手势冲突解决
 */
public class NewSwipeRefreshLayout extends SwipeRefreshLayout {
    private int mScaledTouchSlop;
    private float mPrevX,mPrevY;

    public NewSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public NewSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = ev.getX();
                mPrevY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getX() - this.mPrevX) > Math.abs(ev.getY() - this.mPrevY)) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

}
