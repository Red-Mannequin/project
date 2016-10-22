package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.redmannequin.resonance.BackendTesting.Backend;
import com.redmannequin.resonance.BackendTesting.Project;

import java.util.ArrayList;

public class TrackListView extends AppCompatActivity {

    private Intent intent;
    private ListView trackView;
    private ArrayAdapter adapter;

    private ArrayList<String> trackList;

    private Project project;
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list_view);

        // gets working project and backend
        project = getIntent().getParcelableExtra("project");
        backend = getIntent().getParcelableExtra("backend");

        // set page title as project name
        setTitle(project.getName());

        // creates a list with the track names
        trackList = project.getTrackNames();
        trackList.add("+"); // the add button to add tracks
        adapter = new ArrayAdapter(this, R.layout.activity_tacklist, trackList);

        // create the list view with track names
        trackView = (ListView) findViewById(R.id.tack_list);
        trackView.setAdapter(adapter);
        trackView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount()-1) {
                    // switch to NewTrackView and passes project and backend
                    intent = new Intent(getApplicationContext(), NewTrackView.class);
                    intent.putExtra("project", project);
                    intent.putExtra("backend", backend);
                    startActivity(intent);
                } else {
                    // switch to MainActivity and passes project and backend
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("track", project.getTrack(position));
                    intent.putExtra("project", project);
                    intent.putExtra("backend", backend);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // send changes back to previous activity
        intent = new Intent();
        intent.putExtra("project", project);
        intent.putExtra("backend", backend);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}
