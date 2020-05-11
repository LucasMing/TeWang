package com.qing.tewang.ui;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.PayResult;
import com.qing.tewang.model.RecorderResult;
import com.qing.tewang.model.ShopDetail;
import com.qing.tewang.model.User;
import com.qing.tewang.model.Voice;
import com.qing.tewang.recorder.AndroidAudioRecorder;
import com.qing.tewang.recorder.model.AudioChannel;
import com.qing.tewang.recorder.model.AudioSampleRate;
import com.qing.tewang.recorder.model.AudioSource;
import com.qing.tewang.util.BannerImageUtils;
import com.qing.tewang.util.DisplayUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.util.MediaUtil;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.ProgressView;
import com.qing.tewang.widget.StrokeColorText;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class VoiceDetailActivity extends BaseActivity {

    private MediaUtil mMediaUtil;
    private TextView shopName, address, redPacket, shopText, shopContent;
    private ProgressView mediaView;
    private ImageView likeView, wechat, alipay;
    private Banner banner;
    private TextView content, contentLast, awardMoney, condition, contact;
    private View record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        Voice voice = getIntent().getParcelableExtra("voice");

        banner = findViewById(R.id.banner);
        banner.setImageLoader(new BannerImageUtils());
        banner.setOnBannerListener(position -> {

            if (mData != null && mData.getImg() != null) {
                ArrayList<String> imgs = new ArrayList<>(mData.getImg());
                Intent intent = new Intent(getApplicationContext(), PhotoViewActivity.class);
                intent.putExtra("urls", imgs);
                intent.putExtra("position", position);
                startActivity(intent);
            }

        });


        shopName = findViewById(R.id.name);
        address = findViewById(R.id.address);
        redPacket = findViewById(R.id.red_packet);
        mediaView = findViewById(R.id.progress_view);
        likeView = findViewById(R.id.title_like);
        shopText = findViewById(R.id.text);
        content = findViewById(R.id.content);
        contentLast = findViewById(R.id.content_last);
        awardMoney = findViewById(R.id.award_money);
        condition = findViewById(R.id.condition);
        shopContent = findViewById(R.id.shop_content);
        record = findViewById(R.id.record);
        contact = findViewById(R.id.contact);
        alipay = findViewById(R.id.alipay);
        wechat = findViewById(R.id.wechat);


        findViewById(R.id.title_back)
                .setOnClickListener(view -> finish());


        if (voice != null) {
            loadVoiceById(voice.getVoice_sn());
        } else {
            SongInfo songInfo = getIntent().getParcelableExtra("songInfo");
            if (songInfo != null) {
                loadVoiceById(songInfo.getSongId());
            } else {
                String sn = getIntent().getStringExtra("sn");
                if (!TextUtils.isEmpty(sn)) {
                    loadVoiceById(sn);
                }
            }
        }

        findViewById(R.id.comment)
                .setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                    intent.putExtra("shopId", mData.getShop_id());
                    startActivity(intent);
                });


    }

    private void setData(Voice voice) {
        if (voice.getImg() != null) {
            banner.setImages(voice.getImg()).start();
        }
        shopName.setText(voice.getShop_name());
        address.setText(voice.getAddress());
        redPacket.setText(String.format(Locale.CHINA, "收听可领取%.2f元红包", Float.parseFloat(voice.getRed_packet_money()) / 100));
        shopText.setText(voice.getDescription());
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


        alipay.setOnClickListener(v -> {
            ArrayList<String> imgs = new ArrayList<>(Arrays.asList(mAlipayPath));
            Intent intent = new Intent(getApplicationContext(), PhotoViewActivity.class);
            intent.putExtra("urls", imgs);
            intent.putExtra("position", 0);
            startActivity(intent);
        });

        wechat.setOnClickListener(v -> {
            ArrayList<String> imgs = new ArrayList<>(Arrays.asList(mWechatPath));
            Intent intent = new Intent(getApplicationContext(), PhotoViewActivity.class);
            intent.putExtra("urls", imgs);
            intent.putExtra("position", 0);
            startActivity(intent);
        });

    }

    private String mAlipayPath, mWechatPath;

    private Voice mData;

    private void loadVoiceById(String sn) {
        APIWrapper.getVoiceById(sn)
                .subscribe(new SimpleDialogObserver<CommonData<ShopDetail>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<ShopDetail> data) {
                        if (data.getErrno() == 0) {

                            mData = data.getData().getVoice();

                            Voice voice = data.getData().getVoice();
                            setData(voice);

                            address.setOnLongClickListener(v -> {
                                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("tewang", address.getText().toString());
                                myClipboard.setPrimaryClip(clipData);
                                showToast("复制成功！");
                                return true;
                            });

                            ShopDetail shopDetail = data.getData();
                            shopContent.setText(shopDetail.getShop_content());

                            if (TextUtils.isEmpty(data.getData().getContact())) {
                                findViewById(R.id.contact_view).setVisibility(View.GONE);
                            } else {
                                contact.setText(data.getData().getContact());
                                contact.setOnClickListener(view -> {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri uri = Uri.parse("tel:" + data.getData().getContact());
                                    intent.setData(uri);
                                    startActivity(intent);
                                });
                            }


                            PayResult payInfo = shopDetail.getExt();
                            if (payInfo != null) {
                                if (!TextUtils.isEmpty(payInfo.getAlipay())) {
                                    mAlipayPath = payInfo.getAlipay();
                                    findViewById(R.id.ali_layout)
                                            .setVisibility(View.VISIBLE);
                                    ImageUtils.load(getApplicationContext(),
                                            payInfo.getAlipay(), alipay);
                                }

                                if (!TextUtils.isEmpty(payInfo.getWechat())) {
                                    findViewById(R.id.wechat_layout)
                                            .setVisibility(View.VISIBLE);
                                    mWechatPath = payInfo.getWechat();
                                    ImageUtils.load(getApplicationContext(),
                                            payInfo.getWechat(), wechat);
                                }
                            }


                            if (shopDetail.getIs_award() == 0) {
                                findViewById(R.id.activity).setVisibility(View.GONE);
                                record.setVisibility(View.GONE);
                            } else {
                                condition.setText(shopDetail.getCondition());

                                String conStr = shopDetail.getContent();
                                int width = content.getWidth();
                                if (content.getPaint().measureText(conStr) < width) {//一行能显示
                                    contentLast.setVisibility(View.GONE);
                                    content.setText(conStr);
                                } else {
                                    contentLast.setVisibility(View.VISIBLE);
                                    int size = conStr.length();
                                    int index = 0;
                                    for (int i = 5; i < size; i++) {
                                        index = i;
                                        if (content.getPaint().measureText(conStr, 0, i) > width) {
                                            break;
                                        }
                                    }
                                    content.setText(conStr.substring(0, index - 1));
                                    contentLast.setText(conStr.substring(index - 1));
                                }

                                if (!TextUtils.isEmpty(shopDetail.getAward_money())) {
                                    awardMoney.setText(String.format(Locale.CHINA, "红包%.2f元",
                                            Float.parseFloat(shopDetail.getAward_money()) / 100));
                                }


                                record.setOnClickListener(view -> {


                                    if (SPUtils.isLogin(getApplicationContext())) {
                                        int requestCode = 0;
                                        String filePath = "audio";
                                        int color = getResources().getColor(R.color.main_orange);
                                        AndroidAudioRecorder.with(getActivity())
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
                                                .setCondition(shopDetail.getCondition())
                                                .setMaxSecond(60)
                                                .setSn(shopDetail.getVoice_sn())
                                                .setContent(shopDetail.getContent())
                                                .setAvatarUrl(shopDetail.getShareImage())
                                                // Start recording
                                                .record();
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                    }

                                });


                            }
                        } else {
                            showToast(data.getMsg());
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
                if (MusicManager.get().isPlaying()) {
                    MusicManager.get().stopMusic();
                }
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

                    StrokeColorText getMoney = viewDialog.findViewById(R.id.get_money);


                    if (SPUtils.isLogin(getApplicationContext())) {
                        getMoney.setText("点击领取");
                        getMoney.setOnClickListener(view -> {
                            APIWrapper.getMoney(voice.getVoice_sn())
                                    .subscribe(new SimpleDialogObserver<CommonData<JsonObject>>(getActivity()) {
                                        @Override
                                        public void onNext(CommonData<JsonObject> data) {
                                            mDialog.dismiss();
                                            if (data.getErrno() == 0) {
                                                Toast.makeText(getApplicationContext(), "恭喜获得红包！", Toast.LENGTH_SHORT).show();

                                                JsonObject json = data.getData();
                                                int lineBalance = Integer.parseInt(json.get("balance").getAsString());
                                                int redPacketNum = Integer.parseInt(json.get("red_packet_num").getAsString());

                                                User user = SPUtils.getUser(getApplicationContext());
                                                user.setBalance(lineBalance + "");
                                                user.setRed_packet_num(redPacketNum);
                                                SPUtils.clearUser(getApplicationContext());
                                                SPUtils.saveUser(getApplicationContext(), user);

                                            } else {
                                                if (!TextUtils.isEmpty(data.getMsg()) && getApplication() != null) {
                                                    Toast.makeText(getApplicationContext(), data.getMsg(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable t) {
                                            super.onError(t);
                                        }
                                    });
                        });
                    } else {
                        getMoney.setText("登录领取");
                        getMoney.setOnClickListener(view -> {
                            mDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        });
                    }

                    viewDialog.findViewById(R.id.close)
                            .setOnClickListener(view -> mDialog.dismiss());

                    mDialog = DisplayUtils.getInstance()
                            .getCenterDialog(getActivity(), viewDialog);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaUtil != null) {
            mMediaUtil.release();
        }
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaUtil != null) {
            if (mediaView.isPlaying()) {//暂停
                mMediaUtil.pause();
                mediaView.setPlaying(false);
                mediaView.pauseProgress();
            }
        }
    }
}
