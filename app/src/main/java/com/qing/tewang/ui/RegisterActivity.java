package com.qing.tewang.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.util.StringUtils;
import com.qing.tewang.widget.SmartTitleBar;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends BaseActivity {


    private int mWaitTime = 60;
    private Timer timer;
    private TimeHandler mHandler = new TimeHandler(getActivity());


    private TextView mGetCode;

    private EditText mEditName, mEditPwd, mEditCode, mInviteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        mEditName = findViewById(R.id.user_phone);
        mEditPwd = findViewById(R.id.user_pwd);
        mEditCode = findViewById(R.id.user_token);
        mInviteId = findViewById(R.id.inviteId);

        mGetCode = findViewById(R.id.get_code);

        mGetCode.setOnClickListener(view -> {

            String mobile = mEditName.getText().toString();
            if (mobile.length() == 11 && mobile.startsWith("1")) {
                sendCode(mobile);
            } else {
                showToast("手机格式不正确!");
            }
        });


        findViewById(R.id.register)
                .setOnClickListener(view -> {
                    final String mobile = mEditName.getEditableText().toString();
                    final String pwd = mEditPwd.getEditableText().toString();
                    final String code = mEditCode.getEditableText().toString();
                    final String inviteId = mInviteId.getEditableText().toString();


                    if (mobile.length() == 11 && mobile.startsWith("1")) {
                        if (!TextUtils.isEmpty(code)) {
                            if (StringUtils.isPassword(pwd)) {
                                if (!TextUtils.isEmpty(inviteId)) {
                                    register(mobile, code, pwd, inviteId);
                                } else {
                                    register(mobile, code, pwd, inviteId);
                                }
                            } else {
                                showToast("密码必须八位及以上！");
                            }
                        } else {
                            showToast("请输入验证码！");
                        }
                    } else {
                        showToast("请输入正确的手机号！");
                    }

                });

    }


    private void sendCode(String mobile) {
        //后台判断是否注册过

        APIWrapper.sendCode(mobile, "1")
                .subscribe(new SimpleDialogObserver<JsonObject>(getActivity()) {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        int msg = jsonObject.get("errno").getAsInt();
                        if (msg == 0) {
                            showToast("发送成功");
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    mHandler.sendEmptyMessage(0);
                                }
                            }, 0, 1000);
                        } else {
                            //注册过
                            showToast("失败请重试!");
                        }
                    }
                });

    }


    private void register(String mobile, String code, String pwd, String inviteId) {

        APIWrapper.register(mobile, code, pwd, inviteId)
                .subscribe(new SimpleDialogObserver<JsonObject>(getActivity()) {
                    @Override
                    public void onNext(JsonObject data) {
                        if (data.get("errno").getAsInt() == 0) {
                            showToast("注册成功！");
                            finish();
                        } else {
                            showToast(data.get("msg").getAsString());
                        }
                    }
                });

    }


    private class TimeHandler extends Handler {

        private WeakReference<Activity> reference;

        TimeHandler(Activity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (reference.get() == null) {
                timer.cancel();
                timer = null;
                return;
            }
            mWaitTime--;
            if (mWaitTime == 0) {
                mWaitTime = 60;
                mGetCode.setText("获取验证码");
                mGetCode.setEnabled(true);
                timer.cancel();
            } else {
                mGetCode.setEnabled(false);
                mGetCode.setText("还剩" + mWaitTime + "秒");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
        mHandler = null;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }
}
