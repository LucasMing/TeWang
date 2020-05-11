package com.qing.tewang.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Voice;
import com.qing.tewang.ui.RecorderDetailActivity;
import com.qing.tewang.ui.VoiceDetailActivity;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.MediaUtil;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.util.WechatUtil;
import com.qing.tewang.widget.ProgressView;
import com.qing.tewang.widget.StrokeColorText;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import link.fls.swipestack.SwipeStack;

public class VoiceFragment extends BaseFragment implements View.OnClickListener {

    private SwipeStack mSwipeStack;

    private List<CommonData<Voice>> mVoiceData;

    private SwipeAdapter mAdapter;

    private FloatingActionButton mCollect, mVoice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_voice, null);

        mSwipeStack = rootView.findViewById(R.id.swipeStack);


        FloatingActionButton share = rootView.findViewById(R.id.share);
        share.setOnClickListener(this);


        mCollect = rootView.findViewById(R.id.my_collect);
        mCollect.setOnClickListener(this);


        mVoice = rootView.findViewById(R.id.voice);
        mVoice.setOnClickListener(this);


        mVoiceData = new ArrayList<>();


        mAdapter = new SwipeAdapter(getContext(), mVoiceData);
        mSwipeStack.setAdapter(mAdapter);

        mSwipeStack.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
