package com.qing.tewang.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import com.qing.tewang.util.DisplayUtils;

public class ProgressView extends View {
    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int mTotalValue;
    private int mWidth, mHeight;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    private float mProgress = 0;

    private boolean isPlaying = false;


    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasInnerCircle(canvas);
        canvasOutCircle(canvas);


        if (isPlaying) {
            canvasPlay(canvas);
        } else {
            canvasPause(canvas);
        }


    }

    private void canvasPlay(Canvas canvas) {
        float width = 12;
        float height = 36;
        float space = 6;

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        RectF left = new RectF(mWidth / 2 - space / 2 - width,
                (mHeight - height) / 2.0f, (mWidth - space) / 2.0f,
                (mHeight + height) / 2.0f);

        RectF right = new RectF(mWidth / 2 + space / 2,
                (mHeight - height) / 2.0f, mWidth / 2 + space / 2 + width,
                (mHeight + height) / 2.0f);

        canvas.drawRect(left, paint);
        canvas.drawRect(right, paint);

    }

    private void canvasOutCircle(Canvas canvas) {
        float x = mWidth / 2;
        float y = mHeight / 2;

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#33ffffff"));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x, y, mWidth / 2.0f, paint);


    }

    public int getTotalValue() {
        return mTotalValue;
    }


    private ValueAnimator mAnimator;

    public void setTotalValue(int mTotalValue) {
        this.mTotalValue = mTotalValue;
        mProgress = 0;
        mAnimator = ValueAnimator.ofFloat(0, mTotalValue);

        mAnimator.addUpdateListener(animation -> {
            mProgress = animation.getAnimatedFraction() * 100;
            invalidate();
        });

        mAnimator.setDuration(mTotalValue);
    }

    public float getProgress() {
        return mProgress;
    }

    public void clearProgress(){
        mProgress = 0;
        invalidate();
    }

    public void startProgress() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    public void pauseProgress() {
        if (mAnimator != null) {
            mAnimator.pause();
        }
    }

    public void resumeProgress() {
        if (mAnimator != null) {
            mAnimator.resume();
        }
    }


    private void canvasInnerCircle(Canvas canvas) {
        float x = mWidth / 2;
        float y = mHeight / 2;

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#19000000"));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12);
        canvas.drawCircle(x, y, mWidth / 2.0f - 14, paint);


        RectF rectF = new RectF(18, 18, getWidth() - 18, getHeight() - 18);
        paint.setColor(Color.parseColor("#00c853"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);// 圆形笔头
        paint.setStrokeWidth(14);

        canvas.drawArc(rectF, -90, mProgress * 3.6f, false, paint);


    }

    private void canvasPause(Canvas canvas) {
        float radius = DisplayUtils.getInstance().dp2px(getContext(), 14);
        canvasTriangle(canvas);
        canvasCircle(canvas, radius, Color.WHITE);
    }

    private void canvasCircle(Canvas canvas, float radius, int color) {
        float x = mWidth / 2;
        float y = mHeight / 2;

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        //圆环的宽度默认为半径的1／5
        float ringWidth = radius / 7;
        //由于圆环本身有宽度，所以半径要减去圆环宽度的一半，不然一部分圆会在view外面
        radius = radius - ringWidth / 2;
        paint.setStrokeWidth(ringWidth);

        canvas.drawCircle(x, y, radius, paint);
    }

    private void canvasTriangle(Canvas canvas) {
        float height = DisplayUtils.getInstance().dp2px(getContext(), 12);
        float width = (float) (height / Math.sqrt(2));
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);

        float top_x = (mWidth - width) / 2.0f;
        float top_y = (mWidth - height) / 2.0f;

        Path path = new Path();
        path.moveTo(top_x + 3, top_y);
        path.lineTo(top_x + 3, top_y + height);
        path.lineTo(top_x + width + 3, mWidth / 2.0f);
        path.close();
        canvas.drawPath(path, p);

    }
}
