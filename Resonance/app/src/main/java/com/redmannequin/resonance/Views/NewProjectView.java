package com.redmannequin.resonance.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.R;

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
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        setTitle("New Project");

        // gets backend from called activity
        backend = getIntent().getParcelableExtra("backend");

        // set ui links
        projectNameInput = (EditText) findViewById(R.id.project_name_input);
        projectAuthorInput = (EditText) findViewById(R.id.project_author_input);
        projectDurationInput = null;
        projectSampleRateInput = null;
        projectBPMInput = null;
        createProjectButton = (Button) findViewById(R.id.create_project_button);
        setListeners();
    }

    private void setListeners() {
        createProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String projectName = projectNameInput.getText().toString();
                if (checkName(projectName)) {
                    backend.add(new Project(projectName));
                    Intent intent = new Intent(getApplicationContext(), TrackListView.class);
                    intent.putExtra("projectID", backend.getProjectListSize() - 1);
                    intent.putExtra("backend", backend);
                    startActivityForResult(intent, 0);
                } else {
                    projectNameInput.setError("Project Name Is Taken");
                }
            }
        });

    }

    private boolean checkName(String projectName) {
        ArrayList<String> names = backend.getProjectList();
        if (names != null && !names.isEmpty() && names.contains(projectName)) {
            return false;
        }
        return true;
    }


    // get backend from activity stack
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            backend = data.getParcelableExtra("backend");
            onBackPressed();
        }
    }

    // when back is pressed send backend and finish activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("backend", backend);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
