package com.qing.tewang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qing.tewang.R;
import com.qing.tewang.widget.SmartTitleBar;

public class ManagerActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manager);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView()
                .setOnClickListener(view -> finish());

        findViewById(R.id.terminal_manager).setOnClickListener(this);
        findViewById(R.id.money_manager).setOnClickListener(this);
        findViewById(R.id.message_manager).setOnClickListener(this);
        findViewById(R.id.activity_manager).setOnClickListener(this);

    }

    private Intent intent = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.terminal_manager://店鋪管理
                intent = new Intent(getApplication(), ShopInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.money_manager://資金管理
                intent = new Intent(getApplication(), MoneyManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.message_manager://信息管理
                intent = new Intent(getApplication(), MessageManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_manager://活動管理
                intent = new Intent(getApplicationContext(), ActivityDetailActivity.class);
                startActivity(intent);
                break;
        }
    }
}
