package com.qing.tewang.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class AutoFitImageView extends android.support.v7.widget.AppCompatImageView {
    private int defaultWidth = 0;

    public AutoFitImageView(Context context) {
        super(context);
    }

    public AutoFitImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoFitImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        this.measure(0, 0);
        if (drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return;
        }
        if (defaultWidth == 0) {
            defaultWidth = getWidth();
        }

        float scale = (float) defaultWidth / (float) bitmap.getWidth();
        int defaultHeight = Math.round(bitmap.getHeight() * scale);
        ViewGroup.LayoutParams params = this.getLayoutParams();
        params.width = defaultWidth;
        params.height = defaultHeight;
        this.setLayoutParams(params);
        super.onDraw(canvas);
    }


}
