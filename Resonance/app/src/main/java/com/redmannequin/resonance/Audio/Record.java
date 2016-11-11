package com.redmannequin.resonance.Audio;

import android.media.AudioRecord;
import android.webkit.CookieManager;

public class Record {

    private AudioRecord audioRecord;
    private int freq;
    private int channel;
    private int format;
    private int source;
    private int bufferSize;
    private byte[] buffer;

    public void Record() {
        freq    = 0;
        channel = 0;
        format  = 0;
        source  = 0;
    }

    public void init(){
        freq = Config.FREQUENCY;
        channel = Config.CHANNEL;
        format = Config.FORMAT;
        source = Config.INPUT;

        bufferSize = AudioRecord.getMinBufferSize(freq, channel, format);
        buffer = new byte[bufferSize];
        audioRecord = new AudioRecord(source, freq, channel, format, bufferSize);
    }

    public void start() {
        audioRecord.startRecording();
    }

    public byte[] read() {
        audioRecord.read(buffer, 0, bufferSize);
        return buffer;
    }

    public void release() {
        audioRecord.stop();
        audioRecord.release();
    }
}
