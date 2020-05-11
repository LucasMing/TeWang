package com.qing.tewang.util;

import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.IOException;

public class MediaPlayUtils {

    private volatile static MediaPlayUtils sInstance;

    private MediaPlayer mMediaPlayer;
    private String mUrl;

    private MediaPlayUtils() {
        mMediaPlayer = new MediaPlayer();
    }

    public static MediaPlayUtils getInstance() {
        if (sInstance == null) {
            synchronized (MediaPlayUtils.class) {
                if (sInstance == null) {
                    sInstance = new MediaPlayUtils();
                }
            }
        }
        return sInstance;
    }

    public void play(String url, MediaPlayer.OnCompletionListener listener) {

        if (mMediaPlayer.isPlaying()) {
            if (TextUtils.isEmpty(url) || url.equals(mUrl)) {
                return;
            } else {
                mMediaPlayer.stop();
            }
            player(url, listener);
        } else {
            if (url.equals(mUrl)) {
                mMediaPlayer.start();
            } else {
                player(url, listener);
            }

        }
    }

    private void player(String url, MediaPlayer.OnCompletionListener listener) {
        try {
            mUrl = url;
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(media -> {
                media.start();
                if (mPlayStartListener != null) {
                    mPlayStartListener.begin(media);
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    stop();
                    LogUtils.e("jiang...", "error...error");
                    return false;
                }
            });
            mMediaPlayer.setOnCompletionListener(listener);
            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    float position = mp.getCurrentPosition() * 100;
                    int buffer = Math.round(position / mp.getDuration());

                    if (percent >= buffer) {
                        System.out.println("播放");
//                        if(!mp.isPlaying()){
//                            mp.start();
//                        }
                    } else {
                        System.out.println("缓冲");
//                        mp.pause();
                    }

                    System.out.println(buffer + "..." + percent + "..." + mp.getDuration());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void play(String url) {
        play(url, m -> stop());
    }


    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mUrl = null;
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void release() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        mUrl = null;
        mMediaPlayer = null;
        sInstance = null;
    }


    public boolean isPlaying(String url) {
        if (url.equals(mUrl) && mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public boolean emptyUrl(String url) {
        return url.equals(mUrl);
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    private PlayStartListener mPlayStartListener;

    public void setPlayStartListener(PlayStartListener mPlayStartListener) {
        this.mPlayStartListener = mPlayStartListener;
    }

    public interface PlayStartListener {
        public void begin(MediaPlayer media);
    }


}
