package com.qing.tewang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.qing.tewang.R;
import com.qing.tewang.util.DisplayUtils;


/**
 * Created by Administrator on 2015/10/29 0029.
 */
public class StrokeColorText extends View {
    private boolean isFill;

    public StrokeColorText(Context context) {
        this(context, null);
    }

    public StrokeColorText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int strokeColor;
    private int textColor;
    private float textSize;
    private String text = "青琳";
    private float radius;
    private Paint textPaint;

    public StrokeColorText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.StrokeColorText);
        strokeColor = a.getColor(R.styleable.StrokeColorText_strokeViewColor,
                getResources().getColor(R.color.mainColor));
        textColor = a.getColor(R.styleable.StrokeColorText_strokeTextColor,
                strokeColor);
        text = a.getString(R.styleable.StrokeColorText_strokeColorText);
        isFill = a.getBoolean(R.styleable.StrokeColorText_strokeColorIsFill, false);
        textSize = a.getDimension(R.styleable.StrokeColorText_strokeColorTextSize, DisplayUtils.getInstance().dp2px(getContext(), 16));
        radius = a.getDimension(R.styleable.StrokeColorText_strokeColorRadius, 6);
        mStrokeWidth = a.getInt(R.styleable.StrokeColorText_strokeColorStrokeWidth,2);
        a.recycle();
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
    }

    public String getText() {
        return text;
    }

    private void drawText(Canvas canvas) {
        if (TextUtils.isEmpty(text))
            text = "";
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        // 计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算文字baseline
        float textBaseY = getMeasuredHeight() - (getMeasuredHeight() - fontHeight) / 2 - fontMetrics.bottom;
        float textWidth = textPaint.measureText(text);
        canvas.drawText(text, (getMeasuredWidth() - textWidth) / 2, textBaseY, textPaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            if (text == null || TextUtils.isEmpty(text)) {
                text = "等待";
            }
            setMeasuredDimension((int) (getPaddingLeft() + getPaddingRight() + textPaint.measureText(text)), heightMeasureSpec);
        }
    }

    private int width = 3;
    private int mStrokeWidth = 1;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(strokeColor);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setAntiAlias(true);


        if (isFill) {
            paint.setStyle(Paint.Style.FILL);
        } else {
            paint.setStyle(Paint.Style.STROKE);
        }
        RectF rect = new RectF(0 + width, 0 + width, getMeasuredWidth() - width, getMeasuredHeight() - width);
        canvas.drawRoundRect(rect, radius, radius, paint);
        drawText(canvas);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
        requestLayout();
    }

    public void setColor(int color) {
        this.strokeColor = color;
        textPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public void setBackColor(int color) {
        this.strokeColor = color;
        invalidate();
        requestLayout();
    }


    public void setTextColor(int color) {
        textPaint.setColor(color);
        invalidate();
        requestLayout();
    }
}
