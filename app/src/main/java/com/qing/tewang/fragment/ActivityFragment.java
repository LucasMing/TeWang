package com.qing.tewang.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.RecorderResult;
import com.qing.tewang.recorder.AndroidAudioRecorder;
import com.qing.tewang.recorder.model.AudioChannel;
import com.qing.tewang.recorder.model.AudioSampleRate;
import com.qing.tewang.recorder.model.AudioSource;
import com.qing.tewang.ui.RecorderDetailActivity;
import com.qing.tewang.ui.VoiceDetailActivity;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.widget.MyStack;
import com.qing.tewang.widget.SmartTitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import link.fls.swipestack.SwipeStack;

public class ActivityFragment extends BaseFragment {

    private SwipeRefreshLayout mSwipeLayout;
    private MyStack mSwipeStack;
    private ActivityAdapter mAdapter;
    private List<RecorderResult> mRecordData;
    private ImageView next;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_activity, container, false);

        SmartTitleBar titleBar = rootView.findViewById(R.id.title_bar);
        titleBar.getLeftView().setVisibility(View.GONE);

        TextView emptyText = rootView.findViewById(R.id.empty_text);
        emptyText.setText("您所在的位置暂时没有语音录制活动");

        mSwipeLayout = rootView.findViewById(R.id.swipe_layout);
        mSwipeStack = rootView.findViewById(R.id.swipeStack);
        next = rootView.findViewById(R.id.next);

        mSwipeLayout.setOnRefreshListener(this::loadData);

        mRecordData = new ArrayList<>();
        mAdapter = new ActivityAdapter(getContext(), mRecordData);
        mSwipeStack.setAdapter(mAdapter);

        mSwipeStack.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
            }

            @Override
            public void onViewSwipedToRight(int position) {
            }

            @Override
            public void onStackEmpty() {
                loadData();
            }
        });

        next.setVisibility(View.GONE);
        next.setOnClickListener(view -> {
            if (!mAdapter.isEmpty()) {
                mSwipeStack.swipeTopViewToLeft();
            }
        });


        mSwipeStack.setOnClickListener(view -> {
            if (!mAdapter.isEmpty()) {
                Intent intent = new Intent(getContext(), VoiceDetailActivity.class);
                intent.putExtra("sn", mRecordData.get(mSwipeStack.getCurrentPosition()).getVoice_sn());
                startActivity(intent);
            }
        });


        loadData();

        return rootView;
    }

    private boolean isLoading;

    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        mSwipeLayout.setRefreshing(true);

        APIWrapper.getRecorder()
                .subscribe(new SimpleObserver<CommonData<List<RecorderResult>>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<List<RecorderResult>> data) {
                        isLoading = false;
                        mSwipeLayout.setRefreshing(false);
                        if (data.getErrno() == 0) {
                            mSwipeStack.resetStack();
                            mRecordData.clear();
                            if (data.getData() != null) {
                                mRecordData.addAll(data.getData());
                                mAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        isLoading = false;
                        mSwipeLayout.setRefreshing(false);
                    }
                });


    }


    private class ActivityAdapter extends MyBaseAdapter<RecorderResult> {

        private ActivityAdapter(Context mContext, List<RecorderResult> mList) {
            super(mContext, mList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_activity_record, parent, false);
            }
            RecorderResult item = mList.get(position);

            TextView name = ViewHolder.findViewById(convertView, R.id.name);
            name.setText(item.getShop_name());
            View imageLayout = ViewHolder.findViewById(convertView, R.id.image_layout);
            imageLayout.getLayoutParams().height = (int) (DisplayUtils
                    .getInstance().getHeight(getContext()) * 0.28);

            ImageView shopImage = ViewHolder.findViewById(convertView, R.id.shop_image);
            if (item.getImg() != null && item.getImg().size() > 0) {
                ImageUtils.load(getContext(), item.getImg().get(0), shopImage);
            }
            TextView content = ViewHolder.findViewById(convertView, R.id.content);
            TextView contentLast = ViewHolder.findViewById(convertView, R.id.content_last);
            content.setText(item.getContent());
            contentLast.setVisibility(View.GONE);
//            content.getViewTreeObserver().addOnGlobalLayoutListener(
//                    new ViewTreeObserver.OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            String conStr = item.getContent();
//                            int width = content.getWidth();
//                            if (content.getPaint().measureText(conStr) < width) {//一行能显示
//                                contentLast.setVisibility(View.GONE);
//                                content.setText(conStr);
//                            } else {
//                                contentLast.setVisibility(View.VISIBLE);
//                                int size = conStr.length();
//                                int index = 0;
//                                for (int i = 5; i < size; i++) {
//                                    index = i;
//                                    if (content.getPaint().measureText(conStr, 0, i) > width) {
//                                        break;
//                                    }
//                                }
//                                content.setText(conStr.substring(0, index - 1));
//                                contentLast.setText(conStr.substring(index - 1));
//                            }
//                            content.getViewTreeObserver()
//                                    .removeOnGlobalLayoutListener(this);
//                        }
//                    });


            TextView address = ViewHolder.findViewById(convertView, R.id.address);
            TextView money = ViewHolder.findViewById(convertView, R.id.money);
            TextView condition = ViewHolder.findViewById(convertView, R.id.condition);
            address.setText(item.getAddress());


            ViewHolder.findViewById(convertView, R.id.load_detail)
                    .setOnClickListener(view -> {
                        Intent intent = new Intent(getContext(), VoiceDetailActivity.class);
                        intent.putExtra("sn", item.getVoice_sn());
                        startActivity(intent);
                    });

            ViewHolder.findViewById(convertView, R.id.recorder)
                    .setOnClickListener(view -> {
                        String img = "";
                        if (item.getImg() != null && item.getImg().size() > 0) {
                            img = item.getImg().get(0);
                        }

                        String filePath = "audio";
                        int color = getResources().getColor(R.color.main_orange);
                        AndroidAudioRecorder.with(getActivity())
                                // Required
                                .setFilePath(filePath)
                                .setColor(color)
                                .setRequestCode(0)

                                // Optional
                                .setSource(AudioSource.MIC)
                                .setChannel(AudioChannel.STEREO)
                                .setSampleRate(AudioSampleRate.HZ_48000)
                                .setAutoStart(false)
                                .setKeepDisplayOn(true)
                                .setCondition(item.getCondition())
                                .setMaxSecond(60)
                                .setSn(item.getVoice_sn())
                                .setContent(item.getContent())
                                .setAvatarUrl(img)
                                // Start recording
                                .record();


                    });

            if (!TextUtils.isEmpty(item.getAward_money())) {
                money.setText(String.format(Locale.CHINA, "红包%.2f元",
                        Float.parseFloat(item.getAward_money()) / 100));
            }

            condition.setText(item.getCondition());


            convertView.setOnClickListener(temp -> {
                Intent intent = new Intent(getContext(), VoiceDetailActivity.class);
                intent.putExtra("sn", item.getVoice_sn());
                startActivity(intent);
            });


            return convertView;
        }
    }

}
