package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NewProjectView extends AppCompatActivity {

    // ui elements
    private EditText projectNameInput;
    private EditText projectAuthorInput;
    private EditText projectDurationInput;
    private EditText projectSampleRateInput;
    private EditText projectBPMInput;
    private Button createProjectButton;

    // backend
    private String projectJson;
    private String trackJson;
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        setTitle("New Project");

        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend

        // set ui links
        projectNameInput = (EditText) findViewById(R.id.project_name_input);
        projectAuthorInput = (EditText) findViewById(R.id.project_author_input);
        projectDurationInput = (EditText) findViewById(R.id.project_duration_input);
        projectSampleRateInput = (EditText) findViewById(R.id.project_sample_input);
        projectBPMInput = (EditText) findViewById(R.id.project_bpm_input);
        createProjectButton = (Button) findViewById(R.id.create_project_button);
        setListeners();
    }

    private void setListeners() {
        createProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String projectName = projectNameInput.getText().toString();
                String author = projectAuthorInput.getText().toString();
                String duration = projectDurationInput.getText().toString();
                String sampleRate = projectSampleRateInput.getText().toString();
                String bpm = projectBPMInput.getText().toString();
                if (checkInputs(projectName, author, duration, sampleRate, bpm)) {
                    backend.add(new Project(projectName));

                    String[] JSONfiles = backend.toWrite();
                    outputToFile(JSONfiles[0], "projects");
                    outputToFile(JSONfiles[1], "tracks");

                    Intent intent = new Intent(getApplicationContext(), ProjectView.class);
                    intent.putExtra("projectID", backend.getProjectListSize() - 1);
                    startActivityForResult(intent, 0);
                } else {
                    projectNameInput.setError("Project Name Is Taken");
                }
            }
        });

    }

    private boolean checkName(String projectName) {
        ArrayList<String> names = backend.getProjectList();
        if (names != null && !names.isEmpty() && names.contains(projectName) || projectName.length() == 0) {
            return false;
        }
        return true;
    }

    private boolean checkInputs(String projectName, String author, String duration, String sampleRate, String bpm) {
        boolean check = true;
        if (!checkName(projectName)) {
            check = false;
            projectNameInput.setError("Invalid Project Name");
        }
        if (author.length() == 0) {
            projectAuthorInput.setError("Enter Project Author");
        }
        if (sampleRate.length() == 0) {
            projectSampleRateInput.setError("Enter Project Sample rate");
        }
        if (duration.length() == 0) {
            projectDurationInput.setError("Enter project duration");
        }
        if (bpm.length() == 0) {
            projectBPMInput.setError("Enter project bpm");
        }
        return check;
    }

    // get backend from activity stack
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend
        onBackPressed();
    }

    // when back is pressed send backend and finish activity
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
