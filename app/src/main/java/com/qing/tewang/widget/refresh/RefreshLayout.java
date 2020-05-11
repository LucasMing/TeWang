package com.qing.tewang.widget.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.qing.tewang.util.DisplayUtils;


/**
 * Created by Administrator on 2015/11/5 0005.
 */
public class RefreshLayout extends FrameLayout {
    private FrameLayout mHeadLayout;
    private DecelerateInterpolator decelerateInterpolator;
    private View mChildView;
    private boolean isRefreshing;
    private float mTouchY;
    private float mCurrentY;
    private float mHeadHeight;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        if (getChildCount() > 1) {
            throw new RuntimeException("can only have one child widget");
        }
        decelerateInterpolator = new DecelerateInterpolator(10);
    }

    private LoadingView loadingView;
    private static int LOADVIEW_HEIGHT = 45;


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Context context = getContext();

        FrameLayout headViewLayout = new FrameLayout(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.gravity = Gravity.TOP;
        headViewLayout.setLayoutParams(layoutParams);

        mHeadLayout = headViewLayout;

        this.addView(mHeadLayout);

        mChildView = getChildAt(0);

        if (mChildView == null) {
            return;
        }
        loadingView = new LoadingView(getContext());

        post(new Runnable() {
            @Override
            public void run() {
                mHeadLayout.addView(loadingView);
            }
        });
        setWaveHeight(DisplayUtils.getInstance().dp2px(getContext(), 200));
        setHeaderHeight(DisplayUtils.getInstance().dp2px(getContext(), LOADVIEW_HEIGHT));
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isRefreshing) return true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mCurrentY = mTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float dy = currentY - mTouchY;
                if (dy > 0 && !canChildScrollUp()&&!isRefreshing) {
//                    if (materialHeadView != null) {
//                        materialHeadView.onBegin(this);
//                    }
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    public void setWaveHeight(float waveHeight) {
        this.mWaveHeight = waveHeight;
    }

    public void setHeaderHeight(float headHeight) {
        this.mHeadHeight = headHeight;
    }


    protected float mWaveHeight;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isRefreshing) {

        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurrentY = e.getY();
                float dy = mCurrentY - mTouchY;
                dy = Math.min(mWaveHeight * 2, dy);
                dy = Math.max(0, dy);

                if (isRefreshing) {
                    ViewCompat.setTranslationY(mChildView, dy);
                    ViewCompat.setTranslationY(mHeadLayout, dy);
                }else{
                    if (mChildView != null) {
                        float offsetY = decelerateInterpolator.getInterpolation(dy / mWaveHeight / 2) * dy / 2;
                        float fraction = offsetY / mHeadHeight;
                        mHeadLayout.getLayoutParams().height = (int) offsetY;
                        mHeadLayout.requestLayout();

//                    if (materialHeadView != null) {
//                        materialHeadView.onPull(this, fraction);
//                    }
                        ViewCompat.setTranslationY(mChildView, offsetY);

                    }
                }



                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                if (mChildView != null) {
                    if (ViewCompat.getTranslationY(mChildView) >= mHeadHeight) {
                        loadingView.playAnimator();
                        createAnimatorTranslationY(mChildView, mHeadHeight, mHeadLayout);
                        updateListener();
                        isRefreshing = true;
                    } else {
                        createAnimatorTranslationY(mChildView, 0, mHeadLayout);
                    }
                }
                return true;
        }
        return super.onTouchEvent(e);
    }


    private void updateListener() {
        isRefreshing = true;
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh();
        }
    }

    public void createAnimatorTranslationY(final View v, final float h, final FrameLayout fl) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(v);
        viewPropertyAnimatorCompat.setDuration(200);
        viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator());
        viewPropertyAnimatorCompat.translationY(h);
        viewPropertyAnimatorCompat.start();
        viewPropertyAnimatorCompat.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(View view) {
                float height = ViewCompat.getTranslationY(v);
                fl.getLayoutParams().height = (int) height;
                fl.requestLayout();
            }
        });
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (mChildView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            if (mChildView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mChildView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mChildView, -1) || mChildView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mChildView, -1);
        }
    }

    public boolean canChildScrollDown() {
        if (mChildView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            if (mChildView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mChildView;
                if (absListView.getChildCount() > 0) {
                    int lastChildBottom = absListView.getChildAt(absListView.getChildCount() - 1).getBottom();
                    return absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1 && lastChildBottom <= absListView.getMeasuredHeight();
                } else {
                    return false;
                }

            } else {
                return ViewCompat.canScrollVertically(mChildView, 1) || mChildView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mChildView, 1);
        }
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public void setRefreshing(boolean refreshing) {
        if (!refreshing) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingView.complete(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            isRefreshing = false;
                            createAnimatorTranslationY(mChildView, 0, mHeadLayout);
                        }
                    });
                }
            }, 1000);
        } else {

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    isRefreshing = true;
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, mHeadHeight);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            final float value = (float) animation.getAnimatedValue();
                            mHeadLayout.getLayoutParams().height = (int) value;
                            mHeadLayout.requestLayout();
                            ViewCompat.setTranslationY(mChildView, value);
                        }
                    });
                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            loadingView.playAnimator();
                        }
                    });
                    valueAnimator.start();
                }
            }, 400);


        }
    }

}
