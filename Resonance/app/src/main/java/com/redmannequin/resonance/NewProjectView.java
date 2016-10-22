package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.redmannequin.resonance.BackendTesting.Backend;
import com.redmannequin.resonance.BackendTesting.Project;

public class NewProjectView extends AppCompatActivity {

    // used to retrive info for the ui
    private Button create;
    private EditText projectNameInput;
    private EditText authorInput;

    private Project project;
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_view);

        // get working backend
        backend = getIntent().getParcelableExtra("backend");

        // get view inputs
        projectNameInput = (EditText) findViewById(R.id.project_name_input);
        authorInput = (EditText) findViewById(R.id.author_text_input);

        // listener for the create button
        create = (Button) findViewById(R.id.project_create_button);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // gets input
                String projectName = projectNameInput.getText().toString();
                String author = authorInput.getText().toString();

                // sets backend
                project = new Project(projectName);
                backend.add(project);

                // switches to the TackListView and passes project and backend
                Intent intent = new Intent(getApplicationContext(), TrackListView.class);
                intent.putExtra("project", project);
                intent.putExtra("backend", backend);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // gets data back from returend activity
        if (resultCode == RESULT_OK) {
            project = data.getParcelableExtra("project");
            backend = data.getParcelableExtra("backend");
            projectNameInput.setText(project.getName());
        }
    }
}
