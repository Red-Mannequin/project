package com.redmannequin.resonance.Audio;

import com.redmannequin.resonance.Backend.Track;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.writer.WriterProcessor;

public class AudioEffect {

    private Track track;
    private TarsosDSPAudioFormat audioFormat;
    private UniversalAudioInputStream audioInputStream;
    private AudioDispatcher dispatcher;

    private RandomAccessFile source;
    private String newPath;

    private ArrayList<AudioProcessor> processors;

    public AudioEffect(Track track) {
        this.track = track;
        audioFormat = null;
        audioInputStream = null;
        dispatcher = null;
        processors = new ArrayList<>();
    }

    public void init() {
        audioFormat = new TarsosDSPAudioFormat(track.getSampleRate(), 16, 1, true, false);
        String path = track.getPath() + File.separator + track.getName() + ".pcm";
        newPath = track.getPath() + File.separator + track.getName() + "_final.wav";
        try {
            source = new RandomAccessFile(path, "rw");
            audioInputStream = new UniversalAudioInputStream(new FileInputStream(path), audioFormat);
            dispatcher = new AudioDispatcher(audioInputStream, 1024, 0);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void addDelayEffect(double length, double decay) {
        try {
            source.seek(source.length());
            int b = (int)((source.length()/(length*Config.FREQUENCY*2))*decay);
            source.write(new byte[(int)(b+length+(length*decay)+1)*Config.FREQUENCY*2]);
            DelayEffect delayEffect = new DelayEffect(length, decay, Config.FREQUENCY);
            processors.add(delayEffect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void make() {
        try {
            RandomAccessFile audio = new RandomAccessFile(newPath, "rw");
            audio.setLength(0);
            for (AudioProcessor ap : processors) dispatcher.addAudioProcessor(ap);
            dispatcher.addAudioProcessor(new WriterProcessor(new TarsosDSPAudioFormat(track.getSampleRate(), 16, 2, true, false), audio));
            dispatcher.run();
            dispatcher.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
