package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainMenuView extends AppCompatActivity {

    String json; // holds projects and tracks info
    Backend backend; // backend oject

    // get ui elements
    private Button newProject;
    private Button loadProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        // sets title
        setTitle("Main Menu");

        json = loadJson(); // load json
        backend = new Backend(json); // init backend

        // loads NewProjectView when newProject is clicked
        newProject = (Button) findViewById(R.id.newProjcet);
        newProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewProjectView.class);
                intent.putExtra("backend", backend); // send backend
                startActivityForResult(intent, 0);
            }
        });

        // loads ProjectListView when loadProject is clicked
        loadProject = (Button) findViewById(R.id.loadProject);
        loadProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProjectListView.class);
                intent.putExtra("backend", backend); // send backend
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
        }
    }

    // loads the json file for testing only
    private String loadJson() {
        String str = new String();
        InputStream ins = getResources().openRawResource(R.raw.test);
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                str += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}
