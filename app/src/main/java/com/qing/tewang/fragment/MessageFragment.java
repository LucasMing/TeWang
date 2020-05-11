package com.qing.tewang.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.ApiException;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.MessageResult;
import com.qing.tewang.ui.MoneyMessageActivity;
import com.qing.tewang.ui.SystemMessageActivity;
import com.qing.tewang.widget.SmartTitleBar;

public class MessageFragment extends BaseFragment implements View.OnClickListener {

    private TextView mMoneyText, mSystemText;
    private SwipeRefreshLayout mSwipeLayout;
    private View mSystemMsgView, mMoneyMsgView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        SmartTitleBar titleBar = rootView.findViewById(R.id.title_bar);
        titleBar.getLeftView().setVisibility(View.GONE);

        mMoneyText = rootView.findViewById(R.id.money_view);
        mSystemText = rootView.findViewById(R.id.system_view);
        mSwipeLayout = rootView.findViewById(R.id.swipe_layout);

        mMoneyMsgView = rootView.findViewById(R.id.money_msg_view);
        mSystemMsgView = rootView.findViewById(R.id.system_msg_view);


        rootView.findViewById(R.id.system_message)
                .setOnClickListener(this);
        rootView.findViewById(R.id.money_message)
                .setOnClickListener(this);
        mSwipeLayout.setOnRefreshListener(this::loadData);
        loadData();

        return rootView;
    }

    private boolean isLoading = false;

    private void loadData() {

        if (isLoading) {
            return;
        }
        isLoading = true;
        APIWrapper.getUnReadMessage()
                .subscribe(new SimpleObserver<CommonData<MessageResult>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<MessageResult> data) {
                        mSwipeLayout.setRefreshing(false);
                        isLoading = false;
                        if (data.getErrno() == 0 && data.getData() != null) {
                            if (!TextUtils.isEmpty(data.getData().getSystemMessage())) {
                                mSystemText.setText(data.getData().getSystemMessage());
                            }
                            if (!TextUtils.isEmpty(data.getData().getRedPackMessage())) {
                                mMoneyText.setText(data.getData().getRedPackMessage());
                            }

                            if (data.getData().getUnReadRedPacketNum() > 0) {
                                mMoneyMsgView.setVisibility(View.VISIBLE);
                            } else {
                                mMoneyMsgView.setVisibility(View.GONE);
                            }

                            if (data.getData().getUnReadNum() > 0) {
                                mSystemMsgView.setVisibility(View.VISIBLE);
                            } else {
                                mSystemMsgView.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        super.onError(ex);
                        mSwipeLayout.setRefreshing(false);
                        isLoading = false;
                    }
                });

    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_message:
                intent = new Intent(getContext(), SystemMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.money_message:
                intent = new Intent(getContext(), MoneyMessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
