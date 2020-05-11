package com.qing.tewang.api.exception;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;


import com.qing.tewang.app.MyApplication;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;

import io.reactivex.disposables.Disposable;

/**
 * Created by wuliao on 2018/4/17.
 */

public abstract class SimpleSwipeObserver<T> extends ExceptionObserver<T> {
    private SwipeRefreshListView mList;
    private Activity mActivity;

    public SimpleSwipeObserver(Activity activity, SwipeRefreshListView list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public void onError(ApiException ex) {
//        if (ex.getMessage().equals("Signature has expired.")) {
//
//
//        } else if (ex.getMessage().equals("Not Found") || ex.getMessage().equals("未找到。")) {
//            LogUtils.e(ex.getMessage());
//            onNotFound();
//        } else {
//            LogUtils.e(ex.getMessage());
//            Toast.makeText(MyApplication.getInstance(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }


        if (mList != null) {
            mList.setRefreshing(false);
        }
    }

    @Override
    public void onComplete() {
        if (mList != null) {
            mList.setRefreshing(false);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}
