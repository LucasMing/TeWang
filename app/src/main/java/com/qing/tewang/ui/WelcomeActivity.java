package com.qing.tewang.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qing.tewang.R;
import com.qing.tewang.api.APIWrapper;
import com.qing.tewang.api.exception.SimpleObserver;
import com.qing.tewang.app.MyApplication;
import com.qing.tewang.model.CommonData;
import com.qing.tewang.model.Cover;
import com.qing.tewang.util.CommonUtils;
import com.qing.tewang.util.FileUtils;
import com.qing.tewang.util.ImageUtils;
import com.qing.tewang.util.SPUtils;
import com.qing.tewang.widget.StrokeColorText;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;

public class WelcomeActivity extends BaseActivity {

    private MyHandler mHandler;
    private Timer timer, timer2;
    private StrokeColorText mHomeText, mSkipText;
    private File coverFile2;
    private ImageView image;
    private String json2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        image = findViewById(R.id.image_view);
        mHomeText = findViewById(R.id.home);
        mSkipText = findViewById(R.id.skip);
        mSkipText.setText(totalTime + " 跳过");

        mSkipText.setOnClickListener(view -> {
            if (current == 0) {
                if (isTwo) {
                    goNext();
                } else {
                    jumpActivity();
                }
            } else {
                jumpActivity();
            }
        });
        mHomeText.setOnClickListener(view -> {

        });


