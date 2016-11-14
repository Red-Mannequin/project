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
    private int bufferShortSize;
    //private byte[] buffer;
    private short[] buffer;

    public void Record() {
        freq    = 0;
        channel = 0;
        format  = 0;
        source  = 0;
    }

    public void init(){
        freq = Config.FREQUENCY;
        channel = Config.CHANNEL_IN;
        format = Config.FORMAT;
        source = Config.INPUT;

        bufferSize = AudioRecord.getMinBufferSize(freq, channel, format);
        bufferShortSize = bufferSize/2;
        buffer = new short[bufferShortSize];
        audioRecord = new AudioRecord(source, freq, channel, format, bufferSize);
    }

    public void start() {
        audioRecord.startRecording();
    }

    public short[] read() {
        audioRecord.read(buffer, 0, bufferShortSize);
        return buffer;
    }

    public void stop() {
        audioRecord.stop();
    }

    public void release() {
        audioRecord.stop();
        audioRecord.release();
    }

    public byte[] getEmptyByteBuffer() {
        return new byte[bufferSize];
    }
}
