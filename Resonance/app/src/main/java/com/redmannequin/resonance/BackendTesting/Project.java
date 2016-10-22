package com.redmannequin.resonance.BackendTesting;

// android
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

// java
import java.util.ArrayList;


public class Project implements Parcelable{

    private String name;
    private ArrayList<TrackB> tracks;

    public Project(String name) {
        this.name = name;
        this.tracks = new ArrayList<TrackB>();
    }

    protected Project(Parcel in) {
        name = in.readString();
        tracks = in.readArrayList(TrackB.class.getClassLoader());
    }

    public void add(TrackB track) {
        tracks.add(track);
    }

    public ArrayList<String> getTrackNames() {
        ArrayList<String> trackNames = new ArrayList<String>();
        for (TrackB track: tracks) {
            trackNames.add(track.getName());
        }
        return trackNames;
    }

    public TrackB getTrack(int i) {
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
