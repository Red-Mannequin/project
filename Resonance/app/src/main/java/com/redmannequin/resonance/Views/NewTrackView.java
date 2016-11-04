package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.Backend.Track;
import com.redmannequin.resonance.R;
import com.redmannequin.resonance.Record;

public class NewTrackView extends AppCompatActivity {

    private String trackName;
    private String trackAuthor;
    private String trackPath;
    // ui elements
    private EditText trackNameInput;
    private EditText trackAuthorInput;
    private EditText trackPathInput;
    private Button createTrackButton;

    // backend
    private int projectID;
    private Project project;
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_track);

        setTitle("New Track");

        // get backend info
        backend = getIntent().getParcelableExtra("backend");
        projectID = getIntent().getIntExtra("projectID", 0);
        if (projectID != -1) project = backend.getProject(projectID);

        // get ui textfild
        trackNameInput = (EditText) findViewById(R.id.track_name_input);
        trackAuthorInput = (EditText) findViewById(R.id.track_author_input);
        trackPathInput = (EditText) findViewById(R.id.track_path_input);

        // load TrackView when createTrackButton is pressed
        createTrackButton = (Button) findViewById(R.id.create_track_button);

    }

    private void setListeners() {

        trackPathInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Record.class);
                intent.putExtra("trackID", backend);
                intent.putExtra("backend", backend);
                startActivityForResult(intent, 0);
            }
        });

        createTrackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackName = trackNameInput.getText().toString();
                trackAuthor = trackAuthorInput.getText().toString();
                trackPath = trackPathInput.getText().toString();
                Track track = new Track(trackName, trackPath, 0, 0, 0, 0, 0, 0);
                if (projectID != -1) project.add(track);
                Intent intent = new Intent(getApplicationContext(), TrackView.class);
                // send backend info to TackView
                intent.putExtra("trackID", 0);
                intent.putExtra("projectID", projectID);
                intent.putExtra("backend", backend);
                startActivityForResult(intent, 0);
            }
        });
    }

    // get backend from activity stack
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            backend = getIntent().getParcelableExtra("backend");
            projectID = getIntent().getIntExtra("projectID", 0);
            onBackPressed();
        }
    }

    // when back is pressed send backend and finish activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("projectID", projectID);
        intent.putExtra("backend", backend);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
