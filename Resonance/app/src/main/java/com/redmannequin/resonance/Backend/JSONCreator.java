package com.redmannequin.resonance.Backend;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONCreator {

    Backend backend;
    int idTracker;

    public JSONCreator(Backend back) {
        this.backend = back;
        idTracker = 0;
    }

    public String[] create() {

        /*
             Project:
                      "id" : 0,
                      "name" : "project0",
                      "duration" : 0,
                      "author" : "Matthew Fors",
                      "BPM" : 120,
                      "trackIDs" : [
                        {
                          "id" : 0
                        },
                        {
                          "id" : 1
                        },
                        {
                          "id" : 2
                        }
                      ]
         */


        Project project;

        //This will contain the two strings, where strings[0] is the json for projects, and strings[1] is the json for tracks.
        String[] strings = new String[2];

        JSONArray trackIDs = new JSONArray();
        JSONArray projects = new JSONArray();
        JSONArray tracks = new JSONArray();
        JSONObject newProject = new JSONObject();
        JSONObject trackID = new JSONObject();
        JSONObject track = new JSONObject();
        JSONObject projectRoot = new JSONObject();
        JSONObject trackRoot = new JSONObject();

        //JSONArray  .put(Object) adds the object to the array
        //JSONObject .put(String, Object) adds the values with label to the object

        int numProjects = backend.getProjectListSize();
        int numTracks = 0;

        try {

            //Loops through each project
            for (int i = 0; i < numProjects; i++) {
                //sets project = to the current project we are copying to JSON
                project = backend.getProject(i);
                //Find the number of tracks contained in the current project
                numTracks = project.getTrackListSize();

                //Reset newProject
                newProject = new JSONObject();
                trackIDs = new JSONArray();
                newProject.put("id", i);
                newProject.put("name", project.getName());
                newProject.put("duration", project.getDuration());
                newProject.put("author", project.getAuthor());
                newProject.put("BPM", project.getBPM());


                //Loops through each track for current project
                for (int j = 0; j < numTracks; j++) {

                    //Make an array "trackIDs" with all of the ids of the tracks being added.
                    trackID = new JSONObject();
                    trackID.put("id", idTracker);
                    trackIDs.put(trackID);

                    //Tracks
                    Track newTrack = project.getTrack(j);

                    track = new JSONObject();

                    track.put("name", newTrack.getName());
                    track.put("id", idTracker);
                    track.put("name", newTrack.getName());
                    track.put("path", newTrack.getPath());
                    track.put("duration", newTrack.getDuration());
                    track.put("localStartTime", newTrack.getLocalStartTime());
                    track.put("localEndTime", newTrack.getLocalEndTime());
                    track.put("globalStartTime", newTrack.getGlobalStartTime());
                    track.put("globalEndTime", newTrack.getGlobalEndTime());
                    track.put("sampleRate", newTrack.getSampleRate());

                    tracks.put(track);

                    idTracker++;
                }

                //Add the trackIDs to the newproject, then add the new project to the projects array.
                newProject.put("trackIDs", trackIDs);
                projects.put(newProject);
            }

            projectRoot.put("projects", projects);
            trackRoot.put("tracks", tracks);

            strings[0] = projectRoot.toString();
            strings[1] = trackRoot.toString();
            return strings;
        }
        catch (JSONException e) {}
        return strings;
    }


}
