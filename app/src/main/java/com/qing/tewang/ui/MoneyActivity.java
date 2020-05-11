package com.qing.tewang.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Wallet;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.NewSwipeRefreshLayout;

import java.util.Locale;

public class MoneyActivity extends BaseActivity implements View.OnClickListener {

    private TextView mMoneyText;
    private NewSwipeRefreshLayout mSwipeLayout;
    private Wallet mWallet;
    private TextView mCode;

    private SmartTitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);


        titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());
        titleBar.getRightTextView().setVisibility(View.VISIBLE);
        titleBar.getRightTextView().setText("说明");


        mSwipeLayout = findViewById(R.id.swipe_layout);
        findViewById(R.id.detail_money).setOnClickListener(this);
        findViewById(R.id.get_money).setOnClickListener(this);
        mMoneyText = findViewById(R.id.money);
        mCode = findViewById(R.id.code);

        if (!TextUtils.isEmpty(SPUtils.getUser(getApplicationContext()).getOpenid())) {
            findViewById(R.id.tip_view).setVisibility(View.GONE);
        }


        mSwipeLayout.setOnRefreshListener(this::loadData);

        loadData();

    }

    private void loadData() {
        mSwipeLayout.setRefreshing(true);

        APIWrapper.getWallet().subscribe(new SimpleObserver<CommonData<Wallet>>(getActivity()) {
            @Override
            public void onNext(CommonData<Wallet> data) {
                mSwipeLayout.setRefreshing(false);
                if (data.getErrno() == 0) {
                    mWallet = data.getData();
                    mMoneyText.setText(String.format(Locale.CHINA, "￥%.2f", mWallet.getBalance() / 100.f));
                }
            }

            @Override
            public void onError(Throwable t) {
                super.onError(t);
                mSwipeLayout.setRefreshing(false);
            }
        });


        APIWrapper.bindOpenId()
                .subscribe(new SimpleObserver<CommonData<JsonObject>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<JsonObject> data) {
                        if (data.getErrno() == 0) {
                            JsonObject obj = data.getData();
                            String code = obj.get("code").getAsString();
                            mCode.setText(code);
                            findViewById(R.id.copy).setOnClickListener(view -> {

                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData mClipData = ClipData.newPlainText("Label", code);
                                cm.setPrimaryClip(mClipData);
                                showToast("复制成功");

                            });
                            showToast("详情请点击说明进行查看");


                            titleBar.getRightTextView()
                                    .setOnClickListener(view -> {
                                        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                                        intent.putExtra("url", obj.get("code").getAsString());
                                        intent.putExtra("title", "提现说明");
                                        startActivity(intent);
                                    });

                        } else if (data.getErrno() == 309) {
                            findViewById(R.id.tip_view).setVisibility(View.GONE);

                            titleBar.getRightTextView()
                                    .setOnClickListener(view -> {
                                        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                                        intent.putExtra("url", "http://tw.vlean.xyz/howtobindwx");
                                        intent.putExtra("title", "提现说明");
                                        startActivity(intent);
                                    });

                        } else {
                            showToast(data.getMsg());
                        }
                    }
                });

    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_money:
                if (mWallet == null) {
                    showToast("数据还在加载！");
                    return;
                }
                //微信消息
                APIWrapper.withdraw()
                        .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                            @Override
                            public void onNext(CommonData data) {
                                if (data.getErrno() == 0) {

                                } else {
                                    showToast(data.getMsg());
                                }
                            }
                        });
                break;
            case R.id.detail_money://提现记录
                intent = new Intent(getApplicationContext(), MoneyHistoryActivity.class);
                startActivity(intent);
                break;

        }
    }
}
