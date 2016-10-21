package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.redmannequin.resonance.BackendTesting.Project;

import java.util.ArrayList;

public class TrackListView extends AppCompatActivity {

    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list_view);

        project = (Project) getIntent().getParcelableExtra("project");

        // creates a list with the track names
        ArrayList<String> trackList = project.getTrackNames();
        trackList.add("+");
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_tacklist, trackList);

        // create the list view with track names
        ListView trackView = (ListView) findViewById(R.id.tack_list);
        trackView.setAdapter(adapter);
        trackView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getCount()) {
                    // load new track view
                    Intent intent = new Intent(getApplicationContext(), null);
                    startActivity(intent);
                } else {
                    // load track view with tack
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("track", project.getTrack(position));
                    startActivity(intent);
                }
            }
        });
    }

}
