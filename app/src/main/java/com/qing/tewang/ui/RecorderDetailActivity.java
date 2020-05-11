package com.qing.tewang.ui;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.RecorderResult;
import com.qing.tewang.model.ShopDetail;
import com.qing.tewang.model.Voice;
import com.qing.tewang.recorder.AndroidAudioRecorder;
import com.qing.tewang.recorder.model.AudioChannel;
import com.qing.tewang.recorder.model.AudioSampleRate;
import com.qing.tewang.recorder.model.AudioSource;
import com.qing.tewang.util.BannerImageUtils;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.MediaUtil;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.ProgressView;
import com.youth.banner.Banner;

import java.util.Locale;

public class RecorderDetailActivity extends BaseActivity implements View.OnClickListener {

    private View mRecorder;
    private MediaUtil mMediaUtil;
    private TextView shopName, address;
    private ProgressView mediaView;
    private ImageView likeView;
    private Banner banner;
    private TextView time, money, condition, text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        Voice voice = getIntent().getParcelableExtra("voice");
        mRecorder = findViewById(R.id.recorder);
        mRecorder.setOnClickListener(this);

        banner = findViewById(R.id.banner);
        banner.setImageLoader(new BannerImageUtils());

        shopName = findViewById(R.id.name);
        address = findViewById(R.id.address);
        mediaView = findViewById(R.id.progress_view);
        likeView = findViewById(R.id.title_like);
        text = findViewById(R.id.text);

        time = findViewById(R.id.time);
        money = findViewById(R.id.money);
        condition = findViewById(R.id.condition);


        findViewById(R.id.title_back)
                .setOnClickListener(view -> finish());

        if (voice != null) {
            text.setText(voice.getDescription());
            if (voice.getImg() != null) {
                banner.setImages(voice.getImg()).start();
            }
            loadVoiceById(voice.getVoice_sn());
        } else {
            String sn = getIntent().getStringExtra("sn");
            loadVoiceById(sn);
        }


    }

    private ShopDetail mResult;

    private void loadVoiceById(String sn) {
        APIWrapper.getVoiceById(sn)
                .subscribe(new SimpleDialogObserver<CommonData<ShopDetail>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<ShopDetail> data) {
                        if (data.getErrno() == 0) {
                            mResult = data.getData();
                            //todo
//                            time.setText(mResult.getOnline_time() + "---" + mResult.getOffline_time());
//                            money.setText(String.format(Locale.CHINA, "红包%.2f元", Integer.parseInt(mResult.getAward_money()) / 100.0f));
//                            condition.setText(mResult.getCondition());
//                            Voice voice = data.getData().getVoice();
//                            setData(voice);
                        } else {
                            showToast(data.getMsg());
                        }
                    }
                });
    }

    private Intent intent;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recorder:
                if (mResult != null) {
                    if (SPUtils.isLogin(getApplicationContext())) {
                        int requestCode = 0;

                        String filePath = "audio";
                        int color = getResources().getColor(R.color.main_orange);
                        AndroidAudioRecorder.with(this)
                                // Required
                                .setFilePath(filePath)
                                .setColor(color)
                                .setRequestCode(requestCode)

                                // Optional
                                .setSource(AudioSource.MIC)
                                .setChannel(AudioChannel.STEREO)
                                .setSampleRate(AudioSampleRate.HZ_48000)
                                .setAutoStart(false)
                                .setKeepDisplayOn(true)
                                .setCondition(mResult.getCondition())
                                .setContent(mResult.getContent())
                                .setMaxSecond(60)
                                .setSn(mResult.getVoice_sn())
                                // Start recording
                                .record();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }


                } else {
                    showToast("请等待数据记载！");
                }

                break;
        }
    }


    private void setData(Voice voice) {
        if (voice.getImg() != null) {
            banner.setImages(voice.getImg()).start();
        }
        shopName.setText(voice.getShop_name());
        address.setText(voice.getAddress());

        if (voice.getIs_collect() == 0) {
            likeView.setImageResource(R.mipmap.func_unlike);
        } else {
            likeView.setImageResource(R.mipmap.func_like);
        }

        likeView.setOnClickListener(view -> {

        });
        findViewById(R.id.title_share)
                .setOnClickListener(view -> {

                });

        initMedia(mediaView, voice);
        mediaView.setOnClickListener(view -> {
            if (mediaView.isPlaying()) {//暂停
                mMediaUtil.pause();
                mediaView.setPlaying(false);
                mediaView.pauseProgress();
            } else {//播放
                mMediaUtil.start();
                mediaView.setPlaying(true);
                if (mMediaUtil.isHasPlayed()) {
                    mediaView.resumeProgress();
                    mediaView.setPlaying(true);
                }
            }
        });
    }

    private Dialog mDialog;

    private void initMedia(ProgressView mediaView, Voice voice) {

        mMediaUtil = new MediaUtil(getApplicationContext(), voice.getUrl());

        mMediaUtil.setPlayListener(new MediaUtil.PlayListener() {
            @Override
            public void begin(MediaPlayer media) {
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
                    View viewDialog = LayoutInflater.from(getApplicationContext())
                            .inflate(R.layout.dialog_money, null);
                    TextView moneyText = viewDialog.findViewById(R.id.money_count);
                    moneyText.setText(String.format(Locale.CHINA, "收听可领取%.2f元红包", Float.parseFloat(voice.getRed_packet_money()) / 100));

                    TextView getMoney = viewDialog.findViewById(R.id.get_money);
                    getMoney.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            APIWrapper.getMoney(voice.getVoice_sn())
                                    .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                                        @Override
                                        public void onNext(CommonData data) {

                                        }
                                    });
                        }
                    });
                    viewDialog.findViewById(R.id.get_money)
                            .setOnClickListener(view -> mDialog.cancel());
                    mDialog = DisplayUtils.getInstance()
                            .getCenterDialog(getActivity(), viewDialog);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
            }
        });

    }
}
