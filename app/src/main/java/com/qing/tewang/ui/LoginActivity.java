package com.qing.tewang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.User;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.util.StringUtils;
import com.qing.tewang.widget.SmartTitleBar;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPhone, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPhone = findViewById(R.id.user_phone);
        mPassword = findViewById(R.id.user_pwd);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.forget_pwd).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
    }

    private Intent mIntent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:

                String account = mPhone.getText().toString();
                String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(account)) {
                    showToast("手机号码不能为空。");
                    return;
                }

                if (!StringUtils.isMobile(account)) {
                    showToast("手机号码格式错误");
                    return;
                }

                if (!StringUtils.isPassword(password)) {
                    showToast("密码必须包含至少 8 个字符。");
                    return;
                }

                APIWrapper.login(account, password)
                        .subscribe(new SimpleDialogObserver<CommonData<User>>(getActivity()) {
                            @Override
                            public void onNext(CommonData<User> data) {
                                if (data.getErrno() == 0) {
                                    User user = data.getData();
                                    SPUtils.saveUser(getApplicationContext(), user);
                                    APIWrapper.bindDevice(getApplication());

                                    mIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(mIntent);
                                    finish();

                                } else {
                                    showToast(data.getMsg());
                                }
                            }
                        });


                break;
            case R.id.forget_pwd:
                mIntent = new Intent(getApplicationContext(), ForgetPwdOneActivity.class);
                startActivity(mIntent);
                break;
            case R.id.register:
                mIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(mIntent);
                break;
        }
    }

}
