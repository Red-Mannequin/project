package com.redmannequin.resonance.Backend;

import com.redmannequin.resonance.Backend.Effects.Delay;
import com.redmannequin.resonance.Backend.Effects.Effect;

import java.util.ArrayList;

public class Track {

    //Variable Declarations containing Track info
        //TO ADD:
        //     - WaveData (not in .json)
        //     - Effects Array
    private String  name,
                    path;
    private int duration,
                localStartTime,
                localEndTime,
                globalStartTime,
                globalEndTime,
                sampleRate;

    private ArrayList<Effect> effects = new ArrayList<Effect>();

    public Track(String name) {
        this.name = name;
    }

    //Constructor with required info
    public Track(String name, String path, int duration) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        Effect testEffect = new Delay(0, 0.0001, 0.0002);
        effects.add(testEffect);
    }

    //Constructor with all info
    public Track(String name, String path, int duration, int localEndTime, int localStartTime, int globalEndTime, int globalStartTime, int sampleRate) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        this.localEndTime = localEndTime;
        this.localStartTime = localStartTime;
        this.globalEndTime = globalEndTime;
        this.globalStartTime = globalStartTime;
        this.sampleRate = sampleRate;
    }

    //GETTERS
    public String getName() {return name;}
    public String getPath() { return path; }
    public int getDuration() { return duration;}
    public int getLocalStartTime() {return localStartTime;}
    public int getLocalEndTime() {return localEndTime;}
    public int getGlobalStartTime() {return globalStartTime;}
    public int getGlobalEndTime() {return globalEndTime;}
    public int getSampleRate() {return sampleRate;}

    //SETTERS
    public void setPath(String p) {path = p;}
    public void setDuration(int d) {duration = d;}
    public void setLocalStartTime(int lst) {localStartTime = lst;}
    public void setLocalEndTime(int let) {localEndTime = let;}
    public void setName(String n) {name = n;}
    public void setGlobalStartTime(int gst) {globalStartTime = gst;}
    public void setGlobalEndTime(int get) {globalEndTime = get;}
    public void setSampleRate(int sr) {sampleRate = sr;}

}
