package com.qing.tewang.app;

import android.app.Application;
import android.content.Context;

import com.lzx.musiclibrary.manager.MusicLibrary;
import com.lzx.musiclibrary.notification.NotificationCreater;
import com.lzx.musiclibrary.notification.PendingIntentMode;
import com.lzx.musiclibrary.utils.BaseUtil;
import com.qing.tewang.BuildConfig;
import com.tencent.bugly.Bugly;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wuliao on 2018/3/26.
 */

public class MyApplication extends Application {


    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Bugly.init(getApplicationContext(), "b5448476ad", BuildConfig.DEBUG);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);

        if (BaseUtil.getCurProcessName(this).equals("com.qing.tewang")) {

            NotificationCreater creater = new NotificationCreater.Builder()
                    .setTargetClass("com.qing.tewang.ui.VoiceDetailActivity")
                    .setCreateSystemNotification(true)
//                    .setNotificationCanClearBySystemBtn(true)
                    .setPendingIntentMode(PendingIntentMode.MODE_ACTIVITY)
                    .build();
            MusicLibrary musicLibrary = new MusicLibrary.Builder(this)
                    .setNotificationCreater(creater)
                    .build();
            musicLibrary.startMusicService();
        }
    }

    public static Context getInstance() {
        return sInstance;
    }
}