package com.qing.tewang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.MessageList;
import com.qing.tewang.model.MessageResult;
import com.qing.tewang.widget.SmartTitleBar;

public class MessageActivity extends BaseActivity implements View.OnClickListener {

    private TextView mMoneyText, mSystemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());


        mMoneyText = findViewById(R.id.money_view);
        mSystemText = findViewById(R.id.system_view);
        findViewById(R.id.system_message)
                .setOnClickListener(this);
        findViewById(R.id.money_message)
                .setOnClickListener(this);

        loadData();
    }

    private void loadData() {


        APIWrapper.getUnReadMessage()
                .subscribe(new SimpleObserver<CommonData<MessageResult>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<MessageResult> data) {

                        if (data.getErrno() == 0 && data.getData() != null) {
                            if (!TextUtils.isEmpty(data.getData().getSystemMessage())) {
                                mSystemText.setText(data.getData().getSystemMessage());
                            }
                            if (!TextUtils.isEmpty(data.getData().getRedPackMessage())) {
                                mMoneyText.setText(data.getData().getRedPackMessage());
                            }
                        }
                    }
                });

    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_message:
                intent = new Intent(getApplicationContext(), SystemMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.money_message:
                intent = new Intent(getApplicationContext(), MoneyMessageActivity.class);
                startActivity(intent);
                break;
        }
    }
}
