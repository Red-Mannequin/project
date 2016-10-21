package com.redmannequin.resonance.BackendTesting;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;


public class Project implements Parcelable{

    private String name;
    private ArrayList<Track> tracks;

    public Project(String name) {
        this.name = name;
        this.tracks = new ArrayList<Track>();
    }

    protected Project(Parcel in) {
        name = in.readString();
        tracks = in.readArrayList(Track.class.getClassLoader());
    }

    public void add(Track track) {
        tracks.add(track);
    }

    public ArrayList<String> getTrackNames() {
        ArrayList<String> trackNames = new ArrayList<String>();
        for (Track track: tracks) {
            trackNames.add(track.getName());
            Log.w("Project Track:", track.getName());
        }
        return trackNames;
    }

    public Track getTrack(int i) {
        return tracks.get(i);
    }

    public String getName() {return name;}

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(tracks);
    }
}
