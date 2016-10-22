package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.BaseKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.redmannequin.resonance.BackendTesting.Backend;
import com.redmannequin.resonance.BackendTesting.Project;

import java.util.ArrayList;

public class ProjectListView extends AppCompatActivity {

    private Backend backend;

    private ArrayList<String> projectList;
    private ArrayAdapter adapter;
    private ListView projectView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list_view);

        backend = getIntent().getParcelableExtra("backend");

        // creates a list with the track names
        projectList = backend.getProjectList();
        adapter = new ArrayAdapter(this, R.layout.activity_tacklist, projectList);

        // create the list view with track names
        projectView = (ListView) findViewById(R.id.project_list);
        projectView.setAdapter(adapter);
        projectView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getApplicationContext(), TrackListView.class);
                intent.putExtra("project", backend.getProject(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        // send changes back to previous activity
        intent = new Intent(this, MainMenueView.class);
        intent.putExtra("backend", backend);
        startActivity(intent);
    }

}
