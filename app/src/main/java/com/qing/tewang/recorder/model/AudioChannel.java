package com.qing.tewang.recorder.model;

import android.media.AudioFormat;

public enum AudioChannel {
    STEREO,
    CHANNEL_IN_MONO,
    MONO;

    public int getChannel(){
        switch (this){
            case MONO:
                return AudioFormat.CHANNEL_IN_MONO;
            case CHANNEL_IN_MONO:
                return AudioFormat.CHANNEL_IN_MONO;
            default:
                return AudioFormat.CHANNEL_IN_STEREO;
        }
    }
}