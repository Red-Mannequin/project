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
import com.redmannequin.resonance.R;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TrackListView extends AppCompatActivity {
    // json paths
    private String projectJson;
    private String trackJson;

    // project ID
    private int projectID;

    // backend
    private Project project;
    private Backend backend;

    // ListView + list + adapter
    private ArrayAdapter adapter;
    private ArrayList<String> trackList;
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
        setTitle(project.getName());

        // list view stuff
        trackList = new ArrayList<>();
        trackList.addAll(project.getTrackNames());
        trackList.add("+");
        trackList.add("play");

        // set list adapter
        adapter = new ArrayAdapter(this, R.layout.activity_list, trackList);
        trackView = (ListView) findViewById(R.id.list);

        // wait for track to pressed and load TrackView or NewTrack
        trackView.setAdapter(adapter);
        setListeners();

        if(project.getTrackListSize() == 0) {
            Intent intent = new Intent(getApplicationContext(), NewTrackView.class);
            intent.putExtra("projectID", projectID);
            startActivityForResult(intent, 0);
        }
    }

    public void setListeners() {
        trackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount()-2) {
                    // switch to NewTrackView and passes project and backend

                    String[] JSONfiles = backend.toWrite();
                    outputToFile(JSONfiles[0], "projects");
                    outputToFile(JSONfiles[1], "tracks");

                    Intent intent;
                    intent = new Intent(getApplicationContext(), NewTrackView.class);
                    intent.putExtra("projectID", projectID);
                    startActivityForResult(intent, 0);

                } else if (position == parent.getCount()-1) {
                    if (trackList.get(position).equals("play")) {
                        trackList.set(position, "stop");
                        adapter.notifyDataSetChanged();
                    } else {
                        trackList.set(position, "play");
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    String[] JSONfiles = backend.toWrite();
                    outputToFile(JSONfiles[0], "projects");
                    outputToFile(JSONfiles[1], "tracks");

                    Intent intent = new Intent(getApplicationContext(), TrackView.class);
                    intent.putExtra("trackID", position);
                    intent.putExtra("projectID", projectID);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    // wait for project to pressed and load TrackListView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        trackJson = loadJson("tracks");      // load track json
        projectJson = loadJson("projects");  // load project json
        backend = new Backend(projectJson, trackJson); // init backend

        if(resultCode == RESULT_OK) {
            projectID = data.getIntExtra("projectID", 0);
            project = backend.getProject(projectID);

            trackList.clear();
            trackList.addAll(project.getTrackNames());
            trackList.add("+");
            trackList.add("play");
            adapter.notifyDataSetChanged();
        }
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
            FileOutputStream file = this.openFileOutput(name + ".json", TrackListView.MODE_PRIVATE);
            file.write(data.getBytes());
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
}
