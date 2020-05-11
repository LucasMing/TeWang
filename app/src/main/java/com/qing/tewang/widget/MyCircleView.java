package com.qing.tewang.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MyCircleView extends android.support.v7.widget.AppCompatImageView {

    private int defaultRadius = 200;//默认圆形图片
    public MyCircleView(Context context) {
        super(context);
    }


    @Override protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable && drawable.getIntrinsicWidth() > 0
                    && drawable.getIntrinsicHeight() > 0) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap circleBitmap = getCircleBitmap(bitmap);
                canvas.drawBitmap(circleBitmap, 0, 0, null);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 生成圆形bitmap
     */
    private Bitmap getCircleBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2,
                defaultRadius / 2, paint);
        //取2层交集部分,只显示上层
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public int getDefaultRadius() {
        return defaultRadius;
    }

    public void setDefaultRadius(int defaultRadius) {
        this.defaultRadius = defaultRadius;
        invalidate();
    }
}
