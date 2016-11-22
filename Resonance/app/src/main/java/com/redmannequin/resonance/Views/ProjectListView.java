package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectListView extends AppCompatActivity {

    // list view
    private ArrayAdapter adapter;
    private ArrayList<String> projectList;

    // backend
    private String projectJson;
    private String trackJson;
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_load);
        setTitle("Load Project");

        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend

        projectList = new ArrayList<>();
        projectList.addAll(backend.getProjectList());
        adapter = new ArrayAdapter(this, R.layout.activity_list, projectList);
        ListView trackView = (ListView) findViewById(R.id.list);

        // wait for project to pressed and load TrackListView
        trackView.setAdapter(adapter);
        trackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TrackListView.class);
                intent.putExtra("projectID", position);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend

        projectList.clear();
        projectList.addAll(backend.getProjectList());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        String[] JSONfiles = backend.toWrite();
        outputToFile(JSONfiles[0], "projects");
        outputToFile(JSONfiles[1], "tracks");
        super.onBackPressed();
    }

    private void outputToFile(String data, String name) {
        try {
            FileOutputStream file = this.openFileOutput(name + ".json", this.MODE_PRIVATE);
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
