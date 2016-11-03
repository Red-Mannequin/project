package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Environment;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileReader;


public class MainMenuView extends AppCompatActivity {

    String projectJson; // holds projects
    String trackJson; // holds track info
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

        projectJson = loadJson("projects"); // load json
        trackJson = loadJson("tracks");
        backend = new Backend(projectJson, trackJson); // init backend

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
    //Input projects to open projects json file
    //Input tracks to open tracks json file
    private String loadJson(String name) {
        /*
        String str = new String();
        InputStream ins;
        if (name == "projects") {
            ins = getResources().openRawResource(R.raw.projects);
        }
        else if (name == "tracks") {
            ins = getResources().openRawResource(R.raw.tracks);
        }
        else {
            //Implement some sort of error handler instead of calling test - this will result in an error
            ins = getResources().openRawResource(R.raw.test);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                str += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
        */

        StringBuilder text = new StringBuilder();
        try {
            File path =  new File(Environment.getExternalStorageDirectory() + File.separator  + "Resonance");
            File file = new File(path, name + ".json");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
}
