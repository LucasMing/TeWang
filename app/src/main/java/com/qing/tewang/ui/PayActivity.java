package com.qing.tewang.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.WXPayManager;
import com.qing.tewang.widget.SmartTitleBar;

public class PayActivity extends BaseActivity {

    private CheckBox mLastCheck, mCheckAlipay, mCheckWechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        final SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                finish();
            }
        });

        mCheckAlipay = findViewById(R.id.check_alipay);
        mCheckWechat = findViewById(R.id.check_wechat);
        final EditText moneyNumber = findViewById(R.id.edit_text);

        moneyNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2 + 1);
                        moneyNumber.setText(s);
                        moneyNumber.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    moneyNumber.setText(s);
                    moneyNumber.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        moneyNumber.setText(s.subSequence(0, 1));
                        moneyNumber.setSelection(1);
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLastCheck = mCheckWechat;
        mCheckAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastCheck.getId() != v.getId()) {
                    mLastCheck.setChecked(false);
                }
                mCheckAlipay.setChecked(true);
                mLastCheck = mCheckAlipay;
            }
        });
        mCheckWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastCheck.getId() != v.getId()) {
                    mLastCheck.setChecked(false);
                }
                mCheckWechat.setChecked(true);
                mLastCheck = mCheckWechat;
            }
        });

        registerPay();

        findViewById(R.id.pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String type = getType();

                if (type.equals("alipay")) {
                    showToast("暂不支持支付宝");
                    return;
                }

                if (type == null) {
                    DisplayUtils.getInstance().showMsg(getApplicationContext(), "请选择支付方式");
                    return;
                }

                if (TextUtils.isEmpty(moneyNumber.getText().toString())) {
                    DisplayUtils.getInstance().showMsg(getApplicationContext(), "请选择金额");
                    return;
                }

                if (WXPayManager.checkWXCanPay(getActivity())) {
                    int number = (int) (Float.parseFloat(moneyNumber.getText().toString()) * 100);
                    APIWrapper.recharge(number)
                            .subscribe(new SimpleDialogObserver<CommonData<JsonObject>>(getActivity()) {
                                @Override
                                public void onNext(CommonData<JsonObject> data) {
                                    if (data.getErrno() == 0) {
                                        JsonObject jsonObject = data.getData();
                                        WXPayManager.toPay(getActivity(), jsonObject.get("appid").getAsString(),
                                                jsonObject.get("partnerid").getAsString(), jsonObject.get("prepayid").getAsString(),
                                                jsonObject.get("timestamp").getAsString(), jsonObject.get("sign").getAsString(),
                                                jsonObject.get("noncestr").getAsString());
                                    } else {
                                        showToast(data.getMsg());
                                    }
                                }
                            });
                }

            }
        });

    }


    private String getType() {
        if (mCheckAlipay.isChecked()) {
            return "alipay";
        }
        if (mCheckWechat.isChecked()) {
            return "wechat";
        }
        return null;
    }

    private BroadcastReceiver mReceiver;

    private void registerPay() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.pay.success");
        filter.addAction("com.pay.fail");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("com.pay.success")) {
                    getActivity().setResult(RESULT_OK);
                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                    finish();
                    return;
                }

                if (action.equals("com.pay.fail")) {

                    return;
                }

            }
        };

        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver.isInitialStickyBroadcast()) {
            unregisterReceiver(mReceiver);
        }
    }
}
