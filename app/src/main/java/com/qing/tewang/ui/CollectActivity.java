package com.qing.tewang.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CollectVoice;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Voice;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.MediaUtil;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.util.WechatUtil;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;


import java.util.Locale;

public class CollectActivity extends BaseActivity {

    SwipeRefreshListView mListView;

    private int page = 1;
    private boolean isLoading;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(v -> finish());

        mListView = findViewById(R.id.list_view);
        mEditText = findViewById(R.id.edit_search);

        mCollectAdapter = new CollectAdapter(getApplicationContext());
        mListView.setAdapter(mCollectAdapter);


        mListView.setOnRefreshListener(() -> {
            page = 1;
            loadData();
        });

        mListView.setOnLoadMoreListener(this::loadData);

        findViewById(R.id.commit)
                .setOnClickListener(view -> {
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

        APIWrapper.getCollect(page, mEditText.getEditableText().toString())
                .subscribe(new SimpleDialogObserver<CommonData<CollectVoice>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<CollectVoice> data) {
                        if (data.getErrno() == 0 && data.getData() != null) {
                            CollectVoice voices = data.getData();
                            if (page == 1) {
                                mCollectAdapter.clear();
                            }
                            if (voices.getTotal() == page) {
                                mListView.loadDataComplete();
                            } else {
                                page++;
                            }
                            isLoading = false;
                            mListView.setRefreshing(false);

                            mCollectAdapter.addItems(voices.getList());
                            mCollectAdapter.notifyDataSetChanged();
                        } else {
                            showToast(data.getMsg());
                            if (page == 1) {
                                mCollectAdapter.clear();
                            }
                            mListView.loadDataComplete();
                        }
                    }
                });
    }

    private CollectAdapter mCollectAdapter;


    private class CollectAdapter extends MyBaseAdapter<Voice> {

        public CollectAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_collect, parent, false);
            }
            Voice voice = mList.get(position);


            TextView title = ViewHolder.findViewById(convertView, R.id.title);

            TextView money = ViewHolder.findViewById(convertView, R.id.money);


            title.setText(voice.getShop_name());

            if (!TextUtils.isEmpty(voice.getRed_packet_money())) {
                money.setText(String.format(Locale.CHINA, "%.2f元", Integer.parseInt(voice.getRed_packet_money()) / 100.f));
            } else {
                money.setText("0.00元");
            }

            convertView.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), VoiceDetailActivity.class);
                intent.putExtra("voice", voice);
                startActivity(intent);
            });


            ImageView likeView = ViewHolder.findViewById(convertView, R.id.like_view);
            if (voice.getIs_collect() == 0) {
                likeView.setImageResource(R.mipmap.func_unlike);
            } else {
                likeView.setImageResource(R.mipmap.func_like);
            }

            likeView
                    .setOnClickListener(view -> {
                        if (voice.getIs_collect() == 0) {
                            likeView.setImageResource(R.mipmap.func_like);
                            voice.setIs_collect(1);
                        } else {
                            likeView.setImageResource(R.mipmap.func_unlike);
                            voice.setIs_collect(0);
                        }
                        APIWrapper
                                .collect(voice.getVoice_sn(), voice.getIs_collect())
                                .subscribe(new SimpleObserver<JsonObject>(getActivity()) {
                                    @Override
                                    public void onNext(JsonObject jsonObject) {

                                    }
                                });
                    });

            return convertView;
        }
    }
}
