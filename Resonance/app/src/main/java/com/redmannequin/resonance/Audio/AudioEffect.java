package com.redmannequin.resonance.Audio;

import com.redmannequin.resonance.Backend.Track;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.writer.WriterProcessor;

public class AudioEffect {

    private Track track;
    private TarsosDSPAudioFormat audioFormat;
    private UniversalAudioInputStream audioInputStream;
    private AudioDispatcher dispatcher;

    public AudioEffect() {
        track = null;
        audioFormat = null;
        audioInputStream = null;
        dispatcher = null;
    }

    public void init(Track track) {
        this.track = track;
        audioFormat = new TarsosDSPAudioFormat(track.getSampleRate(), 16, 1, true, false);
    }

    public void DelayEffect(double length, double decay) {
        try {
            String path = track.getPath() +  File.separator + track.getName() + ".pcm";
            String newath = track.getPath() + File.separator + track.getName() +  "_delay.wav";
            audioInputStream = new UniversalAudioInputStream(new FileInputStream(path), audioFormat);
            dispatcher = new AudioDispatcher(audioInputStream, 1024, 0);
            DelayEffect delayEffect = new DelayEffect(length, decay, Config.FREQUENCY);
            dispatcher.addAudioProcessor(delayEffect);
            RandomAccessFile audio = new RandomAccessFile(newath, "rw");
            audio.setLength(0);
            dispatcher.addAudioProcessor(new WriterProcessor(new TarsosDSPAudioFormat(track.getSampleRate(), 16, 2, true, false), audio));
            dispatcher.run();
            dispatcher.stop();
            dispatcher.removeAudioProcessor(delayEffect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
