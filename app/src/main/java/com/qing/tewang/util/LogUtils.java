package com.qing.tewang.util;

import android.text.TextUtils;
import android.util.Log;

import com.qing.tewang.BuildConfig;


/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class LogUtils {
    private static final String TAG = "TeWang";


    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    public static void i(String message) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, message);
    }

    public static void d(String message) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, message);
    }

    public static void d(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message);
    }

    public static void v(String message) {
        if (BuildConfig.DEBUG)
            Log.v(TAG, message);
    }

    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.v(tag, message);
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(message))
            Log.e(tag, message);
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG && !TextUtils.isEmpty(message))
            Log.e(TAG, message);
    }

}
