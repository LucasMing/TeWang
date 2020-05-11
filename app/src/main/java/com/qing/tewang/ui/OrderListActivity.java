package com.qing.tewang.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CollectVoice;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.OrderResult;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;

public class OrderListActivity extends BaseActivity {
    SwipeRefreshListView mListView;

    private int page = 1;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(v -> finish());
        titleBar.getTitleTextView().setText("红包领取明细");
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

        APIWrapper.getPacketOrder(page)
                .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                    @Override
                    public void onNext(CommonData data) {
                        if (data.getErrno() == 0) {
//                            CollectVoice voices = data.getData();


//                            if (page == 1) {
//                                mFlowAdapter.clear();
//                            }
//                            if (voices.getTotal() == page) {
//                                mListView.loadDataComplete();
//                            } else {
//                                page++;
//                            }
//                            isLoading = false;
//                            mListView.setRefreshing(false);

//                            mFlowAdapter.addItems(voices.getList());
//                            mFlowAdapter.notifyDataSetChanged();

                        } else {
                            showToast(data.getMsg());
                        }
                    }
                });
    }

    private FlowAdapter mFlowAdapter;


    private class FlowAdapter extends MyBaseAdapter<String> {

        public FlowAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
