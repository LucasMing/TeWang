package com.qing.tewang.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CollectVoice;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.MessageList;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;

public class SystemMessageActivity extends BaseActivity {

    private SwipeRefreshListView mListView;

    private int page = 1;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(v -> finish());

        mListView = findViewById(R.id.list_view);

        mMessageAdapter = new MessageAdapter(getApplicationContext());
        mListView.setAdapter(mMessageAdapter);


        mListView.setOnRefreshListener(() -> {
            page = 1;
            loadData();
        });

        mListView.setOnLoadMoreListener(this::loadData);


        loadData();

        APIWrapper.readSystemMsg()
                .subscribe(new SimpleObserver<JsonObject>(getActivity()) {
                    @Override
                    public void onNext(JsonObject jsonObject) {

                    }
                });
    }

    private void loadData() {

        if (isLoading) {
            return;
        }
        isLoading = false;
        mListView.setRefreshing(false);


        APIWrapper.getSystemMessage(page)
                .subscribe(new SimpleDialogObserver<CommonData<MessageList>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<MessageList> data) {
                        if (data.getErrno() == 0) {
                            MessageList messages = data.getData();

                            if (page == 1) {
                                mMessageAdapter.clear();
                            }
                            if (mMessageAdapter.getCount() == data.getData().getTotal()) {
                                mListView.loadDataComplete();
                            } else {
                                page++;
                            }
                            isLoading = false;
                            mListView.setRefreshing(false);

                            mMessageAdapter.addItems(messages.getList());
                            mMessageAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    private MessageAdapter mMessageAdapter;


    private class MessageAdapter extends MyBaseAdapter<MessageList.ListBean> {

        public MessageAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_system_message, parent, false);
            }

            MessageList.ListBean bean = mList.get(position);

            TextView time = ViewHolder.findViewById(convertView, R.id.time);
            TextView title = ViewHolder.findViewById(convertView, R.id.title);
            TextView content = ViewHolder.findViewById(convertView, R.id.content);

            time.setText(bean.getCreated_at());
            title.setText(bean.getTitle());
            content.setText(bean.getContent());

            return convertView;
        }
    }
}
