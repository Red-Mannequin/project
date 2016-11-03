package com.redmannequin.resonance.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.*;

import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.R;

import java.util.ArrayList;

public class ProjectListView extends AppCompatActivity {

    // list view
    private ArrayAdapter adapter;
    private ArrayList<String> projectList;

    // backend
    private Backend backend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_load);

        setTitle("Load Project");

        // backend
        backend = getIntent().getParcelableExtra("backend");

        // set listview stuff
        projectList = new ArrayList<String>();
        projectList.addAll(backend.getProjectList());

        adapter = new ArrayAdapter(this, R.layout.activity_list, projectList);
        ListView trackView = (ListView) findViewById(R.id.list);

        // wait for project to pressed and load TrackListView
        trackView.setAdapter(adapter);
        trackView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TrackListView.class);
                intent.putExtra("projectID", position);
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
            projectList.clear();
            projectList.addAll(backend.getProjectList());
            adapter.notifyDataSetChanged();
        }
    }

    // when back is pressed send backend and finish activity
    @Override
    public void onBackPressed() {

        //JSONfiles[0] = JSON meant for the projects .json file
        //JSONfiles[1] = JSON meant for the tracks .json file
        String[] JSONfiles = backend.toWrite();

        outputToFile(JSONfiles[0], "projects");
        outputToFile(JSONfiles[1], "tracks");

        Intent intent = new Intent();
        intent.putExtra("backend", backend);
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
}
