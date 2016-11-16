package com.redmannequin.resonance.Audio;

import com.redmannequin.resonance.Backend.Track;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;

import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.effects.FlangerEffect;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.UniversalAudioInputStream;
import be.tarsos.dsp.writer.WriterProcessor;

public class AudioEffect {

    private Track track;
    private TarsosDSPAudioFormat audioFormat;
    private UniversalAudioInputStream audioInputStream;
    private AudioDispatcher dispatcher;

    private RandomAccessFile source;

    private long sourceLength;
    private String path;
    private String newPath;

    private ArrayList<AudioProcessor> processors;

    public AudioEffect(Track track) {
        this.track = track;
        sourceLength = 0;
        audioFormat = null;
        audioInputStream = null;
        dispatcher = null;
        processors = new ArrayList<>();
    }

    public void init() {
        audioFormat = new TarsosDSPAudioFormat(track.getSampleRate(), 16, 1, true, false);
        path = track.getPath() + File.separator + track.getName() + ".pcm";
        newPath = track.getPath() + File.separator + track.getName() + "_final.wav";
        try {
            source = new RandomAccessFile(path, "r");
            sourceLength = source.length();
            source.close();
            audioInputStream = new UniversalAudioInputStream(new FileInputStream(path), audioFormat);
            dispatcher = new AudioDispatcher(audioInputStream, 1024, 0);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void addDelayEffect(double length, double decay) {
        try {

            source = new RandomAccessFile(path, "rw");
            source.seek(sourceLength);
            int b = (int)((sourceLength/(length*Config.FREQUENCY*2))*decay);
            source.write(new byte[(int)(b+length+(length*decay)+1)*Config.FREQUENCY*2]);
            DelayEffect delayEffect = new DelayEffect(length, decay, Config.FREQUENCY);
            processors.add(delayEffect);
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addFlangerEffect() {
        FlangerEffect flangerEffect = new FlangerEffect(20/1000.0, 50/100.0, Config.FREQUENCY, 3/10.0);
        processors.add(flangerEffect);
    }


    public void make() {
        try {
            RandomAccessFile audio = new RandomAccessFile(newPath, "rw");
            audio.setLength(0);
            for (AudioProcessor ap : processors) dispatcher.addAudioProcessor(ap);
            dispatcher.addAudioProcessor(new WriterProcessor(new TarsosDSPAudioFormat(track.getSampleRate(), 16, 2, true, false), audio));
            dispatcher.run();
            dispatcher.stop();
            source = new RandomAccessFile(path, "rw");
            source.setLength(sourceLength);
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
