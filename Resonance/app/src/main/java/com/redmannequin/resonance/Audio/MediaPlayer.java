package com.redmannequin.resonance.Audio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.effects.FlangerEffect;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.writer.WriterProcessor;

public class MediaPlayer {

    private RandomAccessFile randomAccessFile;
    private Thread thread;
    private Player player;

    private byte buffer[];

    private boolean isPlaying;
    private boolean hasEnded;

    //TarsosDSP
    private AudioDispatcher adp;
    private UniversalAudioInputStream uis;
    private TarsosDSPAudioFormat af;

    public MediaPlayer() {
        randomAccessFile = null;
        thread = null;
        player = null;
        buffer = null;
        isPlaying = false;
        hasEnded = false;
        adp = null;
    }

    // audio playback settings
    public void init(String path) {
        player = new Player();
        player.init();
        isPlaying = false;
        hasEnded = true;

        try {
            af = new TarsosDSPAudioFormat(Config.FREQUENCY, 16, 1, true, false);
            uis = new UniversalAudioInputStream(new FileInputStream(path), af);
            adp = new AudioDispatcher(uis, 1024, 0);
            FlangerEffect fe = new FlangerEffect(1, 0.5, Config.FREQUENCY, 800);
            DelayEffect de = new DelayEffect(1, 0.5, Config.FREQUENCY);
            PitchShifter ps = new PitchShifter(adp, 1.35, Config.FREQUENCY, 1024, 0);
            adp.addAudioProcessor(de);
            RandomAccessFile raf = new RandomAccessFile(path+".wav", "rw");
            raf.setLength(0);
            adp.addAudioProcessor(new WriterProcessor(new TarsosDSPAudioFormat(Config.FREQUENCY, 16, 2, true, false), raf));
            adp.run();
            adp.stop();
            randomAccessFile = new RandomAccessFile(path+".wav", "r");

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
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

