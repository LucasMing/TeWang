package com.qing.tewang.recorder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.audiovisualization.GLAudioVisualizationView;
import com.google.gson.JsonObject;
import com.lzx.musiclibrary.manager.MusicManager;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleDialogObserver;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.ui.BaseActivity;
import com.qing.tewang.ui.VoiceDetailActivity;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.LogUtils;
import com.qing.tewang.util.MediaPlayUtils;
import com.qing.tewang.util.record.AudioChunk;
import com.qing.tewang.util.record.AudioRecorder;
import com.qing.tewang.util.record.RecordStreamListener;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;


public class AudioRecorderActivity extends BaseActivity
        implements MediaPlayer.OnCompletionListener,EasyPermissions.PermissionCallbacks {

    private String filePath;

    private int color;
    private boolean autoStart;
    private boolean keepDisplayOn;


    private VisualizerHandler visualizerHandler;

    private Timer timer;
    private int recorderSecondsElapsed;
    private int playerSecondsElapsed;
    private boolean isRecording;
    private int maxSecond;
    private String condition;
    private String content;

    private RelativeLayout contentLayout;
    private GLAudioVisualizationView visualizerView;
    private TextView statusView;
    private TextView timerView;
    private ImageButton restartView;
    private ImageButton recordView;
    private ImageButton playView;
    private AudioRecorder mAudioRecorder;
    private TextView conditionView, voiceContent;
    private String sn;
    private ImageView avatar;
    private String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aar_activity_audio_recorder);
        if (MusicManager.get().isPlaying()) {
            MusicManager.get().stopMusic();
        }

        //检查录音权限
        checkPerm();



        if (savedInstanceState != null) {
            filePath = savedInstanceState.getString(AndroidAudioRecorder.EXTRA_FILE_PATH);
            color = savedInstanceState.getInt(AndroidAudioRecorder.EXTRA_COLOR);
            autoStart = savedInstanceState.getBoolean(AndroidAudioRecorder.EXTRA_AUTO_START);
            keepDisplayOn = savedInstanceState.getBoolean(AndroidAudioRecorder.EXTRA_KEEP_DISPLAY_ON);
            condition = savedInstanceState.getString("condition");
            content = savedInstanceState.getString("content");
            maxSecond = savedInstanceState.getInt("maxSecond", 60);
            sn = savedInstanceState.getString("sn");
            avatarUrl = savedInstanceState.getString("avatarUrl");
        } else {
            filePath = getIntent().getStringExtra(AndroidAudioRecorder.EXTRA_FILE_PATH);
            color = getIntent().getIntExtra(AndroidAudioRecorder.EXTRA_COLOR, Color.BLACK);
            autoStart = getIntent().getBooleanExtra(AndroidAudioRecorder.EXTRA_AUTO_START, false);
            keepDisplayOn = getIntent().getBooleanExtra(AndroidAudioRecorder.EXTRA_KEEP_DISPLAY_ON, false);
            condition = getIntent().getStringExtra("condition");
            content = getIntent().getStringExtra("content");
            maxSecond = getIntent().getIntExtra("maxSecond", 60);
            sn = getIntent().getStringExtra("sn");
            avatarUrl = getIntent().getStringExtra("avatarUrl");
        }

        if (keepDisplayOn) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Util.getDarkerColor(color)));
            getSupportActionBar().setHomeAsUpIndicator(
                    ContextCompat.getDrawable(this, R.mipmap.aar_ic_clear));
        }

        visualizerView = new GLAudioVisualizationView.Builder(this)
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(R.dimen.aar_wave_height)
                .setWavesFooterHeight(R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(color))
                .setLayerColors(new int[]{color})
                .build();

        contentLayout = findViewById(R.id.content);
        statusView = findViewById(R.id.status);
        timerView = findViewById(R.id.timer);
        restartView = findViewById(R.id.restart);
        recordView = findViewById(R.id.record);
        playView = findViewById(R.id.play);
        conditionView = findViewById(R.id.condition);
        voiceContent = findViewById(R.id.voice_content);
        avatar = findViewById(R.id.avatar);

        if (TextUtils.isEmpty(condition) && TextUtils.isEmpty(content)) {
            findViewById(R.id.scroll_view).setVisibility(View.GONE);
        } else {
            conditionView.setText(condition);
            voiceContent.setText(content);
            ImageUtils.load(getApplicationContext(), avatarUrl, avatar);
            avatar.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), VoiceDetailActivity.class);
                intent.putExtra("sn", sn);
                startActivity(intent);
            });
        }


        contentLayout.setBackgroundColor(Util.getDarkerColor(color));
        contentLayout.addView(visualizerView, 0);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);

        if (Util.isBrightColor(color)) {
            ContextCompat.getDrawable(this, R.mipmap.aar_ic_clear)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            ContextCompat.getDrawable(this, R.mipmap.aar_ic_check)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            statusView.setTextColor(Color.BLACK);
            timerView.setTextColor(Color.BLACK);
            restartView.setColorFilter(Color.BLACK);
            recordView.setColorFilter(Color.BLACK);
            playView.setColorFilter(Color.BLACK);
        }

        findViewById(R.id.right_tv)
                .setOnClickListener(view -> {
                    if (timerView.getText().equals("00:00:00")) {
                        showToast("您还没有录制！");
                        return;
                    }
                    //判断是否录制了
                    stopRecording();

                    File file = new File(mFilePath);
                    if (!file.exists()) {
                        if (mAudioRecorder != null) {
                            mFilePath = mAudioRecorder.createWav();
                        }
                    }
                    if (!TextUtils.isEmpty(sn)) {
                        APIWrapper.uploadFile(mFilePath, sn)
                                .subscribe(new SimpleDialogObserver<CommonData>(getActivity()) {
                                    @Override
                                    public void onNext(CommonData data) {
                                        if (data.getErrno() == 0) {
                                            showToast("上传成功！");
                                            file.delete();
                                            release();
                                            finish();
                                        } else {
                                            showToast(data.getMsg());
                                        }
                                    }
                                });
                    } else {
                        APIWrapper.updateWavFile(mFilePath)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SimpleDialogObserver<CommonData<JsonObject>>(getActivity()) {
                                    @Override
                                    public void onNext(CommonData<JsonObject> data) {
                                        if (data.getErrno() == 0) {
                                            file.delete();
                                            showToast("上传成功！");
                                            release();
                                            String url = data.getData().get("url").getAsString();
                                            Intent intent = new Intent();
                                            intent.putExtra("url", url);
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        } else {
                                            showToast(data.getMsg());
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        super.onError(t);

                                    }
                                });
                    }


                });

        findViewById(R.id.left_iv)
                .setOnClickListener(view -> {
                    finish();
                });


    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (autoStart && !isRecording) {
            toggleRecording(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            visualizerView.onResume();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
//        restartRecording(null);
        pauseRecording();
        try {
            visualizerView.onPause();
        } catch (Exception e) {
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        restartRecording(null);
        setResult(RESULT_CANCELED);
        MediaPlayUtils.getInstance().release();
        try {
            visualizerView.release();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(AndroidAudioRecorder.EXTRA_FILE_PATH, filePath);
        outState.putInt(AndroidAudioRecorder.EXTRA_COLOR, color);
        outState.putString("condition", condition);
        outState.putInt("maxSecond", maxSecond);

        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlaying();
    }

    private void selectAudio() {
        stopRecording();
        setResult(RESULT_OK);
        finish();
    }

    public void toggleRecording(View v) {
        stopPlaying();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    pauseRecording();
                } else {
                    resumeRecording();
                }
            }
        });
    }

    private String mFilePath = "";

    public void togglePlaying(View v) {
        pauseRecording();

        if (mAudioRecorder != null) {
            mFilePath = mAudioRecorder.createWav();
        }
        final String tempUrl = mFilePath;
        if (new File(mFilePath).exists()) {
            Util.wait(100, () -> {
                if (MediaPlayUtils.getInstance().isPlaying()) {
                    stopPlaying();
                } else {
                    startPlaying(tempUrl);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "您还没有录音", Toast.LENGTH_SHORT).show();
        }
    }

    public void restartRecording(View v) {
        if (isRecording) {
            stopRecording();
        } else if (isPlaying()) {
            stopPlaying();
        } else {
            visualizerHandler = new VisualizerHandler();
            visualizerView.linkTo(visualizerHandler);
            visualizerView.release();
            if (visualizerHandler != null) {
                visualizerHandler.stop();
            }
        }
        statusView.setVisibility(View.INVISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.mipmap.aar_ic_rec);
        timerView.setText("00:00:00");
        recorderSecondsElapsed = 0;
        playerSecondsElapsed = 0;

        if (mAudioRecorder != null) {
            mAudioRecorder.cancel();
            mAudioRecorder.clearFile();
            mAudioRecorder = null;
        }

    }

    private void resumeRecording() {
        isRecording = true;
        statusView.setText(R.string.aar_recording);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.mipmap.aar_ic_pause);
        playView.setImageResource(R.mipmap.aar_ic_play);

        visualizerHandler = new VisualizerHandler();
        visualizerView.linkTo(visualizerHandler);

        if (mAudioRecorder == null) {
            timerView.setText("00:00:00");

            mAudioRecorder = new AudioRecorder();
            mAudioRecorder.createDefaultAudio(filePath);
            mAudioRecorder.setListener(new RecordStreamListener() {
                @Override
                public void onRecording(byte[] audioData, int i, int length, AudioChunk audioChunk) {

                    float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
                    if (visualizerHandler != null) {
                        visualizerHandler.onDataReceived(amplitude);
                        LogUtils.e("jiang", ".........");
                    }
                }

                @Override
                public void finishRecord() {

                }
            });
        }

        mAudioRecorder.startRecord();

        startTimer();
    }

    private void pauseRecording() {
        isRecording = false;

        statusView.setText(R.string.aar_paused);
        statusView.setVisibility(View.VISIBLE);
        restartView.setVisibility(View.VISIBLE);
        playView.setVisibility(View.VISIBLE);
        recordView.setImageResource(R.mipmap.aar_ic_rec);
        playView.setImageResource(R.mipmap.aar_ic_play);

        if (mAudioRecorder != null) {
            if (mAudioRecorder.getStatus() == AudioRecorder.Status.STATUS_START) {
                mAudioRecorder.pauseRecord();
            }
        }

        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        stopTimer();
    }

    private void stopRecording() {
        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        recorderSecondsElapsed = 0;

        stopTimer();
    }

    private void release() {
        if (mAudioRecorder != null) {
            mAudioRecorder.stopRecord();
            mAudioRecorder = null;
        }
    }

    private void startPlaying(String tempUrl) {
        try {
//            MediaPlayUtils.getInstance().setPlayListener(media ->
//                    visualizerView.linkTo(DbmHandler.Factory.newVisualizerHandler(getApplicationContext(),
//                            media)));

            MediaPlayUtils.getInstance().play(tempUrl, m -> {
                stopPlaying();
            });

            timerView.setText("00:00:00");
            statusView.setText(R.string.aar_playing);
            statusView.setVisibility(View.VISIBLE);
            playView.setImageResource(R.mipmap.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        statusView.setText("");
        statusView.setVisibility(View.INVISIBLE);
        playView.setImageResource(R.mipmap.aar_ic_play);

        visualizerView.release();
        if (visualizerHandler != null) {
            visualizerHandler.stop();
        }

        MediaPlayUtils.getInstance().stop();

        stopTimer();
    }

    private boolean isPlaying() {
        return MediaPlayUtils.getInstance().isPlaying();
    }

    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    recorderSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(recorderSecondsElapsed));
                } else if (isPlaying()) {
                    playerSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(playerSecondsElapsed));
                }
            }
        });
    }


    public void checkPerm() {

        String[] params = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};


        if (!EasyPermissions.hasPermissions(this, params)) {
            EasyPermissions.requestPermissions(this, "申请运行所必须的权限！", 101, params);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {

            new AlertDialog.Builder(getActivity(), R.style.ColorDialog)
                    .setTitle("权限申请")
                    .setMessage("申请运行所必须的权限！")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                    .create().show();

        } else {
            finish();
        }
    }

}
