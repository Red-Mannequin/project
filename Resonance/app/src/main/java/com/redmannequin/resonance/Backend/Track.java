package com.redmannequin.resonance.Backend;

import com.redmannequin.resonance.Backend.Effects.*;

import java.util.ArrayList;

public class Track {

    //Variable Declarations containing Track info
        //TO ADD:
        //     - WaveData (not in .json)
        //     - Effects Array
    private String  name,
                    sourcePath,
                    productPath;
    private int duration,
                localStartTime,
                localEndTime,
                globalStartTime,
                globalEndTime,
                sampleRate;

    private ArrayList effects = new ArrayList();

    public Track(String name) {
        this.name = name;
    }

    //Constructor with required info
    public Track(String name, String sourcePath, String productPath, int duration) {
        this.name = name;
        this.sourcePath = sourcePath;
        this.productPath = productPath;
        this.duration = duration;
    }

    //Constructor with all info
    public Track(String name, String sourcePath, String productPath, int duration, int localEndTime, int localStartTime, int globalEndTime, int globalStartTime, int sampleRate) {
        this.name = name;
        this.sourcePath = sourcePath;
        this.productPath = productPath;
        this.duration = duration;
        this.localEndTime = localEndTime;
        this.localStartTime = localStartTime;
        this.globalEndTime = globalEndTime;
        this.globalStartTime = globalStartTime;
        this.sampleRate = sampleRate;
    }

    //creating new track out of track
    public Track(Track t) {
        this.name = t.getName();
        this.sourcePath = t.getSourcePath();
        this.productPath = t.getProductPath();
        this.duration = t.getDuration();
        this.localEndTime = t.getLocalEndTime();
        this.localStartTime = t.getLocalStartTime();
        this.globalEndTime = t.getGlobalEndTime();
        this.globalStartTime = t.getGlobalStartTime();
        this.sampleRate = t.getSampleRate();

        for (int i = 0; i < t.numEffects(); i++) {
            this.effects.add(t.getEffect(i));
        }
    }

    //creating new track out of track
    public Track(Track t, String sourcePath, String productPath) {
        this.sourcePath = sourcePath;
        this.productPath = productPath;
        this.name = t.getName();
        this.duration = t.getDuration();
        this.localEndTime = t.getLocalEndTime();
        this.localStartTime = t.getLocalStartTime();
        this.globalEndTime = t.getGlobalEndTime();
        this.globalStartTime = t.getGlobalStartTime();
        this.sampleRate = t.getSampleRate();

        for (int i = 0; i < t.numEffects(); i++) {
            this.effects.add(t.getEffect(i));
        }
    }

    public int numEffects() {
        return effects.size();
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

     public void removeEffect(int ID) {
         for (int i = 0; i < effects.size(); i++) {
             if (getEffect(i).getID() == ID) {
                 effects.remove(i);
             }
         }
     }

     //Type ID's are specified in Effects/Effect.java
     public Effect getEffectByTypeID(int ID) {
         for (int i = 0; i < effects.size(); i++) {
             if (getEffect(i).getID() == ID) {
                 return (Effect) effects.get(i);
             }
         }
         return null;
     }

     public Effect getEffect(int i) {
         return (Effect) effects.get(i);
     }

    //GETTERS
    public String getName() {return name;}
    public String getSourcePath() { return sourcePath; }
    public String getProductPath() { return productPath; }
    public int getDuration() { return duration;}
    public int getLocalStartTime() {return localStartTime;}
    public int getLocalEndTime() {return localEndTime;}
    public int getGlobalStartTime() {return globalStartTime;}
    public int getGlobalEndTime() {return globalEndTime;}
    public int getSampleRate() {return sampleRate;}

    //SETTERS
    public void setSourcePath(String p) {sourcePath = p;}
    public void setProductPath(String p) {productPath = p;}
    public void setDuration(int d) {duration = d;}
    public void setLocalStartTime(int lst) {localStartTime = lst;}
    public void setLocalEndTime(int let) {localEndTime = let;}
    public void setName(String n) {name = n;}
    public void setGlobalStartTime(int gst) {globalStartTime = gst;}
    public void setGlobalEndTime(int get) {globalEndTime = get;}
    public void setSampleRate(int sr) {sampleRate = sr;}

}
