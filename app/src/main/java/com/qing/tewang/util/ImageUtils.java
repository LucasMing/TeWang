package com.qing.tewang.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qing.tewang.R;
import com.qing.tewang.app.MyApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by wuliao on 2018/3/26.
 */

public class ImageUtils {

    private ImageUtils() {
        throw new IllegalStateException("no instance");
    }

    private static <T> void checkNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 检查传入的url或者数组中第一个url是不是为空
     */
    @SuppressWarnings("uncheck")
    public static boolean isUrlsEmpty(String... urls) {
        return urls == null || urls.length == 0 || urls[0] == null || urls[0].trim().isEmpty();
    }

    @SuppressWarnings("uncheck")
    public static String checkAndHandleUrl(String... urls) {
        if (isUrlsEmpty(urls)) {
            return "empty_url";
        }
        return urls[0];
    }

    public static Picasso getInstance(Context context) {
        return Picasso.with(context);
    }

    public static void load(@NonNull Context context, String path, @DrawableRes int placeholderResId,
                            @NonNull ImageView target) {
        Picasso.with(context)
                .load(checkAndHandleUrl(path))
                .placeholder(placeholderResId)
                .error(placeholderResId)
                .into(target);
    }

    public static void load(@NonNull Context context, String path,
                            @NonNull ImageView target) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        Picasso.with(context)
                .load(checkAndHandleUrl(path))
                .placeholder(getPlaceholder())
                .error(getError())
                .into(target);
    }

    public static void loadNoHolder(@NonNull Context context, String path,
                                    @NonNull ImageView target) {
        Picasso.with(context)
                .load(checkAndHandleUrl(path))
                .into(target);
    }




    private static int getPlaceholder() {
        return R.mipmap.common_image_load;
    }

    private static int getError() {
        return R.mipmap.common_image_load;
    }


    public static void loadCenterCrop(@NonNull Context context, String path,
                                      @DrawableRes int placeholderResId, @NonNull ImageView target) {
        loadCenterCrop(context, path, placeholderResId, placeholderResId, target);
    }

    public static void loadCenterCrop(@NonNull Context context, String path,
                                      @DrawableRes int placeholderResId, @DrawableRes int errorResId, @NonNull ImageView target) {
        Picasso.with(context)
                .load(checkAndHandleUrl(path))
                .placeholder(placeholderResId)
                .error(errorResId)
                .centerCrop()
                .fit()
                .into(target);
    }



    public static void download(String url, String path) {

        Picasso.with(MyApplication.getInstance()).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }


    private static class ImageViewTarget implements Target {

        private WeakReference<ImageView> mImageViewReference;

        public ImageViewTarget(ImageView imageView) {
            this.mImageViewReference = new WeakReference<>(imageView);
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            //you can use this bitmap to load image in image view or save it in image file like the one in the above question.
            ImageView imageView = mImageViewReference.get();
            if (imageView != null) {
                float scale = (float) imageView.getWidth() / (float) bitmap.getWidth();
                int defaultHeight = Math.round(bitmap.getHeight() * scale);
                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                params.height = defaultHeight;
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            ImageView imageView = mImageViewReference.get();
            if (imageView != null) {
                imageView.setImageDrawable(errorDrawable);
            }

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            ImageView imageView = mImageViewReference.get();
            if (imageView != null) {
                imageView.setImageDrawable(placeHolderDrawable);
            }
        }
    }

}
