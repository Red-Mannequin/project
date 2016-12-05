package com.redmannequin.resonance.Audio.Mixer;

import android.util.Log;

import com.redmannequin.resonance.Audio.Config;
import com.redmannequin.resonance.Backend.Project;
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

public class Mixer {

    private Project project;
    private Track track;
    private TarsosDSPAudioFormat audioFormat;
    private UniversalAudioInputStream audioInputStream;
    private AudioDispatcher dispatcher;

    private RandomAccessFile source;
    private String path;
    private String newPath;


    private ArrayList<AudioProcessor> processors;
    private ArrayList<Boolean> flags;

    public Mixer(Project project) {
        this.project = project;
        audioFormat = null;
        audioInputStream = null;
        dispatcher = null;
        processors = new ArrayList<>();
        flags = new ArrayList<>();
    }

    public void init() {
        audioFormat = new TarsosDSPAudioFormat(Config.FREQUENCY, 16, 1, true, false);
        path = project.getPath() + File.separator + project.getName() + ".pcm";
        newPath = project.getPath() + File.separator + project.getName() + ".wav";
        try {
            source = new RandomAccessFile(path, "rw");
            source.seek(0);
            source.setLength(Config.FREQUENCY*2*2*5);
            source.close();
            Log.w("temp", "made pcm");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int id) {
        track = project.getTrack(id);
        audioFormat = new TarsosDSPAudioFormat(track.getSampleRate(), 16, 1, true, false);
        path = track.getSourcePath() + File.separator + track.getName() + ".pcm";
        newPath = track.getProductPath() + File.separator + track.getName() + ".wav";
        try {
            source = new RandomAccessFile(path, "r");
            source.seek(0);
            source.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean masterExists() {
        String temp = project.getPath() + File.separator + project.getName() + ".wav";
        File file = new File(temp);
        return file.exists();
    }

    public void addDelayEffect(double length, double decay) {
        try {
            source = new RandomAccessFile(path, "rw");
            DelayEffect delayEffect = new DelayEffect(length, decay, Config.FREQUENCY);
            processors.add(delayEffect);
            flags.add(true);
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addFlangerEffect(double length, double wet, double frequency) {
        FlangerEffect flangerEffect = new FlangerEffect(length/1000.0, wet/100.0, Config.FREQUENCY, frequency/10.0);
        processors.add(flangerEffect);
        flags.add(true);
    }

    public void addPitchShiftEffect(double factor,int size) {
        PitchShifter pitchShifter = new PitchShifter(dispatcher, factor, Config.FREQUENCY, size, 0);
        processors.add(pitchShifter);
    }

    public void addAudioToMerge(Track track) {
        MergeAudio audio = new MergeAudio(track);
        processors.add(audio);
    }

    public void remove(int i) {
        processors.remove(i);
    }

    public void toggle(int i) {
        flags.set(i, !flags.get(i));
    }

    public void make() {
        if (track == null) for (int i=0; i < project.getTrackListSize(); ++i) addAudioToMerge(project.getTrack(i));
        try {
            audioInputStream = new UniversalAudioInputStream(new FileInputStream(path), audioFormat);
            dispatcher = new AudioDispatcher(audioInputStream, 1024, 0);
            RandomAccessFile audio = new RandomAccessFile(newPath, "rw");
            audio.seek(0);
            audio.setLength(0);
            for(int i=0; i < processors.size(); ++i)
                if(flags.get(i)) dispatcher.addAudioProcessor(processors.get(i));
            dispatcher.addAudioProcessor(new WriterProcessor(new TarsosDSPAudioFormat(Config.FREQUENCY, 16, 2, true, false), audio));
            dispatcher.run();
            dispatcher.stop();

            source = new RandomAccessFile(path, "rw");
            source.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
