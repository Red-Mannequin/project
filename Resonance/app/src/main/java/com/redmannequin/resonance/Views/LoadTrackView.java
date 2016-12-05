package com.redmannequin.resonance.Views;

// android imports
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// project imports
import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.Backend.Track;
import com.redmannequin.resonance.R;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoadTrackView extends AppCompatActivity {
    // json paths
    private String projectJson;
    private String trackJson;

    // project ID
    private int projectID;

    // backend
    private Project project;
    private Backend backend;

    // all other projects and tracks
    private ArrayList<Project> projectList;
    private ArrayList<Track> trackList;

    // ListView + list + adapter
    private ArrayAdapter adapter;
    private ArrayList<String> trackNameList;
    private ListView trackView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_load);

        // load backend
        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend

        // get project
        projectID = getIntent().getIntExtra("projectID", 0);
        project = backend.getProject(projectID);

        // set title
        setTitle("Load Track");

        // lists of other projects and tracks
        projectList = getOtherProjects();
        trackList = getOtherTracks(projectList);

        // list of all track names
        trackNameList = getTrackNames(trackList);

        // set list adapter
        adapter = new ArrayAdapter(this, R.layout.activity_list, trackNameList);
        trackView = (ListView) findViewById(R.id.list);

        // wait for track to pressed and add track to project
        trackView.setAdapter(adapter);
        setListeners();
    }

    public void setListeners() {
        trackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // add track to current project, save JSON, go back to project view

                Track dupTrack = new Track(trackList.get(position));
                project.add(dupTrack);

                String[] JSONfiles = backend.toWrite();
                outputToFile(JSONfiles[0], "projects");
                outputToFile(JSONfiles[1], "tracks");

                onBackPressed();
            }
        });
    }

    // get backend from activity stack
    @Override
    public void onBackPressed() {
        String[] JSONfiles = backend.toWrite();
        outputToFile(JSONfiles[0], "projects");
        outputToFile(JSONfiles[1], "tracks");
        super.onBackPressed();

    }

    private void outputToFile(String data, String name) {
        try {
            FileOutputStream file = this.openFileOutput(name + ".json", LoadTrackView.MODE_PRIVATE);
            if (data != null) {
                file.write(data.getBytes());
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadJson(String name) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(this.getFilesDir(), name+".json");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    private ArrayList<Project> getOtherProjects() {
        ArrayList<Project> otherProjects = new ArrayList<Project>();
        for(int i=0; i < backend.getProjectListSize(); ++i) {
            if(i == projectID) {
                ++i;
            } else {
                otherProjects.add(backend.getProject(i));
            }
        }
        return otherProjects;
    }

    private ArrayList<Track> getOtherTracks(ArrayList<Project> otherProjects) {
        ArrayList<Track> otherTracks = new ArrayList<Track>();
        for(int i=0; i < otherProjects.size(); ++i) {
            for(int x=0; x < otherProjects.get(i).getTrackListSize(); ++x) {
                otherTracks.add(otherProjects.get(i).getTrack(x));
                ++x;
            }
        }
        return otherTracks;
    }

    private ArrayList<String> getTrackNames(ArrayList<Track> otherTracks) {
        ArrayList<String> trackNames = new ArrayList<String>();
        for(int i=0; i < otherTracks.size(); ++i) {
            trackNames.add(otherTracks.get(i).getName());
        }
        return trackNames;
    }
}
