package com.qing.tewang.app;

import android.app.Activity;
import android.content.Intent;

import com.lzx.musiclibrary.MusicService;
import com.lzx.musiclibrary.control.BasePlayControl;
import com.lzx.musiclibrary.control.PlayControl;
import com.lzx.musiclibrary.manager.MusicLibrary;
import com.lzx.musiclibrary.manager.MusicManager;

import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Created by wuliao on 2018/7/2.
 */

public class ExitAppUtils {
    private static ExitAppUtils instance = new ExitAppUtils();
    /**
     * 转载Activity的容器
     */
    public static Stack<Activity> mActivityList = new Stack<>();

    /**
     * 将构造函数私有化
     */
    private ExitAppUtils() {
    }

    /**
     * 获取ExitAppUtils的实例，保证只有一个ExitAppUtils实例存在
     *
     * @return
     */
    public static ExitAppUtils getInstance() {
        return instance;
    }

    /**
     * 添加Activity实例到mActivityList中，在onCreate()中调用
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityList.add(activity);
    }

    /**
     * 从容器中删除多余的Activity实例，在onDestroy()中调用
     *
     * @param activity
     */
    public void delActivity(Activity activity) {
        mActivityList.remove(activity);
    }

    public Activity getCurrent() {
        try {
            return mActivityList.lastElement();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * 退出程序的方法
     */
    public void exit() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
        try {
            MusicManager.get().stopMusic();
            MusicManager.get().stopNotification();
//            Intent intent = new Intent(MyApplication.getInstance(), MusicService.class);
//            MyApplication.getInstance().stopService(intent);
//
//            new MusicLibrary.Builder(MyApplication.getInstance())
//                    .build().stopService();
//
//            MusicManager.get().stopMusic();
//            MusicManager.get().stopNotification();


        } catch (Exception e) {

        }

//        System.exit(0);
    }

    /**
     * 退出程序的方法
     */
    public void finish() {
        for (Activity activity : mActivityList) {
            activity.finish();
        }
    }
}
