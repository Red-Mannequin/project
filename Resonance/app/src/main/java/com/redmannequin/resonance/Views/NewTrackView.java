package com.redmannequin.resonance.Views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.Audio.Config;
import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.Backend.Track;
import com.redmannequin.resonance.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NewTrackView extends AppCompatActivity {

    private static final int TRACK_VIEW_RETURN = 0;
    private static final int RECORD_RETURN = 1;

    private String trackName;
    private String trackAuthor;
    private String trackSourcePath;
    private String trackProductPath;
    private String trackSampleRate;

    // ui elements
    private EditText trackNameInput;
    private EditText trackAuthorInput;
    private EditText trackPathInput;
    private EditText sampleRateInput;
    private Button createTrackButton;

    // backend
    private String projectJson;
    private String trackJson;
    private int projectID;
    private Project project;
    private Backend backend;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_track);

        setTitle("New Track");

        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend

        projectID = getIntent().getIntExtra("projectID", 0);
        if (projectID != -1) project = backend.getProject(projectID);

        // get ui textfild
        trackNameInput = (EditText) findViewById(R.id.track_name_input);
        trackAuthorInput = (EditText) findViewById(R.id.track_author_input);
        trackPathInput = (EditText) findViewById(R.id.track_path_input);
        sampleRateInput = (EditText) findViewById(R.id.track_sampleRate_input);

        // load TrackView when createTrackButton is pressed
        createTrackButton = (Button) findViewById(R.id.create_track_button);
        setListeners();
        createTrackButton.setEnabled (false);

    }

    private void setListeners() {
        trackPathInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackName = trackNameInput.getText().toString();
                trackAuthor = trackAuthorInput.getText().toString();
                trackSampleRate = sampleRateInput.getText().toString();

                if (checkInputs(trackName, trackAuthor, trackSampleRate)) {
                    trackNameInput.setFocusable(false);

                    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "Resonance" + File.separator + project.getName() + File.separator + trackName);
                    if (!path.exists()) path.mkdirs();
                    File file = new File(path, trackName+".pcm");

                    final Intent intent = new Intent(getApplicationContext(), RecordTrackView.class);
                    intent.putExtra("path", file.getPath());

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Choose an option");
                    alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Load",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Record",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                startActivity(intent);
                            }
                        });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                    trackSourcePath = path.getPath();
                    trackProductPath = path.getPath();
                    trackPathInput.setText(trackName);
                    trackPathInput.setClickable(true);
                    createTrackButton.setEnabled(true);

                } else {
                    trackNameInput.setError("Track Name Taken");
                }
            }
        });

        createTrackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                trackAuthor = trackAuthorInput.getText().toString();
                trackSampleRate = sampleRateInput.getText().toString();
                Track track;
                //Checks if the string is an integer and acts accordingly
                if (trackSampleRate.matches("^-?\\d+$")) {
                    track = new Track(trackName, trackSourcePath, trackProductPath, 0, 0, 0, 0, 0, Config.FREQUENCY);
                }
                else {
                    track = new Track(trackName, trackSourcePath, trackProductPath, 0, 0, 0, 0, 0, Config.FREQUENCY);
                }
                if (projectID != -1) project.add(track);

                String[] JSONfiles = backend.toWrite();
                outputToFile(JSONfiles[0], "projects");
                outputToFile(JSONfiles[1], "tracks");

                Intent intent = new Intent(getApplicationContext(), TrackView.class);
                intent.putExtra("trackID", project.getTrackListSize()-1);
                intent.putExtra("projectID", projectID);
                startActivityForResult(intent, TRACK_VIEW_RETURN);
            }
        });
    }

    private boolean checkName(String trackName) {
        ArrayList<String> names = project.getTrackNames();
        if (names.contains(trackName) || trackName.length() == 0) return false;
        return true;
    }

    private boolean checkInputs(String trackName, String author, String sampleRate) {
        boolean check = true;
        if (!checkName(trackName)) {
            check = false;
            trackNameInput.setError("Invalid Track Name");
        }
        if (author.length() == 0) {
            trackAuthorInput.setError("Enter Author");
        }
        if (sampleRate.length() == 0) {
            sampleRateInput.setError("Enter Sample rate");
        }
        return check;
    }

    // get backend from activity stack
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TRACK_VIEW_RETURN) {
            if (resultCode == RESULT_OK) {
                projectID = getIntent().getIntExtra("projectID", 0);
                onBackPressed();
            }
        }
        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend
    }

    // when back is pressed send backend and finish activity
    @Override
    public void onBackPressed() {
        String[] JSONfiles = backend.toWrite();
        outputToFile(JSONfiles[0], "projects");
        outputToFile(JSONfiles[1], "tracks");
        Intent intent = new Intent();
        intent.putExtra("projectID", projectID);
        setResult(RESULT_OK, intent);
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
