package com.qing.tewang.widget.refresh;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2015/11/6 0006.
 */
public class LoadingView extends View {

    //画笔
    private Paint mPaint;
    //圆形的半径
    private int circleRadius = 10;
    //浮动的边长
    private int halfDistance = 60;
    private float density;
    //颜色的下标
    private int colorIndex = 0;
    //指定的颜色
    private int colors[] = new int[]{Color.parseColor("#EE454A"), Color.parseColor("#2E9AF2"),
            Color.parseColor("#616161")};
    //中心点的x、y，当前点的x
    private int centerX,centerY,currentX=halfDistance;
    //最左边的起始点坐标x
    private int startX;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = getResources().getDisplayMetrics().density;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        centerX = getWidth()/2;
        centerY = getHeight()/2;
//        startX = (int) (centerX - halfDistance * density);
        drawCircle(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(widthMeasureSpec, DisplayUtil.dp2px(getContext(),45));
    }

    ValueAnimator valueAnimator;
    /**
     * 执行动画
     */
    public void playAnimator(){
        valueAnimator = ValueAnimator.ofInt(halfDistance,0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setDuration(400);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();
    }

    /**
     * 绘制圆形
     * @param canvas
     */
    private void drawCircle(Canvas canvas){
        if(getHeight()<=circleRadius*2){
            canvas.save();
            canvas.translate(0, -circleRadius + getHeight() / 2);
            drawDetail(canvas);
            canvas.restore();
        }else{
            drawDetail(canvas);
        }
    }

    private void drawDetail(Canvas canvas) {
        if(Math.abs(currentX) == 0){
            colorIndex++;
            mPaint.setColor(colors[colorIndex % 3]);
        }else{
            mPaint.setColor(colors[colorIndex]);
        }
        mPaint.setAntiAlias(true);
        canvas.drawCircle(centerX, centerY, circleRadius, mPaint);

        mPaint.setColor(colors[(colorIndex + 1) % 3]);
        canvas.drawCircle(centerX - currentX, centerY, circleRadius, mPaint);

        mPaint.setColor(colors[(colorIndex + 2) % 3]);
        canvas.drawCircle(centerX + currentX, centerY, circleRadius, mPaint);

        if(colorIndex == 3)
            colorIndex=0;
    }


    public void complete(AnimatorListenerAdapter listenerAdapter) {
        if(valueAnimator!=null){
            valueAnimator.cancel();
            valueAnimator = ValueAnimator.ofInt(currentX,halfDistance);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentX = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.addListener(listenerAdapter);
            valueAnimator.setRepeatMode(2);
            valueAnimator.setDuration(400);
            valueAnimator.start();
        }
    }
}
