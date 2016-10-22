package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.BackendTesting.Backend;
import com.redmannequin.resonance.BackendTesting.Project;
import com.redmannequin.resonance.BackendTesting.Track;

public class NewTrackView extends AppCompatActivity {

    private Button create;
    private EditText trackNameInput;
    private Intent intent;

    private Track track;
    private Project project;
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_track_view);

        project = getIntent().getParcelableExtra("project");
        backend = getIntent().getParcelableExtra("backend");

        trackNameInput = (EditText) findViewById(R.id.track_name_input);

        create = (Button) findViewById(R.id.track_create_button);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String trackName = trackNameInput.getText().toString();
                track = new Track(trackName);
                project.add(track);

                // switches to the TackListView
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("track", track);
                intent.putExtra("project", project);
                intent.putExtra("backend", backend);
                startActivity(intent);
                finish();
            }
        });
    }

}
