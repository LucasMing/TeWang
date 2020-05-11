package com.qing.tewang.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.qing.tewang.model.UserRecordList;
import com.qing.tewang.model.Voice;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.MediaUtil;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.util.WechatUtil;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;

import java.util.Locale;

public class VoiceListActivity extends BaseActivity {

    SwipeRefreshListView mListView;

    private int page = 1;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_list);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(v -> finish());

        mListView = findViewById(R.id.list_view);

        mVoiceAdapter = new VoiceAdapter(getApplicationContext());
        mListView.setAdapter(mVoiceAdapter);


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


        APIWrapper.getUserRecords(page)
                .subscribe(new SimpleDialogObserver<CommonData<UserRecordList>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<UserRecordList> data) {
                        if (data.getErrno() == 0) {

                            UserRecordList result = data.getData();

                            if (page == 1) {
                                mVoiceAdapter.clear();
                            }

                            if (Integer.parseInt(result.getNextPage())
                                    == page) {
                                mListView.loadDataComplete();
                            } else {
                                page++;
                            }
                            isLoading = false;
                            mListView.setRefreshing(false);

                            mVoiceAdapter.addItems(result.getList());
                            mVoiceAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    private VoiceAdapter mVoiceAdapter;
    private MediaUtil mMediaUtil;

    private AlertDialog dialog;

    private class VoiceAdapter extends MyBaseAdapter<UserRecordList.ListBean> {

        public VoiceAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_user_record, parent, false);
            }
            UserRecordList.ListBean voice = mList.get(position);

            TextView name = ViewHolder.findViewById(convertView, R.id.user_name);
            TextView time = ViewHolder.findViewById(convertView, R.id.create_time);
            TextView voiceLength = ViewHolder.findViewById(convertView, R.id.voice_length);
            ImageView play = ViewHolder.findViewById(convertView, R.id.play);
            CheckBox checkBox = ViewHolder.findViewById(convertView, R.id.check_one);

            name.setText(voice.getName());
            time.setText(voice.getCreated_at());
            if (voice.getStatus() == 0) {
                checkBox.setFocusable(true);
                checkBox.setChecked(false);
                checkBox.setClickable(true);
                checkBox.setOnClickListener(view -> {
                    checkBox.setChecked(false);
                    dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("提示")
                            .setMessage("您确定将赏金分给" + voice.getName() + "吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    voice.setStatus(0);
                                    notifyDataSetChanged();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    APIWrapper.chooseRecord(voice.getId())
                                            .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                                                @Override
                                                public void onNext(CommonData commonData) {
                                                    if (commonData.getErrno() == 0) {
                                                        showToast("成功");
                                                        voice.setStatus(1);
                                                        notifyDataSetChanged();
                                                    } else {
                                                        showToast(commonData.getMsg());
                                                    }
                                                }
                                            });

                                }
                            }).show();
                });
            } else {
                checkBox.setChecked(true);
                checkBox.setOnClickListener(view -> {
                    checkBox.setChecked(true);
                });
            }

            play.setOnClickListener(view -> {
                if (mMediaUtil != null) {
                    mMediaUtil.stop();
                    mMediaUtil.release();
                }
                mMediaUtil = new MediaUtil(getApplicationContext(),
                        voice.getVoice_url());
                mMediaUtil.setPlayListener(new MediaUtil.PlayListener() {
                    @Override
                    public void begin(MediaPlayer media) {
                        play.setImageResource(R.mipmap.media_pause);
                    }

                    @Override
                    public void end(MediaPlayer media) {
                        play.setImageResource(R.mipmap.media_play);
                    }
                });
                mMediaUtil.start();
            });


//            ImageView image = ViewHolder.findViewById(convertView, R.id.image);
//            if (voice.getImg() != null && voice.getImg().size() > 0) {
//                ImageUtils.load(getApplicationContext(), voice.getImg().get(0), image);
//            }
//            TextView title = ViewHolder.findViewById(convertView, R.id.title);
//            TextView shopName = ViewHolder.findViewById(convertView, R.id.shop_name);
//            TextView money = ViewHolder.findViewById(convertView, R.id.money);
//            ImageView playView = ViewHolder.findViewById(convertView, R.id.play_view);
//
//            title.setText(voice.getDescription());
//            shopName.setText(voice.getShop_name());
//            if (!TextUtils.isEmpty(voice.getRed_packet_money())) {
//                money.setText(String.format(Locale.CHINA, "%.2f", Integer.parseInt(voice.getRed_packet_money()) / 100.f));
//            } else {
//                money.setText("");
//            }
//
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(), VoiceDetailActivity.class);
//                    intent.putExtra("voice", voice);
//                    startActivity(intent);
//                }
//            });
//
//            ViewHolder.findViewById(convertView, R.id.share)
//                    .setOnClickListener(view -> {
//                        new WechatUtil(getApplicationContext())
//                                .share(getActivity(), voice.getShareUrl(), voice.getShareTitle(),
//                                        voice.getShareDesc(), voice.getShareImg());
//                    });
//            ImageView likeView = ViewHolder.findViewById(convertView, R.id.like_view);
//            if (voice.getIs_collect() == 0) {
//                likeView.setImageResource(R.mipmap.func_unlike);
//            } else {
//                likeView.setImageResource(R.mipmap.func_like);
//            }
//
//            ViewHolder.findViewById(convertView, R.id.like)
//                    .setOnClickListener(view -> {
//                        if (voice.getIs_collect() == 0) {
//                            likeView.setImageResource(R.mipmap.func_like);
//                            voice.setIs_collect(1);
//                        } else {
//                            likeView.setImageResource(R.mipmap.func_unlike);
//                            voice.setIs_collect(0);
//                        }
//                        APIWrapper
//                                .collect(voice.getVoice_sn(), voice.getIs_collect())
//                                .subscribe(new SimpleObserver<JsonObject>(getActivity()) {
//                                    @Override
//                                    public void onNext(JsonObject jsonObject) {
//
//                                    }
//                                });
//                    });
//
//            ImageView voiceView = ViewHolder.findViewById(convertView, R.id.voice_view);
//            View activity = ViewHolder.findViewById(convertView, R.id.activity);
//            if (voice.getIs_award() == 0) {//没有
//                voiceView.setImageTintList(ColorStateList.valueOf(Color.parseColor("#999999")));
//                activity.setOnClickListener(null);
//            } else {
//                voiceView.setImageTintList(null);
//                activity.setOnClickListener(view -> {
//                    Intent intent = new Intent(getApplicationContext(), RecorderDetailActivity.class);
//                    intent.putExtra("voice", voice);
//                    startActivity(intent);
//                });
//            }
            return convertView;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }

        if (mMediaUtil != null) {
            mMediaUtil.stop();
            mMediaUtil.release();
        }
    }
}
