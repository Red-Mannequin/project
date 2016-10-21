package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.redmannequin.resonance.BackendTesting.Backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainMenueView extends AppCompatActivity {

    private Button new_project_button;
    private Button setting_button;
    private Button load_project_button;

    private Backend testing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_view);

        init();

        new_project_button = (Button) findViewById(R.id.newProjcet);
        new_project_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewProjectView.class);
                startActivity(intent);
            }
        });

        load_project_button = (Button) findViewById(R.id.loadProject);
        load_project_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProjectListView.class);
                intent.putExtra("backend", testing);
                startActivity(intent);
            }
        });

        setting_button = (Button) findViewById(R.id.setting);
        setting_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsView.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        InputStream ins = getResources().openRawResource(R.raw.test);
        String json = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(ins));
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                json += line;
            }
            Log.w("Backend:json:", json);
            testing = new Backend(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
