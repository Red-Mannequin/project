package com.redmannequin.resonance.Audio;

import android.media.audiofx.AudioEffect;
import android.util.Log;
import android.widget.TextView;

import com.redmannequin.resonance.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MediaPlayer {

    private RandomAccessFile randomAccessFile;
    private Thread thread;
    private Player player;

    private byte buffer[];

    private boolean isPlaying;
    private boolean hasEnded;

    //TarsosDSP
    private AudioDispatcher adp;
    private PitchDetectionHandler pdh;
    private UniversalAudioInputStream uis;
    private TarsosDSPAudioFormat af;

    FileInputStream fs;

    public MediaPlayer() {
        randomAccessFile = null;
        thread = null;
        player = null;
        buffer = null;
        isPlaying = false;
        hasEnded = false;
        adp = null;
        pdh = null;
    }

    // audio playback settings
    public void init(String path) {
        player = new Player();
        player.init();
        isPlaying = false;
        hasEnded = true;

        try{
            randomAccessFile = new RandomAccessFile(path, "r");
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            fs = new FileInputStream(path);
            af = new TarsosDSPAudioFormat(Config.FREQUENCY, 16, 2, true, true);
            uis = new UniversalAudioInputStream(fs, af);
            adp = new AudioDispatcher(uis, 1024, 1024-32);
            PitchShifter ps = new PitchShifter(adp, 1.35, Config.FREQUENCY, 1024, 1024-32);
            adp.addAudioProcessor(ps);
            adp.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void play() {
        player.start();
        isPlaying = true;
        if(hasEnded) {
            try {
                randomAccessFile.seek(0);
                hasEnded = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPlaying && !hasEnded) {
                    buffer = player.getEmptyByteBuffer();
                    try {
                        if (randomAccessFile.read(buffer, 0, buffer.length) == -1) {
                            hasEnded = true;
                            isPlaying = false;
                        }
                        player.write(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void pause() {
        isPlaying = false;
    }

    public void stop() {
        isPlaying = false;
        hasEnded = true;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        try {
            adp.stop();
            player.release();
            fs.close();
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

