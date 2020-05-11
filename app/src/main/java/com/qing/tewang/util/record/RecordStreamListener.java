package com.qing.tewang.util.record;

public interface RecordStreamListener {
    void onRecording(byte[] audioData, int i, int length,AudioChunk audioChunk);

    void finishRecord();
}
