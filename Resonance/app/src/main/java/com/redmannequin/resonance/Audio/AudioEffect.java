package com.redmannequin.resonance.Audio;

import android.util.Log;

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
    private long newLength;

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
        path = track.getSourcePath() + File.separator + track.getName() + ".pcm";
        newPath = track.getSourcePath() + File.separator + track.getName() + "_final.wav";
        try {
            source = new RandomAccessFile(path, "r");
            source.seek(0);
            sourceLength = source.length();
            newLength = sourceLength;
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
            source.seek(newLength);
            long b = (long)((newLength/(length*Config.FREQUENCY*2))*decay);
            b = (long)((b+(length*decay)+length+1)*Config.FREQUENCY*2);
            if (b+sourceLength > newLength) {
                newLength = sourceLength+b;
                source.write(new byte[(int) b]);
            }
            DelayEffect delayEffect = new DelayEffect(length, decay, Config.FREQUENCY);
            processors.add(delayEffect);
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFlangerEffect(double length, double wet, double frequency) {
        FlangerEffect flangerEffect = new FlangerEffect(length/1000.0, wet/100.0, Config.FREQUENCY, frequency/10.0);
        processors.add(flangerEffect);
    }

    public void addPitchShiftEffect(double factor,int size) {
        PitchShifter pitchShifter = new PitchShifter(dispatcher, factor, Config.FREQUENCY, size, 0);
        processors.add(pitchShifter);
    }

    public void addAudioToMerge(Track track) {
        MergeAudio audio = new MergeAudio(track);
        processors.add(audio);
    }

    public void make() {
        try {
            audioInputStream = new UniversalAudioInputStream(new FileInputStream(path), audioFormat);
            dispatcher = new AudioDispatcher(audioInputStream, 1024, 0);
            
            RandomAccessFile audio = new RandomAccessFile(newPath, "rw");
            audio.seek(0);
            audio.setLength(0);
            for (AudioProcessor ap : processors) dispatcher.addAudioProcessor(ap);
            dispatcher.addAudioProcessor(new WriterProcessor(new TarsosDSPAudioFormat(track.getSampleRate(), 16, 2, true, false), audio));
            dispatcher.run();
            dispatcher.stop();
            source = new RandomAccessFile(path, "rw");
            source.seek(0);
            source.setLength(sourceLength);
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
