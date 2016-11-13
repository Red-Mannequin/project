package com.redmannequin.resonance.Audio;

import android.media.AudioRecord;
import android.media.AudioTrack;

public class Player {
    private AudioTrack audioTrack;

    private int freq;
    private int channel;
    private int format;
    private int output;
    private int mode;
    private int bufferSize;
    private int bufferShortSize;
    private short[] buffer;

    public Player() {
        freq    = 0;
        channel = 0;
        format  = 0;
        output  = 0;
        mode    = 0;
    }

    public void init() {
        freq    = Config.FREQUENCY;
        channel = Config.CHANNEL;
        format  = Config.FORMAT;
        output  = Config.OUTPUT;
        mode    = Config.MODE;
        bufferSize = AudioRecord.getMinBufferSize(freq, channel, format);
        bufferShortSize = bufferSize/2;
        audioTrack = new AudioTrack(output, freq, channel, format, bufferSize, mode);
        audioTrack.setPlaybackRate(freq);
        buffer = new short[bufferShortSize];

    }

    public void start() {
        audioTrack.play();
    }

    public void write(short[] b) {
        System.arraycopy(b, 0, buffer, 0, b.length);
        audioTrack.write(buffer, 0, buffer.length);
    }

    public void write(byte[] b) {
        byte buffer[] = new byte[b.length];
        System.arraycopy(b, 0, buffer, 0, b.length);
        audioTrack.write(buffer, 0, buffer.length);
    }

    public void release() {
        audioTrack.stop();
        audioTrack.flush();
        audioTrack.release();
    }

    public byte[] getEmptyByteBuffer() {
        return new byte[bufferSize];
    }
}
