package com.qing.tewang.api.exception;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.widget.Toast;


import com.qing.tewang.app.MyApplication;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.util.SPUtils;

import io.reactivex.disposables.Disposable;

/**
 * Created by wuliao on 2018/4/17.
 */

public abstract class SimpleDialogObserver<T> extends ExceptionObserver<T> {
    private Dialog mDialog;
    private Activity mActivity;

    protected SimpleDialogObserver(Activity activity) {
        mActivity = activity;
        mDialog = DisplayUtils.getInstance().getWaitDialog(activity);
        mDialog.show();
    }

    @Override
    public void onError(ApiException ex) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
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
    }

    @Override
    public void onComplete() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

}
