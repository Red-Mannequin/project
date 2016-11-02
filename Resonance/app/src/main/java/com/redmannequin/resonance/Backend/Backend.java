package com.redmannequin.resonance.Backend;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Backend implements Parcelable {

    private String projectJsonStr;
    private String trackJsonStr;
    private ArrayList<Project> projects;

    public Backend(String projectJson, String trackJson) {
        this.projectJsonStr = projectJson;
        this.trackJsonStr = trackJson;
        this.projects = null;
        init();
    }

    private void init() {
        this.projects = new ArrayList<Project>();

        try {
            //Create JSONarray's containing the data in R.raw.projects and R.raw.tracks
            JSONObject projectRoot = new JSONObject(projectJsonStr);
            JSONObject trackRoot = new JSONObject(trackJsonStr);
            JSONArray projects = projectRoot.optJSONArray("projects");
            JSONArray tracks = trackRoot.optJSONArray("tracks");

            for (int i= 0; i < projects.length(); i++) {
                JSONObject currProject = projects.getJSONObject(i);
                Project newProject = new Project(currProject.optString("name"),
                                                 currProject.optString("author"),
                                                 Integer.parseInt(currProject.optString("BPM")),
                                                 Integer.parseInt(currProject.optString("duration")));

                JSONArray trackIDs = currProject.optJSONArray("trackIDs");

                for (int j = 0; j < trackIDs.length(); j++) {
                    JSONObject currID = trackIDs.getJSONObject(j);
                    JSONObject track = tracks.getJSONObject(currID.optInt("id"));
                    Track newTrack = new Track( track.optString("name"),
                                                track.optString("path"),
                                                track.optInt("duration"),
                                                track.optInt("localEndTime"),
                                                track.optInt("localStartTime"),
                                                track.optInt("globalEndTime"),
                                                track.optInt("globalStartTime"),
                                                track.optInt("sampleRate"));
                    newProject.add(newTrack);
                }
                this.projects.add(newProject);
            }
        }
        catch (JSONException e) {

        }
/*
        try {
            JSONObject root = new JSONObject(jsonStr);
            JSONArray projects = root.optJSONArray("projects");
            for (int i=0; i < projects.length(); ++i) {
                JSONObject project = projects.getJSONObject(i);
                Project newProject = new Project(project.optString("name").toString());
                JSONArray tracks = project.optJSONArray("tracks");
                for (int j=0; j < project.length(); ++j) {
                    JSONObject track = tracks.getJSONObject(j);
                    Track newTrack = new Track(track.optString("name").toString());
                    newProject.add(newTrack);
                }
                this.projects.add(newProject);
            }
        } catch (JSONException e) {

        }*/
    }

    public Project getProject(int i) {
        return projects.get(i);
    }

    public void add(Project project) {
        projects.add(project);
    }

    public ArrayList<String> getProjectList() {
        ArrayList<String> list = new ArrayList<String>();
        if (projects == null) {

        }
        else {
            for (Project project : projects) {
                list.add(project.getName());
            }
        }
        return list;
    }

    public int getProjectListSize() {
        return projects.size();
    }


    protected Backend(Parcel in) {
       // jsonStr = in.readString();
        projects = in.readArrayList(Project.class.getClassLoader());
    }

    public static final Creator<Backend> CREATOR = new Creator<Backend>() {
        @Override
        public Backend createFromParcel(Parcel in) {
            return new Backend(in);
        }

        @Override
        public Backend[] newArray(int size) {
            return new Backend[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(jsonStr);
        dest.writeList(projects);
    }
}
