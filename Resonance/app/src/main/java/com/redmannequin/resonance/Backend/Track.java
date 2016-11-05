package com.redmannequin.resonance.Backend;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

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

    public Track(String name) {
        this.name = name;
    }

    //Constructor with required info
    public Track(String name, String path, int duration) {
        this.name = name;
        this.path = path;
        this.duration = duration;
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


    protected Track(Parcel in) {
        name = in.readString();
        path = in.readString();
        duration = in.readInt();
        localEndTime = in.readInt();
        localStartTime = in.readInt();
        globalEndTime = in.readInt();
        globalStartTime = in.readInt();
        sampleRate = in.readInt();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeInt(duration);
        dest.writeInt(localEndTime);
        dest.writeInt(localEndTime);
        dest.writeInt(globalStartTime);
        dest.writeInt(globalEndTime);
        dest.writeInt(sampleRate);
    }
}
