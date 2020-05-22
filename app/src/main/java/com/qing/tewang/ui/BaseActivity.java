package com.qing.tewang.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.qing.tewang.app.ExitAppUtils;
import com.qing.tewang.util.ImmersiveStatusBar.StatusBarUtil;

public class BaseActivity extends AppCompatActivity {


    public void showToast(String msg) {
        Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showToastInCenter(String msg) {
        Toast toast = Toast.makeText(getApplication(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public BaseActivity getActivity() {
        return this;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitAppUtils.getInstance().addActivity(this);

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ExitAppUtils.getInstance().delActivity(this);
    }

}
