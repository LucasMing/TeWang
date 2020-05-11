package com.qing.tewang.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.MyShop;
import com.qing.tewang.recorder.AndroidAudioRecorder;
import com.qing.tewang.recorder.model.AudioChannel;
import com.qing.tewang.recorder.model.AudioSampleRate;
import com.qing.tewang.recorder.model.AudioSource;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.util.MediaUtil;
import com.qing.tewang.widget.SlideSwitch;
import com.qing.tewang.widget.SmartTitleBar;

import org.angmarch.views.NiceSpinner;


import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


public class ActivityDetailActivity extends BaseActivity {


    private NiceSpinner mNiceSpinner;
    private SlideSwitch mSlideSwitch;
    private View mActivityView;
    private List<String> list = null;
    private ImageView mPlayView;

    private EditText mCondition, mContent, mAwardMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);

        SmartTitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getLeftView().setOnClickListener(view -> finish());

        titleBar.getRightTextView()
                .setOnClickListener(view -> {
                    if (mSlideSwitch.getState()) {
                        saveVoiceAndActivity();
                    } else {
                        saveVoice();
                    }
                });

        mNiceSpinner = findViewById(R.id.spinner);
        mCondition = findViewById(R.id.condition);
        mContent = findViewById(R.id.content);
        mAwardMoney = findViewById(R.id.award_money);


        mSlideSwitch = findViewById(R.id.slide);
        mSlideSwitch.setState(false);

        mSlideSwitch.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                mActivityView.setVisibility(View.VISIBLE);
            }

            @Override
            public void close() {
                mActivityView.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.voice_length);
        mActivityView = findViewById(R.id.activity);
        mTextLength = findViewById(R.id.voice_length);


        findViewById(R.id.record)
                .setOnClickListener(view -> {


                    String filePath = "audio";
                    int color = getResources().getColor(R.color.main_orange);
                    AndroidAudioRecorder.with(this)
                            // Required
                            .setFilePath(filePath)
                            .setColor(color)
                            .setRequestCode(100)

                            // Optional
                            .setSource(AudioSource.MIC)
                            .setChannel(AudioChannel.STEREO)
                            .setSampleRate(AudioSampleRate.HZ_48000)
                            .setAutoStart(false)
                            .setKeepDisplayOn(true)
                            .setMaxSecond(60)
                            // Start recording
                            .record();


                });

        mPlayView = findViewById(R.id.play);
        mPlayView
                .setOnClickListener(view -> {
                    if (TextUtils.isEmpty(mVoiceUrl)) {
                        showToast("您还没有上传语音！");
                    } else {
                        if (mMediaUtil != null && mMediaUtil.isPlaying()) {
                            return;
                        }
                        mMediaUtil = new MediaUtil(getApplicationContext(), mVoiceUrl);
                        mMediaUtil.setPlayListener(new MediaUtil.PlayListener() {
                            @Override
                            public void begin(MediaPlayer media) {
                                mPlayView.setImageResource(R.mipmap.media_pause);

                                if (mTextLength.getText().toString().equals("00:00")) {
                                    float duration = media.getDuration() / 1000.f;
                                    int min = (int) (duration / 60);
                                    int sed = (int) (duration % 60);

                                    mTextLength.setText(String.format("%s:%s",
                                            String.format(Locale.CANADA, "%02d", min), String.format("%02d", sed)));
                                }
                            }

                            @Override
                            public void end(MediaPlayer media) {
                                mPlayView.setImageResource(R.mipmap.media_play);
                            }
                        });
                        mMediaUtil.start();
                    }
                });

        findViewById(R.id.scan_voice)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), VoiceListActivity.class);
                    startActivity(intent);
                });

        loadData();


    }

    private MediaUtil mMediaUtil;

    private String mVoiceUrl;

    private TextView mTextLength;

    private void saveVoice() {
        if (TextUtils.isEmpty(mVoiceUrl)) {
            showToast("您还有没上传语音！");
            return;
        }
        int index = mNiceSpinner.getSelectedIndex();
        int redNum = (int) (Float.parseFloat(list.get(index)) * 100);
        APIWrapper.updateVoice(redNum, mVoiceUrl)
                .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                    @Override
                    public void onNext(CommonData data) {
                        if (data.getErrno() == 0) {
                            showToast("成功！");
                        }
                    }
                });

    }

    private void saveVoiceAndActivity() {

        if (TextUtils.isEmpty(mVoiceUrl)) {
            showToast("您还有没上传语音！");
            return;
        }

        String textContent = mContent.getText().toString();
        String textCondition = mCondition.getText().toString();
        String awardMoney = mAwardMoney.getText().toString();
        if (TextUtils.isEmpty(textCondition)) {
            showToast("活动要求不能为空");
            return;
        }

        if (TextUtils.isEmpty(textContent)) {
            showToast("活动内容不能为空");
            return;
        }
        if (TextUtils.isEmpty(awardMoney)) {
            showToast("奖励金额不能为空");
            return;
        }

        int index = mNiceSpinner.getSelectedIndex();
        int redNum = (int) (Float.parseFloat(list.get(index)) * 100);

        APIWrapper.updateVoiceAndActivity(redNum, mVoiceUrl,
                textContent, textCondition, awardMoney)
                .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                    @Override
                    public void onNext(CommonData data) {
                        if (data.getErrno() == 0) {
                            showToast("成功");
                        } else {
                            showToast(data.getMsg());
                        }
                    }
                });


    }

    private MyShop mShop;

    private void loadData() {


        APIWrapper.getPackets()
                .subscribe(new SimpleDialogObserver<List<String>>(getActivity()) {
                    @Override
                    public void onNext(List<String> strings) {
                        list = strings;

                        List<String> dataSet = new LinkedList<>(Stream.of(list)
                                .map(s -> s = s + "元/条")
                                .collect(Collectors.toList()));

                        mNiceSpinner.attachDataSource(dataSet);
                        mNiceSpinner.setSelectedIndex(0);
                        loadShopData();
                    }
                });

    }


    private void loadShopData() {
        APIWrapper.getMyShop()
                .subscribe(new SimpleDialogObserver<CommonData<MyShop>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<MyShop> data) {
                        if (data.getErrno() == 0) {
                            mShop = data.getData();
                            int hasVoice = mShop.getHasVoice();
                            if (hasVoice == 0) {//没有语音
                                mSlideSwitch.setState(false);
                            } else {//有语音
                                if (!TextUtils.isEmpty(mShop.getVoice_url())) {
                                    mVoiceUrl = mShop.getVoice_url();
                                    setVoiceLength();
                                }

                                float redPacket = Float.parseFloat(mShop.getRed_packet_money()) / 100;
                                int index = 0;
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).equals(String.valueOf(redPacket))) {
                                        index = i;
                                        break;
                                    }
                                }
                                mNiceSpinner.setSelectedIndex(index);
                                if (mShop.getIs_award() == 0) {//没有活动
                                    mSlideSwitch.setState(false);
                                } else {//有活动
                                    mSlideSwitch.setState(true);
                                    mCondition.setText(mShop.getCondition());
                                    mContent.setText(mShop.getVoice_content());
                                    mAwardMoney.setText(String.format(Locale.CHINA, "%.2f",
                                            mShop.getAward_money() / 100.f));
                                }
                            }
                        } else {
                            showToast(data.getMsg());
                        }
                    }
                });
    }


    private void setVoiceLength() {


        FileUtils.getMediaDuration(mVoiceUrl, media -> {

            float duration = media.getDuration() / 1000.f;
            int min = (int) (duration / 60);
            int sed = (int) (duration % 60);

            mTextLength.setText(String.format("%s:%s",
                    String.format(Locale.CANADA, "%02d", min), String.format("%02d", sed)));

        });

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK) {
            mVoiceUrl = data.getStringExtra("url");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            mVoiceUrl = data.getStringExtra("url");
        }
    }
}
