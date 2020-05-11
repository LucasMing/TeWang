package com.qing.tewang.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CollectVoice;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Withdraw;
import com.qing.tewang.util.StringUtils;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;

import java.util.List;

public class MoneyHistoryActivity extends BaseActivity {

    private SwipeRefreshListView mListView;

    private int page = 1;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_history);


        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(v -> finish());

        mListView = findViewById(R.id.list_view);

        mHistoryAdapter = new HistoryAdapter(getApplicationContext());
        mListView.setAdapter(mHistoryAdapter);


        mListView.setOnRefreshListener(() -> {
            page = 1;
            loadData();
        });

        loadData();
    }


    private void loadData() {

        if (isLoading) {
            return;
        }
        isLoading = false;
        mListView.setRefreshing(false);

        APIWrapper.withdrawOrders()
                .subscribe(new SimpleDialogObserver<CommonData<List<Withdraw>>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<List<Withdraw>> data) {

                        if (data.getErrno() == 0) {
                            List<Withdraw> voices = data.getData();

                            if (page == 1) {
                                mHistoryAdapter.clear();
                            }

                            isLoading = false;
                            mListView.setRefreshing(false);

                            mHistoryAdapter.addItems(voices);
                            mHistoryAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    private HistoryAdapter mHistoryAdapter;


    private class HistoryAdapter extends MyBaseAdapter<Withdraw> {

        private HistoryAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_money_history, parent, false);
            }
            Withdraw item = mList.get(position);

            TextView number = ViewHolder.findViewById(convertView, R.id.number);
            TextView time = ViewHolder.findViewById(convertView, R.id.time);
            TextView money = ViewHolder.findViewById(convertView, R.id.money);


            money.setText("￥" + StringUtils.formatPrice(item.getMoney()));
            time.setText(item.getCreate_time());
            number.setText(item.getTitle());

            return convertView;
        }
    }
}
