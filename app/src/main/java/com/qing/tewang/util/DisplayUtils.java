package com.qing.tewang.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.qing.tewang.R;

public class DisplayUtils {

    private static DisplayUtils sInstance;

    public static DisplayUtils getInstance() {
        if (sInstance == null) {
            synchronized (DisplayUtils.class) {
                if (sInstance == null) {
                    sInstance = new DisplayUtils();
                }
            }
        }
        return sInstance;
    }

    public Dialog getWaitDialog(Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_wait, null);
        Dialog dialog = new Dialog(activity, R.style.TranslucentNoTitleDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public void showMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * SP转px
     *
     * @param context
     * @param sp
     * @return
     */
    public int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public int getHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public int getWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public Dialog getCenterDialog(Activity activity, View view) {
        Dialog dialog = new Dialog(activity, R.style.TranslucentNoTitleDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialog_animate);
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        return dialog;
    }






}
