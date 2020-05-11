package com.qing.tewang.util;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by wuliao on 2018/4/3.
 */

public class BannerImageUtils extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageUtils.load(context, (String) path, imageView);
    }
}
