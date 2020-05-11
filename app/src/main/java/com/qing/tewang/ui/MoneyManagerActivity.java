package com.qing.tewang.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.MyShop;
import com.qing.tewang.model.Wallet;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.NewSwipeRefreshLayout;

import java.util.Locale;

public class MoneyManagerActivity extends BaseActivity implements View.OnClickListener {

    private TextView mMoneyText;
    private NewSwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_manager);


        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());
        mSwipeLayout = findViewById(R.id.swipe_layout);
        findViewById(R.id.add_money).setOnClickListener(this);
        findViewById(R.id.get_money).setOnClickListener(this);
        findViewById(R.id.record_list).setOnClickListener(this);
        findViewById(R.id.flow_list).setOnClickListener(this);
        mMoneyText = findViewById(R.id.money);

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


    }

    private Wallet mWallet;

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_money://提现
                if (mWallet == null) {
                    showToast("数据还在加载！");
                    return;
                }
                intent = new Intent(getActivity(), GetMoneyActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.add_money://充值
                if (mWallet == null) {
                    showToast("数据还在加载！");
                    return;
                }
                intent = new Intent(getActivity(), PayActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.record_list://明细
                if (mWallet == null) {
                    showToast("数据还在加载！");
                    return;
                }
                intent = new Intent(getActivity(), OrderListActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.flow_list:
                if (mWallet == null) {
                    showToast("数据还在加载！");
                    return;
                }
                intent = new Intent(getActivity(), FlowListActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
    }
}
