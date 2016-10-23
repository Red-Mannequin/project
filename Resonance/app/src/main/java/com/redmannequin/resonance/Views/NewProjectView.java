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

public class NewProjectView extends AppCompatActivity {

    // ui elements
    private EditText projectNameInput;
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

        // get ui textfild
        projectNameInput = (EditText) findViewById(R.id.project_name_input);

        // loads TrackListView when createProjectButton is clicked
        createProjectButton = (Button) findViewById(R.id.create_project_button);
        createProjectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String projectName = projectNameInput.getText().toString();
                backend.add(new Project(projectName));

                Intent intent = new Intent(getApplicationContext(), TrackListView.class);
                intent.putExtra("projectID", backend.getProjectListSize()-1);
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
