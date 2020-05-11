package com.qing.tewang.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CollectVoice;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.ManagerMessage;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;

public class MessageManagerActivity extends BaseActivity {

    private SwipeRefreshListView mListView;

    private int page = 1;
    private boolean isLoading;
    private MessageAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_manager);
        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        mListView = findViewById(R.id.list_view);
        mMessageAdapter = new MessageAdapter(getApplicationContext());
        mListView.setAdapter(mMessageAdapter);


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

        APIWrapper.getComments(page)
                .subscribe(new SimpleDialogObserver<CommonData<ManagerMessage>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<ManagerMessage> data) {
                        if (data.getErrno() == 0 && data.getData() != null) {
                            ManagerMessage voices = data.getData();
                            if (page == 1) {
                                mMessageAdapter.clear();
                            }
                            if (voices.getTotal() == page) {
                                mListView.loadDataComplete();
                            } else {
                                page++;
                            }
                            isLoading = false;
                            mListView.setRefreshing(false);

                            mMessageAdapter.addItems(voices.getList());
                            mMessageAdapter.notifyDataSetChanged();
                        } else {
                            showToast(data.getMsg());
                        }
                    }
                });
    }


    private class MessageAdapter extends MyBaseAdapter<ManagerMessage.ListBean> {

        public MessageAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_manager_message, null);
            }
            TextView content = ViewHolder.findViewById(convertView, R.id.content);
            TextView time = ViewHolder.findViewById(convertView, R.id.time);

            ManagerMessage.ListBean bean = mList.get(position);
            content.setText(bean.getMessage());
            time.setText(bean.getCreated_at());


            return convertView;
        }
    }
}
