package com.redmannequin.resonance.BackendTesting;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Backend implements Parcelable {

    private String jsonStr;
    private ArrayList<Project> projects;

    public Backend(String json) {
        this.jsonStr = json;
        this.projects = null;
        init();
    }

    private void init() {
        this.projects = new ArrayList<Project>();

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

        }
    }

    public Project getProject(int i) {
        return projects.get(i);
    }

    public ArrayList<String> getProjectList() {
        Log.w("Backend: ", "getting projects");
        ArrayList<String> list = new ArrayList<String>();
        for (Project project : projects) {
            list.add(project.getName());
            Log.w("Backend : project : ", project.getName());
        }
        return list;
    }


    protected Backend(Parcel in) {
        jsonStr = in.readString();
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
        dest.writeString(jsonStr);
        dest.writeList(projects);
    }
}
