package com.redmannequin.resonance.Views;

// android imports
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

// project imports
import com.redmannequin.resonance.Audio.MediaPlayer;
import com.redmannequin.resonance.Audio.Mixer.Mixer;
import com.redmannequin.resonance.Backend.Backend;
import com.redmannequin.resonance.Backend.Project;
import com.redmannequin.resonance.R;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectView extends AppCompatActivity {
    // json paths
    private String projectJson;
    private String trackJson;

    // project ID
    private int projectID;

    // backend
    private Project project;
    private Backend backend;

    // Drawer elements
    private DrawerLayout DrawerLayout;
    private ListView TrackDrawerList;
    private ListView EffectDrawerList;
    private ListView TrackPropertiesDrawerList;

    // Track Drawer elements
    private ArrayAdapter TrackAdapter;
    private ArrayList<String> trackList;

    // Buttons
    private Button track_button;
    private Button play_button;
    private Button stop_button;

    //
    private Mixer mixer;
    private MediaPlayer player;
    private Handler handle;
    private Runnable seek;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        // load backend
        projectJson = loadJson("projects"); // load project json
        trackJson = loadJson("tracks");     // load track json
        backend = new Backend(projectJson, trackJson); // init backend

        // get project
        projectID = getIntent().getIntExtra("projectID", 0);
        project = backend.getProject(projectID);

        // set title
        setTitle(project.getName());

        //
        mixer = new Mixer(project);
        if (project.getTrackListSize() != 0) {
            mixer.init();
            mixer.make();
        }

        player = new MediaPlayer();
        player.init(project.getPath() + File.separator + project.getName() + ".wav");

        handle = new Handler();
        seek = new Runnable() {
            @Override
            public void run() {
                if (player.isPlaying()) {
                    handle.postDelayed(this, 50);
                } else {
                    play_button.setText("play");
                }
            }
        };

        // list view stuff
        trackList = new ArrayList<>();
        trackList.addAll(project.getTrackNames());
        trackList.add("+");

        if(project.getTrackListSize() == 0) {
            Intent intent = new Intent(getApplicationContext(), NewTrackView.class);
            intent.putExtra("projectID", projectID);
            startActivityForResult(intent, 0);
        }

        //initialize drawer layout
        DrawerLayout = (DrawerLayout) findViewById(R.id.project_drawer_layout);

        //initialize track drawer and adapter
        TrackAdapter = new ArrayAdapter<String>(this, R.layout.activity_list, trackList);
        TrackDrawerList = (ListView) findViewById(R.id.track_drawer);
        TrackDrawerList.setAdapter(TrackAdapter);
        TrackDrawerList.setOnItemClickListener(new ProjectView.TrackItemClickListener());

        //initialize effect drawer

        //initialize track properties drawer

        // set up buttons
        track_button = (Button) findViewById(R.id.project_track_button);
        play_button = (Button) findViewById(R.id.project_play_button);
        stop_button = (Button) findViewById(R.id.project_stop_button);
        setupListeners();
    }

    // sets up the listeners for the ui elements
    private void setupListeners() {
        play_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(play_button.getText().toString().equals("play")) {
                    player.play();
                    play_button.setText("pause");
                    handle.postDelayed(seek, 50);
                } else {
                    player.pause();
                    play_button.setText("play");
                }
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //player.stop();
            }
        });

        track_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    // wait for project to pressed and load ProjectView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        trackJson = loadJson("tracks");      // load track json
        projectJson = loadJson("projects");  // load project json
        backend = new Backend(projectJson, trackJson); // init backend

        project = backend.getProject(projectID);

        trackList.clear();
        trackList.addAll(project.getTrackNames());
        trackList.add("+");
        TrackAdapter.notifyDataSetChanged();

        mixer = new Mixer(project);
        mixer.init();
        mixer.make();
    }

    // get backend from activity stack
    @Override
    public void onBackPressed() {
        String[] JSONfiles = backend.toWrite();
        outputToFile(JSONfiles[0], "projects");
        outputToFile(JSONfiles[1], "tracks");
        super.onBackPressed();

    }

    private void outputToFile(String data, String name) {
        try {
            FileOutputStream file = this.openFileOutput(name + ".json", ProjectView.MODE_PRIVATE);
            if (data != null) {
                file.write(data.getBytes());
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadJson(String name) {
        StringBuilder text = new StringBuilder();
        try {
            File file = new File(this.getFilesDir(), name+".json");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    private class TrackItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent;
            String[] JSONfiles = backend.toWrite();
            outputToFile(JSONfiles[0], "projects");
            outputToFile(JSONfiles[1], "tracks");

            // Highlight the selected item, update the title, and close the drawer
            if (position == parent.getCount()-1) {
                // switch to NewTrackView and passes project and backend
                intent = new Intent(getApplicationContext(), NewTrackView.class);

                final Intent intentCreate = new Intent(getApplicationContext(), NewTrackView.class);
                intentCreate.putExtra("projectID", projectID);

                final Intent intentLoad = new Intent(getApplicationContext(), LoadTrackView.class);
                intentLoad.putExtra("projectID", projectID);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Choose an option");
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Load",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                startActivityForResult(intentLoad, 0);
                            }
                        })
                        .setNegativeButton("Create",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                startActivityForResult(intentCreate, 0);
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            } else {
                intent = new Intent(getApplicationContext(), TrackView.class);
                intent.putExtra("trackID", position);
            }

            intent.putExtra("projectID", projectID);
            startActivityForResult(intent, 0);
            TrackDrawerList.setItemChecked(position, true);
        }
    }
}
