package com.redmannequin.resonance.Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.redmannequin.resonance.Backend.Effects.*;

import java.util.ArrayList;

public class Backend {

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

                    JSONArray effects = track.getJSONArray("effects");

                    for (int q = 0; q < effects.length(); q++) {
                        JSONObject effectInfo = effects.getJSONObject(q);

                        int eventID = effectInfo.optInt("id");
                        Effect effect = new Effect();

                        switch(eventID) {
                            case 0:
                                effect = new DelayEffect(
                                    effectInfo.optBoolean("on"),
                                    effectInfo.optDouble("delay"),
                                    effectInfo.optDouble("factor")
                                );
                                break;
                        }
/*
                        Effect effect = new Effect(
                            effectInfo.optInt("id")
                        );
*/
                        newTrack.addEffect(effect);
                    }

                    newProject.add(newTrack);
                }
                this.projects.add(newProject);
            }
        }
        catch (JSONException e) {

        }
    }

    public String[] toWrite() {
        JSONCreator creator = new JSONCreator(this);
        return creator.create();
    }

    public Project getProject(int i) {
        return projects.get(i);
    }

    public void add(Project project) {
        projects.add(project);
    }

    public ArrayList<String> getProjectList() {
        ArrayList<String> list = new ArrayList<String>();
        if (projects != null) {
            for (Project project : projects) {
                list.add(project.getName());
            }
        }
        return list;
    }

    public int getProjectListSize() {
        return projects.size();
    }

}
