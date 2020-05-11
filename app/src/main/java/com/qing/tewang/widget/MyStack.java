package com.qing.tewang.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;


import link.fls.swipestack.SwipeStack;

public class MyStack extends SwipeStack {
    public MyStack(Context context) {
        super(context);
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public MyStack(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public MyStack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private int mScaledTouchSlop;
    private float mPrevX, mPrevY;
    private long startTime;

    private boolean moveSelf = false;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = ev.getX();
                mPrevY = ev.getY();
                startTime = System.currentTimeMillis();
                super.onTouchEvent(ev);
                moveSelf = true;
                break;
            case MotionEvent.ACTION_MOVE:
                long endTime = System.currentTimeMillis();
                float endX = ev.getX();
                float endY = ev.getY();
                if (Math.abs(endX - this.mPrevX) > mScaledTouchSlop ||
                        Math.abs(endY - this.mPrevY) > mScaledTouchSlop ||
                        (endTime - startTime) > 200) {
                    moveSelf = false;
                } else {
                    moveSelf = true;
                }
                super.dispatchTouchEvent(ev);
                return true;

            case MotionEvent.ACTION_UP:
                if (moveSelf) {
                    moveSelf = false;
                    super.dispatchTouchEvent(ev);
                    return onTouchEvent(ev);
                } else {
                    moveSelf = false;
                    return super.dispatchTouchEvent(ev);
                }

        }
        return super.dispatchTouchEvent(ev);
    }


}