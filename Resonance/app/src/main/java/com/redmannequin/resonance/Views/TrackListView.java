package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.R;

import java.util.ArrayList;

public class TrackListView extends AppCompatActivity {

    // backend
    private int projectID;
    private Project project;
    private Backend backend;

    // listview
    private ArrayAdapter adapter;
    private ArrayList<String> trackList;

    private ListView trackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_load);

        // load backend
        backend = getIntent().getParcelableExtra("backend");
        projectID = getIntent().getIntExtra("projectID", 0);
        project = backend.getProject(projectID);

        setTitle(project.getName());

        // list view stuff
        trackList = new ArrayList<String>();
        trackList.addAll(project.getTrackNames());
        trackList.add("+");

        adapter = new ArrayAdapter(this, R.layout.activity_list, trackList);
        trackView = (ListView) findViewById(R.id.list);

        // wait for track to pressed and load TrackView or NewTrack
        trackView.setAdapter(adapter);
        setListeners();

        if(project.getTrackListSize() == 0) {
            Intent intent = new Intent();
            intent = new Intent(getApplicationContext(), NewTrackView.class);
            intent.putExtra("projectID", projectID);
            intent.putExtra("backend", backend);
            startActivityForResult(intent, 0);
        }
    }

    public void setListeners() {
        trackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount()-1) {
                    // switch to NewTrackView and passes project and backend
                    Intent intent = new Intent();
                    intent = new Intent(getApplicationContext(), NewTrackView.class);
                    intent.putExtra("projectID", projectID);
                    intent.putExtra("backend", backend);
                    startActivityForResult(intent, 0);
                } else {
                    // switch to MainActivity and passes project and backend
                    Intent intent = new Intent();
                    intent = new Intent(getApplicationContext(), TrackView.class);
                    intent.putExtra("trackID", position);
                    intent.putExtra("projectID", projectID);
                    intent.putExtra("backend", backend);
                    startActivityForResult(intent, 0);
                }
            }
        });
    }

    // wait for project to pressed and load TrackListView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            backend = data.getParcelableExtra("backend");
            projectID = data.getIntExtra("projectID", 0);
            project = backend.getProject(projectID);

            trackList.clear();
            trackList.addAll(project.getTrackNames());
            trackList.add("+");
            adapter.notifyDataSetChanged();
        }
    }

    // get backend from activity stack
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("backend", backend);
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }
}
