package com.qing.tewang.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.OrderResult;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;

import java.util.Locale;

public class FlowListActivity extends BaseActivity {
    SwipeRefreshListView mListView;

    private int page = 1;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(v -> finish());

        titleBar.getTitleTextView().setText("资金流水");

        mListView = findViewById(R.id.list_view);

        mFlowAdapter = new FlowAdapter(getApplicationContext());
        mListView.setAdapter(mFlowAdapter);


        mListView.setOnRefreshListener(() -> {
            page = 1;
            loadData();
        });

        mListView.setOnLoadMoreListener(this::loadData);


        loadData();

    }

    private void loadData() {

        if (isLoading) {
            return;
        }
        isLoading = false;
        mListView.setRefreshing(false);

        APIWrapper.getOrder(page)
                .subscribe(new SimpleDialogObserver<CommonData<OrderResult>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<OrderResult> data) {
                        if (data.getErrno() == 0) {
                            OrderResult result = data.getData();

                            if (page == 1) {
                                mFlowAdapter.clear();
                            }
                            if (result.getNextPage() == page) {
                                mListView.loadDataComplete();
                            } else {
                                page++;
                            }
                            isLoading = false;
                            mListView.setRefreshing(false);

                            mFlowAdapter.addItems(result.getList());
                            mFlowAdapter.notifyDataSetChanged();

                        } else {
                            showToast(data.getMsg());
                        }
                    }
                });
    }

    private FlowAdapter mFlowAdapter;


    private class FlowAdapter extends MyBaseAdapter<OrderResult.ListBean> {

        public FlowAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_money_history, parent, false);
            }
            OrderResult.ListBean item = mList.get(position);
            TextView title = ViewHolder.findViewById(convertView, R.id.number);
            TextView time = ViewHolder.findViewById(convertView, R.id.time);
            TextView money = ViewHolder.findViewById(convertView, R.id.money);

            title.setText(item.getTitle());
            time.setText(item.getCreate_time());
            money.setText(String.format(Locale.CHINA, "￥%.2f元", Integer.parseInt(item.getMoney()) / 100.0f));

            return convertView;
        }
    }

}
