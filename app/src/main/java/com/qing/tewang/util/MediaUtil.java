package com.qing.tewang.util;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

public class MediaUtil {
    private String url;
    private MediaPlayer mMediaPlayer;
    private boolean hasPlayed;

    public boolean isHasPlayed() {
        return hasPlayed;
    }

    public void setHasPlayed(boolean hasPlayed) {
        this.hasPlayed = hasPlayed;
    }

    public MediaUtil(Context context, String url) {
        mMediaPlayer = new MediaPlayer();
        this.url = url;
    }

    public void start() {
        if (hasPlayed) {
            resume();
        } else {
            try {
                mMediaPlayer.setDataSource(url);
            } catch (IOException e) {
                mMediaPlayer.stop();
                return;
            }
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    hasPlayed = true;
                    mp.start();
                    LogUtils.e("jiang...", "start");
                    if (mPlayListener != null) {
                        mPlayListener.begin(mMediaPlayer);
                    }
                }
            });
            mMediaPlayer.setOnCompletionListener(media -> {
                if (mPlayListener != null) {
                    mPlayListener.end(mMediaPlayer);
                }
                stop();
                mMediaPlayer.reset();
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    stop();
                    return false;
                }
            });
        }
    }

    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        hasPlayed = false;
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    private void resume() {
        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
    }

    private PlayListener mPlayListener;

    public void setPlayListener(PlayListener mPlayListener) {
        this.mPlayListener = mPlayListener;
    }

    public interface PlayListener {
        public void begin(MediaPlayer media);

        public void end(MediaPlayer media);
    }


    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

}