        File cacheDir = new File(Environment.getExternalStorageDirectory().toString() + "/" + getPackageName() + "/cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        final File coverFile = new File(cacheDir, "cover.tmp");
        coverFile2 = new File(cacheDir, "cover2.tmp");
        if (coverFile.exists()) {
            ImageUtils.loadNoHolder(getApplicationContext(), "file://" + coverFile.toString(), image);
        } else {
            image.setImageResource(R.mipmap.welcome);
        }
        final File urlFile = new File(cacheDir, "cover.urlFile");
        final File urlFile2 = new File(cacheDir, "cover2.urlFile");

        final String json = FileUtils.readToString(urlFile);
        json2 = FileUtils.readToString(urlFile2);
        if (!TextUtils.isEmpty(json)) {
            final JsonObject object;
            try {
                object = new JsonParser().parse(json).getAsJsonObject();

                image.setOnClickListener(v -> {

                    if (object == null || object.get("type") == null) {
                        return;
                    }

                    int type = object.get("type").getAsInt();
                    if (type == 1) {
                        if (object.get("voice_sn") != null &&
                                !TextUtils.isEmpty(object.get("voice_sn").getAsString())) {
                            Intent intent = new Intent(getActivity(), VoiceDetailActivity.class);
                            intent.putExtra("sn", object.get("voice_sn").getAsString());
                            startActivity(intent);
                            timer.cancel();
                        }
                    } else if (type == 2) {
                        if (object.get("title") != null && object.get("url") != null &&
                                !TextUtils.isEmpty(object.get("title").getAsString()) &&
                                !TextUtils.isEmpty(object.get("url").getAsString())) {

                            Intent intent = new Intent(getActivity(), WebActivity.class);
                            intent.putExtra("title", object.get("title").getAsString());
                            intent.putExtra("url", object.get("url").getAsString());
                            startActivity(intent);
                            timer.cancel();
                        }
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        int cityId = 310100;
        int areaId = 610630;

        APIWrapper.getStartAd(cityId, areaId)
                .subscribe(new SimpleObserver<CommonData<List<Cover>>>(getActivity()) {
                    @Override
                    public void onNext(CommonData<List<Cover>> data) {
                        if (data.getErrno() == 0 && data.getData() != null) {
                            List<Cover> covers = data.getData();
                            Cover cover = null, cover2 = null;
                            if (covers.size() > 0) {
                                cover = covers.get(0);
                            }
                            if (covers.size() > 1) {
                                cover2 = covers.get(1);
                            } else if (covers.size() == 1) {
                                isTwo = false;
                            }
                            if (cover != null) {
                                initFile(cover, json, coverFile, urlFile);
                            }
                            if (cover2 != null) {
                                initFile(cover2, json2, coverFile2, urlFile2);
                            }
                        }
                    }
                });

        mHandler = new MyHandler(this);
    }

    //是否有第二页
    private boolean isTwo = true;

    private void initFile(Cover cover, String json, File coverFile, File urlFile) {
        if (json == null) {
            if (coverFile.exists()) {
                coverFile.delete();
            }
            if (urlFile.exists()) {
                urlFile.delete();
            }
            if (cover != null) {
                APIWrapper.downloadFile(cover.getImg())
                        .subscribe(new SimpleObserver<ResponseBody>(getActivity()) {
                            @Override
                            public void onNext(ResponseBody responseBody) {
                                FileUtils.writeStringToDisk(new Gson().toJson(cover), urlFile);
                                FileUtils.writeResponseBodyToDisk(responseBody, coverFile);
                            }
                        });
            }

        } else {
            if (!json.equals(cover.toString())) {
                if (coverFile.exists()) {
                    coverFile.delete();
                }
                if (urlFile.exists()) {
                    urlFile.delete();
                }
                if (cover != null) {
                    APIWrapper.downloadFile(cover.getImg())
                            .subscribe(new SimpleObserver<ResponseBody>(getActivity()) {
                                @Override
                                public void onNext(ResponseBody responseBody) {
                                    FileUtils.writeStringToDisk(new Gson().toJson(cover), urlFile);
                                    FileUtils.writeResponseBodyToDisk(responseBody, coverFile);
                                }
                            });
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (current == 0) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mHandler != null) {
                        int t = totalTime;
                        mHandler.sendEmptyMessage(t);
                    }
                }
            }, 0, 1000);
        } else if (isTwo) {
            timer2 = new Timer();
            timer2.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(totalTime2);
                    }
                }
            }, 0, 1000);
        }

    }

    private int totalTime = 10;
    private int totalTime2 = 10;
    private int current = 0;

    private class MyHandler extends Handler {
        private WeakReference<BaseActivity> mLeakActivityRef;

        private MyHandler(BaseActivity leakActivity) {
            mLeakActivityRef = new WeakReference<>(leakActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mLeakActivityRef.get() != null) {
                if (current == 0) {
                    mSkipText.setText(totalTime + " 跳过");
                } else {
                    mSkipText.setText(totalTime2 + " 跳过");
                }

                if (msg.what == 0) {
                    //判断
                    if (current == 0) {
                        if (isTwo) {
                            goNext();
                        } else {
                            jumpActivity();
                        }
                    } else {
                        jumpActivity();
                    }
                } else {
                    if (current == 0) {
                        if (totalTime != 0) {
                            totalTime--;
                        }
                    } else {
                        totalTime2--;
                    }
                }
            } else {
                release();
            }
        }
    }

    private void goNext() {
        mSkipText.setText(totalTime2 + " 跳过");
        totalTime = 0;
        timer.cancel();
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(totalTime2);
                }
            }
        }, 0, 1000);
        current = 1;
        if (coverFile2.exists()) {
            ImageUtils.loadNoHolder(getApplicationContext(), "file://" + coverFile2.toString(), image);
        } else {
            image.setImageResource(R.mipmap.welcome);
        }

        if (!TextUtils.isEmpty(json2)) {
            final JsonObject object;
            try {
                object = new JsonParser().parse(json2).getAsJsonObject();

                image.setOnClickListener(v -> {
                    int type = object.get("type").getAsInt();
                    if (type == 1) {
                        timer2.cancel();
                        Intent intent = new Intent(getActivity(), VoiceDetailActivity.class);
                        intent.putExtra("sn", object.get("voice_sn").getAsString());
                        startActivity(intent);
                    } else if (type == 2) {
                        timer2.cancel();
                        Intent intent = new Intent(getActivity(), WebActivity.class);
                        intent.putExtra("title", object.get("title").getAsString());
                        intent.putExtra("url", object.get("url").getAsString());
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            image.setOnClickListener(null);
        }
    }

    public void jumpActivity() {
        release();

        if (!SPUtils.get(MyApplication.getInstance(), "save_version", "")
                .equals(CommonUtils.getVersion(MyApplication.getInstance()))) {
            //SPUtils.clearUser(getApplicationContext());
            SPUtils.put(getApplicationContext(), "save_version", CommonUtils.getVersion(MyApplication.getInstance()));
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void release() {
        if (timer != null) {
            timer.cancel();
        }
        if (timer2 != null) {
            timer2.cancel();
        }
        if (mHandler != null) {
            mHandler.removeMessages(totalTime);
            mHandler = null;
        }
    }
}
