package com.redmannequin.resonance.Backend;

import java.util.ArrayList;

public class Project {

    //Name of project and author
    private String name;
    private String author;
    private String path;

    //milliseconds
    private int duration;

    //Beats per minute
    private int BPM;

    //track info
    private ArrayList<Track> tracks;
    private int numTracks;

    public Project(String name) {
        this.name = name;
        this.tracks = new ArrayList<Track>();
        author = "Unknown";
        duration = 0;
        BPM = 0;
        path = "";
    }

    //Constructs project with all info.
    public Project(String name, String author, int BPM, int duration) {
        this.name = name;
        this.author = author;
        this.duration = duration;
        this.BPM = BPM;
        this.tracks = new ArrayList<Track>();
        this.path = "";
    }

    //Constructs project with all info.
    public Project(String name, String path, String author, int BPM, int duration) {
        this.name = name;
        this.author = author;
        this.duration = duration;
        this.BPM = BPM;
        this.path = path;
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

    public ArrayList<String> getTrackNames() {
        ArrayList<String> trackNames = new ArrayList<>();
        for (Track track: tracks) {
            trackNames.add(track.getName());
        }
        return trackNames;
    }

    public Track getTrack(int i) {return tracks.get(i);}
    public void add(Track track) {tracks.add(track);}

    public int getTrackListSize() {return tracks.size();}
    public int getBPM()       {return BPM;}
    public int getDuration()  {return duration;}
    public String getName()   {return name;}
    public String getAuthor() {return author;}
    public String getPath() { return author;}


}
