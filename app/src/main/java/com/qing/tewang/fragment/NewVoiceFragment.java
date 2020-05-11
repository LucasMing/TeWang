package com.qing.tewang.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lzx.musiclibrary.MusicService;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.manager.MusicManager;
import com.qing.tewang.R;
import com.qing.tewang.adapter.MyBaseAdapter;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.ApiException;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.api.exception.SimpleSwipeObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.HomeAd;
import com.qing.tewang.model.Location;
import com.qing.tewang.model.User;
import com.qing.tewang.model.Voice;
import com.qing.tewang.ui.LoginActivity;
import com.qing.tewang.ui.MainActivity;
import com.qing.tewang.ui.VoiceDetailActivity;
import com.qing.tewang.ui.WebActivity;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.util.StringUtils;
import com.qing.tewang.util.ViewHolder;
import com.qing.tewang.util.WechatUtil;
import com.qing.tewang.widget.SmartTitleBar;
import com.qing.tewang.widget.StrokeColorText;
import com.qing.tewang.widget.refresh.SwipeRefreshListView;
import com.superluo.textbannerlibrary.ITextBannerItemClickListener;
import com.superluo.textbannerlibrary.TextBannerView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class NewVoiceFragment extends BaseFragment {
    private SwipeRefreshListView mListView;
    private boolean isLoading;
    private VoiceAdapter mVoiceAdapter;
    private TextBannerView mTextBanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_voice, container, false);

        mListView = rootView.findViewById(R.id.list_view);
        mTextBanner = rootView.findViewById(R.id.tv_banner);
        SmartTitleBar titleBar = rootView.findViewById(R.id.title_bar);
        titleBar.getLeftView().setVisibility(View.GONE);


        titleBar.getRightTextView()
                .setOnClickListener(view -> loadData());


        mVoiceAdapter = new VoiceAdapter(getContext());
        mListView.setAdapter(mVoiceAdapter);

        MusicManager.get().addStateObservable(mVoiceAdapter);
        MusicManager.get().setPlayMode(PlayMode.PLAY_IN_ORDER);

        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.empty_voice_view, null);

        TextView emptyText = emptyView.findViewById(R.id.empty_text);
        emptyText.setText("您所在的位置暂时没有语音");

        mListView.setEmptyView(emptyView);

        mListView.setOnRefreshListener(() -> {


            Location location = SPUtils.getLocation(getContext());
            if (location == null) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity a = (MainActivity) getActivity();
                    a.checkPerm();
                }
            }
            loadData();

        });

        loadData();
        getHomeAd();

        return rootView;
    }


    private void getHomeAd() {
        APIWrapper.getHomeAd()
                .subscribe(new SimpleObserver<CommonData<List<HomeAd>>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<List<HomeAd>> data) {
                        if (data.getErrno() == 0) {
                            List<HomeAd> ads = data.getData();
                            if (ads != null && ads.size() > 0) {
                                List<String> temps = Stream.of(ads)
                                        .map(HomeAd::getTitle)
                                        .collect(Collectors.toList());

                                mTextBanner.setDatas(temps);
                                mTextBanner.startViewAnimator();
                                mTextBanner.setItemOnClickListener(new ITextBannerItemClickListener() {
                                    @Override
                                    public void onItemClick(String data, int position) {

                                        HomeAd ad = ads.get(position);
                                        if (!TextUtils.isEmpty(ad.getVoice_sn())) {
                                            Intent intent = new Intent(getContext(), VoiceDetailActivity.class);
                                            intent.putExtra("sn", ad.getVoice_sn());
                                            startActivity(intent);
                                        } else if (!TextUtils.isEmpty(ad.getUrl())) {
                                            Intent intent = new Intent(getContext(), WebActivity.class);
                                            intent.putExtra("title", ad.getTitle());
                                            intent.putExtra("url", ad.getUrl());
                                            startActivity(intent);
                                        }

                                    }
                                });
                            }
                        }
                    }
                });

    }

    private void loadData() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        mListView.setRefreshing(true);

        Location location = SPUtils.getLocation(getContext());
        if (location == null) {
            isLoading = false;
            mListView.setRefreshing(false);
            return;
        }

        APIWrapper.getNearVoice(location.getLat(), location.getLng())
                .subscribe(new SimpleSwipeObserver<CommonData<List<Voice>>>(getActivity(), mListView) {
                    @Override
                    public void onNext(CommonData<List<Voice>> data) {
                        isLoading = false;
                        mListView.setRefreshing(false);
                        if (data.getErrno() == 0) {
                            List<Voice> voices = data.getData();
                            if (voices != null && voices.size() > 0) {
                                mVoiceAdapter.clear();

                                mVoiceAdapter.addItems(voices);
                                mListView.notifyDataSetChanged(mVoiceAdapter);

                                List<SongInfo> songs = Stream.of(voices)
                                        .map(voice -> {
                                            SongInfo songInfo = new SongInfo();
                                            songInfo.setSongId(voice.getVoice_sn());
                                            songInfo.setSongUrl(voice.getUrl());
                                            songInfo.setSongName(voice.getDescription());
                                            return songInfo;
                                        }).collect(Collectors.toList());


                                MusicManager.get()
                                        .playMusic(songs, 0);
                            }
                        }
                    }

                    @Override
                    public void onError(ApiException ex) {
                        super.onError(ex);
                        isLoading = false;
                        mListView.setRefreshing(false);
                    }
                });
    }

    private int lastPosition = -1;

    private class VoiceAdapter extends MyBaseAdapter<Voice> implements Observer {

        private VoiceAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_voice, parent, false);
            }
            Voice voice = mList.get(position);

            ImageView image = ViewHolder.findViewById(convertView, R.id.image);
            if (voice.getImg() != null && voice.getImg().size() > 0) {
                ImageUtils.load(getContext(), voice.getImg().get(0), image);
            }
            TextView title = ViewHolder.findViewById(convertView, R.id.title);
            TextView shopName = ViewHolder.findViewById(convertView, R.id.shop_name);
            TextView money = ViewHolder.findViewById(convertView, R.id.money);
            ImageView playView = ViewHolder.findViewById(convertView, R.id.play_view);
            TextView distance = ViewHolder.findViewById(convertView, R.id.distance);


            title.setText(voice.getDescription());
            shopName.setText(voice.getShop_name());

            float dis = Float.parseFloat(voice.getDistance());
            if (dis > 1000) {
                distance.setText(String.format(Locale.CHINA, "%.0f公里", dis / 1000));
            } else {
                distance.setText(String.format(Locale.CHINA, "%.0f米", dis));
            }

            money.setText(String.format(Locale.CHINA, "%.2f", Integer.parseInt(voice.getRed_packet_money()) / 100.f));

            SongInfo songInfo = new SongInfo();
            songInfo.setSongId(voice.getVoice_sn());
            songInfo.setSongUrl(voice.getUrl());
            songInfo.setSongName(voice.getDescription());

            playView.setOnClickListener(view -> {
                if (MusicManager.isPlaying() && MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                    //展示当前播放音乐的UI
                    playView.setImageResource(R.mipmap.media_play);
                    MusicManager.get().stopMusic();
                } else {
                    //展示其他没在播放音乐的UI
                    MusicManager.get()
                            .playMusicByIndex(position);
                    playView.setImageResource(R.mipmap.media_pause);
                }
            });

            if (MusicManager.isPlaying() && MusicManager.isCurrMusicIsPlayingMusic(songInfo)) {
                //展示当前播放音乐的UI
                playView.setImageResource(R.mipmap.media_pause);
            } else {
                //展示其他没在播放音乐的UI
                playView.setImageResource(R.mipmap.media_play);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), VoiceDetailActivity.class);
                    intent.putExtra("voice", voice);
                    startActivity(intent);
                }
            });

            ViewHolder.findViewById(convertView, R.id.share)
                    .setOnClickListener(view -> {
                        new WechatUtil(getContext())
                                .share(getActivity(), voice.getShareUrl(), voice.getShareTitle(),
                                        voice.getShareDesc(), voice.getShareImg());
                    });
            ImageView likeView = ViewHolder.findViewById(convertView, R.id.like_view);
            if (voice.getIs_collect() == 0) {
                likeView.setImageResource(R.mipmap.func_unlike);
            } else {
                likeView.setImageResource(R.mipmap.func_like);
            }

            ViewHolder.findViewById(convertView, R.id.like)
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

            AppCompatImageView voiceView = ViewHolder.findViewById(convertView, R.id.voice_view);
            View activity = ViewHolder.findViewById(convertView, R.id.activity);
            if (voice.getIs_award() == 0) {//没有
                Drawable icon = getResources().getDrawable(R.mipmap.func_voice);
                Drawable tintIcon = DrawableCompat.wrap(icon);
                DrawableCompat.setTintList(tintIcon, ColorStateList.valueOf(Color.parseColor("#999999")));
                voiceView.setImageDrawable(tintIcon);
                activity.setOnClickListener(null);
            } else {
                Drawable icon = getResources().getDrawable(R.mipmap.func_voice);
                Drawable tintIcon = DrawableCompat.wrap(icon);
                DrawableCompat.setTintList(tintIcon, null);
                voiceView.setImageDrawable(tintIcon);

                activity.setOnClickListener(view -> {
                    Intent intent = new Intent(getContext(), VoiceDetailActivity.class);
                    intent.putExtra("voice", voice);
                    startActivity(intent);
                });
            }

            View tagView = ViewHolder.findViewById(convertView, R.id.tag_view);
            TagFlowLayout tagLayout = ViewHolder.findViewById(convertView, R.id.tag_layout);

            if (voice.getTags() == null || voice.getTags().size() == 0) {
                tagView.setVisibility(View.GONE);
            } else {

                TagAdapter mAdapter = new TagAdapter<String>(voice.getTags()) {

                    @Override
                    public View getView(final FlowLayout parent, int position, final String text) {
                        View view = LayoutInflater.from(getContext())
                                .inflate(R.layout.item_flow_text, parent, false);
                        TextView tv = view.findViewById(R.id.text);
                        tv.setBackground(getResources().getDrawable(R.drawable.checked_bg));
                        tv.setText(text);
                        return view;
                    }

                };

                tagLayout.setAdapter(mAdapter);
            }

            return convertView;
        }

        @Override
        public void update(Observable o, Object arg) {
            int msg = (int) arg;
            if (msg == MusicManager.MSG_PLAYER_START || msg == MusicManager.MSG_PLAYER_PAUSE
                    || msg == MusicManager.MSG_PLAYER_STOP) {
                LogUtils.e("jiang", "start...");
                notifyDataSetChanged();
            }
            //todo 监听播放开始事件通过mList.get(mCurrentPotion)
            if (msg == MusicManager.MSG_PLAY_COMPLETION) {
                //领取红包
                int position = MusicManager.get().getCurrPlayingIndex();
                if (mList == null || mList.size() == 0) {
                    return;
                }
                if (position != mList.size() - 1 && position != 0) {
                    position--;
                }
                if (lastPosition == position) {
                    return;
                }
                lastPosition = position;
                Voice voice = mList.get(position);
                if (getContext() != null && SPUtils.isLogin(getContext())) {
                    APIWrapper.getMoney(voice.getVoice_sn())
                            .subscribe(new SimpleObserver<CommonData<JsonObject>>(getActivity()) {
                                @Override
                                public void onNext(CommonData<JsonObject> data) {
                                    if (data.getErrno() == 0) {
                                        Toast.makeText(getContext(), "恭喜获得红包！", Toast.LENGTH_SHORT).show();

                                        JsonObject json = data.getData();
                                        int lineBalance = Integer.parseInt(json.get("balance").getAsString());
                                        int redPacketNum = Integer.parseInt(json.get("red_packet_num").getAsString());

                                        User user = SPUtils.getUser(getContext());
                                        user.setBalance(lineBalance + "");
                                        user.setRed_packet_num(redPacketNum);
                                        SPUtils.clearUser(getContext());
                                        SPUtils.saveUser(getContext(), user);

                                        if (getActivity() instanceof MainActivity) {
                                            MainActivity activity = (MainActivity) getActivity();
                                            activity.setMoneyView();
                                        }

                                    } else {
                                        if (!TextUtils.isEmpty(data.getMsg()) && getContext() != null) {
                                            Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                } else {
                    if (MusicManager.get().isPlaying()) {
                        MusicManager.get().stopMusic();
                    }

                    if (getActivity() != null) {
                        View dialogView = LayoutInflater.from(getContext())
                                .inflate(R.layout.dialog_money, null);

                        mDialog = DisplayUtils.getInstance()
                                .getCenterDialog(getActivity(), dialogView);

                        TextView moneyText = dialogView.findViewById(R.id.money_count);
                        moneyText.setText(String.format(Locale.CHINA, "%.2f", Float.parseFloat(voice.getRed_packet_money()) / 100));

                        StrokeColorText getMoney = dialogView.findViewById(R.id.get_money);
                        getMoney.setOnClickListener(view -> {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        });

                        dialogView.findViewById(R.id.close)
                                .setOnClickListener(view -> mDialog.cancel());
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    }
                }


                if (position == mList.size() - 1) {
                    if (MusicManager.get().isPlaying()) {
                        MusicManager.get().stopMusic();
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    private Dialog mDialog;


}
