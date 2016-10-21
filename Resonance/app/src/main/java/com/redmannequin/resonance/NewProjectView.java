package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewProjectView extends AppCompatActivity {

    // used to retrive info for the ui
    private Button create;
    private EditText projectNameInput;
    private EditText authorInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project_view);

        projectNameInput = (EditText) findViewById(R.id.project_name_input);
        authorInput = (EditText) findViewById(R.id.author_text_input);

        // listener for the button
        create = (Button) findViewById(R.id.project_create_button);
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String projectName = projectNameInput.getText().toString();
                String author = authorInput.getText().toString();

                // add project details BACKEND
                // project.setName(projectName);
                // project.setAuthor(author);

                // switches to the TackListView
                Intent intent = new Intent(getApplicationContext(), TrackListView.class);
                startActivity(intent);

            }
        });

    }
}
