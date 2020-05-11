package com.qing.tewang.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.app.ExitAppUtils;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Wallet;
import com.qing.tewang.util.CommonUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.util.WechatUtil;
import com.qing.tewang.widget.SmartTitleBar;

public class SettingActivity extends BaseActivity {

    private TextView mBindWechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        mBindWechat = findViewById(R.id.bind_wechat);

        loadData();


        findViewById(R.id.logout)
                .setOnClickListener(view -> {
                    showToast("退出登录！");
                    SPUtils.clearUser(getApplicationContext());
                    SPUtils.clearLocation(getApplicationContext());
                    ExitAppUtils.getInstance().exit();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.feedback)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(getActivity(), NewFeedbackActivity.class);
                    startActivity(intent);
                });

        TextView version = findViewById(R.id.version);
        version.setText(CommonUtils.getVersion(getApplicationContext()));


    }

    private void loadData() {
        APIWrapper.getUserInfo()
                .subscribe(new SimpleDialogObserver<CommonData<Wallet>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<Wallet> data) {
                        if (data.getErrno() == 0) {
                            String openId = data.getData().getOpenid();
                            if (!TextUtils.isEmpty(openId)) {
                                mBindWechat.setText("已绑定");
                            } else {
                                mBindWechat.setText("未绑定");
                            }
                        } else {
                            mBindWechat.setText("未绑定");
                        }
                    }
                });
    }
}