//                MediaPlayUtils.getInstance().stop();
                if (mMediaUtil != null) {
                    mMediaUtil.release();
                }
                mVoiceData.remove(0);
                loadData();
            }

            @Override
            public void onViewSwipedToRight(int position) {
//                MediaPlayUtils.getInstance().stop();
                if (mMediaUtil != null) {
                    mMediaUtil.release();
                }
                mVoiceData.remove(0);
                loadData();
            }

            @Override
            public void onStackEmpty() {
                loadData();
            }
        });

        loadData();

        return rootView;
    }

    private void loadData() {
        String lat = "121.402203";
        String lng = "31.317468";
//        APIWrapper.getNearVoice(lat, lng)
//                .subscribe(new SimpleObserver<CommonData<Voice>>(getActivity()) {
//                    @Override
//                    public void onNext(CommonData<Voice> voiceCommonData) {
//                        if (voiceCommonData.getErrno() == 0 && voiceCommonData.getData() != null) {
//                            mVoiceData.clear();
//                            mVoiceData.add(voiceCommonData);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });


    }


    private Dialog mDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                if (mVoiceData != null && mVoiceData.size() > 0
                        && mVoiceData.get(0).getData() != null) {
                    Voice voice = mVoiceData.get(0).getData();
                    new WechatUtil(getContext())
                            .share(getActivity(), voice.getShareUrl(), voice.getShareTitle(),
                                    voice.getShareDesc(), voice.getShareImg());

                } else {
                    Toast.makeText(getContext(), "等待数据记载...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_collect:
                if (mVoiceData != null && mVoiceData.size() > 0
                        && mVoiceData.get(0).getData() != null) {
                    CommonData<Voice> temp = mVoiceData.get(0);
                    Voice data = temp.getData();
                    if (data.getIs_collect() == 0) {
                        mCollect.setImageResource(R.mipmap.func_like);
                        data.setIs_collect(1);
                    } else {
                        mCollect.setImageResource(R.mipmap.func_unlike);
                        data.setIs_collect(0);
                    }
                    APIWrapper
                            .collect(data.getVoice_sn(), data.getIs_collect())
                            .subscribe(new SimpleDialogObserver<JsonObject>(getActivity()) {
                                @Override
                                public void onNext(JsonObject jsonObject) {

                                }
                            });
                } else {
                    Toast.makeText(getContext(), "等待数据记载...", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.voice:
                if (mVoiceData != null && mVoiceData.size() > 0
                        && mVoiceData.get(0).getData() != null) {
                    Intent intent = new Intent(getContext(), RecorderDetailActivity.class);
                    intent.putExtra("voice", mVoiceData.get(0).getData());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "等待数据记载...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private MediaUtil mMediaUtil;

    private class SwipeAdapter extends MyBaseAdapter<CommonData<Voice>> {


        public SwipeAdapter(Context mContext, List<CommonData<Voice>> mList) {
            super(mContext, mList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_swipe_layout, parent, false);
            }

            Voice voice = mList.get(position).getData();
            voice.setUrl("http://media.vlean.xyz/201811/e06453eda90bfeeb9f4338606e85fc2f.mp3");

            TextView text = ViewHolder.findViewById(convertView, R.id.text);
            TextView name = ViewHolder.findViewById(convertView, R.id.name);
            TextView distance = ViewHolder.findViewById(convertView, R.id.distance);
            TextView time = ViewHolder.findViewById(convertView, R.id.time);

            name.setText(voice.getShop_name());
            text.setText(voice.getDescription());
            distance.setText(String.format(Locale.CHINA, "距离您%d米", (int) Double.parseDouble(voice.getDistance())));

            ImageView shopImage = ViewHolder.findViewById(convertView, R.id.shop_image);

            if (voice.getImg() != null && voice.getImg().size() > 0) {
                ImageUtils.load(getContext(), voice.getImg().get(0), shopImage);
            }

            if (voice.getIs_collect() == 0) {//没有收藏
                mCollect.setImageResource(R.mipmap.func_unlike);
            } else {//收藏
                mCollect.setImageResource(R.mipmap.func_like);
            }
            TextView redPacket = ViewHolder.findViewById(convertView, R.id.red_packet);

            redPacket.setText(String.format(Locale.CHINA, "收听可领取%.2f元红包", Float.parseFloat(voice.getRed_packet_money()) / 100));

            TextView loadDetail = ViewHolder.findViewById(convertView, R.id.load_detail);

            FileUtils.getMediaDuration(voice.getUrl(), media -> {

                int duration = media.getDuration();
                int min = duration / (60 * 1000);
                int sed = duration % (60 * 1000);

                time.setText(String.format("%s:%s",
                        String.format(Locale.CANADA, "%02d", min), String.format("%02d", sed)));

            });

            ProgressView mediaView = ViewHolder.findViewById(convertView, R.id.media_player);
            mMediaUtil = new MediaUtil(getContext(), voice.getUrl());

            mMediaUtil.setPlayListener(new MediaUtil.PlayListener() {
                @Override
                public void begin(MediaPlayer media) {
                    if (time.getText().toString().equals("00:00")) {
                        int duration = media.getDuration();
                        int min = duration / (60 * 1000);
                        int sed = duration % (60 * 1000);

                        time.setText(String.format("%s:%s",
                                String.format(Locale.CANADA, "%02d", min), String.format("%02d", sed)));
                    }
                    mediaView.setTotalValue(media.getDuration());
                    mediaView.setPlaying(true);
                    mediaView.startProgress();
                }

                @Override
                public void end(MediaPlayer media) {
                    mediaView.setPlaying(false);
                    mediaView.pauseProgress();

                    if (media.getDuration() - media.getCurrentPosition() < 1000
                            && mediaView.getProgress() > 95) {
                        View viewDialog = LayoutInflater.from(getContext())
                                .inflate(R.layout.dialog_money, null);
                        TextView moneyText = viewDialog.findViewById(R.id.money_count);
                        moneyText.setText(String.format(Locale.CHINA, "%.2f", Float.parseFloat(voice.getRed_packet_money()) / 100));

                        StrokeColorText getMoney = viewDialog.findViewById(R.id.get_money);
                        getMoney.setOnClickListener(view -> {
                            APIWrapper.getMoney(voice.getVoice_sn())
                                    .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                                        @Override
                                        public void onNext(CommonData data) {
                                            if (data.getErrno() == 0) {
                                                Toast.makeText(getContext(), "获取成功！", Toast.LENGTH_SHORT).show();
                                                mDialog.dismiss();
                                            } else {
                                                Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        });
                        viewDialog.findViewById(R.id.close)
                                .setOnClickListener(view -> mDialog.cancel());
                        mDialog = DisplayUtils.getInstance()
                                .getCenterDialog(getActivity(), viewDialog);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    }
                }
            });
            mediaView.setOnClickListener(view -> {
                if (mediaView.isPlaying()) {//暂停
                    mMediaUtil.pause();
                    mediaView.setPlaying(false);
                    mediaView.pauseProgress();
//                    MediaPlayUtils.getInstance().pause();
                } else {//播放
                    mMediaUtil.start();
                    mediaView.setPlaying(true);
                    if (mMediaUtil.isHasPlayed()) {
                        mediaView.resumeProgress();
                        mediaView.setPlaying(true);
                    }
//                    MediaPlayUtils.getInstance()
//                            .play(voice.getUrl(), mp -> {
//                                        mediaView.setPlaying(false);
//                                        mediaView.pauseProgress();
//
//                                        if (mp.getDuration() - mp.getCurrentPosition() < 1000
//                                                && mediaView.getProgress() > 95) {
//                                            View viewDialog = LayoutInflater.from(getContext())
//                                                    .inflate(R.layout.dialog_money, null);
//                                            mDialog = DisplayUtils.getInstance()
//                                                    .getCenterDialog(getActivity(), viewDialog);
//                                            mDialog.show();
//                                        }
//
//                                    }
//                            );
                }
            });


            loadDetail.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), VoiceDetailActivity.class);
                intent.putExtra("voice", voice);
                startActivity(intent);
            });

            //判断是有录制活动
//            if (voice.getIs_award() == 0) {//没有
//                mVoice.setImageTintList(ColorStateList.valueOf(Color.parseColor("#999999")));
//                mVoice.setOnClickListener(null);
//            } else {
//                mVoice.setImageTintList(null);
//            }

            return convertView;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaUtil != null) {
            mMediaUtil.release();
        }
//        MediaPlayUtils.getInstance().release();
    }


}
