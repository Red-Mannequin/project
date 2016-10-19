package com.redmannequin.resonance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenueView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_view);

        final Button new_project_button = (Button) findViewById(R.id.newProjcet);
        new_project_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrackListView.class);
                startActivity(intent);
            }
        });

        final Button load_project_button = (Button) findViewById(R.id.loadProject);
        load_project_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProjectListView.class);
                startActivity(intent);
            }
        });

        final Button setting_button = (Button) findViewById(R.id.setting);
        setting_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsView.class);
                startActivity(intent);
            }
        });
    }
}
