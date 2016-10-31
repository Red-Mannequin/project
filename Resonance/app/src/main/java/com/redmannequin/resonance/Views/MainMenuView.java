package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

public class MainMenuView extends AppCompatActivity {

    String projectJson; // holds projects
    String trackJson; // holds track info
    Backend backend; // backend oject

    // ui elements
    private Button record;
    private Button newProject;
    private Button loadProject;
    private Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        projectJson = loadJson("projects"); // load json
        trackJson = loadJson("tracks");
        backend = new Backend(projectJson, trackJson); // init backend

        // gets ui elements from layout
        record = (Button) findViewById(R.id.recordNow);
        newProject = (Button) findViewById(R.id.newProjcet);
        loadProject = (Button) findViewById(R.id.loadProject);
        settings = (Button) findViewById(R.id.setting);
        setListeners();

    }

    private void setListeners() {

        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecordTrackView.class);
                startActivity(intent);
            }
        });

        newProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewProjectView.class);
                intent.putExtra("backend", backend); // send backend
                startActivityForResult(intent, 0);
            }
        });

        loadProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProjectListView.class);
                intent.putExtra("backend", backend); // send backend
                startActivityForResult(intent, 0);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecordTrackView.class);
                startActivity(intent);
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
    //Input projects to open projects json file
    //Input tracks to open tracks json file
    private String loadJson(String name) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(this.getFilesDir(), name+".json");
            if (!file.exists()) {
                file.createNewFile();
            } else {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}
