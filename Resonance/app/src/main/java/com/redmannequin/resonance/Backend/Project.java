package com.redmannequin.resonance.Backend;

// android
import android.os.Parcel;
import android.os.Parcelable;

// java
import java.util.ArrayList;


public class Project implements Parcelable{

    //Name of project and author
    private String name;
    private String author;
    //milliseconds
    private int duration;
    //Beats per minute
    private int BPM;
    //trackinfo
    private ArrayList<Track> tracks;

    public Project(String name) {
        this.name = name;
        this.tracks = new ArrayList<Track>();
        author = "Unknown";
        duration = 0;
        BPM = 0;
    }

    //Constructs project with all info.
    public Project(String name, String author, int BPM, int duration) {
        this.name = name;
        this.author = author;
        this.duration = duration;
        this.BPM = BPM;
        this.tracks = new ArrayList<Track>();
    }
    //Constructs project with no duration set
    public Project(String name, String author, int BPM) {
        this.name = name;
        this.author = author;
        duration = 0;
        this.BPM = BPM;
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
        }
        return trackNames;
    }

    public Track getTrack(int i) {
        return tracks.get(i);
    }

    public int getTrackListSize() {
        return tracks.size();
    }

    public String getName() {return name;}
    public String getAuthor() {return author;}
    public int getDuration() {return duration;}
    public int getBPM() {return BPM;}

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
